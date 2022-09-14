<template>
  <div id="app">
    <NavBar />
    <router-view />
  </div>
</template>

<script>
import NavBar from './components/partials/NavBar.vue';
export default {
  name: 'app',
  components: {
    NavBar,
  }
};
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 20px;
  background-color: rgba(1, 1, 1, 0.10);

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
