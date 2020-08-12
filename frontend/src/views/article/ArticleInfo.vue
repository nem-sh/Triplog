<template>
  <div>
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
      :blogMasterName="item.userNickname"
      :articleLikeCount="item.likeCount"
      :isLoginedUserLikeThisArticle="isLike"
      :articleViews="item.views"
      v-on:userSnackBar="userSnackBar"
      v-if="likeLoaded & itemLoaded"
      @send-paragraph-info="sendParagraphInfo"
    />
    <comment-paragraph-comp :paragraphInfo="paragraphInfo" />
    <comment-comp :items="comments" v-if="commentLoaded" />
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import ArticleInfoComp from "@/components/article/ArticleInfoComp.vue";
import CommentComp from "@/components/comment/CommentComp.vue";
import CommentParagraphComp from "@/components/comment/CommentParagraphComp.vue";
import http from "@/util/http-common";

export default {
  name: "ArticleInfo",
  components: {
    ArticleInfoComp,
    CommentComp,
    CommentParagraphComp
  },
  data: function() {
    return {
      paragraphInfo: {
        paragraph: "",
        choiceId: null
      },
      item: {},
      isLike: null,
      likeLoaded: false,
      itemLoaded: false,
      commentLoaded: false,
      comments: []
    };
  },
  methods: {
    sendParagraphInfo(paragraphInfo) {
      this.paragraphInfo.paragraph = paragraphInfo.paragraph;
      this.paragraphInfo.choiceId = paragraphInfo.choiceId;
    }
  },
  created() {
    http.get(`/article/${this.$route.params.articleNum}`).then(({ data }) => {
      this.item = data;
      console.log(this.item.views);
      this.itemLoaded = true;
    });
    http
      .get(`/article/like/${this.$route.params.articleNum}/${this.getUserNum}`)
      .then(({ data }) => {
        this.isLike = data;
        this.likeLoaded = true;
      });
    http.get(`/comment/${this.$route.params.articleNum}`).then(({ data }) => {
      this.comments = data;
      this.commentLoaded = true;
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
