<template>
    <div>
        <article-info-comp
            :articleTitle = "item.articleno"
            :articlePlace = "item.title"
            :articleDateStart = "item.date_start"
            :articleDateEnd = "item.date_end"
            :articleCreateAt = "item.create_at"
            :articleContent = "content"
            v-if="item.articleTitle != null"
        />
    </div>
</template>

<script>
import ArticleInfoComp from "@/components/article/ArticleInfoComp.vue";
import http from "@/util/http-common";

export default {
  name: "ArticleInfo",
  components: {
    ArticleInfoComp
  },
  data: function() {
    return {
      item: {}
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
