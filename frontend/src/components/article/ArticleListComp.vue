<template>
  <div class="item">
    <router-link to="/article/detail">
        <v-img :src="require(`@/assets/${thumbnail}`)" class="thumb" />
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