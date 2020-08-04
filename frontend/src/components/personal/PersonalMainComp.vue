<template>
  <div class="mx-auto bg">
    <v-img
      :src="require(`@/assets/blogImage/${titleimg}`)"
      style="position: relative; z-index: 1; width: 900px;height: 375px; border-radius: 10px;"
      alt
    />
    <v-container style="height: 100%; opacity:1; position: relative; z-index: 2;    top: -375px;">
      <v-row style="height: 100%; width: 100%;  margin :0;">
        <v-col
          cols="3"
          style="height : 100%; back; background-color: rgba( 255, 255, 255, 0.85 ); border-radius:10px;"
        >
          <v-hover v-slot:default="{ hover }">
            <div class="mx-auto" width="100%" height="100%">
              <v-img
                v-if="hostImg"
                :aspect-ratio="10/10"
                :src="require(`@/assets/userImage/${hostImg}`)"
                style="align-items:flex-end; display : flex;"
              >
                <div
                  v-if="hover & isMyBlog"
                  class="black white--text"
                  style="height : 50px; opacity: 0.5; text-align: center; line-height: 50px; cursor:pointer;"
                  @click="updateProfile"
                >프로필 수정</div>
              </v-img>
              <v-img
                v-else
                :aspect-ratio="10/10"
                :src="require(`@/assets/blogImage/profile_init.png`)"
                style="align-items:flex-end; display : flex;"
              >
                <div
                  v-if="hover & isMyBlog"
                  class="black white--text"
                  style="height : 50px; opacity: 0.5; text-align: center; line-height: 50px; cursor:pointer;"
                  @click="updateProfile"
                >프로필 수정</div>
              </v-img>
              <v-card-text class="pt-6" style="position: relative;">
                <h3 class="orange--text mb-2">{{hostNickName}}</h3>
                <div class="font-weight-light mb-2">{{hostEmail}}</div>
                <div v-if="hostIntro" class="font-weight-light mb-2">{{hostIntro}}</div>
                <br v-else />

                <div v-if="isMyBlog" class="font-weight-light mb-2" style="text-align: center;">
                  <v-btn @click="getNeighborList">이웃 목록</v-btn>
                </div>
                <div v-else class="font-weight-light mb-2" style="text-align: center;">
                  <v-btn v-if="isMyNeighbor" @click="removeNeighbor">이웃 해제</v-btn>
                  <v-btn v-else @click="addNeighbor">이웃 추가</v-btn>
                </div>
              </v-card-text>
            </div>
          </v-hover>
        </v-col>
        <v-col>
          <v-card v-if="showNeighborList" width="200px">
            <NeighborListComp
              v-for="(neighbor, index) in neighbors"
              :key="`${index}_neighbors`"
              :userNum="neighbor.userNum"
              :neighborNum="neighbor.neighborNum"
              :neighborNickname="neighbor.neighborNickname"
            />
          </v-card>
        </v-col>
        <v-col
          cols="9"
          style="height : 100%; display:flex; justify-content:flex-end; align-items:flex-end;"
        >
          <h1 :style="getColor">{{title}}</h1>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import http from "@/util/http-common";
import { mapGetters, mapState } from "vuex";
import NeighborListComp from "@/components/personal/NeighborListComp.vue";

export default {
  name: "PersonalMainComp",
  components: {
    NeighborListComp
  },
  data() {
    return {
      title: "",
      titleColor: "#000000FF",
      visitcount: 0,
      titleimg: "adventurealtitude.jpg",
      isMyNeighbor: false,
      neighbors: [],
      alertMsg: "",
      dialog: false,
      showNeighborList: false,
      alert: false,
    };
  },
  props: {
    hostNum: { type: Number },
    hostNickName: { type: String },
    hostEmail: { type: String },
    hostImg: { type: String },
    hostIntro: { type: String },
    isMyBlog: { type: Boolean }
  },
  methods: {
    updateProfile: function() {
      this.$emit("update-profile");
      console.log("test");
    },

    getBlogInfo() {
      http
        .get(`/blog/${this.$route.params.hostNum}`)
        .then(response => {
          this.titleColor = response.data.title.slice(0, 9);
          this.title = response.data.title.slice(9);
          this.visitcount = response.data.visitcount;
          this.titleimg = response.data.titleimg;
          console.log(this.titleimg);
          if (this.title == "") {
            this.title = this.hostNickName + "'s blog";
          }
          if (this.titleimg == null) {
            this.titleimg = "adventurealtitude.jpg";
            console.log(this.titleimg);
          }
          console.log(response.data);
        })
        .catch(error => {
          console.log(error.data);
        });
    },
    addNeighbor() {
      http
        .post("/neighbor/", {
          userNum: this.getUserNum,
          neighborNum: this.$route.params.hostNum
        })
        .then(({ data }) => {
          let msg = "이웃추가 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "이웃추가가 완료되었습니다.";
          }
          this.alertMsg = msg;
          this.alert= true;
          this.$emit("closeLoginModal", this.alertMsg);
          this.$router.go(this.$router.currentRoute);
        })
        .catch(error => {
          console.log(error.data);
        });
        this.$router.go(this.$router.currentRoute);
    },
    removeNeighbor() {
      http
        .delete(`/neighbor/${this.getUserNum}/${this.$route.params.hostNum}`)
        .then(({ data }) => {
          let msg = "이웃해제 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "이웃해제가 완료되었습니다.";
          }
          this.alertMsg = msg;
          this.alert= true;
          this.$emit("closeLoginModal", msg);
          this.$router.go(this.$router.currentRoute);
        })
        .catch(error => {
          console.log(error.data);
        });
    },
    getNeighborList() {
      if(this.showNeighborList == false){
        this.showNeighborList = true;
      }else{
        this.showNeighborList = false;
      }
      
    }
  },
  computed: {
    getImg: function() {
      return `@/assets/blogImage/${this.titleimg}`;
    },
    getUserImg: function() {
      if (this.hostImg == null) {
        return `@/assets/userImage/${this.titleimg}`;
      } else {
        return `@/assets/userImage/${this.hostImg}`;
      }
    },
    getColor: function() {
      return `margin-bottom: 20px; margin-right: 20px; color : ${this.titleColor};`;
    },
    ...mapGetters(["isAuthenticated", "isProfileLoaded", "getUserNum"]),
    ...mapState({
      authLoading: state => state.auth.status === "loading",
      userNum: state => `${state.user.getUserNum}`
    }),
    loading: function() {
      return this.authStatus === "loading" && !this.isAuthenticated;
    }
  },
  created() {
    this.getBlogInfo();
  },
  mounted() {
    http
      .get(`/neighbor/${this.getUserNum}`)
      .then(response => {
        this.neighbors = response.data;
        var list = response.data;
        for (let index = 0; index < list.length; index++) {
          if (list[index].neighborNum == this.$route.params.hostNum) {
            this.isMyNeighbor = true;
          }
        }
      })
      .catch(error => {
        console.log(error.data);
      });
  }
};
</script>

<style>
.bg::after {
  width: 100%;
  height: 100%;
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
  opacity: 0.6;
  background-size: cover;
  border-radius: 10px;
}
.bg {
  color: var(--c-olor);
  width: 900px;
  height: 375px;
  border-radius: 10px;
  position: relative;
  z-index: 1;
}
</style>