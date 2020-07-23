<template>
  <div>
    <v-simple-table>
      <template v-slot:default>
        <tbody>
          <tr>
              <th class="text-left">프로필 사진</th>
              <td>
                <v-file-input
                  v-model="imagesrc"
                  color="deep-purple accent-4"
                  counter
                  label="File input"
                  multiple
                  placeholder="Select your files"
                  prepend-icon="mdi-paperclip"
                  outlined
                  :show-size="500"
                  style="margin-top: 15px;"
                >
                  <template v-slot:selection="{ index, text }">
                    <v-chip
                      v-if="index < 2"
                      color="deep-purple accent-4"
                      dark
                      label
                      small
                    >
                      {{ text }}
                    </v-chip>
              
                    <span
                      v-else-if="index === 2"
                      class="overline grey--text text--darken-3 mx-2"
                    >
                      +{{ files.length - 2 }} File(s)
                    </span>
                  </template>
                </v-file-input>
              </td>
          </tr>
          <tr>
              <th class="text-left">이메일</th>
              <td>{{email}}</td>
          </tr>
          <tr>
              <th class="text-left">이름</th>
              <td>
                <v-text-field
                  type="text"
                  class="form-control"
                  id="name"
                  ref="name"
                  label="이름을 입력하세요"
                  v-model="name"
                />
              </td>
          </tr>
          <tr>
              <th class="text-left">별명</th>
              <td>
                <v-text-field
                  type="text"
                  class="form-control"
                  id="nick"
                  ref="nick"
                  label="별명을 입력하세요"
                  v-model="nickName"
                />
              </td>
          </tr>
          <tr>
              <th class="text-left">자기소개 글</th>
              <td>
                <v-textarea style="margin-top: 15px;"
                outlined
                label="자신을 소개하세요."
                v-model="intro">
                </v-textarea>
              </td>
          </tr>
          <tr>
              <th class="text-left">권한</th>
              <td>{{valid}}</td>
          </tr>
          <tr>
              <th class="text-left">가입일</th>
              <td>{{getFormatDate(joinedAt)}}</td>
          </tr>
          <tr>
            <td colspan="2">
            <div class="text-right">
              <v-btn @click="modify">수정</v-btn>
              <v-btn @click="confirmSignOut">탈퇴</v-btn>
            </div>
            </td>
          </tr>
        </tbody>
      </template>
    </v-simple-table>

    <v-snackbar
        v-model="alert"
        timeout="5000"
      >
        <v-icon color="deep-orange darken-3">mdi-home</v-icon>
        {{ alertMsg }}
        
        <template v-slot:action="{ attrs }">
          <v-btn
            color="red"
            text
            v-bind="attrs"
            @click="alert = false"
          >
            Close
          </v-btn>
        </template>
      </v-snackbar>

      <v-dialog
        v-model="dialog"
        max-width="350"
      >
        <v-card>
          <v-card-title class="headline">정말 탈퇴하시겠습니까?</v-card-title>
  
          <v-card-text>
            탈퇴하면 계정 정보의 복구가 불가능합니다.
          </v-card-text>
  
          <v-card-actions>
            <v-spacer></v-spacer>
  
            <v-btn
              color="blue darken-1"
              text
              @click="dialog = false"
            >
              취소
            </v-btn>
  
            <v-btn
              color="blue darken-1"
              text
              @click="signOut"
            >
              확인
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
  </div>
</template>

<script>
import http from "@/util/http-common";
import moment from "moment";
import { AUTH_LOGOUT } from "../store/actions/auth";

export default {
  name: "userInfoComp",
  props: {
    propImage : {type : String},
    propEmail : {type : String},
    propNickname : {type : String},
    propIntro : {type : String},
    propValid : {type : Boolean},
    propName: { type: String },
    propJoinedAt: { type: String }
  },
  data: function() {
    return {
      email: this.propEmail,
      name: this.propName,
      nickName: this.propNickname,
      valid: this.propValid,
      intro: this.propIntro,
      imagesrc: this.propImage,
      joinedAt: this.propJoinedAt,
      alert : false,
      alertMsg : "",
      dialog: false,
    };
  },
  computed: {
    computeEmail() {
      return this.propEmail;
    },
    computeName() {
      return this.propName;
    },
    computeJoinedAt() {
      return this.propJoinedAt;
    },
    computeNickName() {
      return this.propNickname;
    },
    computeValid() {
      return this.propValid;
    },
    computeIntro() {
      return this.propIntro;
    },
    computeImagesrc() {
        return this.propImage;
    },
  },
  methods: {
    getFormatDate(joinedAt) {
      return moment(new Date(joinedAt)).format("YYYY.MM.DD HH:mm:ss");
    },
    modify() {
      let err = true;
      let msg = "";
      !this.name &&
        ((msg = "이름을 입력해주세요"),
        (err = false),
        this.$refs.name.focus());
      !this.nickName &&
        ((msg = "별명을 입력해주세요"),
        (err = false),
        this.$refs.nick.focus());

      if (!err) {
        this.alertMsg = msg;
        this.alert = true;
      }
      else this.modifyHandler();
    },
    modifyHandler() {
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
        
        // .catch(() => {
        //   this.alertMsg = "수정 처리시 에러가 발생했습니다.";
        //   this.alert = true;
        // });
    },
    signOut() {
      http
        .delete(`/users/delete/${this.email}`)
        .then(() => {
          // let msg = "탈퇴 처리시 문제가 발생했습니다.";
          // if (data === "success") {
          //   msg = "탈퇴가 완료되었습니다.";
          // }
          let msg = "탈퇴가 완료되었습니다."
          this.alertMsg = msg;
          this.alert = true;
          this.logout();
          this.dialog = false;
          this.$emit("closeUserInfoModal", this.alertMsg);
        }).catch((e) => {
          if (e.request.status === 404){
            this.alertMsg = "탈퇴 처리시 에러가 발생했습니다.";
            this.alert = true;
          } else{
            this.$router.push(`/apierror/${e.request.status}/`)
          }
          console.log(e.request.status)
          
 
        });
        
        // .catch(() => {
        //   this.alertMsg = "탈퇴 처리시 에러가 발생했습니다.";
        //   this.alert = true;
        // });
    },
    confirmSignOut() {
      this.dialog = true;
    },
    logout: function() {
      this.$store.dispatch(AUTH_LOGOUT);
    }
  }
};
</script>

<style scoped>
th {
  background-color:#eeeeee; 
  color:#3e5fba;
}
</style>
