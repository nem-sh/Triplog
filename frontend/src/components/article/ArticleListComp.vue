<template>
  <div class="item">
    <router-link :to="{ name: 'articleDetail', params: { articleNum: this.num }}">
      <v-img v-if="thumbnail" :src="require(`@/assets/articleImage/${thumbnail}`)" class="thumb" />
      <v-img v-else :src="require(`@/assets/articleImage/noimage.png`)" class="thumb" />
    </router-link>
    <p class="title">{{title}}</p>
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

<style>
</style>