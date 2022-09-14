<template>
  <div class="container is-centered" v-if="this.release">
    <div class="card is-centered">
      <div class="card-content">
        <div class="media">
          <div class="media-left">
            <img :src="this.release['album-cover']">
          </div>
          <div class="media-content">
            <p class="title">
              {{ this.release['title'] }}
            </p>
            <p class="subtitle">
              by {{ this.release['artist-name'] }}
            </p>
            <br>
            <div class="inner-card">
              <p v-if="this.release['date']">Released: {{ this.release['date'] }}</p>
              <p>MBID: {{ this.release['id'] }}</p>
              <p v-if="this.release['disambiguation']">Disambiguation: {{ this.release['disambiguation'] }}</p>
              <p v-if="this.release['packaging']">Packaging: {{ this.release['packaging'] }}</p>
              <p v-if="this.release['labels'] != []">Labels:
                <span v-for="label in this.release['labels']" :key="label" class="tag is-black mx-1">{{ label }}</span>
              </p>
              <p v-if="this.release['text-representation']?.['language']">Language:
                {{ this.release['text-representation']['language'] }}</p>
            </div>
          </div>
          <div v-if="currentUser">
            <button class="button is-dark mb-6" v-if="is_on_list" @click="removeFromForLaterList()">
              Remove from "for later" list
            </button>
            <button class="button is-dark mb-6" v-else @click="addToForLaterList()">
              Add to "for later" list
            </button>
            <br>
            <rating id="rating" :items="items" :value="stars" @change="update" class="stars-block"></rating>
            <p v-if="stars">
              Your rate: {{ stars }} <br>
              <button class="button is-dark is-small" @click="removeRate()">Remove rate</button>
            </p>
          </div>
        </div>
      </div>
      <div class="card-body">
        <div class="table-container columns is-multiline" v-if="this.release['tracklist']">
          <table class="table is-striped is-hoverable" v-for="(part, i) in this.release['tracklist']" :key="i">
            <caption class="title is-4">CD {{ i + 1 }}.</caption>
            <thead>
              <tr>
                <th>#</th>
                <th>Title</th>
                <th>Length</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="track in part" :key="track">
                <td>{{ track['position'] }}</td>
                <td>{{ track['title'] }}</td>
                <td>{{ this.convertToHHMMSS(track['length'] / 1000) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import SearchAlbumView from './SearchAlbumView.vue';
import token2JSON from "@/utils/utils.js";
import UserService from "@/services/user.service";
import Rating from 'vue-bulma-rating';

export default {
  name: 'releaseDetailsPage',
  components: {
    Rating
  },
  computed: {
    currentUser() {
      let user_info = this.$store.state.auth.user;
      let user = null;
      if (user_info) { user = token2JSON(user_info["access_token"]); }
      return user;
    }
  },
  mounted() {
    if (this.currentUser) {
      let how_many_stars = 5
      for (var i = how_many_stars; i > 0; i--) {
        this.items.push({
          value: i,
          title: i + ' Stars'
        })
      }
      UserService.getUserInfo(this.currentUser.sub).then(
        (response) => {
          this.user = response;
          UserService.getForLaterList(this.user?.id).then(
            (list_response) => {
              for (const release of Object.values(list_response)) {
                if (this.$route.params.mbid == release.albumMBID) {
                  this.is_on_list = true;
                  this.forLaterListRecordID = release.id
                  break;
                }
              }
            },
            (error) => {
              this.message = error.toString()
              console.log("Couldn't fetch user list", error)
            }
          );
          this.fetchUserRatings().then(
            () => {
              for (const rating of Object.values(this.ratingList)) {
                if (this.$route.params.mbid == rating.albumMBID) {
                  this.stars = rating?.rate
                  this.rate_id = rating?.id
                  // document.getElementById("rating").setAttribute('value', rating?.rate);
                  break;
                }
              }
            },
            (error) => {
              this.message = error.toString()
              console.log("Couldn't fetch user ratings", error)
            }
          );
        },
        (error) => {
          console.log("Couldn't fetch user info", error)
          this.message = error.toString()
        }
      );
    }
    axios
      .get('https://musicbrainz.org/ws/2/release/'
        + this.$route.params.mbid
        + '?inc=aliases%2Bartist-credits%2Blabels%2Bdiscids%2Brecordings&fmt=json')
      .then(response => {
        this.release = response.data;
        this.release['tracklist'] = [];
        this.release['labels'] = [];
        for (const part of Object.values(this.release['media'])) {
          if (Object.prototype.hasOwnProperty.call(part, "tracks")) {
            this.release['tracklist'].push(part['tracks'])
          }
        }
        for (const label of Object.values(this.release['label-info'])) {
          if (Object.prototype.hasOwnProperty.call(label['label'], "name")) {
            this.release['labels'].push(label['label']['name'])
          }
        }
        SearchAlbumView.methods.getAlbumCoverByMBID(this.$route.params.mbid)
          .then(
            (response) => {
              this.release['album-cover'] = response;
            },
            (reason) => {
              this.release['album-cover'] = reason;
            })
          .catch(error => { console.log("Error while getting album cover.", error) })
      }, error => {
        console.error(error);
      })
      .finally(
        () => {
          this.release['artist-name'] = this.release['artist-credit'][0]['name'];
        }
      );
  },

  data() {
    return {
      release: [],
      message: null,
      is_on_list: false,
      user: null,
      forLaterListRecordID: null,
      stars: null,
      items: [],
      rate: null,
      ratingList: [],
      rate_id: null,
    }
  },
  methods: {
    convertToHHMMSS(length) {
      var date = new Date(0);
      date.setSeconds(length);
      var timeString = date.toISOString().substr(14, 5);
      return timeString;
    },

    addToForLaterList() {
      UserService.addToForLaterList(this.user?.id, this.$route.params.mbid).then(
        () => {
          this.is_on_list = true;
        },
        (error) => {
          console.log("Error while adding to for later list", error)
        }
      )
    },

    removeFromForLaterList() {
      UserService.removeFromForLaterList(this.forLaterListRecordID).then(
        () => {
          this.is_on_list = false;
        },
        (error) => {
          console.log("Error while deleting from for later list", error)
        }
      )
    },

    update(val) {
      if (typeof val === "number") {
        if (this.stars === null) {
          UserService.addRate(this.user?.id, this.$route.params.mbid, val).then(
            () => {
              this.stars = val
            },
            (error) => {
              console.log("Error while adding rating", error)
            });
        }
        else if (this.stars !== val && this.rate_id !== null) {
          UserService.editRate(this.rate_id, val).then(
            () => {
              this.stars = val
            },
            (error) => {
              console.log("Error while adding rating", error)
            });
        }
      }
    },

    removeRate() {
      if(this.rate_id !== null) {
        UserService.removeRate(this.rate_id).then(
          () => {
            this.stars = null;
          },
          (error) => {
            console.log("Error while removing rate", error)
          }
        )
      }
    },

    async fetchUserRatings() {
      await UserService.getUserRates(this.user?.id).then(
        (list_response) => {
          this.ratingList = list_response
        },
        (error) => {
          console.log("Couldn't fetch user ratings", error)
        }
      )
    }
  },
}
</script>

<style scoped>
.table {
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 50px;
}

.inner-card {
  margin-left: 0px;
  margin-right: 0px;
  display: inline-block;
  text-align: left;
}

.stars-block {
  margin-left: auto;
  margin-right: auto;
  display: block;
}
</style>