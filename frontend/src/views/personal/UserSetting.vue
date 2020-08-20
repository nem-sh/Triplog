<template>
  <div style="margin-bottom: 80px;">
    <div id="app">
      <v-app id="inspire">
        <v-card width="800px">
          <v-toolbar flat color="cyan darken-2" dark>
            <v-toolbar-title>User Setting</v-toolbar-title>
          </v-toolbar>
          <v-tabs centered grow color="cyan darken-2">
            <v-tab>
              <v-icon left>fas fa-robot</v-icon>챗봇 글
            </v-tab>
            <v-tab>
              <v-icon left>mdi-account</v-icon>My Account
            </v-tab>
            <v-tab>
              <v-icon left>mdi-cogs</v-icon>Blog Setting
            </v-tab>
            <v-tab-item>
              
              <v-row class="justify-space-around"  v-if="chatbot">
                <v-col sm="5" cols="12" v-for="item in chatbotList" :key="item.num">
              <v-card max-height="240">
                
                <v-img :src="item.media" max-height="160"><v-btn small @click="deleteChatbotCard(item.num)"><v-icon>fas fa-trash</v-icon></v-btn></v-img>
                <p>{{item.comment}}</p>
               
              </v-card>
              
              </v-col>
              </v-row>
              <v-row v-if="!chatbot">
                <v-dialog
                  v-model="dialog"
                  max-width="1000"
                  >
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    color="teal"
                    dark
                    v-bind="attrs"
                    v-on="on"
                    class="mx-auto my-3"
                  >
                  챗봇 사용하기
                  </v-btn>
                  </template>
  
            <v-stepper v-model="e1" color="teal">
      <v-stepper-header>
        <v-stepper-step :complete="e1 > 1" step="1">챗봇 찾기</v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step :complete="e1 > 2" step="2">이메일 등록하기</v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step :complete="e1 > 3" step="3">사진과 글올리기</v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="4">완료</v-stepper-step>
      </v-stepper-header>
  
      <v-stepper-items>
        <v-stepper-content step="1">
          <v-card
            class="mb-12"
            color="white"
            max-height="600"
          >
          
          <h2>카카오톡 검색에서 "TRIPLOG"를 검색합니다</h2>  
          <br>
          <v-img :src="`../../TRIPLOG검색.jpg`" max-height="500px"></v-img>
          
          </v-card>
  
          <v-btn
            color="primary"
            @click="e1 = 2"
          >
            Continue
          </v-btn>
  
          <v-btn
              color="primary"
              text
              @click="dialog = false"
            >cancel</v-btn>
        </v-stepper-content>
  
        <v-stepper-content step="2">
          <v-card
            class="mb-12"
            color="white"
            max-height="600"
          >
          <h2>TRIPLOG에 가입된 E-mail을 적어줍니다.</h2>  
          <br>
          <v-img :src="`../../emailvalid.jpg`" max-height="600"></v-img>
          
          </v-card>
          <h3>가입된 이메일이라면 확인 메세지가 나옵니다</h3>
          <br>
          <v-btn
            color="primary"
            @click="e1 = 3"
          >
            Continue
          </v-btn>
  
          <v-btn
              color="primary"
              text
              @click="dialog = false"
            >cancel</v-btn>
        </v-stepper-content>
  
        <v-stepper-content step="3">
          <v-card
            class="mb-12"
            color="white"
            
          >
          <h2>기록하고싶은 사진과 코멘트를 하나씩 적어줍니다</h2>  
          <br>
          
          <v-img :src="`../../assets/media.jpg`" max-width="500"  max-height="600" class="mx-auto"></v-img>
          <br>
          
          </v-card>
  
          <v-btn
            color="primary"
            @click="e1 = 4"
          >
            Continue
          </v-btn>
  
          <v-btn
              color="primary"
              text
              @click="dialog = false"
            >cancel</v-btn>
        </v-stepper-content>
        <v-stepper-content step="4">
          <v-card
            class="mb-12"
            color="white"
            
          >
          
          <h2>My Account에서 올린 사진과 글을 보실수 있습니다</h2>  
          <br>
          <v-img :src="`../../chatbotcomplete.jpg`" max-height="600" max-width="600" class="mx-auto"></v-img>
          
          </v-card>
  
          <v-btn
            color="primary"
            @click="e1 = 1"
          >
            Continue
          </v-btn>
  
          <v-btn
              color="primary"
              text
              @click="dialog = false"
            >cancel</v-btn>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
          </v-dialog>
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
      e1: 1,
      dialog:false,
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
      .then(()=>{
        this.$router.go(this.$router.currentRoute);
      })
    }
  }
};
</script>

<style>
.v-stepper__header {
  color: teal;
}
.v-stepper__label {
  color:teal;
}
</style>