<template>
  <div>
      <h1>dd</h1>
      <v-simple-table>
            <template v-slot:default>
            <thead>
                <tr>
                <th class="text-left">num</th>
                <th class="text-left">user_num</th>
                <th class="text-left">title</th>
                <th class="text-left">thumbnail</th>
                </tr>
            </thead>
            <tbody>
      <ArticleListComp
        v-for="(item, index) in items"
        :key="`${index}_items`"
        :num="item.num"
        :user_num="item.user_num"
        :title="item.title"
        :thumbnail="item.thumbnail"
      />
      </tbody>
            </template>
      </v-simple-table>
  </div>
</template>

<script>
import http from "@/util/http-common";
import ArticleListComp from "@/components/article/ArticleListComp.vue";

export default {
  name: "ArticleList",
  components: {
    ArticleListComp
  },
  data: function() {
    return {
      items: [],
    };
  },
  created() {
    http
      .get(`/article/getList`)
      .then(({ data }) => {
        this.items = data;
        console.dir(data);
      });
  }
}
</script>

<style>

</style>