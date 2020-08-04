<template>
  <div>
    <v-container class="cyan darken-2">
      <v-row>
        <v-col class="white--text pa-2 ml-3">
          My Account
        </v-col>
      </v-row>
    </v-container>
    <v-simple-table>
      <template v-slot:default>
        <tbody>
          <tr>
              <th class="white text-center teal--text">
                <input ref="imageInput" type="file" hidden @change="onChangeImages">
                <v-btn type="button" @click="onClickImageUpload">프로필 이미지 변경</v-btn>
              </th>
              <td>
                <div align="center">
                  <div v-if="firstImage">
                    <v-img v-if="imagesrc" :src="require(`@/assets/userImage/${imagesrc}`)" class="img" width="100" height="100">
                      <template v-slot:placeholder>
                        <v-row
                            class="fill-height ma-0"
                            align="center"
                            justify="center"
                        >
                            <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                        </v-row>
                      </template>
                    </v-img>
                    <v-img v-else :src="require(`@/assets/blogImage/noimage.png`)" class="img" width="100" height="100">
                      <template v-slot:placeholder>
                        <v-row
                            class="fill-height ma-0"
                            align="center"
                            justify="center"
                        >
                            <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                        </v-row>
                      </template>
                    </v-img>
                  </div>
                  <div v-else>
                    <v-img v-if="imageUrl" :src="imageUrl" class="img" width="100" height="100">
                      <template v-slot:placeholder>
                        <v-row
                            class="fill-height ma-0"
                            align="center"
                            justify="center"
                        >
                            <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                        </v-row>
                      </template>
                    </v-img>
                    <v-img v-else :src="require(`@/assets/blogImage/noimage.png`)" class="img" width="100" height="100">
                      <template v-slot:placeholder>
                        <v-row
                            class="fill-height ma-0"
                            align="center"
                            justify="center"
                        >
                            <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                        </v-row>
                      </template>
                    </v-img>
                  </div>
                </div>
              </td>
          </tr>
          <tr>
              <th class="teal lighten-5 text-center teal--text">Email (ID)</th>
              <td class="teal lighten-5">{{email}}</td>
          </tr>
          <tr>
              <th class="white text-center teal--text">Name</th>
              <td class="white">
                <v-text-field
                  type="text"
                  class="form-control"
                  id="name"
                  ref="name"
                  label="이름을 입력하세요"
                  color="cyan darken-2"
                  v-model="name"
                />
              </td>
          </tr>
          <tr>
              <th class="teal lighten-5 text-center teal--text">NickName</th>
              <td class="teal lighten-5">
                <v-text-field
                  type="text"
                  class="form-control"
                  id="nick"
                  ref="nick"
                  label="별명을 입력하세요"
                  color="cyan darken-2"
                  v-model="nickName"
                />
              </td>
          </tr>
          <tr>
              <th class="white text-center teal--text">Self-introduction</th>
              <td class="white text-center">
                <v-textarea style="margin-top: 15px;"
                color="cyan darken-2"
                outlined
                counter="50"
                label="자신을 소개하세요."
                height="100"
                v-model="intro">
                </v-textarea>
              </td>
          </tr>
          <tr>
              <th class="teal lighten-5 text-center teal--text">Authority</th>
              <td class="teal lighten-5" v-if="!valid">권한이 없습니다.
                이메일 인증을 통해 권한을 얻으세요!
              </td>
              <td class="teal lighten-5" v-else>인증된 사용자입니다.</td>
          </tr>
          <tr>
              <th class="white text-center teal--text">Joined At</th>
              <td class="white">{{getFormatDate(joinedAt)}}</td>
          </tr>
          <tr class="cyan darken-2">
            <td>
            <div>
              <v-btn inline-block @click="modify" class="cyan darken-3 white--text">수정</v-btn>
            </div>
            </td>
            <td class="text-right">
              <div>
                <v-btn inline-block @click="confirmSignOut" class="cyan darken-3 white--text">탈퇴</v-btn>
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
import { mapGetters, mapState } from 'vuex';
import http from "@/util/http-common";
import http3 from "@/util/http-common3";
import moment from "moment";
import { AUTH_LOGOUT } from "@/store/actions/auth";

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
      imageUrl : "",
      alert : false,
      alertMsg : "",
      dialog: false,
      fileInfo: null,
      firstImage: true,
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
  methods: {
    onChangeImages(e) {
      const file = e.target.files[0];
      this.fileInfo = file;
      this.imageUrl = URL.createObjectURL(file);
      this.firstImage = false;
    },
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
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
      if(this.fileInfo != null) {
        console.log("asdasd");
        var formData = new FormData();
        formData.append('img', this.fileInfo);
        http3
          .post(`/user/img`, formData).then(({ data }) => {
            http
            .put(`/users/${this.getUserNum}`, {
              name: this.name,
              nickname: this.nickName,
              intro : this.intro,
              imagesrc : data,
            })
            .then(({ data }) => {
              let msg = "수정 처리시 문제가 발생했습니다.";
              if (data === "success") {
                msg = "수정이 완료되었습니다.";
              }
              this.alertMsg = msg;
              this.alert = true;
              this.$emit("closeUserInfoModal", this.alertMsg, this.nickName);
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
          }).catch((e) => {
        if (e.request.status === 404){
          this.alertMsg = "등록 처리시 에러가 발생했습니다.";
          this.alert = true;
        } else{
          this.$router.push(`/apierror/${e.request.status}/`)
        }
        console.log(e.request.status)
        });
      }else{
        http
        .put(`/users/${this.getUserNum}`, {
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
          this.$emit("closeUserInfoModal", this.alertMsg, this.nickName);
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
      }
    },
    signOut() {
      http
        .delete(`/users/delete/${this.getUserNum}`)
        .then(() => {
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
