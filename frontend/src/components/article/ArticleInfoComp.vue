<template>
  <div>
    <v-simple-table>
      <template v-slot:default borderless>
        <tbody>
            <tr>
                <th class="text-left">제목</th>
                <td class="text-left">{{articleTitle}}</td>
            </tr>
            <tr>
                <th class="text-left">장소</th>
                <td class="text-left">{{articlePlace}}</td>
            </tr>
            <tr>
                <th class="text-left">일정 시작 날짜</th>
                <td class="text-left">{{getFormatDate(articleDateStart)}}</td>
            </tr>
            <tr>
                <th class="text-left">일정 종료 날짜</th>
                <td class="text-left">{{getFormatDate(articleDateEnd)}}</td>
            </tr>
            <tr>
                <th class="text-left">작성일</th>
                <td class="text-left">{{getFormatDate(articleCreatedAt)}}</td>
            </tr>
            <tr>
                <td colspan="2">
                    <v-p>
                        {{articleContent}}
                    </v-p>
                </td>
            </tr>
            <tr>
              <td>
                <v-btn @click="likeBtnClick">
                  <v-icon v-if="isLoginedUserLikeThisArticle">heart</v-icon>
                  <v-icon v-else>heart-outline</v-icon>
                </v-btn>
                <v-content>{{articleLikeCount}}</v-content>
              </td>
            </tr>
        </tbody>
      </template>
    </v-simple-table>
    <br />
    <br />
    <div class="text-center">
        <v-btn to="/noticeList" v-if="getProfile === blogMasterName">수정</v-btn>
        <v-btn @click="deleteArticle" v-if="getProfile === blogMasterName">삭제</v-btn>
        <v-btn to="/noticeList">목록</v-btn>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import moment from "moment";
import http from "@/util/http-common";

export default {
  name: "articleInfoComp",
  props: {
    articleNum: {type: Number},
    articleUserNum: {type: Number},
    articleThumbnail: {type: String},
    articleTemp: {type: Boolean},
    articleTitle: { type: String },
    articlePlace: { type: String },
    articleDateStart: { type: String },
    articleDateEnd: { type: String },
    articleCreatedAt: { type: String },
    articleContent: {type: String},
    blogMasterName: {type: String},
    articleLikeCount: {type: Number},
    isLoginedUserLikeThisArticle: {type: Boolean}
  },
  methods: {
    getFormatDate(regtime) {
      return moment(new Date(regtime)).format("YYYY.MM.DD HH:mm:ss");
    },
    deleteArticle: function() {
        http
        .delete(`/article/${this.articleNum}`)
        .then(({ data }) => {
            // let msg = "삭제 처리시 문제가 발생했습니다.";
            if (data === "success") {
            // msg = "삭제가 완료되었습니다.";
            }
            // this.alertMsg = msg;
            // this.alert = true;
            // this.$router.push("/qna");
        })
        .catch(() => {
            // this.alertMsg = "삭제 처리시 에러가 발생했습니다.";
            // this.alert = true;
        });
    },
    likeBtnClick: function() {
      if(this.isLoginedUserLikeThisArticle) {
        this.articleLikeCount--;
      } else {
        this.articleLikeCount++;
      }
      this.isLoginedUserLikeThisArticle = !this.isLoginedUserLikeThisArticle;

      http
        .put(`/article/${this.articleNum}/${this.getProfile}/${this.isLoginedUserLikeThisArticle}`, {
          num: this.articleNum,
          user_num: this.articleUserNum,
          trippackage_num: this.articleTripPackageNum,
          title: this.articleTitle,
          place: this.articlePlace,
          content: this.articleContent,
          thumbnail: this.articleThumbnail,
          temp: this.articleTemp,
          created_at: this.articleCreatedAt,
          date_start: this.articleDateStart,
          date_end: this.articleDateEnd,
          likeCount: this.articleLikeCount
        })
        .then(({ data }) => {
          let msg = "수정 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "수정이 완료되었습니다.";
          }
          this.alertMsg = msg;
          this.alert = true;
        //   this.moveList();
        })
        .catch(() => {
          this.alertMsg = "수정 처리시 에러가 발생했습니다.";
          this.alert = true;
          });
    }
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
