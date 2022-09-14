<template>
  <div class="artists container is-centered">
    <div class="">
      <div class="columns is-multiline is-mobile" v-if="artists.length">
        <div class="column is-one-fifth-desktop is-half-mobile is-half-tablet" v-for="artist in this.artists "
          :key="artist">
          <ArtistCard :artist="artist"></ArtistCard>
        </div>
      </div>
    </div>
    <article class="message is-danger" v-if="displayNoResultsString">
      <div class="message-body">
        No results found :( <br />
        Please try again with different phrase.
      </div>
    </article>
  </div>
</template>

<script>
import ArtistCard from './ArtistCard.vue';
export default {
  name: 'ArtistResults',
  components: {
    ArtistCard
  },
  props: ['artists', 'displayNoResultsString'],
};
</script>
<style lang="scss" scoped>
.rows {
  display: flex;
  flex-direction: column;
}
.card{
  border-radius: 5px;
}
</style>