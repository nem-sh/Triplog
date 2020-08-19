<template>
  <div>
    <div class="mx-auto bg-md d-none d-md-block">
      <v-img
        :src="`../../blogImage/${titleimg}`"
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
                  :src="`../../userImage/${hostImg}`"
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
                  :src="`../../blogImage/profile_init.png`"
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
                  <!-- <div class="font-weight-light mb-2">{{hostEmail}}</div> -->
                  <div v-if="hostIntro" class="font-weight-light mb-2">{{hostIntro}}</div>
                  <br v-else />
                </v-card-text>
              </div>
            </v-hover>
          </v-col>
          <v-col>
            <div v-if="isMyBlog" class="font-weight-light mb-2">
              <v-btn small @click="getNeighborList">팔로우 목록</v-btn>
            </div>
            <div v-else class="font-weight-light mb-2">
              <v-btn small v-if="isMyNeighbor" @click="removeNeighbor">팔로우 해제</v-btn>
              <v-btn small v-else @click="addNeighbor">팔로우 추가</v-btn>
            </div>
            <v-card v-if="showNeighborList" width="100px">
              <v-simple-table>
                <tbody>
                  <NeighborListComp
                    v-for="(neighbor, index) in neighbors"
                    :key="`${index}_neighbors`"
                    :userNum="neighbor.userNum"
                    :neighborNum="neighbor.neighborNum"
                    :neighborNickname="neighbor.neighborNickname"
                  />
                </tbody>
              </v-simple-table>
            </v-card>
          </v-col>
          <h1
            style="height : 100%; display:flex; justify-content:flex-end; align-items:flex-end;"
            :style="getColor"
          >{{title}}</h1>
        </v-row>
      </v-container>
    </div>
    <div class="mx-auto bg-sm d-none d-sm-block d-md-none">
      <v-img
        :src="`../../blogImage/${titleimg}`"
        style="position: relative; z-index: 1; width: 500px;height: 375px; border-radius: 10px;"
        alt
      />
      <v-container style="height: 100%; opacity:1; position: relative; z-index: 2;    top: -375px;">
        <v-row style="height: 100%; width: 100%;  margin :0;">
          <v-col
            cols="5"
            style="height : 100%; back; background-color: rgba( 255, 255, 255, 0.85 ); border-radius:10px;"
          >
            <v-hover v-slot:default="{ hover }">
              <div class="mx-auto" width="100%" height="100%">
                <v-img
                  v-if="hostImg"
                  :aspect-ratio="10/10"
                  :src="`../../userImage/${hostImg}`"
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
                  :src="`../../blogImage/profile_init.png`"
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
                  <!-- <div class="font-weight-light mb-2">{{hostEmail}}</div> -->
                  <div v-if="hostIntro" class="font-weight-light mb-2">{{hostIntro}}</div>
                  <br v-else />
                </v-card-text>
              </div>
            </v-hover>
          </v-col>
          <v-col>
            <div style="height:80%;">
              <div v-if="isMyBlog" class="font-weight-light mb-2">
                <v-btn small @click="getNeighborList">팔로우 목록</v-btn>
              </div>
              <div v-else class="font-weight-light mb-2">
                <v-btn small v-if="isMyNeighbor" @click="removeNeighbor">팔로우 해제</v-btn>
                <v-btn small v-else @click="addNeighbor">팔로우 추가</v-btn>
              </div>
              <v-card v-if="showNeighborList" width="100px">
                <v-simple-table>
                  <tbody>
                    <NeighborListComp
                      v-for="(neighbor, index) in neighbors"
                      :key="`${index}_neighbors`"
                      :userNum="neighbor.userNum"
                      :neighborNum="neighbor.neighborNum"
                      :neighborNickname="neighbor.neighborNickname"
                    />
                  </tbody>
                </v-simple-table>
              </v-card>
            </div>
            <h3
              style=" display:flex; justify-content:flex-end; align-items:flex-end;"
              :style="getColor"
            >{{title}}</h3>
          </v-col>
        </v-row>
      </v-container>
    </div>
    <div class="mx-auto bg d-block d-sm-none">
      <v-img
        :src="`../../blogImage/${titleimg}`"
        style="position: relative; z-index: 1; width: 300px;height: 325px; border-radius: 10px;"
        alt
      />
      <v-container style=" opacity:1; position: relative; z-index: 2;    top: -325px;">
        <v-row style="height:200px;">
          <v-col>
            <div v-if="isMyBlog" class="font-weight-light mb-2">
              <v-btn small @click="getNeighborList">팔로우 목록</v-btn>
            </div>
            <div v-else class="font-weight-light mb-2">
              <v-btn small v-if="isMyNeighbor" @click="removeNeighbor">팔로우 해제</v-btn>
              <v-btn small v-else @click="addNeighbor">팔로우 추가</v-btn>
            </div>
            <v-card v-if="showNeighborList" width="100px">
              <v-simple-table>
                <tbody>
                  <NeighborListComp
                    v-for="(neighbor, index) in neighbors"
                    :key="`${index}_neighbors`"
                    :userNum="neighbor.userNum"
                    :neighborNum="neighbor.neighborNum"
                    :neighborNickname="neighbor.neighborNickname"
                  />
                </tbody>
              </v-simple-table>
            </v-card>
          </v-col>
          <h3
            style="height : 100%; display:flex; justify-content:flex-end; align-items:flex-end;"
            :style="getColor"
          >{{title}}</h3>
        </v-row>
        <v-container>
          <div
            style="height:80px;  background-color: rgba( 255, 255, 255, 0.85 ); border-radius:10px;"
          >
            <v-row>
              <v-col cols="3">
                <v-list-item-avatar class="ml-1" style="cursor:pointer">
                  <v-img v-if="hostImg" :src="`../../userImage/${hostImg}`"></v-img>
                </v-list-item-avatar>
              </v-col>
              <v-col cols="9">
                <v-row>
                  <v-col class="pa-0">
                    <h3 v-html="hostNickName" style="cursor:pointer"></h3>
                  </v-col>
                </v-row>
                <h5 v-html="hostIntro"></h5>
              </v-col>
            </v-row>
          </div>
        </v-container>
      </v-container>
    </div>
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
      alert: false
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
    },

    getBlogInfo() {
      http
        .get(`/blog/${this.$route.params.hostNum}`)
        .then(response => {
          this.visitcount = response.data.visitcount;
          if (response.data.title == null) {
            this.title = this.hostNickName + "'s blog";
          } else {
            this.titleColor = response.data.title.slice(0, 9);
            this.title = response.data.title.slice(9);
          }
          if (response.data.titleimg == null) {
            this.titleimg = "adventurealtitude.jpg";
          } else {
            this.titleimg = response.data.titleimg;
          }
        })
        .catch(error => {
          alert(error.data);
        });
    },
    addNeighbor() {
      if (this.getUserNum != "") {
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
            this.alert = true;
            this.$emit("closeLoginModal", this.alertMsg);
            this.$router.go(this.$router.currentRoute);
          })
          .catch(error => {
            console.log(error.data);
          });
        this.$router.go(this.$router.currentRoute);
      } else {
        alert("먼저 로그인을 진행해주세요");
      }
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
          this.alert = true;
          this.$emit("closeLoginModal", msg);
          this.$router.go(this.$router.currentRoute);
        })
        .catch(error => {
          console.log(error.data);
        });
    },
    getNeighborList() {
      if (this.showNeighborList == false) {
        this.showNeighborList = true;
      } else {
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
      authLoading: state => state.auth.status === "loading"
    }),
    loading: function() {
      return this.authStatus === "loading" && !this.isAuthenticated;
    }
  },
  created() {},
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
    this.getBlogInfo();
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
.bg-md {
  color: var(--c-olor);
  width: 900px;
  height: 375px;
  border-radius: 10px;
  position: relative;
  z-index: 1;
}
.bg-sm {
  color: var(--c-olor);
  width: 500px;
  height: 375px;
  border-radius: 10px;
  position: relative;
  z-index: 1;
}
.bg {
  color: var(--c-olor);
  width: 300px;
  height: 325px;
  border-radius: 10px;
  position: relative;
  z-index: 1;
}
</style>