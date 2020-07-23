<template>
    <div>
        <ArticleInfoComp
            :articleno = "item.articleno"
            v-if="item.articleno != null"
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
