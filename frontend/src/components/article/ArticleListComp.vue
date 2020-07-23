<template>
  <tr>
    <td>{{num}}</td>
    <td>{{user_num}}</td>
    <td>{{title}}</td>
    <td>
      <v-img :src="require(thumbnail)"/>
      <!-- {{thumbnail}} -->
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
  }
}
</script>

<style>

</style>