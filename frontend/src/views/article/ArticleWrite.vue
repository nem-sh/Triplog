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
            <td>
              <!-- <input ref="imageInput" type="file" hidden @change="onChangeImages">
              <v-btn type="button" @click="onClickImageUpload">이미지 업로드</v-btn> -->
              <v-file-input ref="imageInput" @change="onChangeImages">
              </v-file-input>
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
      imageUrl: null,
      fileInfo: "",
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
    else {
      const formData = new FormData();
      formData.append('file', this.fileInfo);
      this.registHandler();
    }
   },
   registHandler() {
     http
      .post(`/article/post`, {
        user_num: this.getUserNum,
        title: this.articleTitle,
        content: this.articleContent,
        // file: this.formData,
        created_at: new Date(),
      })
      .then(({ data }) => {
        
        let msg = "등록 처리시 문제가 발생했습니다.";
        if (data === "success") {
          this.registSuccess = true;
          msg = "등록이 완료되었습니다.";
          this.$router.push('/article/list');
        }
        this.alertMsg = msg;
        this.alert = true;
        this.registSuccess = true;
        this.$router.push(`/article/list/${this.getUserNum}`);
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
    onChangeImages(e) {
      const file = e.target.files[0];
      this.fileInfo = file;
      this.imageUrl = URL.createObjectURL(file);
    },
    // onClickImageUpload() {
    //   this.$refs.imageInput.click();
    // },
 },
 computed: {
    ...mapGetters(['isAuthenticated', 'isProfileLoaded','getProfile', 'getRealName', 'getEmail', 'getUserNum']),
    ...mapState({
      authLoading: state => state.auth.status === 'loading',
      uname: state => `${state.user.getProfile}`,
      userEmail : state => `${state.user.getEmail}`,
      userNum : state => `${state.user.getUserNum}`
    }),
    loading: function () {
      return this.authStatus === 'loading' && !this.isAuthenticated
    }
  },
};
</script>
