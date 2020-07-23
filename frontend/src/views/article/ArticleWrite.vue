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
              <th class="text-left">Content</th>
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
export default {
  name: "login",
  data() {
    return {
      articleContent: "",
      articleTitle: "",
      alert : false,
      alertMsg : "",
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
      .put(`/users/${this.email}`, {
        name: this.name,
        nickname: this.nickName,
        intro : this.intro,
        imagesrc : this.imagesrc,
      })
      .then(({ data }) => {
        let msg = "수정 처리시 문제가 발생했습니다.";
        if (data === "success") {
          msg = "수정이 완료되었습니다.";
        }
        this.alertMsg = msg;
        this.alert = true;
        this.$emit("closeUserInfoModal", this.alertMsg);
      }).catch((e) => {
        if (e.request.status === 404){
          this.alertMsg = "탈퇴 처리시 에러가 발생했습니다.";
          this.alert = true;
        } else{
          this.$emit("closeLoginModal");
          this.$router.push(`/apierror/${e.request.status}/`)
        }
        console.log(e.request.status)
        

      });
   },
 },
};
</script>
