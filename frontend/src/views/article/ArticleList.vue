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
    <div
      v-masonry="containerId"
      transition-duration="0.3s"
      item-selector=".item"
      class="masonry-container"
    >
      <ArticleListComp
        v-masonry-tile
        v-for="(item, index) in items"
        :key="`${index}_items`"
        :num="item.num"
        :user_num="item.user_num"
        :title="item.title"
        :thumbnail="item.thumbnail"
      />
    </div>
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
    SetGridItemHeight: function() {
      let grid = document.getElementsByClassName("grid")[0];
      let rowHeight = parseInt(
        window.getComputedStyle(grid).getPropertyValue("grid-auto-rows")
      );
      let rowGap = parseInt(
        window.getComputedStyle(grid).getPropertyValue("grid-row-gap")
      );
      let item = grid.getElementsByClassName("item");
      for (let i = 0; i < item.length; ++i) {
        item[i].style.gridRowEnd = `span ${Math.floor(
          item[i].children[0].children[0].offsetHeight / (rowHeight + rowGap)
        )}`;
      }
    },
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
  }
};
</script>

<style>
p.title {
  color: rgb(255, 255, 255);
  position: relative;
  top: 8px;
  left: 15px;
}
.item {
  margin-bottom: 20px;
  margin-right: 20px;
  background: darkturquoise;
  border-radius: 10px;
  max-width: 280px;
}
.item:hover {
  filter: drop-shadow(3px 3px 5px rgb(136, 136, 136));
  background: turquoise;
}
</style>