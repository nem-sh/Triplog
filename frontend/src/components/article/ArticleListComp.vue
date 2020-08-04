<template>
  <div>
    <v-hover v-slot:default="{ hover }">
      <v-card :class="{ 'on-hover': hover }" width="300px" style="background: white;">
        <v-img
          v-if="thumbnail"
          :src="require(`@/assets/articleImage/${thumbnail}`)"
          class="thumb"
          @click="moveDetail"
        >
          <v-expand-transition>
            <div
              v-if="hover"
              class="d-flex transition-fast-in-fast-out cyan darken-2 v-card--reveal display-0 white--text"
              style="height: 100%;"
            >
              <div align="center">
                <h2>{{title}}</h2>
                <p>{{getFormatDate(created_at)}}</p>
              </div>
            </div>
          </v-expand-transition>
        </v-img>
        <v-card-subtitle>{{title}}</v-card-subtitle>
        <!-- <v-img
          v-else
          :src="require(`@/assets/articleImage/noimage.png`)"
          class="thumb"
          @click="moveDetail"
        >
          <v-expand-transition>
            <div
              v-if="hover"
              class="d-flex transition-fast-in-fast-out cyan darken-2 v-card--reveal display-3 white--text"
              style="height: 100%;"
            >
              <div align="center">
                <h2>{{title}}</h2>
                <p>{{getFormatDate(created_at)}}</p>
              </div>
            </div>
          </v-expand-transition>
        </v-img> -->
      </v-card>
    </v-hover>
    <br />
  </div>
</template>

<script>
import moment from "moment";

export default {
  name: "list",
  props: {
    num: { type: Number },
    user_num: { type: Number },
    title: { type: String },
    thumbnail: { type: String },
    created_at: { type: String }
  },
  methods: {
    moveDetail: function() {
      this.$router.push(`/article/detail/${this.num}`);
    },
    getFormatDate(regtime) {
      return moment(new Date(regtime)).format("YY.MM.DD");
    }
  }
};
</script>

<style>
.pimage {
  width: 300px;
}
.thumb {
  width: 300px;
  height: 300px;
}
.v-card:hover {
  filter: drop-shadow(3px 3px 5px rgb(136, 136, 136));
  transition: opacity 0.4s ease-in-out;
}
.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: 0.5;
  position: absolute;
  width: 100%;
}
</style>