<template>
  <div>
    <div id="app">
      <v-container style="max-width: 700px;">
        <v-row>
          <v-col>
            <p class="teal--text">블로그 이미지</p>
            <div align="center">
              <v-img
                  v-if="titleimg"
                  :src="require(`@/assets/blogImage/${titleimg}`)"
                  class="img"
                  width="200"
                  height="100"
                />
              <input ref="imageInput" type="file" hidden @change="onChangeImages" />
              <v-btn type="button" @click="onClickImageUpload">이미지 업로드</v-btn>
            </div>
          </v-col>
          <v-col >
            <p class="teal--text">방문자 수</p>
            <p>{{visitcount}} 명이 다녀갔습니다.</p>
          </v-col>
        </v-row>
        <v-row>
          <v-col class="teal--text">
            블로그 명
            <v-container fluid class="pa-0">
              <v-row>
                <v-col cols="10">
                  <v-textarea
                    auto-grow
                    outlined
                    rows="1"
                    row-height="15"
                    label="블로그 명을 입력하세요"
                    v-model="title"
                  ></v-textarea>
                </v-col>
                <v-col cols="2">
                  <h1>
                    <i class="fas fa-square mt-5" :style="getColor" @click="onOffDialog" />
                  </h1>
                </v-col>
              </v-row>
              <v-row v-if="dialog">
                <v-col>
                  <v-color-picker class="ma-2" mode="hexa" v-model="titleColor" hide-inputs />
                </v-col>
              </v-row>
            </v-container>
          </v-col>
        </v-row>

        <v-row>
          <v-col>
            <v-btn inline-block @click="regist" class="cyan darken-3 white--text">수정</v-btn>
          </v-col>
          <v-col class="text-right">
            <v-btn inline-block @click="closeSetBlogMadal2" class="cyan darken-3 white--text">취소</v-btn>
          </v-col>
        </v-row>
      </v-container>
    </div>

    <v-snackbar v-model="alert" timeout="5000">
      <v-icon color="deep-orange darken-3">mdi-home</v-icon>
      {{ alertMsg }}
      <template v-slot:action="{ attrs }">
        <v-btn color="red" text v-bind="attrs" @click="alert = false">Close</v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
import http from "@/util/http-common";
import http3 from "@/util/http-common3";
import { mapGetters, mapState } from "vuex";
export default {
  name: "SetBlogComp",
  props: {},
  data: function() {
    return {
      title: "",
      titleColor: "#000000FF",
      sendTitle: "",
      titleimg: "",
      visitcount: 0,
      alert: false,
      alertMsg: "",
      imageUrl: "",
      num: null,
      dialog: false
    };
  },
  methods: {
    getBlogInfo() {
      http
        .get(`/blog/${this.getUserNum}`)
        .then(response => {
          this.titleColor = response.data.title.slice(0, 9);
          this.title = response.data.title.slice(9);
          this.visitcount = response.data.visitcount;
          this.num = response.data.num;
          this.titleimg = response.data.titleimg;
          console.log(response.data);
        })
        .catch(error => {
          console.log(error.data);
        });
    },
    regist() {
      this.sendTitle = this.sendTitle.concat(this.titleColor, this.title);
      if (this.titleimg) {
        var formData = new FormData();
        formData.append("img", this.titleimg);
        http3
          .put(`/blog/img`, formData)
          .then(({ data }) => {
            console.log(data, "이미지");
            http
              .put(`/blog/`, {
                usernum: this.getUserNum,
                title: this.sendTitle,
                titleimg: data
              })
              .then(({ data }) => {
                let msg = "수정 처리시 문제가 발생했습니다.";
                if (data === "success") {
                  this.registSuccess = true;
                  msg = "수정이 완료되었습니다.";
                }
                this.alertMsg = msg;
                this.alert = true;
                this.registSuccess = true;
                this.$emit("closeSetBlogModal");
              })
              .catch(e => {
                if (e.request.status === 404) {
                  this.alertMsg = "수정 처리시 에러가 발생했습니다.";
                  this.alert = true;
                } else {
                  this.$router.push(`/apierror/${e.request.status}/`);
                }
                console.log(e.request.status);
              });
          })
          .catch(e => {
            if (e.request.status === 404) {
              this.alertMsg = "등록 처리시 에러가 발생했습니다.";
              this.alert = true;
            } else {
              this.$router.push(`/apierror/${e.request.status}/`);
            }
            console.log(e.request.status);
          });
      } else {
        http
          .put(`/blog/`, {
            usernum: this.getUserNum,
            title: this.sendTitle
          })
          .then(({ data }) => {
            let msg = "수정 처리시 문제가 발생했습니다.";
            if (data === "success") {
              this.registSuccess = true;
              msg = "수정이 완료되었습니다.";
            }
            this.alertMsg = msg;
            this.alert = true;
            this.registSuccess = true;
            this.$emit("closeSetBlogModal");
          })
          .catch(e => {
            if (e.request.status === 404) {
              this.alertMsg = "수정 처리시 에러가 발생했습니다.";
              this.alert = true;
            } else {
              this.$router.push(`/apierror/${e.request.status}/`);
            }
            console.log(e.request.status);
          });
      }
    },
    closeSetBlogMadal2() {
      this.$emit("closeSetBlogModal2");
    },

    onChangeImages(e) {
      console.log("asdasd");
      const file = e.target.files[0];
      this.titleimg = file;
      this.imageUrl = URL.createObjectURL(file);
      console.log(this.imageUrl);
    },
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onOffDialog() {
      this.dialog = !this.dialog;
    }
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
    },
    getColor: function() {
      return `color : ${this.titleColor};`;
    }
  },
  created: function() {
    this.getBlogInfo();
  }
};
</script>

<style scoped>
th {
  background-color: #eeeeee;
  color: #3e5fba;
}
</style>
