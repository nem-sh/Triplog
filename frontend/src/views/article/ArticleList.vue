<template>
  <div>
    <div>
      <PersonalMainComp
        :hostNum="item.num"
        :hostNickName="item.nickname"
        :hostEmail="item.email"
        :hostIntro="item.intro"
        :hostImg="item.imagesrc"
        :isMyBlog="isMyBlog"
        @update-profile="updateProfile"
      />
    </div>
    <v-sheet height="50"></v-sheet>
    <v-app id="inspire" style="max-width: 970px">
      <v-container fluid>
        <v-row>
          <v-col cols="12">
            <v-row align="stretch" justify="space-around">
              <ArticleListComp
                v-for="(item, index) in items"
                :key="`${index}_items`"
                :num="item.num"
                :user_num="item.user_num"
                :title="item.title"
                :thumbnail="item.thumbnail"
                :created_at="item.created_at"
              />
            </v-row>
          </v-col>
        </v-row>
      </v-container>
    </v-app>
    <infinite-loading @infinite="infiniteHandler" spinner="waveDots"></infinite-loading>
  </div>
</template>

<script>
import http from "@/util/http-common";
import ArticleListComp from "@/components/article/ArticleListComp.vue";
import InfiniteLoading from "vue-infinite-loading";
import { mapGetters, mapState } from "vuex";
import PersonalMainComp from "@/components/personal/PersonalMainComp.vue";

export default {
  name: "ArticleList",
  components: {
    ArticleListComp,
    InfiniteLoading,
    PersonalMainComp
  },
  data: function() {
    return {
      items: [],
      limit: 0,
      item: {},
      isMyBlog: false
    };
  },
  created() {
    http
      .post("/article/getList/", {
        usernum: this.$route.params.hostNum,
        limit: this.limit
      })
      .then(({ data }) => {
        this.items = data;
      });
    http.get(`/users/get/${this.$route.params.hostNum}`).then(({ data }) => {
      this.item = data;
      console.dir(data);
      if (this.getUserNum == this.item.num) {
        this.isMyBlog = true;
      }
    });
  },
  methods: {
    infiniteHandler($state) {
      http
        .post("/article/getList/", {
          usernum: this.$route.params.hostNum,
          limit: this.limit + 10
        })
        .then(response => {
          setTimeout(() => {
            if (response.data.length) {
              this.items = this.items.concat(response.data);
              console.log(this.items);
              $state.loaded();
              this.limit += 10;
              if (this.items.length / 10 == 0) {
                $state.complete();
              }
            } else {
              $state.complete();
            }
          }, 1000);
        })
        .catch(error => {
          console.error(error);
        });
    },
    updateProfile: function() {
      console.log("zz");
      this.$emit("update-profile");
      console.log("zz");
    }
  },
  computed: {
    ...mapGetters(["getUserNum"]),
    ...mapState({
      userNum: state => `${state.user.getUserNum}`
    })
  },
  activated() {
    this.$router.go(this.$router.currentRoute);
  }
};
</script>

<style>
</style>