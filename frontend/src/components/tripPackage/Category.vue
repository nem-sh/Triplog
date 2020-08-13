<template>
  <v-app id="inspire" style="max-width: 970px">
    <v-container fluid>
      <v-row>
        <v-col cols="12">
          <v-row align="stretch" justify="space-around">
            <ArticleListComp
              v-for="(item, index) in items"
              :key="`${index}_items`"
              :num="item.num"
              :user_num="item.user_num"
              :title="item.title"
              :thumbnail="item.thumbnail"
              :created_at="item.created_at"
            />
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
import http from "@/util/http-common";
import ArticleListComp from "@/components/article/ArticleListComp.vue";

export default {
  name: "Category",
  components: {
    ArticleListComp
  },
  props: {
    num: { type: Number },
    userNum: { type: Number },
    name: { type: String }
  },
  data: function() {
    return {
      items: []
    };
  },
  created() {
    http.get(`/article/tripPackage/${this.num}`).then(({ data }) => {
      this.items = data;
    });
  }
};
</script>

<style>
</style>