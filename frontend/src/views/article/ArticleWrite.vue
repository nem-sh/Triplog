<template>
  <div id="main_wrap" >
    <v-simple-table style="width: 80%;">
      <template v-slot:default>
        <tbody>
          <tr>
              <th class="text-left">TITLE</th>
              <td>
                <v-text-field
                  type="text"
                  class="form-control"
                  id="title"
                  ref="title"
                  label="제목을 입력하세요"
                  v-model="articleTitle"
                />
              </td>
          </tr>
          <tr>
              <th class="text-left">CONTENT</th>
              <td>
              <v-textarea
               label="내용을 입력하세요"
               id="content"
               ref="content"
               v-model="articleContent"
              >
              </v-textarea>
              </td>
          </tr>
          
        </tbody>
        <v-row>
          <v-col>
            <v-btn @click="temp">임시 저장</v-btn>
          </v-col>
          <v-col>
            <v-btn @click="regist">등록</v-btn>
          </v-col>
        </v-row>
      </template>
    </v-simple-table>

    <v-snackbar
        v-model="registSuccess"
        timeout="3000"
      >
        <v-icon color="deep-orange darken-3">mdi-home</v-icon>
         게시물이 등록되었습니다.
        
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

  </div>
</template>

<script>
import http from "@/util/http-common";
import { mapGetters, mapState } from 'vuex';
export default {
  name: "login",
  data() {
    return {
      articleContent: "",
      articleTitle: "",
      alert : false,
      alertMsg : "",
      registSuccess: false,
    };
  },
 methods: {
   temp() {

   },
   regist() {
    let err = true;
    let msg = "";
    !this.articleContent &&
      ((msg = "내용을 입력해주세요"),
      (err = false),
      this.$refs.content.focus());
    !this.articleTitle &&
      ((msg = "제목을 입력해주세요"),
      (err = false),
      this.$refs.title.focus());

    if (!err) {
      this.alertMsg = msg;
      this.alert = true;
    }
    else this.registHandler();
   },
   registHandler() {
     http
      .post(`/article/post`, {
        user_num: 1,
        title: this.articleTitle,
        content: this.articleContent,
        created_at: new Date(),
      })
      .then(({ data }) => {
        let msg = "등록 처리시 문제가 발생했습니다.";
        if (data === "success") {
          msg = "등록이 완료되었습니다.";
        }
        this.alertMsg = msg;
        this.alert = true;
        this.registSuccess = true;
        this.$router.push('/article/list');
      }).catch((e) => {
        if (e.request.status === 404){
          this.alertMsg = "등록 처리시 에러가 발생했습니다.";
          this.alert = true;
        } else{
          this.$router.push(`/apierror/${e.request.status}/`)
        }
        console.log(e.request.status)
        
      });
   },
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
};
</script>
