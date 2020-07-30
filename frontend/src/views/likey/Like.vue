<template>
  <v-container style="width: 80%;">
    <v-row>
      <v-col cols="12">
        <slot />
      </v-col>

      <Card
        v-for="(likeArticle, i) in likeArticle"
        :key="likeArticle.title"
        :size="layout[i]"
        :value="likeArticle"
      />
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import http from "../../util/http-common";
import Card from "../../components/likey/Card.vue";

export default {
  name: "Like",
  components: {
    Card
  },
  data() {
    return {
      layout: [3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3],
      likeArticle: [],
      limit: 0,
    };
  },

  methods: {
    getLikeList: function() {
      console.log();
      http
        .post("/article/likelist/", {
          usernum: this.getUserNum,
          limit: this.limit
        })
        .then(response => {
          this.likeArticle = response.data;
        })
        .catch(error => {
          console.log(error.data);
        });
    }
  },

  computed: {
    ...mapGetters([
      "isAuthenticated",
      "isProfileLoaded",
      "getProfile",
      "getRealName",
      "getEmail",
      "getUserNum"
    ]),
    ...mapState({
      authLoading: state => state.auth.status === "loading",
      uname: state => `${state.user.getProfile}`,
      userEmail: state => `${state.user.getEmail}`,
      userNum: state => `${state.user.getUserNum}`
    }),
    loading: function() {
      return this.authStatus === "loading" && !this.isAuthenticated;
    }
  },
  created: function() {
    this.getLikeList();
  }
};
</script>

<style>
</style>