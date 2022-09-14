<template>
  <div class="container is-centered" v-if="this.artist">
    <div class="card is-centered">
      <div class="card-content">
        <div class="media">
          <div class="media-left">
            <img :src="this.artist['image']">
          </div>
          <div class="media-content">
            <p class="title">
              {{ this.artist['name'] }}
            </p>
            <p class="subtitle">
              {{ this.artist['type'] }}
            </p>
            <br>
            <div class="inner-card">
              <p v-if="this.artist['country']">
                From: {{ this.artist['country'] }}
              </p>
              <span v-if="this.artist['life-span-begin']">Lifespan: {{ this.artist['life-span-begin']
              }}</span>
              <span v-else>No lifespan info</span>
              <span v-if="artist['life-span-end']"> - {{ artist['life-span-end'] }}</span>
              <span v-else-if="this.artist['life-span-begin']"> - currently</span>
              <p>MBID: {{ this.artist['id'] }}</p>
              <p v-if="this.artist['disambiguation']">
                Disambiguation: {{ this.artist['disambiguation'] }}
              </p>
              <p v-if="this.artist['gender']">
                Gender: {{ this.artist['gender'] }}
              </p>
            </div>
          </div>
        </div>
      </div>
      <p class="title is-4" v-if="this.artist['discography']?.length > 0">Discography</p>
      <div class="columns is-multiline is-mobile" v-if="this.artist['discography']">
        <div class="column is-one-quarter-desktop is-half-mobile is-half-tablet"
          v-for="album in this.artist['discography']" :key="album">
          <div class="card">
            <div class="card-body" v-on:click="goToReleasePage(album['mbid'])">
              <span><span class="is-italic">{{ album['title'] }}</span></span><br />
              <span class="is-size-7 has-text-grey" v-if="album['type']">
                {{ album['type'] }}</span><br />
              <span class="is-size-7 has-text-grey">Released: {{ album['release_date'] }}</span><br />
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "artistDetailsPage",
  mounted() {
    axios
      .get("https://musicbrainz.org/ws/2/artist/"
        + this.$route.params.mbid
        + "?fmt=json")
      .then(response => {
        this.artist = response.data;
        this.searchForWikiImage(this.artist.name).then((response) => {
          this.artist["image"] = response;
        });
        this.artist["life-span-begin"] = this.artist["life-span"]["begin"];
        this.artist["life-span-end"] = this.artist["life-span"]["end"];
        this.artist["discography"] = [];
        axios
          .get("https://musicbrainz.org/ws/2/release-group?artist="
            + this.$route.params.mbid
            + "&type=album|ep&limit=40&fmt=json")
          .then(resp => {
            for (const i in resp.data["release-groups"]) {
              let release = resp.data["release-groups"][i];
              this.artist["discography"].push({
                "mbid": release.id,
                "title": release.title,
                "type": release["primary-type"],
                "release_date": release["first-release-date"],
              });
            }
            this.artist.discography.sort(
              (a, b) => (
                a?.["release_date"] < b?.["release_date"])
                ? 1
                : ((b?.["release_date"] < a?.["release_date"]) ? -1 : 0)
            )
          })
          .catch(error => {
            console.log(error);
          });
        this.artist["country"] = this.artist.area?.["name"];
      }, error => {
        console.error(error);
      })
      .finally(() => {
      });
  },
  data() {
    return {
      artist: [],
    };
  },
  methods: {
    async searchForWikiImage(name) {
      let wiki_url = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=original&origin=*&titles=";
      let r = null;
      return new Promise((resolve, reject) => {
        axios.get(wiki_url + name, (res) => { res.header("Access-Control-Allow-Origin", "*"); })
          .then((response) => {
            for (const obj in response.data["query"]["pages"]) {
              r = response.data["query"]["pages"][obj]["original"]?.source;
            }
            if (typeof r === "undefined") {
              r = "https://www.shazam.com/resources/dc421b8b2c65a0d7262d8cb2931324134e67b2f0/no-artist-image.jpg";
            }
            resolve(r);
            return r;
          }, (reason) => {
            console.log("Error while getting artist image. Default image will be used.", reason);
            r = "https://www.shazam.com/resources/dc421b8b2c65a0d7262d8cb2931324134e67b2f0/no-artist-image.jpg";
            reject(r);
            return r;
          })
          .catch(error => {
            console.log(error);
            r = "https://upload.wikimedia.org/wikipedia/commons/3/3c/No-album-art.png";
            return r;
          });
      });
    }
  }
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

img {
  width: 400px;
  /* you can use % */
  height: auto;
}
</style>