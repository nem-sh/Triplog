<template>
  <div style="min-width: 1000px;">
    <div>
      <h1>{{articleTitle}}</h1>
      <h4 style="color:gray">{{blogMasterName}}</h4>
      <h4 style="color:gray">{{getFormatDate(articleCreatedAt)}}</h4>
    </div>
    <br>
    <v-divider/>
    <v-tabs
        v-model="tab"
      >
        <v-tab
          v-for="item in tabItems"
          :key="item.tab"
        >
          {{ item.tab }}
        </v-tab>
      </v-tabs>
  
      <v-tabs-items v-model="tab">
        <v-tab-item
          v-for="item in tabItems"
          :key="item.tab"
        >
          <v-card flat>
            <v-card-text>{{ item.content }}</v-card-text>
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    <v-divider/>
    <br />
    <div align="center">
    <v-img v-if="articleThumbnail" :src="require(`@/assets/articleImage/${articleThumbnail}`)" class="img" />
    <v-img v-else :src="require(`@/assets/articleImage/noimage.png`)" class="img" />
    </div>
    <br/>
    <article-content-comp
      :content = "realContent"
      v-if="realContent"
    />
    <v-divider/>
    <v-row 
      class="ma-1"
      align="center"
      justify="space-between"
      >
      <v-btn
        class="ml-6"
        icon
        :disabled = "likeBtnFlag"
        :loading = "likeBtnFlag"
      >
      <v-chip
        @click="likeBtnClick" 
        color="pink"
        text-color="white"
      >
        <v-avatar
          left
          class="pink darken-4"
        >
          {{articleLikeCount}}
        </v-avatar>

        <v-icon v-if="isLoginedUserLikeThisArticle">mdi-heart</v-icon>
        <v-icon v-else>mdi-heart-outline</v-icon>
      </v-chip>
      </v-btn>
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
          <v-btn 
            @click="confirmDelete"
            v-if="btnToggle"
            tile
            outlined
            class="ma-2"
          >
          <v-icon left>mdi-delete</v-icon>
          삭제</v-btn>
        
      </v-sheet>
    </v-row>
    <v-row
      justify="end"
      class="ma-1"
      >
      <v-btn 
        tile
        outlined
        class="mr-2"
        :to="{ name: 'articleList', params: { hostNum: articleUserNum }}"
        >
        <v-icon left>mdi-arrow-left</v-icon>
        뒤로
      </v-btn>
    </v-row>
    
    <br/>
    <br/>
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
    isLoginedUserLikeThisArticle: { type: Boolean }
  },
  data: function() {
    return {
      alert: false,
      alertMsg: "",
      dialog: false,
      realContent: "",
      btnToggle: false,
      likeBtnFlag: false,
      tab: null,
      tabItems: [
        { tab: '장소', content: this.articlePlace },
        { tab: '일정', content: this.getFormatDate(this.articleDateStart) + ' ~ ' + this.getFormatDate(this.articleDateEnd) },
      ],
    };
  },
  mounted() {
    if(this.getUserNum == this.articleUserNum) this.btnToggle = true;
  },
  created() {
    console.log(this.getUserNum);
    console.log(this.articleUserNum);
    this.openContentFile()
  },
  methods: {
    openContentFile: function() {
      var url = '../../content/registered/' + this.articleContent;
      var xhr = new XMLHttpRequest();
      xhr.responseType = 'text';
      var setRealContent = (val) => {this.realContent = val};
      xhr.onload = function(e) {
        var resp = xhr.responseText || e.target.responseText;
        setRealContent(resp);
      }
      xhr.open('GET', url);
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
      this.likeBtnFlag = true;
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
          this.likeBtnFlag = false;
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