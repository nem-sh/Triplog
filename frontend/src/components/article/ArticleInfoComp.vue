<template>
  <v-container>
    <div>
      <h1>{{articleTitle}}</h1>
      <h4 style="color:gray; cursor:pointer" @click="goToBlog">{{blogMasterName}}</h4>
      <h4 style="color:gray">{{getFormatDate(articleCreatedAt)}}</h4>
      <h4 style="color:gary">{{ articlePlace }}</h4>
    </div>
    <br />
    <v-divider />
    <br />
    <div @click="clickThis" @mouseover="hoverThis" @mouseout="nonHoverThis" :style="cursorStyle">
      <article-content-comp :content="realContent" v-if="realContent" />
    </div>
    <v-divider />
    <v-row class="ma-1" align="center">
      <v-btn class="ml-6" icon :disabled="likeBtnFlag" :loading="likeBtnFlag">
        <v-chip @click="likeBtnClick" color="pink" text-color="white">
          <v-avatar left class="pink darken-4">{{likeCount}}</v-avatar>

          <v-icon v-if="isLoginedUserLikeThisArticleprop">mdi-heart</v-icon>
          <v-icon v-else>mdi-heart-outline</v-icon>
        </v-chip>
      </v-btn>

      <v-chip class="ml-6" color="light-green" text-color="white">
        <p class="text-align-center ma-0">view</p>
        <v-avatar right class="green">{{articleViews}}</v-avatar>
      </v-chip>
      <v-spacer></v-spacer>

      <v-sheet>
        <v-btn
          :to="{ name: 'articleModify', params: { articleNum: articleNum }}"
          v-if="btnToggle"
          tile
          outlined
        >
          <v-icon left>mdi-pencil</v-icon>
          <span>수정</span>
        </v-btn>
        <v-btn @click="confirmDelete" v-if="btnToggle" tile outlined class="ma-2">
          <v-icon left>mdi-delete</v-icon>삭제
        </v-btn>
      </v-sheet>

    </v-row>
    <v-row justify="end" class="ma-1">
      <v-btn tile outlined class="mr-2" @click="goBack">
        <v-icon left>mdi-arrow-left</v-icon>뒤로
      </v-btn>
    </v-row>

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
  </v-container>
</template>

<script>
import ArticleContentComp from "@/components/article/ArticleContentComp.vue";
import { mapGetters, mapState } from "vuex";
import moment from "moment";
import http from "@/util/http-common";

export default {
  name: "articleInfoComp",
  components: {
    ArticleContentComp
  },
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
    isLoginedUserLikeThisArticle: { type: Boolean },
    articleViews: { type: Number },
    onOffParagraph: { type: Boolean }
  },
  data: function() {
    return {
      isLoginedUserLikeThisArticleprop: this.isLoginedUserLikeThisArticle,
      cursorEventOn: false,
      paragraphInfo: {
        paragraph: "",
        choiceId: null
      },
      alert: false,
      alertMsg: "",
      dialog: false,
      realContent: "",
      btnToggle: false,
      likeBtnFlag: false,
      views: 0
    };
  },
  mounted() {
    if (this.getUserNum == this.articleUserNum) this.btnToggle = true;
    // this.$refs.calendar.checkChange();
  },
  created: function() {
    this.likeCount = this.articleLikeCount;
    this.openContentFile();
  },
  methods: {
    clickThis() {
      var e = window.event,
        btn = e.target || e.srcElement;
      this.paragraphInfo.paragraph = btn.innerText;
      if (btn.id != "") {
        this.paragraphInfo.choiceId = btn.id;
      }
      this.$emit("send-paragraph-info", this.paragraphInfo);
    },
    hoverThis() {
      this.cursorEventOn = true;
    },
    nonHoverThis() {
      this.cursorEventOn = false;
    },
    goToBlog: function() {
      this.$router.push(`/article/list/${this.articleUserNum}`);
    },
    viewDay({ date }) {
      this.focus = date;
      this.type = "day";
    },
    getEventColor(event) {
      return event.color;
    },
    setToday() {
      this.focus = "";
    },
    prev() {
      this.$refs.calendar.prev();
    },
    next() {
      this.$refs.calendar.next();
    },
    showEvent({ nativeEvent, event }) {
      const open = () => {
        this.selectedEvent = event;
        this.selectedElement = nativeEvent.target;
        setTimeout(() => (this.selectedOpen = true), 10);
      };

      if (this.selectedOpen) {
        this.selectedOpen = false;
        setTimeout(open, 10);
      } else {
        open();
      }

      nativeEvent.stopPropagation();
    },
    updateRange({ start, end }) {
      const events = [];

      const min = new Date(`${start.date}T00:00:00`);
      const max = new Date(`${end.date}T23:59:59`);
      const days = (max.getTime() - min.getTime()) / 86400000;
      const eventCount = this.rnd(days, days + 20);

      for (let i = 0; i < eventCount; i++) {
        const allDay = this.rnd(0, 3) === 0;
        const firstTimestamp = this.rnd(min.getTime(), max.getTime());
        const first = new Date(firstTimestamp - (firstTimestamp % 900000));
        const secondTimestamp = this.rnd(2, allDay ? 288 : 8) * 900000;
        const second = new Date(first.getTime() + secondTimestamp);

        events.push({
          name: this.names[this.rnd(0, this.names.length - 1)],
          start: first,
          end: second,
          color: this.colors[this.rnd(0, this.colors.length - 1)],
          timed: !allDay
        });
      }

      this.events = events;
    },
    rnd(a, b) {
      return Math.floor((b - a + 1) * Math.random()) + a;
    },
    // 여기까지 캘린더 메소드
    goBack: function() {
      window.history.back();
    },
    openContentFile: function() {
      var url = "../../content/registered/" + this.articleContent;
      var xhr = new XMLHttpRequest();
      xhr.responseType = "text";
      var setRealContent = val => {
        this.realContent = val;
      };
      xhr.onload = function(e) {
        var resp = xhr.responseText || e.target.responseText;
        setRealContent(resp);
      };
      xhr.open("GET", url);
      xhr.send();
    },
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
          this.dialog = false;
          // this.$emit("useSnackBar", this.alertMsg);
          this.$router.push(`/article/list/${this.getUserNum}`);
        })
        .catch(() => {});
    },
    likeBtnClick: function() {
      if (this.getUserNum != "") {
        this.likeBtnFlag = true;
        if (this.isLoginedUserLikeThisArticleprop) {
          this.likeCount--;
        } else {
          this.likeCount++;
        }
        this.isLoginedUserLikeThisArticleprop = !this
          .isLoginedUserLikeThisArticleprop;

        http
          .put(
            `/article/${this.articleNum}/${this.getUserNum}/${this.isLoginedUserLikeThisArticleprop}`,
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
              likeCount: this.likeCount,
              viws: this.articleViews
            }
          )
          .then(({ data }) => {
            let msg = "수정 처리시 문제가 발생했습니다.";
            if (data === "success") {
              msg = "수정이 완료되었습니다.";
            }
            this.alertMsg = msg;
            this.alert = true;
            this.likeBtnFlag = false;
          })
          .catch(() => {
            this.alertMsg = "수정 처리시 에러가 발생했습니다.";
            this.alert = true;
          });
      } else {
        alert("먼저 로그인을 진행해주세요");
      }
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
    },
    cursorStyle() {
      if (this.cursorEventOn && this.onOffParagraph) {
        return "cursor: pointer;";
      } else {
        return "";
      }
    }
  }
};
</script>

<style>
.img {
  max-width: 500px;
}
</style>