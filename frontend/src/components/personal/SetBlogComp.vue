<template>
  <div>
    <v-simple-table>
      <template v-slot:default>
        <tbody>
          <tr>
            <th class="white text-center teal--text">블로그 이미지</th>
            <td class="white">
              <input ref="imageInput" type="file" hidden @change="onChangeImages" />
              <v-btn type="button" @click="onClickImageUpload">이미지 업로드</v-btn>
            </td>
          </tr>
          <tr>
            <th class="teal lighten-5 text-center teal--text">블로그 명</th>
            <td class="teal lighten-5">
              <v-container fluid class="pa-0">
                <v-row>
                  <v-col cols="10">
                    <v-text-field
                      type="text"
                      class="form-control"
                      id="title"
                      ref="title"
                      label="블로그 명을 입력하세요"
                      v-model="title"
                    />
                  </v-col>
                  <v-col cols="2">
                    <h1>
                      <i class="fas fa-square mt-5" :style="getColor" @click="onOffDialog" />
                    </h1>
                  </v-col>
                </v-row>
                <v-row v-if="dialog">
                  <v-color-picker class="ma-2" mode="hexa" v-model="titleColor" hide-inputs />
                </v-row>
              </v-container>
            </td>
          </tr>

          <tr>
            <th class="white text-center teal--text">방문자 수</th>
            <td class="white">{{visitcount}} 명이 다녀갔습니다.</td>
          </tr>

          <tr class="cyan darken-2">
            <td>
              <div>
                <v-btn inline-block @click="regist" class="cyan darken-3 white--text">수정</v-btn>
              </div>
            </td>
            <td class="text-right">
              <div>
                <v-btn inline-block @click="closeSetBlogMadal2" class="cyan darken-3 white--text">취소</v-btn>
              </div>
            </td>
          </tr>
        </tbody>
      </template>
    </v-simple-table>

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
