<template>
  <div class="item">
    <router-link :to="{ name: 'articleDetail', params: { articleNum: this.num }}">
      <v-img v-if="thumbnail" :src="require(`@/assets/${thumbnail}`)" class="thumb" />
      <v-img v-else :src="require(`@/assets/noimage.png`)" class="thumb" />
    <p class="title">{{title}}</p>
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
  },
};
</script>

<style>
</style>