<template >
  <v-container>
    <v-subheader v-if="item.header" :key="item.header" v-text="item.header"></v-subheader>

    <v-divider v-else-if="item.divider" :key="index" :inset="item.inset"></v-divider>

    <v-list-item v-else :key="item.comment.num">
      <v-list-item-avatar>
        <v-img :src="require(`@/assets/blogImage/${userimg}`)"></v-img>
      </v-list-item-avatar>

      <v-list-item-content class="pb-0">
        <div @mouseover="display = true" @mouseleave="display = false" @click.stop="onOffComment">
          <v-row>
            <v-col cols="8">
              <v-list-item-title v-html="getName"></v-list-item-title>
            </v-col>
            <v-col cols="4" v-if="display" style="display: flex; justify-content:flex-end;">
              <i
                v-if="isMyComment"
                class="fas fa-times"
                :style="xButton"
                @mouseover="displayX = true"
                @mouseleave="displayX = false"
                @click.stop="deleteComment"
              />
            </v-col>
          </v-row>
          <br />
          <v-list-item-subtitle v-html="item.comment.content"></v-list-item-subtitle>
          <br />
          <br />
          <v-row>
            <v-col cols="5" class="mb-4" style="display: flex; justify-content:flex-end;">
              <v-list-item-subtitle v-html="item.comment.createdat" />
            </v-col>
            <v-col cols="2" class="pb-0"></v-col>
            <v-col cols="5" style="display: flex; justify-content:flex-end;" :style="button">
              <i v-if="!openComment" class="fas fa-caret-down mr-2" />
              <i v-if="openComment" class="fas fa-caret-up mr-2" />&nbsp;
              댓글
            </v-col>
          </v-row>
        </div>
        <v-container v-if="openComment" style="width:80%;">
          <v-row>
            <v-col cols="12">
              <v-textarea
                :label="getContentLength"
                auto-grow
                outlined
                rows="2"
                row-height="50"
                maxlength="100"
                v-model="content"
                hide-details="false"
                color="teal lighten-3"
              />
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="6"></v-col>
            <v-col cols="6" style="display: flex; justify-content:flex-end;">
              <v-btn class="teal lighten-3" @click="submit">작성</v-btn>
            </v-col>
          </v-row>
          <div v-for="(coItem, index) in item.cocomments" :key="coItem.num">
            <cocomment-unit-comp :item="coItem" :index="index" />
          </div>
        </v-container>
      </v-list-item-content>
    </v-list-item>
  </v-container>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import http from "@/util/http-common";
import CocommentUnitComp from "./CocommentUnitComp.vue";
export default {
  name: "CommentUnitComp",
  components: {
    CocommentUnitComp
  },
  data() {
    return {
      contentLength: "0/100",
      content: "",
      display: false,
      displayX: false,
      openComment: false,
      userimg: "profile_init.png"
    };
  },
  props: {
    item: Object,
    index: Number
  },
  methods: {
    onOffComment() {
      if (this.openComment == true) {
        this.openComment = false;
      } else {
        this.openComment = true;
      }
    },
    deleteComment() {
      this.item.comment.content = "삭제되었습니다.";
      http
        .delete(`/comment/${this.item.comment.num}`)
        .then(({ data }) => {
          console.log(data);
        })
        .catch(e => {
          console.log(e);
        });
    },
    submit() {
      let obj = {
        usernickname: this.getProfile,
        content: this.content,
        createdat: new Date(),
        useremail: this.getEmail
      };
      this.item.cocomments.unshift(obj);
      http
        .post(`/comment/${this.item.comment.num}`, {
          content: this.content,
          createdat: new Date(),
          articlenum: this.$route.params.articleNum,

          usernum: this.getUserNum,
          usernickname: this.getProfile,
          useremail: this.getEmail
        })
        .then(({ data }) => {
          console.log(data);
        })
        .catch(e => {
          console.log(e);
        });
    }
  },
  computed: {
    getContentLength: function() {
      if (this.content.length == 0) {
        return "My Comment";
      }
      return this.content.length + "/100";
    },
    xButton: function() {
      if (this.displayX) {
        return "color:red;";
      } else {
        return "color:black;";
      }
    },
    button: function() {
      if (this.display) {
        return "color:blue;";
      } else {
        return "color:black;";
      }
    },
    getName: function() {
      return `${this.item.comment.usernickname}(${this.item.comment.useremail})`;
    },
    isMyComment: function() {
      if (this.getUserNum == this.item.comment.usernum) {
        return true;
      }
      return false;
    },
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
    })
  },
  created() {
    if (this.item.comment) {
      if (this.item.comment.userimg != null) {
        this.userimg = this.item.comment.userimg;
      }
    }
  }
};
</script>

<style>
</style>