<template>
  <div style="margin-bottom: 80px;">
    <div id="app">
      <v-app id="inspire">
        <v-card width="800px">
          <v-toolbar flat color="cyan darken-2" dark>
            <v-toolbar-title>User Setting</v-toolbar-title>
          </v-toolbar>
          <v-tabs centered grow color="cyan darken-2">
            <v-tab v-if="chatbot">
              <v-icon left>fas fa-robot</v-icon>챗봇 글
            </v-tab>
            <v-tab>
              <v-icon left>mdi-account</v-icon>My Account
            </v-tab>
            <v-tab>
              <v-icon left>mdi-cogs</v-icon>Blog Setting
            </v-tab>
            <v-tab-item v-if="chatbot">
              
              <v-row class="justify-space-around">
                <v-col cols="5" v-for="item in chatbotList" :key="item.num">
              <v-card>
                
                <v-img :src="item.media" max-height="200"><v-btn small @click="deleteChatbotCard(item.num)"><v-icon>fas fa-trash</v-icon></v-btn></v-img>
                <p>{{item.comment}}</p>
               
              </v-card>
              
              </v-col>
              </v-row>
              
            </v-tab-item>
            <v-tab-item>
              <userInfoComp
                v-if="userInfo.email"
                :propImage="userInfo.imagesrc"
                :propEmail="userInfo.email"
                :propName="userInfo.name"
                :propNickname="userInfo.nickname"
                :propIntro="userInfo.intro"
                :propValid="userInfo.valid"
                :propJoinedAt="userInfo.joinedAt"
                :key="1"
              />
            </v-tab-item>
            <v-tab-item>
              <SetBlogComp v-if="userInfo.email" />
            </v-tab-item>
          </v-tabs>
        </v-card>
      </v-app>
    </div>
  </div>
</template>

<script>
import http from "@/util/http-common";
import UserInfoComp from "@/components/account/UserInfoComp.vue";
import SetBlogComp from "@/components/personal/SetBlogComp.vue";

export default {
  name: "UsetSetting",
  components: {
    UserInfoComp,
    SetBlogComp
  },
  data: function() {
    return {
      userInfo: {},
      chatbotList:[],
      chatbot: false,
    };
  },
  created() {
    http.get(`/users/get/${this.$route.params.hostNum}`).then(({ data }) => {
      this.userInfo = data;
      this.userInfoCompKey += 1;
      
    });
    http.get(`/chatbot/${this.$route.params.hostNum}`)
    .then(({data})=>{
      this.chatbotList = data;
      if(this.chatbotList.length != 0){
        this.chatbot = true;
      }
    })
  },
  methods: {
    deleteChatbotCard: function(num) {
      http.delete(`chatbot/${num}`)
      .then(({data})=>{
        console.log(data)
        this.$router.go(this.$router.currentRoute);
      })
    }
  }
};
</script>

<style>
</style>