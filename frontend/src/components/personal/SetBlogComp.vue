<template>
  <div>
    <v-simple-table>
      <template v-slot:default>
        <tbody>
          <tr>
            <th class="text-left">블로그 이미지</th>
            <td>
              <input ref="imageInput" type="file" hidden @change="onChangeImages" />
              <v-btn type="button" @click="onClickImageUpload">이미지 업로드</v-btn>
            </td>
          </tr>
          <tr>
            <th class="text-left">블로그 명</th>
            <td>
              <v-text-field
                type="text"
                class="form-control"
                id="title"
                ref="title"
                label="블로그 명을 입력하세요"
                v-model="title"
              />
            </td>
          </tr>
          <tr>
            <th class="text-left">방문자 수</th>
            <td>{{visitcount}}</td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="text-right">
                <v-btn @click="regist">수정</v-btn>
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
      titleimg: "",
      visitcount: 0,
      alert: false,
      alertMsg: "",
      imageUrl: "",
      num: null
    };
  },
  methods: {
    getBlogInfo() {
      http
        .get(`/blog/${this.getUserNum}`)
        .then(response => {
          this.title = response.data.title;
          this.visitcount = response.data.visitcount;
          this.num = response.data.num;
          console.log(response.data);
        })
        .catch(error => {
          console.log(error.data);
        });
    },
    regist() {
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
                title: this.title,
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
            title: this.title
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

    onChangeImages(e) {
      console.log("asdasd");
      const file = e.target.files[0];
      this.titleimg = file;
      this.imageUrl = URL.createObjectURL(file);
      console.log(this.imageUrl);
    },
    onClickImageUpload() {
      this.$refs.imageInput.click();
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
