<template>
  <v-app>
    <v-sheet class="overflow-hidden"
    >
      <v-app-bar
        width="80%"
        height="200px"
        color="#6A76AB"
        dark
        absolute
        shrink-on-scroll
        elevate-on-scroll
        prominent
        src="https://picsum.photos/1920/1080?random"
        fade-img-on-scroll
        scroll-target="#scrolling-techniques-7"
      >
        <template v-slot:img="{ props }">
          <v-img
            v-bind="props"
            gradient="to top right, rgba(100,115,201,.7), rgba(25,32,72,.7)"
          ></v-img>
        </template>
  
        <v-app-bar-nav-icon>
          <v-btn @click="loginModalToggle = !loginModalToggle"
          v-if="!(isAuthenticated && isProfileLoaded)"
          icon>
          <v-icon>mdi-account-arrow-right</v-icon>
        </v-btn>

        <div v-else>
          <v-app-bar-nav-icon @click="drawer = true">
            <v-avatar :color="ranColor" size="36">
              <span class="white--text headline">{{avatarName(this.getProfile)}}</span>
            </v-avatar>
          </v-app-bar-nav-icon>
        </div>
        </v-app-bar-nav-icon>
  
        <v-form action="/article/searchArticle"> 
        <v-text-field
          flat
          solo-inverted
          hide-details
          prepend-inner-icon="mdi-magnify"
          label="Search"
          class="hidden-sm-and-down"
          name="keyword"
        ></v-text-field>
        </v-form>
        <v-spacer></v-spacer>
        <v-toolbar-title>TITLE</v-toolbar-title>
  
        <v-btn icon @click="searchGoogle">
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
  
        <v-btn icon @click="gitPage">
          <v-icon>mdi-github</v-icon>
        </v-btn>

      </v-app-bar>
      
      <v-sheet
        id="scrolling-techniques-3"
        class="overflow-y-auto"
        :max-height="clientHeight"
        outliend = false
      >
        <v-container style="height: 210px;"></v-container>

        <v-main style="padding: 10px; margin:0 auto;">
          <v-container>
            <router-view></router-view>
          </v-container>
        </v-main>
        
        <v-navigation-drawer permanent right fixed width="20%">
          <v-card
         class="mx-auto"
         max-width="344"
         >
          <v-img
            src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
            height="200px"
            ></v-img>

            <v-card-title>
              Top western road trips
            </v-card-title>

            <v-card-subtitle>
              1,000 miles of wonder
            </v-card-subtitle>

            <v-card-actions>
              <v-btn text>Shard</v-btn>
            </v-card-actions>
          </v-card>
        </v-navigation-drawer>



      </v-sheet>


      <v-footer fixed color="white">
        <v-row justify="center" no-gutters>
          <v-btn rounded color="deep-purple lighten-4" class="my-2" @click="goWrite">
            <v-icon>mdi-file-edit</v-icon>
            <div>WRITE</div>
          </v-btn>
        </v-row>
      </v-footer>
    </v-sheet>

    <v-navigation-drawer 
      v-model="drawer"
      absolute
      temporary
      left
      src = 'https://cdn.vuetifyjs.com/images/backgrounds/bg-2.jpg'
      dark
    >
        <v-list-item two-line :class="miniVariant && 'px-0'">
          <v-list-item-avatar>
            <v-avatar :color="ranColor">
              <span class="white--text headline">{{avatarName(this.getProfile)}}</span>
            </v-avatar>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>{{this.getProfile}}</v-list-item-title>
            <v-list-item-subtitle>일반 회원</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
  
        <v-divider></v-divider>
  
        <v-list
          dense
          nav
          class="py-0"
        >

          <v-list-item to = "/">
            <v-list-item-icon>
              <v-icon></v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>HOME</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-list-item-icon>
              <v-icon></v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>좋아요 목록</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-list-item-icon>
              <v-icon></v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>임시 저장 목록</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-list-item-icon>
              <v-icon></v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>방구석 여행</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item to = "/ArticleList">
            <v-list-item-icon>
              <v-icon></v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>게시물 목록 보기</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

        </v-list>

        <template v-slot:append>
          <div class="pa-2" style="text-align: center;">
            <v-btn inline-block @click.stop="info" style="margin-right:20px;">정보 수정</v-btn>
            <v-btn inline-block @click.stop="logout">로그아웃</v-btn>
          </div>
        </template>
      </v-navigation-drawer>
      
      <v-snackbar
        v-model="loginSuccess"
        timeout="5000"
      >
        <v-icon color="deep-orange darken-3">mdi-home</v-icon>
        {{ getProfile + "님 반갑습니다." }}
        
        <template v-slot:action="{ attrs }">
          <v-btn
            color="red"
            text
            v-bind="attrs"
            @click="loginSuccess = false"
          >
            Close
          </v-btn>
        </template>
      </v-snackbar>

      <v-snackbar
        v-model="logoutSuccess"
        timeout="5000"
      >
        <v-icon color="deep-orange darken-3">mdi-home</v-icon>
         정상적으로 로그아웃 되었습니다.
        
        <template v-slot:action="{ attrs }">
          <v-btn
            color="red"
            text
            v-bind="attrs"
            @click="logoutSuccess = false"
          >
            Close
          </v-btn>
        </template>
      </v-snackbar>

      <v-snackbar
        v-model="alert"
        timeout="5000"
      >
        <v-icon color="deep-orange darken-3">mdi-home</v-icon>
         {{alertMsg}}
        
        <template v-slot:action="{ attrs }">
          <v-btn
            color="red"
            text
            v-bind="attrs"
            @click="alert = false"
          >
            Close
          </v-btn>
        </template>
      </v-snackbar>

        <v-dialog 
          v-model="loginModalToggle"
          max-width=800
          persistent
        >
          <login v-on:closeLoginModal="closeLoginModal"></login>
        </v-dialog>
        <v-dialog 
          v-model="userInfoModalToggle"
          max-width=800
        >
          <user-info-comp 
          v-if="userInfo.email"
          :propImage="userInfo.imagesrc"
          :propEmail="userInfo.email"
          :propName="userInfo.name"
          :propNickname="userInfo.nickname"
          :propIntro="userInfo.intro"
          :propValid="userInfo.valid"
          :propJoinedAt="userInfo.joinedAt"
          :key="userInfoCompKey"
          v-on:closeUserInfoModal="closeUserInfoModal"
          >
          </user-info-comp>
        </v-dialog>
  </v-app>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import {AUTH_LOGOUT} from './store/actions/auth'
import Login from "@/components/account/Login.vue";
import UserInfoComp from "@/components/account/UserInfoComp.vue";
import http from "@/util/http-common";


export default {
  name: "App",
  data: () => ({
    drawer: false,
    ranColor: "red",
    clientHeight: 1000,
    loginSuccess: false,
    logoutSuccess: false,
    loginModalToggle: false,
    userInfoModalToggle: false,
    userInfo:{},
    alert: false,
    alertMsg: "",
    userInfoCompKey: 0,
  }),
  components: {
    Login,
    UserInfoComp,
  },
  methods: {
    logout: function () {
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        this.drawer = false;
        const path = '/';
        if (this.$route.path !== path) this.$router.push(path)
      })
    },
    info: function () {
      http.get(`/users/${this.getEmail}`).then(({ data }) => {
        this.userInfo = data;
        console.dir(data);
        this.userInfoCompKey += 1;
        this.userInfoModalToggle=true;
      });
    },
    avatarName : function(name) {
      var tempName = name.split(/(?=[A-Z])/);
      if(tempName.length == 1) return tempName[0].substring(0, 2).toUpperCase();
      else return (tempName[0].substring(0,1) + tempName[1].substring(0,1)).toUpperCase();
    },
    randomColorGenerateor : function() {
      var letters = '0123456789ABCDEF';
      var color = '#';
      for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      this.ranColor = color;
    },
    handleResize : function() {
      this.clientHeight = document.documentElement.clientHeight;
    },
    closeLoginModal: function() {
      this.loginModalToggle = false;
      this.$router.push('/');
    },
    searchGoogle: function() {
      window.open("https://www.google.com/", "PopupWin", "width=1000,height=900");
    },
    gitPage: function() {
      window.open("https://lab.ssafy.com/edonc700/happyhousefinal", "PopupGit", "width=1000,height=900");
    },
    goWrite: function() {
      this.$router.push('/article/write');
    },
    closeUserInfoModal: function(msg) {
      if(msg != null) {
        this.alertMsg = msg;
        this.alert = true;
      }
      this.userInfoModalToggle = false;
    },
    notice: function() {
      this.$router.push('noticeList');
    }
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'isProfileLoaded','getProfile', 'getRealName', 'getEmail']),
    ...mapState({
      authLoading: state => state.auth.status === 'loading'
      ,uname: state => `${state.user.getProfile}`,
      userEmail : state => `${state.user.getEmail}`,
    }),
    loading: function () {
      return this.authStatus === 'loading' && !this.isAuthenticated
    }
  },
  mounted() {
    this.randomColorGenerateor();
    this.handleResize();
    window.addEventListener('resize', this.handleResize);
  },
  watch: {
    getProfile : function() {
      if(this.getProfile == "") {
        this.logoutSuccess= true;
      }
      else {
        this.loginSuccess= true;
      }
    }
  }
};
</script>