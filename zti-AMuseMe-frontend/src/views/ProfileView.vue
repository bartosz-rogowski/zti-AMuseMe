<template>
  <div class="profile">
    <div class="hero is-link mb-6">
      <div class="hero-body">
        <div class="container">
          <h1 class="title is-size-1">Profile information</h1>
        </div>
      </div>
    </div>
    <div class="container is-centered" v-if="content">
      <div class="card mb-6">
        <div class="card-body">
          <p v-if="content.email">Email: {{content.email}}</p>
          <p v-if="content.nickname">Nickname: {{content.nickname}}</p>
          <p v-if="content.birthdate">
            Born: {{content.birthdate}}
            <span v-if="content.age" class="is-size-7 has-text-grey">
              &nbsp;({{content.age}} years)
            </span>
          </p>
        </div>
      </div>
      <div v-if="content.error" class="notification is-danger" role="alert">
        {{content.error}}
      </div>
    </div>
    <button class="button" v-if="content.email" @click.prevent="formVisible = !formVisible">
      Edit your profile
    </button>
    <br>
    <button class="button is-danger mt-6" v-if="content.email" @click.prevent="deleteUser()">
      Delete your profile
    </button>
    <div class="container is-centered mt-6" v-if="formVisible">
      <p class="is-text-6 is-gray mb-6">You can edit some of your data below</p>
      <Form @submit="editInfo" :validation-schema="schema">
        <div class="field">
          <label for="email" class="label">Email</label>
          <Field name="email" type="email" class="input" :placeholder=content.email />
          <ErrorMessage name="email" class="has-text-danger" />
        </div>
        <div class="field">
          <label for="password" class="label">Password</label>
          <Field name="password" type="password" class="input" />
          <ErrorMessage name="password" class="has-text-danger" />
        </div>
        <div class="field">
          <label for="nickname" class="label">Nickname</label>
          <Field name="nickname" type="text" class="input" :placeholder=content.nickname />
          <ErrorMessage name="nickname" class="has-text-danger" />
        </div>
        <div class="field">
          <label for="birthdate" class="label">Date of birth</label>
          <Field name="birthdate" type="date" class="date" :value=content.birthdate v-if="content.birthdate" />
          <Field name="birthdate" type="date" class="date" v-else />
          <!-- <ErrorMessage name="birthdate" class="has-text-danger" /> -->
        </div>

        <div class="field mt-6">
          <button :class="buttonClass">
            <span>Edit</span>
          </button>
        </div>

        <div class="form-group">
          <div v-if="message" class="notification is-danger" role="alert">
            {{ message }}
          </div>
        </div>

      </Form>

      <div v-if="message_good" class="notification is-success mb-6" role="alert">
        {{ message_good }}
      </div>
    </div>
  </div>
</template>


<script>
import token2JSON from "@/utils/utils.js";
import UserService from "@/services/user.service";
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
export default {
  name: 'profilePage',
  computed: {
    currentUser() {
      let user_info = this.$store.state.auth.user;
      let user = token2JSON(user_info["access_token"]);
      return user;
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }
    else {
      UserService.getUserInfo(this.currentUser.sub).then(
        (response) => {
          this.content = response;
        },
        (error) => {
          this.content["error"] = error.toString();
        }
      );
    }
  },
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      email: yup.string().email(),
      password: yup.string().min(3),
      nickname: yup.string().min(3),
      birthdate: yup.date()
    });
    return {
      content: [],
      buttonClass: "button is-link",
      message: null,
      message_good: null,
      formVisible: false,
      schema,
    }
  },
  methods: {
    async editInfo(user) {
      UserService.editUserInfo(user, this.content.id).then(
        () => {
          this.message = null
          this.message_good = "Perfect! Please, refresh this page to see changes in your data"
        },
        (error) => {
          this.message =
            error.response?.data?.message ||
            error?.message ||
            error.toString();
        }
      );
    },

    async deleteUser() {
      if (confirm('Are you sure you want to delete your account? This operation CANNOT be undone.')) {
        UserService.deleteUser(this.content.id).then(
          () => {
            // logout procedure
            this.$store.dispatch('auth/logout');
            this.$router.push('/');
          }
        )
      }
    }
  }
}
</script>

<style scoped>
.card {
  padding-left: 20px;
  padding-right: 20px;
  display: inline-block;
  text-align: left;
}
</style>