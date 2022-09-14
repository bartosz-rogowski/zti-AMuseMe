<template>
  <div class="search_artist">
    <div class="hero is-info">
      <div class="hero-body">
        <div class="container">
          <h1 class="title is-size-1">Search artist</h1>
        </div>
      </div>
    </div>
    <div class="container">
      <input class="input is-rounded" type="text" v-model="artistNameString"
        placeholder="Search artist... (for example: Taylor Swift)" v-on:keyup.enter="getArtistInfo(artistNameString)" />
      <button :class="searchButtonClass" @click="getArtistInfo(artistNameString)">Search</button>
      <div class="artist_results">
        <ArtistResults :artists="this.artists" :displayNoResultsString="displayNoResultsString" />
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import ArtistResults from '@/components/ArtistResults.vue';
export default {
  name: 'searchAlbumPage',
  components: {
    ArtistResults,
  },
  data() {
    return {
      artists: [],
      artistNameString: null,
      searchButtonClass: "button is-info is-outlined",
      displayNoResultsString: false,
    }
  },

  methods: {
    async getArtistInfo(artistName) {
      if (artistName) {
        this.searchButtonClass += " is-loading";
        this.artists = [];
        this.displayNoResultsString = false;
        axios
          .get('https://musicbrainz.org/ws/2/artist?query=%22'
            + artistName
            + '%22%20&&limit=40&fmt=json')
          .then(async response => {
            for (const artist of Object.values(response.data.artists)) {
              this.artists.push({
                'mbid': artist['id'],
                'name': artist['name'],
                'type': artist['type'],
                'life-span-begin': artist['life-span']['begin'],
                'life-span-end': artist['life-span']['end'],
                'country': artist['country']
              })
            }
          })
          .catch(error => console.log(error))
          .finally(() => {
            this.searchButtonClass = "button is-info is-outlined";
            if (this.artists.length == 0) { this.displayNoResultsString = true }
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