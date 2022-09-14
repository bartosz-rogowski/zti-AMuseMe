<template>
  <div class="albums container is-centered">
    <div class="">
      <div class="columns is-multiline is-mobile" v-if="albums.length">
        <div class="column is-one-fifth-desktop is-half-mobile is-half-tablet" v-for="album in this.albums "
          :key="album">
          <div class="">
            <AlbumCard v-if="album" :album="album" />
          </div>
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
import AlbumCard from './AlbumCard.vue';
export default {
  name: 'AlbumResults',
  components: {
    AlbumCard,
  },
  props: ['albums', 'displayNoResultsString'],
};
</script>
<style lang="scss" scoped>
.rows {
  display: flex;
  flex-direction: column;
}
</style>