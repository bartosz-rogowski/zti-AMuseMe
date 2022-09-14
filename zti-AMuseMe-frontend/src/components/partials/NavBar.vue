<template>
  <nav class="navbar container" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <a class="navbar-item" href="/">
        <strong class="is-size-4">AmuseMe</strong>
      </a>
      <a role="button" class="navbar-burger burger" aria-label="menu" aria-expanded="false">
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>
    <div id="navbar" class="navbar-menu">
      <div class="navbar-start">
        <router-link to="/" class="navbar-item">Home</router-link>
        <router-link to="/profile" class="navbar-item" v-if="user">Profile</router-link>
        <router-link to="/my_ratings" class="navbar-item" v-if="user">My Ratings</router-link>
        <router-link to="/later_list" class="navbar-item" v-if="user">"For later" list</router-link>
        <router-link to="/search_album" class="navbar-item">Search Album</router-link>
        <router-link to="/search_artist" class="navbar-item">Search Artist</router-link>
        <router-link to="/about" class="navbar-item">About</router-link>
      </div>
      <div class="navbar-end">
        <div class="navbar-item">
          <div class="buttons">
            <a @click.prevent="logOut" class="button is-primary" v-if="user">
              <strong>Log Out</strong>
            </a>
            <router-link to="/login" class="button is-primary" v-else>
              <strong>Log In</strong>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavBar',
  computed: {
    user() {
      return this.$store.state.auth.user;
    },
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/');
    }
  }
};
</script>

<style lang="scss" scoped>
nav {
  margin-top: 5px;
  margin-bottom: 5px;

  a {
    font-weight: bold;
    color: #202d3a;
    transition: 0.5s;

    &.router-link-exact-active {
      color: #3bc285;
    }

    &:hover {
      color: #0091eb;
    }
  }
}
</style>