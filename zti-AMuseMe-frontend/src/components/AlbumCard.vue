<template>
  <div class="album-card">
    <div class="card centeralign addmargin">
      <div class="card-body" v-on:click="goToReleasePage(album['mbid'])">
        <span><span class="is-italic">{{ album['title'] }}</span></span><br />
        <span>by <span class="title is-6">{{ album['artist'] }}</span></span><br />
        <img :src="album['album_cover']" alt="album_cover"><br />
        <span>Released: {{ album['release_date'] }}</span><br />
        <span v-if="album['no_of_tracks']">Number of tracks: {{ album['no_of_tracks'] }}<br></span>
        <span v-if="album['rating']">Your rate: {{ album['rating'] }}<br></span>
        <span class="is-size-7 has-text-grey" v-if="album['disambiguation']">{{ album['disambiguation'] }}</span><br />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['album'],

  methods: {
    goToReleasePage(mbid){
      this.$router.push("/release/" + mbid + "/details");
    }
  }
};
</script>

<style scoped>
.card-body{
  border-radius: 5px;
}
.card-body:hover{
  background-color: rgba(0, 255, 64, 0.382);
  transition: 0.5s;
  cursor: pointer;
}
</style>