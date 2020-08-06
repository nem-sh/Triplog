<template>
  <div>
    <h1 style="color: white">-</h1>
    <article-info-comp
      :articleNum="item.num"
      :articleUserNum="item.user_num"
      :articleTripPackageNum="item.trippackage_num"
      :articleThumbnail="item.thumbnail"
      :articleTemp="item.temp"
      :articleTitle="item.title"
      :articlePlace="item.place"
      :articleDateStart="item.date_start"
      :articleDateEnd="item.date_end"
      :articleCreatedAt="item.created_at"
      :articleContent="item.content"
      :blogMasterName="blogMaster"
      :articleLikeCount="item.likeCount"
      :isLoginedUserLikeThisArticle="isLike"
      v-on:userSnackBar="userSnackBar"
      v-if="isLike !== null"
    />
    <comment-comp :items="comments" />
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import ArticleInfoComp from "@/components/article/ArticleInfoComp.vue";
import CommentComp from "@/components/comment/CommentComp.vue";
import http from "@/util/http-common";

export default {
  name: "ArticleInfo",
  components: {
    ArticleInfoComp,
    CommentComp
  },
  data: function() {
    return {
      item: {},
      blogMaster: "a옹",
      isLike: null,
      comments: []
    };
  },
  created() {
    http.get(`/article/${this.$route.params.articleNum}`).then(({ data }) => {
      this.item = data;
      console.dir(data);
    });
    http
      .get(`/article/like/${this.$route.params.articleNum}/${this.getUserNum}`)
      .then(({ data }) => {
        this.isLike = data;
        console.dir(data);
      });
    http.get(`/comment/${this.$route.params.articleNum}`).then(({ data }) => {
      this.comments = data;
      console.dir(data);
    });
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
  }
};
</script>
