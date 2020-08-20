<template>
  <div>
        <v-col>
            <v-card @click="goDetail">
              <v-img 
                v-if="thumbnail" 
                :src="`../../articleImage/${thumbnail}`"
                height="200" 
                width="250"
                lazy-src="https://picsum.photos/id/11/100/60"
                >
                <template v-slot:placeholder>
                <v-row
                    class="fill-height ma-0"
                    align="center"
                    justify="center"
                >
                    <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                </v-row>
                </template>
              </v-img>
              <v-img v-else :src="`../../noimage.jpg`"
                height="200" 
                width="250"
                lazy-src="https://picsum.photos/id/11/100/60"
              >
              <v-layout column align-center justify-center class="white--text" fill-height>
                <h1 class="grey--text font-weight-bold text-center">No Image</h1>
                </v-layout>
              <template v-slot:placeholder>
                <v-row
                    class="fill-height ma-0"
                    align="center"
                    justify="center"
                >
                    <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                </v-row>
                </template>
              </v-img>
              <v-card-actions>
                <v-spacer></v-spacer>
                {{title}}
              </v-card-actions>
            </v-card>
        </v-col>
  </div>
</template>

<script>
export default {
  name: "list",
  props: {
    num: { type: Number },
    user_num: { type: Number },
    title: { type: String },
    thumbnail: { type: String }
  },
  methods: {
    goDetail: function() {
      this.$router.push(`/article/detail/${this.num}`);
    },
  },
};
</script>