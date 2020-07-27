<template>
  <v-col
    cols="12"
    :md="size === 2 ? 6 : size === 3 ? 4 : undefined"
  > 
    <v-hover v-slot:default="{ hover }">
    
    <v-card
      :class="{ 'on-hover': hover }"
      flat
      tile
      v-bind="$attrs"
      v-on="$listeners"
      :height="value.prominent ? 450 : 350"
      color="grey lighten-1"
      dark
      href="#!"
    >
     
     
     
     
     
      <v-img
        :src="getImg"
        height="100%"
        @click.stop="dialog = true"
      >


      <!-- modal str-->
      <v-dialog
        v-model="dialog"
        max-width="590"
      >
        <v-card @click="dialog = false" style="opacity: 1; height:300px;">
          <v-card-title class="headline">{{value.title}}</v-card-title>
         
        <br>
          <v-card-text>
           {{value.content}}
          </v-card-text>

  

        </v-card>
      </v-dialog>
      <!-- modal end -->


      
        <v-row
          v-if="!value.prominent"
          class="fill-height text-right ma-0"
        >
          <v-col cols="12">
            <v-chip
              label
              class="mx-0 mb-2 text-uppercase"
              color="grey darken-3"
              text-color="white"
              small
              @click.stop="movePackage"
            >
              -패키지로 넘어가는 버튼-
            </v-chip>

            <h3 class="title font-weight-bold mb-2">
              {{value.title}}
            </h3>

            <div class="caption">
              -작성자 이름-
            </div>
          </v-col>

          <v-col align-self="end">
            <h1 class="mr-1 mb-1"><i v-if="like" @click.stop="updateLike" class="fas fa-heart icon-4x" style="color : red;"/> <i v-else @click.stop="updateLike" class="far fa-heart icon-4x" style="color : red;"/></h1>


            <v-chip
              class="text-uppercase ma-0"
              color="primary"
              label
              small
              style="height : 40px"
              @click.stop="moveDetail"
            >
              Read Detail
            </v-chip>
          </v-col>
        </v-row>
        
      </v-img>
    </v-card>
    
  </v-hover>
  
    
  </v-col>
</template>
<script>

import http from "../util/http-common";
  export default {
    name: 'Card',
    data (){
      return {
        like : true,
        dialog :false,
      }
    },
    props: {
      size: {
        type: Number,
        required: true,
      },
      value: {
        type: Object,
        default: () => ({}),
      },
    },
    methods: {
      updateLike: function () {
        if (this.like === true){
            http
            .delete(`article/likelist/${this.$store.state.user.email}/${this.value.num}`,null)
            .then(response => {
                this.like = response.data
                
                 this.like = false
            }).catch((error) => {
                console.log(error.data)
            })
        } else {
          this.like = true
          http
            .put(`article/likelist/${this.$store.state.user.email}/${this.value.num}`,null)
            .then(response => {
                this.like = response.data
            }).catch((error) => {
                console.log(error.data)
            })
        }
      },
      
      moveDetail:function(){
        console.log("구현아직")
      },
      movePackage:function(){
        console.log("구현 아직 ㅎㅎ")
      },
    },
    computed: {
        getImg: function () {
            return `http://localhost:8080/img/mountain.jpg`
        },
        
    },


  }
</script>

<style>
.v-card {
transition: opacity .4s ease-in-out;
}

.v-card:not(.on-hover) {
opacity: 0.7;
 }
</style>
