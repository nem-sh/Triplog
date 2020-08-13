<template>
  <div style="min-width: 1000px;">
    <div>
      <h1>{{articleTitle}}</h1>
      <h4 style="color:gray; cursor:pointer" @click="goToBlog">{{blogMasterName}}</h4>
      <h4 style="color:gray">{{getFormatDate(articleCreatedAt)}}</h4>
    </div>
    <br>
    <v-divider/>
    <v-tabs
      >
        <v-tab>
          장소
        </v-tab>
        <v-tab>
          일정
        </v-tab>

        <v-tab-item
          >
          <v-card flat>
            <v-card-text>{{ articlePlace }}</v-card-text>
          </v-card>
        </v-tab-item>

        <!-- 여기부터 캘린더 -->
        <v-tab-item
        >
          <v-row class="fill-height">
            <v-col>
              <v-sheet height="64">
                <v-toolbar flat color="white">
                  <v-btn outlined class="mr-4" color="grey darken-2" @click="setToday">
                    Today
                  </v-btn>
                  <v-btn fab text small color="grey darken-2" @click="prev">
                    <v-icon small>mdi-chevron-left</v-icon>
                  </v-btn>
                  <v-btn fab text small color="grey darken-2" @click="next">
                    <v-icon small>mdi-chevron-right</v-icon>
                  </v-btn>
                  <v-toolbar-title v-if="$refs.calendar">
                    {{ $refs.calendar.title }}
                  </v-toolbar-title>
                  <v-spacer></v-spacer>
                  <v-menu bottom right>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        outlined
                        color="grey darken-2"
                        v-bind="attrs"
                        v-on="on"
                      >
                        <span>{{ typeToLabel[type] }}</span>
                        <v-icon right>mdi-menu-down</v-icon>
                      </v-btn>
                    </template>
                    <v-list>
                      <v-list-item @click="type = 'day'">
                        <v-list-item-title>Day</v-list-item-title>
                      </v-list-item>
                      <v-list-item @click="type = 'week'">
                        <v-list-item-title>Week</v-list-item-title>
                      </v-list-item>
                      <v-list-item @click="type = 'month'">
                        <v-list-item-title>Month</v-list-item-title>
                      </v-list-item>
                      <v-list-item @click="type = '4day'">
                        <v-list-item-title>4 days</v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </v-toolbar>
              </v-sheet>
              <v-sheet height="600">
                <v-calendar
                  ref="calendar"
                  v-model="focus"
                  color="primary"
                  :events="events"
                  :event-color="getEventColor"
                  :type="type"
                  @click:event="showEvent"
                  @click:more="viewDay"
                  @click:date="viewDay"
                  @change="updateRange"
                ></v-calendar>
                <v-menu
                  v-model="selectedOpen"
                  :close-on-content-click="false"
                  :activator="selectedElement"
                  offset-x
                >
                  <v-card
                    color="grey lighten-4"
                    min-width="350px"
                    flat
                  >
                    <v-toolbar
                      :color="selectedEvent.color"
                      dark
                    >
                      <v-btn icon>
                        <v-icon>mdi-pencil</v-icon>
                      </v-btn>
                      <v-toolbar-title v-html="selectedEvent.name"></v-toolbar-title>
                      <v-spacer></v-spacer>
                      <v-btn icon>
                        <v-icon>mdi-heart</v-icon>
                      </v-btn>
                      <v-btn icon>
                        <v-icon>mdi-dots-vertical</v-icon>
                      </v-btn>
                    </v-toolbar>
                    <v-card-text>
                      <span v-html="selectedEvent.details"></span>
                    </v-card-text>
                    <v-card-actions>
                      <v-btn
                        text
                        color="secondary"
                        @click="selectedOpen = false"
                      >
                        Cancel
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </v-menu>
              </v-sheet>
            </v-col>
          </v-row>
        </v-tab-item>
        <!-- 여기까지 캘린더 -->
      </v-tabs>
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
      justify="between"
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
      
        <v-chip
          class="ml-6"
          color="light-green"
          text-color="white">
          <p class="text-align-center ma-0">view</p>
          <v-avatar
        right
        class="green"
      >
        {{articleViews}}
      </v-avatar>
        </v-chip>

      
      
      
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
        @click="goBack"
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
    isLoginedUserLikeThisArticle: { type: Boolean },
    articleViews: {type: Number}
  },
  data: function() {
    return {
      alert: false,
      alertMsg: "",
      dialog: false,
      realContent: "",
      btnToggle: false,
      likeBtnFlag: false,
      //여기부터 캘린더 데이터
      focus: '',
      type: 'month',
      typeToLabel: {
        month: 'Month',
        week: 'Week',
        day: 'Day',
        '4day': '4 Days',
      },
      selectedEvent: {},
      selectedElement: null,
      selectedOpen: false,
      events: [],
      colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
      names: ['Meeting', 'Holiday', 'PTO', 'Travel', 'Event', 'Birthday', 'Conference', 'Party'],
      views: 0,
    };
  },
  mounted() {
    if(this.getUserNum == this.articleUserNum) this.btnToggle = true;
    this.$refs.calendar.checkChange();
  },
  created() {
    // console.log(this.getUserNum);
    // console.log(this.articleUserNum);
    // console.log(this.articleViews)
    this.openContentFile()
  },
  methods: {
    goToBlog: function() {
      this.$router.push(`/article/list/${this.articleUserNum}`);
    },
    viewDay ({ date }) {
      this.focus = date
      this.type = 'day'
    },
    getEventColor (event) {
      return event.color
    },
    setToday () {
      this.focus = ''
    },
    prev () {
      this.$refs.calendar.prev()
    },
    next () {
      this.$refs.calendar.next()
    },
    showEvent ({ nativeEvent, event }) {
      const open = () => {
        this.selectedEvent = event
        this.selectedElement = nativeEvent.target
        setTimeout(() => this.selectedOpen = true, 10)
      }

      if (this.selectedOpen) {
        this.selectedOpen = false
        setTimeout(open, 10)
      } else {
        open()
      }

      nativeEvent.stopPropagation()
    },
    updateRange ({ start, end }) {
      const events = []

      const min = new Date(`${start.date}T00:00:00`)
      const max = new Date(`${end.date}T23:59:59`)
      const days = (max.getTime() - min.getTime()) / 86400000
      const eventCount = this.rnd(days, days + 20)

      for (let i = 0; i < eventCount; i++) {
        const allDay = this.rnd(0, 3) === 0
        const firstTimestamp = this.rnd(min.getTime(), max.getTime())
        const first = new Date(firstTimestamp - (firstTimestamp % 900000))
        const secondTimestamp = this.rnd(2, allDay ? 288 : 8) * 900000
        const second = new Date(first.getTime() + secondTimestamp)

        events.push({
          name: this.names[this.rnd(0, this.names.length - 1)],
          start: first,
          end: second,
          color: this.colors[this.rnd(0, this.colors.length - 1)],
          timed: !allDay,
        })
      }

      this.events = events
    },
    rnd (a, b) {
      return Math.floor((b - a + 1) * Math.random()) + a
    },
    // 여기까지 캘린더 메소드
    goBack: function() {
      window.history.back();
    },
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
            likeCount: this.articleLikeCount,
            viws: this.articleViews,

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