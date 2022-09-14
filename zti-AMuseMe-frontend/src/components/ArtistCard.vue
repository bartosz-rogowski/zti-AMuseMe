<template>
  <div class="artist-card">
    <div class="card centeralign addmargin">
      <div class="card-body" v-on:click="goToArtistPage(artist['mbid'])">
        <p>
          <span class="is-size-7 has-text-grey">{{ artist['type'] }}</span><br>
          <span class="title is-6">{{ artist['name'] }}</span><br>
          <span class="is-size-7 has-text-grey" v-if="artist['life-span-begin']">{{ artist['life-span-begin'] }}</span>
          <span class="is-size-7 has-text-grey" v-else>No lifespan info</span>
          <span class="is-size-7 has-text-grey" v-if="artist['life-span-end']"> - {{ artist['life-span-end'] }}</span>
        </p>
        <p class="is-size-7 has-text-grey" v-if="artist['country']">{{ artist['country'] }}</p>
        <p class="is-size-7 has-text-grey" v-else>No country info</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['artist'],

  methods: {
    goToArtistPage(mbid) {
      this.$router.push("/artist/" + mbid + "/details");
    }
  }
};
</script>

<style scoped>
.card-body {
  border-radius: 5px;
}

.card-body:hover {
  background-color: rgba(0, 42, 255, 0.382);
  transition: 0.5s;
  cursor: pointer;
}
</style>