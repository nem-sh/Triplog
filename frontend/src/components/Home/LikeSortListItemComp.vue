<template>
  <div>
    <router-link :to="{ name: 'articleDetail', params: { articleNum: this.num }}">
        <v-col>
            <v-img 
                v-if="thumbnail" 
                :src="require(`@/assets/${thumbnail}`)" 
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
                <h5>{{title}}</h5>
            </v-img>
            <v-img v-else :src="require(`@/assets/noimage.png`)" />
        </v-col>
    </router-link>
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
  }
};
</script>