<template>
    <div>
        <article-info-comp
            :articleNum = "item.num"
            :articleUserNum = "item.user_num"
            :articleTripPackageNum = "item.trippackage_num"
            :articleThumbnail = "item.thumbnail"
            :articleTemp = "item.temp"
            :articleTitle = "item.articleno"
            :articlePlace = "item.title"
            :articleDateStart = "item.date_start"
            :articleDateEnd = "item.date_end"
            :articleCreatedAt = "item.created_at"
            :articleContent = "item.content"
            :blogMasterName = "blogMaster"
            :articleLikeCount = "item.likeCount"
            :isLoginedUserLikeThisArticle = "isLike"
        />
    </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import ArticleInfoComp from "@/components/article/ArticleInfoComp.vue";
import http from "@/util/http-common";

export default {
  name: "ArticleInfo",
  components: {
    ArticleInfoComp
  },
  data: function() {
    return {
      item: {},
      blogMaster: "유성",
      isLike: false,
    };
  },
  created() {
    http
      .get(`/article/${this.$route.query.articleno}`)
      .then(({ data }) => {
        this.item = data;
        console.dir(data);
      });
    http
      .get(`/article/like/${this.$route.query.articleno}/${this.getProfile}`)
      .then(({ data }) => {
        this.isLike = data;
        console.dir(data);
      });
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'isProfileLoaded','getProfile', 'getRealName', 'getEmail']),
    ...mapState({
      authLoading: state => state.auth.status === 'loading'
      ,uname: state => `${state.user.getProfile}`,
      userEmail : state => `${state.user.getEmail}`,
    }),
    loading: function () {
      return this.authStatus === 'loading' && !this.isAuthenticated
    }
  },
};
</script>
