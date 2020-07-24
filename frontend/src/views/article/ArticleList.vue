<template>
  <div>
    <div class="grid">
      <ArticleListComp
        v-for="(item, index) in items"
        :key="`${index}_items`"
        :num="item.num"
        :user_num="item.user_num"
        :title="item.title"
        :thumbnail="item.thumbnail"
      />
    </div>
  </div>
</template>

<script>
import http from "@/util/http-common";
import ArticleListComp from "@/components/article/ArticleListComp.vue";

export default {
  name: "ArticleList",
  components: {
    ArticleListComp
  },
  data: function() {
    return {
      items: []
    };
  },
  created() {
    http.get(`/article/getList`).then(({ data }) => {
      this.items = data;
    });
  },
  mounted() {
    window.addEventListener("load", this.SetGridItemHeight);
    window.addEventListener("resize", this.SetGridItemHeight);
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
    }
  }
};
</script>

<style>
p.title {
  text-shadow: 1px 1px 2px #666666;
  color: rgb(255, 255, 255);
  text-align: center;
  position: relative;
  bottom: 60px;
}
.grid {
  display: grid;
  max-width: 900px;
  width: 80%;
  background: rgb(255, 255, 255);
  min-height: 400px;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  grid-auto-rows: 20px;
  grid-gap: 10px;
}
.item {
  width: 100%;
  overflow: hidden;
  position: relative;
  border-radius: 10px;
}
.thumb {
  width: 100%;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  
}
</style>