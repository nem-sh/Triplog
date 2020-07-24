<template>
  <tr>
    <td>{{num}}</td>
    <td>{{user_num}}</td>
    <td>{{title}}</td>
    <td>
      <!-- <img :src="imgSrc()"> -->
      <!-- <img src= "../../assets/1.png"> -->
      <!-- <v-img :src = thumbnail> -->
      <!-- <v-img :src="require('../../assets/1.png')"/> -->
      <!-- {{thumbnail}} -->
      <!-- <img src = "../../assets/1.png" contain height="200"> -->
      <img src = "@/assets/1.png" contain height="200">
      <img v-bind:src="thumbnail" contain height="200">
    </td>
  </tr>
</template>

<script>
export default {
  name: "list",
  props: {
    num: { type: Number },
    user_num: { type: Number },
    title: { type: String },
    thumbnail: { type: String },
  },
  methods: {
    imgSrc : function() {
      // return this.thumbnail;
      // return createElement('img', { attrs: { src: require(this.thumbnail) }});
      return "@/assets/1.png";
    },
  }
}
</script>

<style>

</style>