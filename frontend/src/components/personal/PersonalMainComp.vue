<template>
  <v-app>
    <br />
    <br />
    <br />
    <div class="mx-auto bg" :style="styleObject">
      <v-container style="height: 100%; opacity:1; ">
        <v-row style="height: 100%; width: 100%;  margin :0;">
          <v-col
            cols="3"
            style="height : 100%; back; background-color: rgba( 255, 255, 255, 0.6 ); border-radius:10px;"
          >
            <v-hover v-slot:default="{ hover }">
              <div class="mx-auto" width="100%" height="100%">
                <v-img
                  :aspect-ratio="10/10"
                  src="@/assets/profile_init.png"
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
                  <div class="font-weight-light grey--text mb-2">{{hostEmail}}</div>
                  <div class="font-weight-light mb-2">{{hostIntro}}</div>

                  <div
                    v-if="isMyBlog"
                    class="font-weight-light grey--text mb-2"
                    style="text-align: center;"
                  >나의 이웃</div>
                  <div v-else>
                    <div
                      v-if="true"
                      class="font-weight-light grey--text mb-2"
                      style="text-align: center;"
                    >이웃 신청</div>
                    <div v-else>이웃 친구</div>
                  </div>
                </v-card-text>
              </div>
            </v-hover>
          </v-col>
          <v-col
            cols="9"
            style="height : 100%; display:flex; justify-content:flex-end; align-items:flex-end;"
          >
            <h1 style="margin-bottom: 20px; margin-right: 20px;">{{title}}</h1>
          </v-col>
        </v-row>
      </v-container>
    </div>
  </v-app>
</template>

<script>
import http from "@/util/http-common";
export default {
  name: "PersonalMainComp",
  data() {
    return {
      title: "",
      visitcount: 0,
      titleimg: "",
      imgurl: "url('~@/assets/1.png')"
    };
  },
  props: {
    hostNum: { type: Number },
    hostNickName: { type: String },
    hostEmail: { type: String },

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
          this.title = response.data.title;
          this.visitcount = response.data.visitcount;
          this.titleimg = response.data.titleimg;
          if (this.title == "") {
            this.title = this.hostNickName + "'s blog";
          }
          console.log(response.data);
        })
        .catch(error => {
          console.log(error.data);
        });
    }
  },
  computed: {
    styleObject() {
      if (this.titleimg == null) {
        return {
          "--background-image": `url('http://localhost:8080/image/mountain.jpg')`
        };
      } else {
        return {
          "--background-image": `url('http://localhost:8080/image/${this.titleimg}')`
        };
      }
    }
  },
  created: function() {
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
  background-image: var(--background-image);
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