<template>
  <div class="search_album">
    <div class="hero is-primary">
      <div class="hero-body">
        <div class="container">
          <h1 class="title is-size-1">Search album</h1>
        </div>
      </div>
    </div>
    <div class="container">
      <input class="input is-rounded" type="text" v-model="albumNameString"
        placeholder="Search album by typing phrase, name or artist..."
        v-on:keyup.enter="getAlbumInfo(albumNameString)" />
      <button :class="searchButtonClass" @click="getAlbumInfo(albumNameString)">Search</button>
      <div class="album_results">
        <AlbumResults :albums="this.albums" :displayNoResultsString="displayNoResultsString" />
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import AlbumResults from '@/components/AlbumResults.vue'
export default {
  name: 'searchAlbumPage',
  components: {
    AlbumResults,
  },
  data() {
    return {
      albums: [],
      albumNameString: null,
      searchButtonClass: "button is-primary is-outlined",
      displayNoResultsString: false,
    }
  },

  methods: {
    async getAlbumCoverByMBID(mbid, large = false) {
      let album_cover_url = "http://coverartarchive.org/release/"
      var r;
      return new Promise((resolve, reject) => {
        axios.get(album_cover_url + mbid)
          .then(
            (response) => {
              r = response.data['images'][0]['thumbnails']['500'];
              if (large) {
                r = response.data['images'][0]['thumbnails']['1200']
              }
              if (typeof r === 'undefined') {
                r = "https://upload.wikimedia.org/wikipedia/commons/3/3c/No-album-art.png";
              }
              resolve(r);
              return r;
            },
            (reason) => {
              console.log("Error while getting album cover data. Default cover will be used.", reason);
              r = "https://upload.wikimedia.org/wikipedia/commons/3/3c/No-album-art.png";
              reject(r);
              return r;
            }
          )
          .catch(error => {
            console.log(error);
            r = "https://upload.wikimedia.org/wikipedia/commons/3/3c/No-album-art.png";
            return r;
          })
      })
    },

    async getAlbumInfo(albumName) {
      if (albumName) {
        this.searchButtonClass += " is-loading";
        this.albums = [];
        this.displayNoResultsString = false;
        axios
          .get('https://musicbrainz.org/ws/2/release?query=%22'
            + albumName
            + '%22%20&status=official&type=album|ep&limit=40&fmt=json')
          .then(async response => {
            for (const release of Object.values(response.data.releases)) {
              let disambiguation = null
              if (Object.prototype.hasOwnProperty.call(release, "disambiguation")) {
                disambiguation = release['disambiguation'];
              }
              var album_cover_url = "https://upload.wikimedia.org/wikipedia/commons/3/3c/No-album-art.png";
              await this.getAlbumCoverByMBID(release['id'])
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
              })

            }
          })
          .catch(error => console.log(error))
          .finally(() => {
            this.searchButtonClass = "button is-primary is-outlined";
            if (this.albums.length == 0) { this.displayNoResultsString = true }
          })
      }
    }
  }
}

</script>

<style lang="scss" scoped>
.org-description {
  margin-top: 50px;
}

.input {
  margin: 20px;
}

.button {
  margin-bottom: 20px;
}
</style>