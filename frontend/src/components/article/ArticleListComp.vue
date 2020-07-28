<template>
  <div class="item" @click="showDetail">
      <v-img v-if="thumbnail" :src="require(`@/assets/${thumbnail}`)" class="thumb" />
      <v-img v-else :src="require(`@/assets/noimage.png`)" class="thumb" />
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
  },
  methods: {
    showDetail() {
      this.$router.push(`/article/detail?articleno=${this.num}`)
    },
  }
};
</script>

<style>
</style>