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
              <input ref="imageInput" type="file" hidden @change="onChangeImages">
              <v-btn type="button" @click="onClickImageUpload">이미지 업로드</v-btn>
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


    <v-dialog
        v-model="dialog"
        max-width="350"
      >
        <v-card>
          <v-card-title class="headline">임시 저장된 게시글이 있습니다. </v-card-title>
  
          <v-card-text>
            불러오시겠습니까?
          </v-card-text>
  
          <v-card-actions>
            <v-spacer></v-spacer>
  
            <v-btn
              color="blue darken-1"
              text
              @click="deleteData"
            >
              취소
            </v-btn>
  
            <v-btn
              color="blue darken-1"
              text
              @click="loadData"
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
import http3 from "@/util/http-common3";
import { mapGetters, mapState } from 'vuex';

export default {
  name: "ArticleWriteComp",
  data() {
    return {
      articleContent: "",
      articleTitle: "",
      alert : false,
      alertMsg : "",
      registSuccess: false,
      imageUrl: null,
      fileInfo: "",
      polling: null,
      dialog: false,
    };
  },
  created() {
      this.start()
  },
  mounted() {
      if(window.localStorage.getItem("isSaved") == "true") {
          this.confirmLoadData();
      }
  },
  methods: {
    deleteData() {
        this.storeClean();
        this.dialog = false;
    },
    storeClean() {
        window.localStorage.removeItem('isSaved');
        window.localStorage.removeItem('textval');
        window.localStorage.removeItem('articleTitle');
    },
    loadData() {
        this.articleContent = window.localStorage.getItem("textval");
        this.articleTitle = window.localStorage.getItem('articleTitle');
        this.dialog = false;
    },
    confirmLoadData() {
        this.dialog = true;
    },
    storeInLocal() {
        if ((window.localStorage) && (this.articleContent || this.articleTitle)){
            window.localStorage.setItem('isSaved', 'true');
            window.localStorage.setItem('textval', this.articleContent);
            window.localStorage.setItem('articleTitle', this.articleTitle);
        }
        else if (!window.localStorage) {
            console.log('Error' + ': Your browser not support')
        }
    },
   temp() {
        this.storeInLocal();
        this.$router.push(`/article/list/${this.getUserNum}`);
   },
   start: function() {
        this.polling = setInterval( () => {
            this.storeInLocal();
        }, 60000)
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
      this.registHandler();
    }
   },
   registHandler() {
     var formData = new FormData();
     formData.append('img', this.fileInfo);
     http3
      .post(`/article/img`, formData).then(({ data }) => {
        http
          .post(`/article`, {
            thumbnail : data,
            title: this.articleTitle,
            content: this.articleContent,
            created_at: new Date(),
            user_num: this.getUserNum,
          }).then(({ data }) => {
            let msg = "등록 처리시 문제가 발생했습니다.";
            if (data === "success") {
              this.registSuccess = true;
              msg = "등록이 완료되었습니다.";
            }
            this.alertMsg = msg;
            this.alert = true;
            this.registSuccess = true;
            this.storeClean();
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
      console.log("asdasd");
      const file = e.target.files[0];
      this.fileInfo = file;
      this.imageUrl = URL.createObjectURL(file);
      console.log(this.imageUrl);
    },
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
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
