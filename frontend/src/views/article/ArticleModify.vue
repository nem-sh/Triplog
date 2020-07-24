<template>
    <div>
        <article-modify-comp
            :articleNum = "item.num"
            :articleUserNum = "item.user_num"
            :articleTripPackageNum = "item.trippackage_num"
            :articleTitle = "item.articleno"
            :articlePlace = "item.title"
            :articleDateStart = "item.date_start"
            :articleDateEnd = "item.date_end"
            :articleCreatedAt = "item.created_at"
            :articleContent = "item.content"
        />
    </div>
</template>

<script>
import ArticleModifyComp from "@/components/article/ArticleModifyComp.vue";
import http from "@/util/http-common";

export default {
  name: "ArticleModify",
  components: {
    ArticleModifyComp
  },
  data: function() {
    return {
      item: {},
      blogMaster: "유성"
    };
  },
  created() {
    http
      .get(`/article/${this.$route.query.articleno}`)
      .then(({ data }) => {
        this.item = data;
        console.dir(data);
      });
  }
};
</script>
