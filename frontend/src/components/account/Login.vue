<template>
  <div id="main_wrap" style="width:800px;">
    <div id="middle_wrap" v-if="visablelogin & !nowlogin">
      <v-container>
        <div>
          <v-tabs v-model="tab" show-arrows background-color="black accent-4" icons-and-text dark grow>
          <v-tabs-slider></v-tabs-slider>
          <v-tab v-for="i in tabs" :key="i">
            <v-icon large>{{i.icon}}</v-icon>
            <div class="caption py-1">{{i.name}}</div> 
            </v-tab>
            <v-tab-item>
              <v-card class="px-4">
                <v-card-text>
                  <v-form @submit.prevent="login" >
                    <v-row>
                      <v-col cols="2"><p style="margin-top:10px;font-weight:bold;"> 이메일 :  </p></v-col>
                      <v-col cols="10">
                         <v-text-field v-model="email" label="E-mail" required
                        type="text"
                        outlined
                        value
                        placeholder="이메일을 입력하세요"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field v-model="password" label="Password"
                        
                        id="_pwd"
                        name="pwd"
                        required
                        placeholder="패스워드를 입력하세요"
                        title="패스워드"></v-text-field>
                      </v-col>
                       <v-col class="d-flex" cols="12" sm="6" xsm="12">
                        </v-col>
                      <v-spacer></v-spacer>
                      <v-col class="d-flex" cols="12" sm="3" xsm="12" align-end>
                        <v-btn type="submit" x-large block color="black"><p style="color:white;"> Login</p></v-btn>
                      </v-col>
                    </v-row>

                  </v-form>
                </v-card-text>
              </v-card>
            </v-tab-item>

            <v-tab-item v-if="!submitted">
              <v-card class="px-4">
                <v-card-text>
                  <v-form action
                    method="post"
                    id="_frmForm"
                    name="frmForm"
                    @submit.prevent="registerMyself">
                    <v-row>
                      <v-col cols="12">
                        <v-text-field v-model="customer.email" label="E-mail" required
                        placeholder="이메일을 입력하세요"
                        id="_email"
                        name="email"
                        type="email"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field v-model="customer.nickname"
                          data-msg="별명"
                          name="nickname"
                          id="_nickname"
                          placeholder="별명을 입력하세요"
                          label="Nickname"></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field v-model="customer.cname"
                        label="Name"
                        required
                        placeholder="이름을 입력하세요"
                        id="_cname"
                        name="cname"
                        data-msg="이름"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field block v-model="customer.password"
                          type="password"
                          data-msg=" 패스워드"
                          name="password"
                          id="_password"
                        placeholder="패스워드를 입력하세요"
                        label="Password">

                        </v-text-field>

                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                        data-msg="패스워드 재확인"
                        type="password"
                        name="password_confirm"
                        id="_password_confirm"
                        placeholder="패스워드를 다시 입력하세요"
                        v-model="password_confirm"
                        block
                        label="Confirm-password"
                       
                        ></v-text-field>
                      </v-col>
                      <v-spacer></v-spacer>
                      <v-col class="d-flex ml-auto" cols="12">
                          <v-btn type="submit" x-large block color="black"><p style="color:white;">Register</p> </v-btn>
                      </v-col>
                    </v-row>

                  </v-form>
                </v-card-text>
              </v-card>
            </v-tab-item>
            <div class="search_box" v-else>
             <h3>
                 <span style="color:blue;">성공적으로 회원가입을 완료하였습니다!</span>
              </h3>
             </div>
      </v-tabs>
        </div>
      </v-container>
      
      
      
      <div id="content_wrap">


          

        <!-- <div
        >
          <div id="login_wrap" style="position: absolute; top:20%; width:100%;">
            <v-form class="login" @submit.prevent="login">
              <v-simple-table class="content_table">
                <colgroup>
                  <col style="width:20%;" />
                  <col style="width:80%;" />
                </colgroup>
                <tbody>
                  <tr>
                    <td colspan="2"><h2>로그인</h2></td>
                  </tr>
                  <tr>
                    <th style="background-color:#eeeeee; color:#3e5fba; text-align:center;">
                      이메일
                    </th>
                    <td>
                      &nbsp;
                      <input
                        style="width:80%;height:80%;"
                        type="text"
                        id="_email"
                        name="email"
                        value
                        data-msg="이메일을"
                        title="이메일"
                        required
                        v-model="email"
                        placeholder="이메일을 입력하세요."
                      />
                    </td>
                  </tr>
                  <tr>
                    <th style="background-color:#eeeeee; color:#3e5fba;">
                      패스워드
                    </th>
                    <td>
                      &nbsp;
                      <input
                        style="width:80%;height:80%;"
                        type="password"
                        id="_pwd"
                        name="pwd"
                        value
                        v-model="password"
                        required
                        placeholder="패스워드를 입력하세요."
                        title="패스워드"
                      />
                    </td>
                  </tr>
                  <tr>
                    <td colspan="2" style="height:50px; text-align:center;">
                      <v-btn type="submit">로그인</v-btn> &nbsp;&nbsp;
                      <v-btn
                        type="button"
                        @click="moveToSignUp"
                      >
                        회원가입으로 이동
                      </v-btn>
                      <v-btn @click="close">취소</v-btn>
                    </td>
                  </tr>
                </tbody>
              </v-simple-table>
            </v-form>
          </div>
        </div> -->
      </div>
    </div>
    <!-- <div v-if="!visablelogin && !nowlogin" style="position:absolute; top:10%; width:800px;">
      <div v-if="!submitted" style="width:100%">
        <form
          action
          method="post"
          id="_frmForm"
          name="frmForm"
          @submit.prevent="registryMyself"
        >
          <v-simple-table class="content_table">
            <colgroup>
              <col style="width:20%;" />
              <col style="width:80%;" />
            </colgroup>
            <tbody>
              <tr> 
                <td colspan="2"><h2>회원가입</h2></td>
              </tr>
            <tr>
              <th style="text-align:center;">이메일</th>
              <td>
                <input
                  style="width:50%;height:80%;"
                  size="30"
                  data-msg="이메일"
                  type="email"
                  name="email"
                  id="_email"
                  placeholder="이메일을 입력하세요."
                  v-model="customer.email"
                />
                ex) trip123@naver.com
              </td>
            </tr>
            <tr>
              <th style="text-align:center;">별명</th>
              <td>
                <input
                  style="width:80%;height:80%;"
                  size="30"
                  data-msg="별명"
                  type="text"
                  name="nickname"
                  id="_nickname"
                  placeholder="별명을 입력하세요."
                  v-model="customer.nickname"
                />
              </td>
            </tr>
            <tr>
              <th style="text-align:center;">이름</th>
              <td>
                <input
                  style="width:80%;height:80%;"
                  size="30"
                  data-msg="이름"
                  type="text"
                  name="cname"
                  id="_cname"
                  placeholder="이름을 입력하세요."
                  v-model="customer.cname"
                />
              </td>
            </tr>
            <tr>
              <th style="text-align:center;">패스워드</th>
              <td>
                <input
                  style="width:80%;height:80%;"
                  size="30"
                  data-msg="패스워드"
                  type="password"
                  name="password"
                  id="_password"
                  placeholder="패스워드를 입력하세요."
                  v-model="customer.password"
                />
              </td>
            </tr>
            <tr>
              <th style="text-align:center;">패스워드 재확인</th>
              <td>
                <input
                  style="width:80%;height:80%;"
                  size="30"
                  data-msg="패스워드 재확인"
                  type="password"
                  name="password_confirm"
                  id="_password_confirm"
                  placeholder="패스워드를 다시 입력하세요."
                  v-model="password_confirm"
                />
              </td>
            </tr>

            <tr>
              <td colspan="2" style="height:50px; text-align:center;">
                <v-btn type="submit" name="button">회원가입</v-btn
                >&nbsp;&nbsp;
                <v-btn type="button" @click="moveToSignIn">
                  로그인으로 이동
                </v-btn>
                <v-btn @click="close">취소</v-btn>
              </td>
            </tr>
            </tbody>
          </v-simple-table>
        </form>
      </div>

      <div class="search_box" v-else>
        <h3>
          <span style="color:blue;">성공적으로 회원가입을 완료하였습니다!</span>
        </h3>
      </div>
    </div>

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
      </v-snackbar> -->
  </div>
</template>
<script>
import { AUTH_REQUEST } from "@/store/actions/auth";
import http2 from "@/util/http-common2.js";
export default {
  name: "login",
  data() {
    return {
      tab: 0,
      tabs: [
          {name:"Login", icon:"mdi-account"},
          {name:"Register", icon:"mdi-account-outline"}
       ],
       show1:false,
      nowlogin: false,
      visablelogin: true,
      email: "",
      password: "",
      customer: {
        nickname: "",
        cname: "",
        email: "",
        password: ""
      },
      submitted: false,
      alert: false,
      alertMsg: "",
      password_confirm: "",
    };
  },
  methods: {
    login: function() {
      //model에 바인딩된 데이터 모두 ==> this
      const { email, password } = this;
      this.$store
        .dispatch(AUTH_REQUEST, { email, password })
        .then(() => {
          this.email = "";
          this.password = "";
          this.nowlogin = !this.nowlogin
          this.$emit("closeLoginModal");
        })
        .catch((e) => {
          if (e.request.status === 404){
            this.alertMsg = "로그인에 실패하였습니다. 아이디와 비밀번호를 확인하세요.";
            this.alert = true;
          } else{
            this.$emit("closeLoginModal");
            this.$router.push(`/apierror/${e.request.status}/`)
          }
          console.log(e.request.status)
          
        });
        // .catch(() => {
        //   this.alertMsg = "로그인에 실패하였습니다. 이메일과 비밀번호를 확인하세요.";
        //   this.alert = true;
        // });
    },
    registryMyself() {
      if (this.customer.email == "") {
        this.alertMsg = "이메일을 입력하세요.";
        this.alert = true;
        return;
      }
      if (this.customer.nickname == "") {
        this.alertMsg = "별명을 입력하세요.";
        this.alert = true;
        return;
      }
      if (this.customer.cname == "") {
        this.alertMsg = "이름을 입력하세요.";
        this.alert = true;
        return;
      }
      if (this.customer.password == "") {
        this.alertMsg = "패스워드를 입력하세요.";
        this.alert = true;
        return;
      }
      if (this.customer.password != this.password_confirm) {
        this.alertMsg = "재입력한 패스워드가 다릅니다.";
        this.alert = true;
        return;
      }
      if(!/^[a-zA-Z0-9]{8,20}$/.test(this.customer.password))
      { 
        this.alertMsg = "비밀번호는 숫자와 영문자 조합으로 8~20자리를 사용해야 합니다.";
        this.alert = true;
        return;
      }
      var chk_num = this.customer.password.search(/[0-9]/g);
      var chk_eng = this.customer.password.search(/[a-z]/ig);
      if(chk_num < 0 || chk_eng < 0)
      {
        this.alertMsg = "비밀번호는 숫자와 영문자를 혼용하여야 합니다.'";
        this.alert = true;
        return;
      }

      http2
        .post("/signup", {
          nickname: this.customer.nickname,
          name: this.customer.cname,
          email: this.customer.email,
          password: this.customer.password
        })
        .then(response => {
          //console.log(response.data);
          if (
            response.data.success == true ||
            response.data.success == "true"
          ) {
            this.alertMsg = "성공적으로 가입 하였습니다. 이메일인증을 해주세요.";
            this.alert = true;
            this.visablelogin = true;
            this.submitted = true;
            this.newCustomer();
          } else {
            this.alertMsg = response.data.message;
            this.alert = true;
            this.visablelogin = false;
            this.submitted = false;
          } 
        }).catch((e) => {
          if (e.request.status === 404){
            this.alertMsg = "회원가입에 실패하였습니다.";
            this.alert = true;
          } else{
            this.visablelogin = false;
            this.submitted = false;
            this.newCustomer();
            this.$router.push(`/apierror/${e.request.status}/`)
          }
          console.log(e.request.status)
        });
        // .catch(() => {
        //   this.alertMsg = "회원가입에 실패하였습니다.";
        //   this.alert = true;
        //   this.visablelogin = false;
        //   this.submitted = false;
        //   this.newCustomer();
        // });
    },
    newCustomer() {
      this.customer = {
        nickname: "",
        cname: "",
        email: "",
        password: ""
      };
    },
    close() {
      this.newCustomer();
      this.email="";
      this.password="";
      this.visablelogin= true;
      this.submitted= false;
      this.$emit("closeLoginModal");
    },
    moveToSignUp() {
      this.visablelogin = !this.visablelogin;
      this.email = "";
      this.password = "";
    },
    moveToSignIn() {
      this.visablelogin = !this.visablelogin;
      this.newCustomer();
    },
    

  },
  computed: {
    passwordMatch() {
      return () => this.password === this.verify || "Password must match";
    }
  },

};
</script>

<style scoped>
th {
  background-color:#eeeeee; 
  color:#3e5fba;
}
</style>
