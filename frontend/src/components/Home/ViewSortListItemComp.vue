<template>
  <div class="mx-3">
    
    <v-hover v-slot:default="{ hover }">
      
      <v-card :class="{ 'on-hover': hover } " style="background: white;">
        <v-img
          v-if="thumbnail"
          :src="`../../articleImage/${thumbnail}`"
          class="thumb"
          max-width="310"
          max-height="200"
          @click="moveDetail"
        >
        <h1><span style="background-color:teal; color:white;" class="px-3 rounded">{{rank+1}}</span></h1>
          <v-expand-transition>
            <div
              v-if="hover"
              class="d-flex transition-fast-in-fast-out teal v-card--reveal display-0 white--text"
              style="height: 100%;"
            >
              <div align="center">
                <h2>{{title}}</h2>
                
              </div>
            </div>
          </v-expand-transition>
          
        </v-img>
        <v-img v-else :src="`../../articleImage/noimage.jpg`" class="thumb" @click="moveDetail"  max-width="310"
          max-height="200">
        <v-layout column align-center justify-center class="white--text" fill-height>
          <h1 class="grey--text font-weight-bold text-center">No Image</h1>
        </v-layout>
          <v-expand-transition>
            <div
              v-if="hover"
              class="d-flex transition-fast-in-fast-out teal v-card--reveal display-0 white--text"
              style="height: 100%;"
            >
              <div align="center">
                <h2>{{title}}</h2>
              </div>
            </div>
          </v-expand-transition>
        </v-img>
        
      </v-card>
      
    </v-hover>
    
    <div class="aInfo">
     
      <h3 style="text-overflow:ellipsis;" class="my-1">{{title}}</h3>
      <v-row>
              <v-col cols="6">
              <v-avatar class="d-flex mr-0 ml-2" style="margin:0;"><v-img :src="`../../userImage/${userimg}`" style="margin:0;" class="d-flex mr-0"></v-img></v-avatar>
              </v-col>
              <v-col cols="6">
              <v-card-text class="d-flex  pr-0"><h4 class="d-flex">{{views}}명이 방문</h4></v-card-text>
              </v-col>
        </v-row>
      
    </div>
  </div>
</template>

<script>
import http from "@/util/http-common";

export default {
  name: "list",
  props: {
    num: { type: Number },
    user_num: { type: Number },
    title: { type: String },
    thumbnail: { type: String },
    rank: {type:Number},
    views: {type:Number},
  },
  data: function() {
    return {
      userimg: "profile_init.png",
    };
  },
 
  methods: {
    moveDetail: function() {
      this.$router.push(`/article/detail/${this.num}`);
    },
   
    },
    created(){
   
   http.get(`/${this.user_num}/avatarimage`)
    .then(({data})=>{
      if (data != "null") {
      this.userimg = data; 
      console.log(this.userimg)
      }
      
    })
  
    }
};
</script>

<style>
.pimage {
  width: 100%;
}
.thumb {
  width: 100%;
  height: 200px;
}

.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: 0.5;
  position: absolute;
  width: 100%;
}
.aInfo {
  margin-top: 5px;
  margin-left: 10px;
}
</style>