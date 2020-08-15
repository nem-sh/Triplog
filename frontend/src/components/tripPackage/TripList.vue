<template>
  <div>
    <v-row>
      <v-col>
        <v-flex xs4 class="elevation-1 pa-3 ma-2">
          <v-list two-line width="800px">
            <v-subheader>{{name}}</v-subheader>
            <draggable v-model="items" :options="{group:'people'}" style="min-height: 10px">
              <template>
                <TripPost
                  v-for="(item, index) in items"
                  :key="`${index}_items`"
                  :num="item.num"
                  :user_num="item.user_num"
                  :title="item.title"
                  :thumbnail="item.thumbnail"
                  :created_at="item.created_at"
                />
              </template>
            </draggable>
          </v-list>
        </v-flex>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import http from "@/util/http-common";
import draggable from "vuedraggable";
import TripPost from "@/components/tripPackage/TripPost.vue";

export default {
  name: "TripList",
  components: {
    draggable,
    TripPost
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
    if (this.num == 0) {
      http
        .get(`/article/tripPackage/default/${this.userNum}`)
        .then(({ data }) => {
          this.items = data;
        });
    } else {
      http.get(`/article/tripPackage/${this.num}`).then(({ data }) => {
        this.items = data;
      });
    }
  },
  watch: {
    items: function() {
      for (let index = 0; index < this.items.length; index++) {
        if (this.num == 0) {
          if (this.items[index].trippackage_num != null) {
            http
              .put(`/article/tripPackage/0/${this.items[index].num}`)
          }
        } else {
          if (this.items[index].trippackage_num != this.num) {
            http
              .put(`/article/tripPackage/${this.num}/${this.items[index].num}`)
          }
        }
      }
    }
  }
};
</script>

<style>
</style>