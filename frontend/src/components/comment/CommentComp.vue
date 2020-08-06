<template>
  <v-container fluid style="width:5000px;">
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
      <v-col cols="6">
        <v-checkbox class="mt-0" color="teal lighten-3" v-model="secret" label="비밀글" />
      </v-col>
      <v-col cols="6" style="display: flex; justify-content:flex-end;">
        <v-btn class="teal lighten-3" @click="writeComment">작성</v-btn>
      </v-col>
    </v-row>

    <v-list three-line>
      <CommentUnitComp v-if="items[0]" :item="header" />
      <div v-for="(item, index) in items" :key="item.comment.num">
        <CommentUnitComp :item="item" :index="index" />
        <CommentUnitComp :item="{ divider: true, inset: true }" />
      </div>
    </v-list>
  </v-container>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import http from "@/util/http-common";
import CommentUnitComp from "./CommentUnitComp";
export default {
  name: "CommentComp",
  components: {
    CommentUnitComp
  },
  data: function() {
    return {
      contentLength: "0/100",
      content: "",
      secret: false,
      header: { header: "Comment" },
      itemss: [
        {
          avatar: "https://cdn.vuetifyjs.com/images/lists/1.jpg",
          title: "Brunch this weekend?",
          subtitle:
            "<span class='text--primary'>Ali Connors</span> &mdash; I'll be in your neighborhood doing errands this weekend. Do you want to hang out?",
          created_at: "2017.07.02 14:43"
        },
        { divider: true, inset: true },
        {
          avatar: "https://cdn.vuetifyjs.com/images/lists/2.jpg",
          title: 'Summer BBQ <span class="grey--text text--lighten-1">4</span>',
          subtitle:
            "<span class='text--primary'>to Alex, Scott, Jennifer</span> &mdash; Wish I could come, but I'm out of town this weekend."
        },
        { divider: true, inset: true },
        {
          avatar: "https://cdn.vuetifyjs.com/images/lists/3.jpg",
          title: "Oui oui",
          subtitle:
            "<span class='text--primary'>Sandra Adams</span> &mdash; Do you have Paris recommendations? Have you ever been?"
        }
      ]
    };
  },
  props: {
    items: Array
  },
  methods: {
    writeComment() {
      let obj = {
        avatar: "https://cdn.vuetifyjs.com/images/lists/3.jpg",
        usernickname: this.getProfile,
        useremail: this.getEmail,
        content: this.content,
        createdat: new Date(),
        userimg: this.getUserImg,
        usernum: this.getUserNum
      };
      this.items.unshift({ comment: obj, cocomments: [] });
      http
        .post(`/comment/`, {
          content: this.content,
          createdat: new Date(),
          articlenum: this.$route.params.articleNum,
          userimg: this.getUserImg,
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
    ...mapGetters([
      "isAuthenticated",
      "isProfileLoaded",
      "getProfile",
      "getRealName",
      "getEmail",
      "getUserNum",
      "getUserImg"
    ]),
    ...mapState({
      authLoading: state => state.auth.status === "loading",
      uname: state => `${state.user.getProfile}`,
      userEmail: state => `${state.user.getEmail}`,
      userNum: state => `${state.user.getUserNum}`,
      userImg: state => `${state.user.getUserImg}`
    })
  },
  created() {}
};
</script>

<style>
</style>