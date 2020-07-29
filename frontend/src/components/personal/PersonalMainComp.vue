<template>
  <v-container>
    <v-content>{{this.hostNum}}</v-content>
    <v-content>{{this.hostNickName}}</v-content>
  </v-container>
</template>

<script>
export default {
  name: "HelloWorld",
  props: {
    hostNum : {type : Number},
    hostNickName : {type : String},
  },
};
</script>

<style>
h1.title {
  text-shadow: 1px 1px 2px #666666;
  color: rgb(255, 255, 255);
  text-align: center;
  position: relative;
  bottom: 60px;
}
.my-3:hover {
  filter: drop-shadow(10px 10px 20px rgb(136, 136, 136));
}
</style>