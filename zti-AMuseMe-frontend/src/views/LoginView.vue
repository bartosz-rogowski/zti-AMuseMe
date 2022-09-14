<template>
  <div class="hero is-primary mb-6">
    <div class="hero-body">
      <div class="container">
        <h1 class="title is-size-2">Log In</h1>
      </div>
    </div>
  </div>
  <div class="container is-centered">
    <Form @submit="login" :validation-schema="schema">
      <div class="field">
        <label for="email" class="label">Email</label>
        <Field name="email" type="email" class="input" />
        <ErrorMessage name="email" class="has-text-danger" />
      </div>
      <div class="field">
        <label for="password" class="label">Password</label>
        <Field name="password" type="password" class="input" />
        <ErrorMessage name="password" class="has-text-danger" />
      </div>

      <div class="field">
        <button :class="buttonClass">
          <span>Login</span>
        </button>
      </div>

      <div class="form-group">
        <div v-if="message" class="notification is-danger" role="alert">
          {{ message }}
        </div>
      </div>
    </Form>
    <div class="mt-6">
      <p class="subtitle is-size-6">
        If you don't have an account, you can swiftly create one!
      </p>
      <router-link to="/signup" class="button is-dark mb-6">
        Sign Up now
      </router-link>
    </div>
  </div>
</template>

<script>
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";

export default {
  name: 'loginView',
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      email: yup.string().required("Email is required!").email(),
      password: yup.string().required("Password is required!").min(3),
    });

    return {
      buttonClass: "button is-success",
      message: null,
      schema,
    }
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  created() {
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    async login(user) {
      this.buttonClass += " is-loading";
      let canGoToProfile = false
      await this.$store.dispatch("auth/login", user).then(
        () => {
          canGoToProfile = true
        },
        (error) => {
          this.buttonClass = "button is-success"
          this.message =
            error.response?.data?.message ||
            error?.message ||
            error.toString();
        }
      );
      if (canGoToProfile) {
        this.buttonClass += "button is-success"
        this.$router.push("/");
      }
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 400px !important;
}
</style>