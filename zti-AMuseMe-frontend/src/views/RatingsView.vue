<template>
    <div class="ratingList">
      <div class="hero is-link mb-6">
        <div class="hero-body">
          <div class="container">
            <h1 class="title is-size-1">My ratings</h1>
          </div>
        </div>
      </div>
      <div class="container">
        <progress class="progress is-small" max="100" v-if="is_loading"></progress>
        <div v-if="message" class="notification is-danger" role="alert">
          {{ message }}
        </div>
        <article class="message is-dark is-centered" v-if="!is_loading && !message && ratingList.length < 1">
          <div class="message-header">
            <p>Your rating list is empty</p>
          </div>
          <div class="message-body">
            It looks like you had not added any rates yet.<br>
            You can add one by clicking a star
            in an album info page.
          </div>
        </article>
        <div v-else>
          <AlbumResults :albums="this.albums" />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import token2JSON from "@/utils/utils.js";
  import UserService from "@/services/user.service";
  import axios from 'axios'
  import SearchAlbumView from './SearchAlbumView.vue';
  import AlbumResults from '@/components/AlbumResults.vue'
  export default {
    name: 'ratingListPage',
    components: {
      AlbumResults,
    },
    computed: {
      currentUser() {
        let user_info = this.$store.state.auth.user;
        let user = token2JSON(user_info["access_token"]);
        return user;
      }
    },
    mounted() {
      if (!this.currentUser) {
        this.$router.push('/login');
      }
      else {
        UserService.getUserInfo(this.currentUser.sub).then(
          (response) => {
            this.user = response;
            UserService.getUserRates(this.user?.id).then(
              (list_response) => {
                this.ratingList = list_response
                this.getAlbumsInfo()
                this.is_loading = false;
              },
              (error) => {
                this.message = error.toString()
                console.log("Couldn't fetch user ratings", error)
                this.is_loading = false;
              }
            )
          },
          (error) => {
            console.log("Couldn't fetch user info", error)
            this.message = error.toString()
            this.is_loading = false;
          }
        );
      }
    },
    data() {
      return {
        user: null,
        ratingList: [],
        albums: [],
        is_loading: true,
        message: null,
      }
    },
    methods: {
      async getAlbumsInfo() {
        for (const record of Object.values(this.ratingList)) {
          axios
            .get('https://musicbrainz.org/ws/2/release/'
              + record.albumMBID
              + '?inc=aliases%2Bartist-credits%2Blabels%2Bdiscids%2Brecordings&fmt=json')
            .then(async response => {
              let release = response.data;
              let disambiguation = null;
              let album_cover_url = null;
              if (Object.prototype.hasOwnProperty.call(release, "disambiguation")) {
                disambiguation = release['disambiguation'];
              }
              await SearchAlbumView.methods.getAlbumCoverByMBID(record.albumMBID)
                .then(
                  (response) => {
                    album_cover_url = response;
                  },
                  (reason) => {
                    album_cover_url = reason;
                  })
                .catch(error => { console.log("Error while getting album cover.", error) })
              this.albums.push({
                'mbid': release['id'],
                'title': release['title'],
                'artist': release['artist-credit'][0]['name'],
                'release_date': release['date'],
                'no_of_tracks': release['track-count'],
                'album_cover': album_cover_url,
                'disambiguation': disambiguation,
                'rating': record?.rate
              })
            })
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .hero-body {
    background-color: rgb(255, 221, 0);
  }
  
  .message {
    max-width: 500px !important;
    display: inline-block;
  }
  </style>