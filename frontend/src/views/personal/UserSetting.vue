<template>
  <div style="display:flex; justify-content: center;">
    <v-container id="app" style="min-width: 375px;">
      <v-app id="inspire">
        <v-card elevation="5" shaped style="width: 800px;">
          <v-toolbar flat class="teal--text" style="font-family: 'Sunflower'; font-weight: bold;">
            <v-toolbar-title>Setting</v-toolbar-title>
          </v-toolbar>
          <v-tabs show-arrows centered grow color="teal">
            <v-tab style="font-family: 'Sunflower'; font-weight: bold">
              <v-icon left>mdi-account</v-icon>Account
            </v-tab>
            <v-tab style="font-family: 'Sunflower'; font-weight: bold">
              <v-icon left>mdi-cogs</v-icon>Blog
            </v-tab>
            <v-tab style="font-family: 'Sunflower'; font-weight: bold">
              <v-icon left>fas fa-robot</v-icon>Storage
            </v-tab>
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
            <v-tab-item>
              <chat-bot-comp :propChatBot="chatbot" :propChatList="chatbotList" />
            </v-tab-item>
          </v-tabs>
        </v-card>
      </v-app>
    </v-container>
  </div>
</template>

<script>
import http from "@/util/http-common";
import UserInfoComp from "@/components/account/UserInfoComp.vue";
import SetBlogComp from "@/components/personal/SetBlogComp.vue";
import ChatBotComp from "@/components/account/ChatBotComp.vue";

export default {
  name: "UsetSetting",
  components: {
    UserInfoComp,
    SetBlogComp,
    ChatBotComp
  },
  data: function() {
    return {
      userInfo: {},
      chatbotList: [],
      chatbot: false,
      e1: 1,
      dialog: false
    };
  },
  created() {
    http.get(`/users/get/${this.$route.params.hostNum}`).then(({ data }) => {
      this.userInfo = data;
      this.userInfoCompKey += 1;
    });
    http.get(`/chatbot/${this.$route.params.hostNum}`).then(({ data }) => {
      this.chatbotList = data;
      if (this.chatbotList.length != 0) {
        this.chatbot = true;
      }
    });
  },
  methods: {
    deleteChatbotCard: function(num) {
      http.delete(`chatbot/${num}`).then(() => {
        this.$router.go(this.$router.currentRoute);
      });
    }
  }
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=East+Sea+Dokdo&family=Gaegu&family=Hi+Melody&family=Nanum+Gothic&family=Nanum+Myeongjo&family=Nanum+Pen+Script&family=Poor+Story&family=Sunflower:wght@300&family=Yeon+Sung&display=swap");

.v-stepper__header {
  color: teal;
}
.v-stepper__label {
  color: teal;
}
</style>