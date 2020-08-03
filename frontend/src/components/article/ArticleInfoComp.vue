<template>
  <div style="min-width: 1000px;">
    <h1>{{articleTitle}}</h1>
    <h4 style="color:gray">{{blogMasterName}} | {{getFormatDate(articleCreatedAt)}}</h4>
    <br />
    <hr width="100%" color="whitegray" />
    <br />
    <v-simple-table>
      <template v-slot:default borderless>
        <tbody>
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
        </tbody>
      </template>
    </v-simple-table>
    <br />
    <div align="center">
    <v-img v-if="articleThumbnail" :src="require(`@/assets/blogImage/${articleThumbnail}`)" class="img" />
    <v-img v-else :src="require(`@/assets/blogImage/noimage.png`)" class="img" />
    </div>
    <br/>
    <p>{{articleContent}}</p>
    <!-- 본문 끝 -->
    <br/><br/>
    <div>
      <v-btn @click="likeBtnClick" style="float: left;">
        <v-icon v-if="isLoginedUserLikeThisArticle">mdi-heart</v-icon>
        <v-icon v-else>mdi-heart-outline</v-icon>
        {{articleLikeCount}}
      </v-btn>

      <div style="float: right;">
        <!-- <v-btn
          :to="{ name: 'articleModify', params: { articleNum: articleNum }}"
          v-if="this.getUserNum === articleUserNum"
        >수정</v-btn>
        <v-btn @click="confirmDelete" v-if="this.getUserNum === articleUserNum">삭제</v-btn> -->
        <v-btn
          :to="{ name: 'articleModify', params: { articleNum: articleNum }}"
        >수정</v-btn>
        <v-btn @click="confirmDelete">삭제</v-btn>
        <v-btn :to="{ name: 'articleList', params: { hostNum: articleUserNum }}">목록</v-btn>
      </div>
    </div>
    <br />
    <br />
    <br />
    <br />
    <br />
    <br />
    <v-dialog v-model="dialog" max-width="350">
      <v-card>
        <v-card-title class="headline">정말 삭제하시겠습니까?</v-card-title>

        <v-card-text>삭제하면 게시글 복구가 불가능합니다.</v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn color="blue darken-1" text @click="dialog = false">취소</v-btn>

          <v-btn color="blue darken-1" text @click="deleteArticle">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import moment from "moment";
import http from "@/util/http-common";

export default {
  name: "articleInfoComp",
  props: {
    articleNum: { type: Number },
    articleUserNum: { type: Number },
    articleThumbnail: { type: String },
    articleTemp: { type: Boolean },
    articleTitle: { type: String },
    articlePlace: { type: String },
    articleDateStart: { type: String },
    articleDateEnd: { type: String },
    articleCreatedAt: { type: String },
    articleContent: { type: String },
    blogMasterName: { type: String },
    articleLikeCount: { type: Number },
    isLoginedUserLikeThisArticle: { type: Boolean }
  },
  data: function() {
    return {
      alert: false,
      alertMsg: "",
      dialog: false
    };
  },
  methods: {
    confirmDelete() {
      this.dialog = true;
    },
    getFormatDate(regtime) {
      return moment(new Date(regtime)).format("YYYY.MM.DD HH:mm:ss");
    },
    deleteArticle: function() {
      http
        .delete(`/article/${this.articleNum}`)
        .then(({ data }) => {
          let msg = "삭제 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "삭제가 완료되었습니다.";
          }
          this.alertMsg = msg;
          // this.alert = true;
          this.dialog = false;
          this.$emit("useSnackBar", this.alertMsg);
          this.$router.push(`/article/list/${this.getUserNum}`);
        })
        .catch(() => {
          // this.alertMsg = "삭제 처리시 에러가 발생했습니다.";
          // this.alert = true;
        });
    },
    likeBtnClick: function() {
      if (this.isLoginedUserLikeThisArticle) {
        this.articleLikeCount--;
      } else {
        this.articleLikeCount++;
      }
      this.isLoginedUserLikeThisArticle = !this.isLoginedUserLikeThisArticle;

      http
        .put(
          `/article/${this.articleNum}/${this.getUserNum}/${this.isLoginedUserLikeThisArticle}`,
          {
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
          }
        )
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
  }
};
</script>

<style>
.img {
  max-width: 500px;
}
</style>