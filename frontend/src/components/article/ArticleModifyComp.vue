<template>
  <div>
    <v-simple-table>
      <template v-slot:default borderless>
        <tbody>
            <tr>
                <th class="text-left">제목</th>
                <td class="text-left">
                    <v-text-field
                    label="제목을 입력하세요"
                    id="articleTitle"
                    ref="articleTitle"
                    v-model="articleTitle"
                    ></v-text-field>
                </td>
            </tr>
            <tr>
                <th class="text-left">장소</th>
                <td class="text-left">
                    <v-text-field
                    label="장소를 입력하세요"
                    id="articlePlace"
                    ref="articlePlace"
                    v-model="articlePlace"
                    ></v-text-field>
                </td>
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
                <td colspan="2">
                  <br/>
                    <v-textarea
                        outlined
                        name="input-7-1"
                        label="내용을 입력하세요"
                        id="articleContent"
                        ref="articleContent"
                        v-model="articleContent"
                        auto-grow
                        row-height="100px"
                    ></v-textarea>
                </td>
            </tr>
        </tbody>
      </template>
    </v-simple-table>
    <br />
    <br />
    <div class="text-center">
        <v-btn @click="checkHandler">수정</v-btn>
        <v-btn @click="goBack">뒤로</v-btn>
    </div>
    <br />
    <br />
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import moment from "moment";
import http from "@/util/http-common";

export default {
  name: "articleModifyComp",
  props: {
    articleNum: {type: Number},
    articleUserNum: {type: Number},
    articleTripPackageNum: {type: Number},
    articleThumbnail: {type: String},
    articleTemp: {type: Boolean},
    articleTitle: { type: String },
    articlePlace: { type: String },
    articleDateStart: { type: String },
    articleDateEnd: { type: String },
    articleCreatedAt: { type: String },
    articleContent: {type: String},
    blogMasterName: {type: String}
  },
  data: function() {
    return {
      alert:false,
      alertMsg:"",
    };
  },
  methods: {
    goBack: function() {
      this.$router.push(`/article/detail/${this.articleNum}`);
    },
    getFormatDate(regtime) {
      return moment(new Date(regtime)).format("YYYY.MM.DD HH:mm:ss");
    },
    checkHandler() {
      let err = true;
      let msg = "";
      !this.articleTitle &&
        ((msg = "제목을 입력해주세요"),
        (err = false),
        this.$refs.articleTitle.focus());
      err &&
        !this.articlePlace &&
        ((msg = "장소를 입력해주세요"),
        (err = false),
        this.$refs.articlePlace.focus());
      err &&
        !this.articleContent &&
        ((msg = "내용을 입력해주세요"),
        (err = false),
        this.$refs.articleContent.focus());

      if (!err) {
        this.alertMsg = msg;
        this.alert = true;
      }
      else this.updateHandler();
    },
    updateHandler() {
      http
        .put(`/article/${this.articleNum}`, {
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
          date_end: this.articleDateEnd
        })
        .then(({ data }) => {
          let msg = "수정 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "수정이 완료되었습니다.";
          }
          this.alertMsg = msg;
          this.alert = true;
          this.goBack();
        })
        .catch(() => {
          this.alertMsg = "수정 처리시 에러가 발생했습니다.";
          this.alert = true;
          });
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
