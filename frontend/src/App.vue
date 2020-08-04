<template>
  <v-app>
    <div>
      <v-navigation-drawer :mini-variant.sync="mini" clipped app permanent v-if="this.getProfile">
        <v-list-item class="px-2 mb-6" style="padding: 10px;">
          <v-list-item-avatar :color="ranColor" size="40">
            <span class="white--text headline">{{avatarName(this.getProfile)}}</span>
          </v-list-item-avatar>

          <v-list-item-title class="font-weight-bold">{{this.getProfile}}님</v-list-item-title>

          <v-btn icon @click.stop="mini = !mini">
            <v-icon color="teal">mdi-arrow-left-drop-circle-outline</v-icon>
          </v-btn>
        </v-list-item>

        <v-list>
          <v-list-item to="/" @click.stop class="mb-5">
            <v-list-item-icon>
              <v-icon color="green">mdi-home-city</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title class="font-weight-bold teal--text">Home</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item @click.stop="info" class="mb-5">
            <v-list-item-icon>
              <v-icon color="indigo">mdi-account</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title class="font-weight-bold teal--text">My Account</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item to="/like" @click.stop class="mb-5">
            <v-list-item-icon>
              <v-icon color="pink">mdi-charity</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title class="font-weight-bold teal--text">좋아요 목록</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            class="mb-5"
            @click.stop
            :to="{name: 'articleList', params: {hostNum: this.getUserNum}}"
          >
            <v-list-item-icon>
              <v-icon color="green darken-4">mdi-blogger</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title class="font-weight-bold teal--text">My Blog</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            class="mb-5"
            @click.stop="goWrite"
          >
            <v-list-item-icon>
              <v-icon color="green darken-4">mdi-file-edit</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title class="font-weight-bold teal--text">Posting</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item @click.stop="setBlog" class="mb-5">
            <v-list-item-icon>
              <v-icon color="gray">mdi-cogs</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title class="font-weight-bold teal--text">Setting</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-list-item-icon></v-list-item-icon>

            <v-list-item-content>
              <div class="pa-2">
                <v-btn @click.stop="logout" class="teal lighten-1 white--text">LOGOUT</v-btn>
              </div>
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-btn icon v-if="mini">
              <v-icon color="teal">mdi-arrow-right-drop-circle</v-icon>
            </v-btn>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>

      <v-app-bar app dense color="cyan darken-2" dark clipped-left>
        <v-toolbar-title style="font-size: 40px;">
          TRIPLOG
          <v-icon color="teal lighten-4">mdi-compass-outline</v-icon>
        </v-toolbar-title>
        <v-spacer></v-spacer>
        <v-form class="d-flex" action="/article/ArticleSearch">
          <v-text-field
              label="search"
              rounded
              class="shrink mt-5 d-flex"
              height="100%"
              v-if="searchtoggle"
              background-color="grey"
              name="keyword"
            ></v-text-field>
          <v-icon class="d-flex" @click="searchtoggle = !searchtoggle">fas fa-search</v-icon>
          </v-form>

        <v-btn
          @click="loginModalToggle = !loginModalToggle"
          v-if="!(isAuthenticated && isProfileLoaded)"
          icon
        >
          <v-icon>mdi-account-arrow-right</v-icon>
        </v-btn>
      </v-app-bar>
    </div>

    <v-sheet height="50"></v-sheet>
    <v-main style="padding: 10px; margin:0 auto;">
      <v-container>
        <router-view @update-profile="info"></router-view>
      </v-container>
    </v-main>

    <v-snackbar v-model="loginSuccess" timeout="5000">
      <v-icon color="deep-orange darken-3">mdi-home</v-icon>
      {{ getProfile + "님 반갑습니다." }}
      <template v-slot:action="{ attrs }">
        <v-btn color="red" text v-bind="attrs" @click="loginSuccess = false">Close</v-btn>
      </template>
    </v-snackbar>

    <v-snackbar v-model="logoutSuccess" timeout="5000">
      <v-icon color="deep-orange darken-3">mdi-home</v-icon>정상적으로 로그아웃 되었습니다.
      <template v-slot:action="{ attrs }">
        <v-btn color="red" text v-bind="attrs" @click="logoutSuccess = false">Close</v-btn>
      </template>
    </v-snackbar>

    <v-snackbar v-model="alert" timeout="5000">
      <v-icon color="deep-orange darken-3">mdi-home</v-icon>
      {{alertMsg}}
      <template v-slot:action="{ attrs }">
        <v-btn color="red" text v-bind="attrs" @click="alert = false">Close</v-btn>
      </template>
    </v-snackbar>

    <v-dialog v-model="loginModalToggle" max-width="800" persistent>
      <login v-on:closeLoginModal="closeLoginModal"></login>
    </v-dialog>
    <v-dialog v-model="userInfoModalToggle" max-width="800">
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
      ></user-info-comp>
    </v-dialog>
    <v-dialog v-model="setBlogModalToggle" max-width="800">
      <set-blog-comp v-if="userInfo.email" v-on:closeSetBlogModal="closeSetBlogModal" />
    </v-dialog>
  </v-app>
</template>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script>
import { mapGetters, mapState } from "vuex";
import { AUTH_LOGOUT } from "./store/actions/auth";
import Login from "@/components/account/Login.vue";
import UserInfoComp from "@/components/account/UserInfoComp.vue";
import SetBlogComp from "@/components/personal/SetBlogComp.vue";
import http from "@/util/http-common";
import $ from 'jquery';

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
    setBlogModalToggle: false,
    userInfo: {},
    alert: false,
    alertMsg: "",
    userInfoCompKey: 0,
    defaultSelected: "제목",
    items: ["제목", "작성자"],
    titleSearch: "",
    title: "UnTitled",
    mini: true,
    navitems: [
      { title: "Home", icon: "mdi-home-city" },
      { title: "My Account", icon: "mdi-account" },
      { title: "좋아요 목록", icon: "mdi-account-group-outline" },
      { title: "게시물 목록", icon: "mdi-account-group-outline" }
    ],
    searchtoggle: false,
  }),
  components: {
    Login,
    UserInfoComp,
    SetBlogComp
  },
  methods: {
    goToMyBlog: function() {
      this.$router.push(`/${this.getUserNum}`);
    },
    logout: function() {
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        this.drawer = false;
        const path = "/";
        if (this.$route.path !== path) this.$router.push(path);
      });
    },
    info: function() {
      http.get(`/users/get/${this.getUserNum}`).then(({ data }) => {
        this.userInfo = data;
        console.dir(data);
        this.userInfoCompKey += 1;
        this.userInfoModalToggle = true;
      });
    },
    setBlog: function() {
      http.get(`/users/get/${this.getUserNum}`).then(({ data }) => {
        this.userInfo = data;
        console.dir(data);
        this.setBlogCompKey += 1;
        this.setBlogModalToggle = true;
      });
    },
    avatarName: function(name) {
      var tempName = name.split(/(?=[A-Z])/);
      if (tempName.length == 1)
        return tempName[0].substring(0, 2).toUpperCase();
      else
        return (
          tempName[0].substring(0, 1) + tempName[1].substring(0, 1)
        ).toUpperCase();
    },
    randomColorGenerateor: function() {
      var letters = "0123456789ABCDEF";
      var color = "#";
      for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      this.ranColor = color;
    },
    handleResize: function() {
      this.clientHeight = document.documentElement.clientHeight;
    },
    closeLoginModal: function() {
      this.loginModalToggle = false;
      this.$router.push("/");
    },

    gitPage: function() {
      window.open(
        "https://lab.ssafy.com/edonc700/happyhousefinal",
        "PopupGit",
        "width=1000,height=900"
      );
    },
    goWrite: function() {
      this.$router.push("/article/write");
    },
    closeUserInfoModal: function(msg, afterNickName) {
      if (msg != null) {
        this.alertMsg = msg;
        this.alert = true;
        this.$store.commit("modifyProfileName", afterNickName);

        this.$router.go();
      }
      this.userInfoModalToggle = false;
    },
    closeSetBlogModal: function() {
      this.setBlogModalToggle = false;
      this.$router.go();
    },
    useSnackBar: function(msg) {
      if (msg != null) {
        this.alertMsg = msg;
        this.alert = true;
      }
    },
    notice: function() {
      this.$router.push("noticeList");
    },
   
  
  },
  computed: {
    ...mapGetters([
      "isAuthenticated",
      "isProfileLoaded",
      "getProfile",
      "getRealName",
      "getEmail",
      "getUserNum"
    ]),
    ...mapState({
      authLoading: state => state.auth.status === "loading",
      uname: state => `${state.user.getProfile}`,
      userEmail: state => `${state.user.getEmail}`,
      userNum: state => `${state.user.getUserNum}`
    }),
    loading: function() {
      return this.authStatus === "loading" && !this.isAuthenticated;
    }
  },
  mounted() {
    this.randomColorGenerateor();
    this.handleResize();
    window.addEventListener("resize", this.handleResize);
  },
  watch: {
    getProfile: function() {
      if (this.getProfile == "") {
        this.logoutSuccess = true;
      } else {
        this.loginSuccess = true;
      }
    }
  }
};
</script>