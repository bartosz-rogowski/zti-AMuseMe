<template>
  <div class="hero is-link mb-6">
    <div class="hero-body">
      <div class="container">
        <h1 class="title is-size-2">Sign Up</h1>
      </div>
    </div>
  </div>
  <div class="container is-centered">
    <Form @submit="signup" :validation-schema="schema">
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
        <label for="nickname" class="label">Nickname</label>
        <Field name="nickname" type="text" class="input" />
        <ErrorMessage name="nickname" class="has-text-danger" />
      </div>
      <div class="field">
        <label for="birthdate" class="label">Date of birth</label>
        <Field name="birthdate" type="date" class="date" />
        <!-- <ErrorMessage name="birthdate" class="has-text-danger" /> -->
      </div>

      <div class="field mt-6">
        <button :class="buttonClass">
          <span>Sign Up</span>
        </button>
      </div>

      <div class="form-group">
        <div v-if="message" class="notification is-danger" role="alert">
          {{ message }}
        </div>
      </div>
    </Form>
  </div>
</template>

<script>
  import { Form, Field, ErrorMessage } from "vee-validate";
  import * as yup from "yup";
  
  export default {
    name: 'signupView',
    components: {
      Form,
      Field,
      ErrorMessage,
    },
    data() {
      const schema = yup.object().shape({
        email: yup.string().required("Email is required!").email(),
        password: yup.string().required("Password is required!").min(3),
        nickname: yup.string().required("Nickname is required!").min(3),
        birthdate: yup.date().optional("Date of birth is optional")
      });
  
      return {
        buttonClass: "button is-link",
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
      async signup(user) {
        this.buttonClass += " is-loading";
        let canGoToLogin = false
        await this.$store.dispatch("auth/signup", user).then(
          () => {
            canGoToLogin = true
          },
          (error) => {
            this.buttonClass = "button is-link"
            this.message =
              error.response?.data?.message ||
              error?.message ||
              error.toString();
          }
        );
        if (canGoToLogin) {
          this.buttonClass += "button is-link"
          this.$router.push("/login");
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