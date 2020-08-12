<template>
  <div>
    <v-row style="display: flex;
justify-content:center;">
      <comment-sort-list-item-comp
        v-for="(item,index) in items"
        :key="`${index}_items`"
        :num="item.num"
        :user_num="item.user_num"
        :title="item.title"
        :thumbnail="item.thumbnail"
      />
    </v-row>
  </div>
</template>

<script>
import http from "@/util/http-common";
import CommentSortListItemComp from "@/components/Home/CommentSortListItemComp.vue";

export default {
  name: "CommentSortListComp",
  components: {
    CommentSortListItemComp
  },
  data: function() {
    return {
      items: []
    };
  },
  created() {
    http.get(`/article/commentsort`).then(({ data }) => {
      this.items = data;
      console.log(data);
    });
  }
};
</script>

<style>
</style>