<template>
  <v-container style="width: 1200px;">
    <v-row>
      <v-col cols="12">
        <slot />
      </v-col>

      <Card
        v-for="(likeArticle, i) in paginatedArticles"
        :key="likeArticle.title"
        :size="layout[i]"
        :value="likeArticle"
      />
    </v-row>

    <v-row align="center">
      <v-col cols="3">
        <v-btn
          color="primary"
          v-if="page !== 1"
          class="ml-0"
          square
          title="Previous page"
          @click="page--"
        >
          <i class="fas fa-arrow-left" /> Prev
        </v-btn>
      </v-col>

      <v-col class="text-center subheading" cols="6">
        <h1>{{page}}</h1>
      </v-col>

      <v-col class="text-right" cols="3">
        <v-btn
          color="primary"
          v-if="pages > 1 && page < pages"
          class="mr-0"
          square
          title="Next page"
          @click="page++"
        >
          Next
          <i class="fas fa-arrow-right" />
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import http from "../../util/http-common";
import Card from "../../components/likey/Card.vue";
export default {
  name: "Like",
  components: {
    Card
  },
  data() {
    return {
      layout: [3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3],
      page: 1,
      likeArticle: []
    };
  },

  methods: {
    getLikeList: function() {
      console.log();
      http
        .get(`/article/likelist/${this.$store.state.user.userNum}`, null)
        .then(response => {
          this.likeArticle = response.data;
        })
        .catch(error => {
          console.log(error.data);
        });
    }
  },

  computed: {
    pages() {
      return Math.ceil(this.likeArticle.length / 6);
    },
    paginatedArticles() {
      const start = (this.page - 1) * 6;
      const stop = this.page * 6;

      return this.likeArticle.slice(start, stop);
    }
  },
  watch: {
    page() {
      window.scrollTo(0, 0);
    }
  },
  created: function() {
    this.getLikeList();
  }
};
</script>

<style>
</style>