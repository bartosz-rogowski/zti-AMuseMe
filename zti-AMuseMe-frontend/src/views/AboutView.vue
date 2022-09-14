<template>
  <div class="hero is-warning mb-6">
    <div class="hero-body">
      <div class="container">
        <h1 class="title is-size-1">About</h1>
      </div>
    </div>
  </div>
  <div class="container is-centered">
    <article class="message is-dark is-centered mb-6">
      <div class="message-header">
        <p>Project information</p>
      </div>
      <div class="message-body">
        AMuseMe is an academic project created in order to present the possibilities of different technologies such as:
        Java Spring Boot, Java Spring Data JPA (backend) and Vue.js with
        <a href="https://bulma.io/" target="_blank" class="is-ghost">Bulma</a> styling (frontend).
        The app's type is SPA (single page application).
      </div>
    </article>
    <article class="message is-primary is-centered mb-6">
      <div class="message-header">
        <p>What can I do here?</p>
      </div>
      <div class="message-body">
        Non-logged user can search artists and records information as well as create a new account.
        Logged user moreover is able to: manage their account, add albums to "for later" list and rate them.
        Rating and adding albums to list in possible on its details page which can be visited after searching
        and clicking an interesting album.
      </div>
    </article>
    <article class="message is-link is-centered mb-6">
      <div class="message-header">
        <p>Resources</p>
      </div>
      <div class="message-body">
        Searching artists and records information is available through the
        <a href="https://musicbrainz.org/doc/MusicBrainz_API" target="_blank" class="is-ghost">MusicBrainz API</a> as
        well as <a href="https://coverartarchive.org/" target="_blank" class="is-ghost">Cover Art Archive API</a>.
        Artists' images are fetched from their Wikipedia's page, so it may occur that the artist image does not present
        an artist. <strong>No copyright infringement intended.</strong>
      </div>
    </article>
  </div>
</template>

<script>
export default {
  name: "AboutPage",
}
</script>

<style scoped>
.container {
  max-width: 700px !important;
}
</style>