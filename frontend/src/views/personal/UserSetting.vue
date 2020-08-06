<template>
  <div>
    <div id="app">
      <v-app id="inspire">
        <v-card width="1000px">
          <v-toolbar flat color="cyan darken-2" dark>
            <v-toolbar-title>User Setting</v-toolbar-title>
          </v-toolbar>
          <v-tabs centered grow color="cyan darken-2">
            <v-tab>
              <v-icon left >mdi-account</v-icon>My Account
            </v-tab>
            <v-tab>
              <v-icon left>mdi-cogs</v-icon>Blog Setting
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
              <SetBlogComp
                v-if="userInfo.email"
              />
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
    SetBlogComp,
  },
  data: function() {
    return {
      userInfo: {}
    };
  },
  created() {
    http.get(`/users/get/${this.$route.params.hostNum}`).then(({ data }) => {
      this.userInfo = data;
      console.dir(data);
      this.userInfoCompKey += 1;
      // this.userInfoModalToggle = true;
    });
  }
};
</script>

<style>
</style>