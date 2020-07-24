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
    articleTitle: { type: String },
    articlePlace: { type: String },
    articleDateStart: { type: String },
    articleDateEnd: { type: String },
    articleCreatedAt: { type: String },
    articleContent: {type: String},
    blogMasterName: {type: String}
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
