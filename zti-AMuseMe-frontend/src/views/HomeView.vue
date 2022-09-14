<template>
  <div class="home">
    <section class="hero is-dark">
      <div class="hero-body">
        <div class="container">
          <h1 class="title mt-6 mb-6">Welcome to AmuseMe</h1>
          <h2 class="subtitle" v-if="user">
            Welcome back!
          </h2>
          <h2 class="subtitle mb-6" v-if="!user">
            Make sure you check out full potential by logging in!
          </h2>
          <div class="button-block" v-if="!user">
            <p class="subtitle is-size-6">
              If you do not have an account yet, you can create one by clicking button bellow:
            </p>
            <router-link to="/signup" class="button is-xl is-dark">
              Sign Up now
            </router-link>
          </div>
        </div>
      </div>
      <footer>
        Bartosz Rogowski <br/>
        ZTI 2022
      </footer>
    </section>
  </div>
</template>

<script>
export default {
  name: 'homePage',
  components: {},
  computed: {
    user() {
      return this.$store.state.auth.user;
    },
  },
};
</script>

<style lang="scss" scoped>
.hero {
  text-align: center;
  background-image: url("../../public/baner.jpg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  height: 915px;
}

.hero-body .title {
  text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.6);
  padding: 40px 0 20px 0;
  font-size: 60px;
}

.subtitle {
  text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.7);
  font-size: 30px;
}

.button-block {
  text-align: center;
  margin-left: auto;
  margin-right: auto;
  width: 100%;
  position: absolute;
  bottom: -150px;

  .button {
    margin-right: 50px;
    padding-left: 50px;
    padding-right: 50px;
  }

  .welcome {
    width: 400px;
    padding: 10px;
    margin-left: auto;
    margin-right: auto;
  }
}

.is-xl {
  font-size: 1.7rem;
}

.container {
  margin-top: 250px;
}
</style>