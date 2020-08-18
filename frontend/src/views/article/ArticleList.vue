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

    <v-tabs centered grow color="cyan darken-2">
      <v-tab>
        <v-icon left>mdi-format-list-bulleted</v-icon>All Posts
      </v-tab>
      <v-tab v-for="(tripTitle,i) in tripList" :key="i">
        <v-icon left>mdi-airplane-takeoff</v-icon>
        {{tripTitle.name}}
      </v-tab>
      <v-tab v-if="isMyBlog">
        <v-icon left>mdi-cogs</v-icon>TripPackage
      </v-tab>
      <v-tab-item>
        <v-app id="inspire" style="max-width: 900px">
          <v-card>
            <GoogleMapLoader
              class="travel-map"
              :mapConfig="mapConfig"
              apiKey="AIzaSyC3JEsAuKanTHq2XVnX2uWx9y-0bFEp9iY"
            >
              <template slot-scope="{ google, map }">
                <GoogleMapMarker
                  v-for="marker in markers"
                  :key="marker.id"
                  :marker="marker"
                  :google="google"
                  :map="map"
                />
              </template>
            </GoogleMapLoader>
          </v-card>
          <v-container fluid>
            <v-row>
              <v-col cols="12">
                <v-row align="stretch" justify="space-around">
                  <ArticleListComp
                    v-for="(item, index) in items"
                    :key="index"
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
      </v-tab-item>

      <v-tab-item v-for="(tripItem,i) in tripList" :key="i">
        <Category :num="tripItem.num" :userNum="tripItem.userNum" :name="tripItem.name" />
        <infinite-loading @infinite="infiniteHandler" spinner="waveDots"></infinite-loading>
      </v-tab-item>

      <v-tab-item v-if="isMyBlog">
        <v-app id="inspire" style="max-width: 900px">
          <v-container fluid>
            <v-row>
              <v-col cols="12">
                <v-row align="stretch" justify="space-around">
                  <TripPackageComp @childs-event="reTripList" />
                </v-row>
              </v-col>
            </v-row>
          </v-container>
        </v-app>
      </v-tab-item>
    </v-tabs>
  </div>
</template>

<script>
import http from "@/util/http-common";
import ArticleListComp from "@/components/article/ArticleListComp.vue";
import InfiniteLoading from "vue-infinite-loading";
import { mapGetters, mapState } from "vuex";
import PersonalMainComp from "@/components/personal/PersonalMainComp.vue";
import TripPackageComp from "@/components/tripPackage/TripPackageComp.vue";
import Category from "@/components/tripPackage/Category.vue";
import GoogleMapLoader from "@/components/GoogleMap/GoogleMapLoader.vue";
import GoogleMapMarker from "@/components/GoogleMap/GoogleMapMarker.vue";
import { mapSettings } from "@/constants/mapSettings";
// import Axios from 'axios';

export default {
  name: "ArticleList",
  components: {
    ArticleListComp,
    InfiniteLoading,
    PersonalMainComp,
    TripPackageComp,
    Category,
    GoogleMapLoader,
    GoogleMapMarker
  },
  data: function() {
    return {
      items: [],
      limit: 0,
      item: {},
      isMyBlog: false,
      tripList: [],
      visitCount: 0,
      markers: [],
      mapCenter: { lat: 0, lng: 0 },
      zoom: 0
    };
  },
  created() {
    http
      .get(`/blog/visit/${this.$route.params.hostNum}`)
      .then(({ data }) => {
        this.visitCount = data.visitcount;
      })
      .catch(err => {
        console.log(err);
      }),
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
      if (this.getUserNum == this.item.num) {
        this.isMyBlog = true;
      }
    });
    http.get(`/tripPackage/${this.$route.params.hostNum}`).then(({ data }) => {
      this.tripList = data;
      for (let index = 0; index < this.tripList.length; index++) {
        http
          .get(`/article/tripPackage/${this.tripList[index].num}`)
          .then(({ data }) => {
            var items = data;
            var y = new Array(items.length);
            var x = new Array(items.length);

            for (let i = 0; i < items.length; i++) {
              y[i] = items[i].lat;
              x[i] = items[i].lng;
            }

            var x1 = Math.min.apply(null, x);
            var x2 = Math.max.apply(null, x);
            var y1 = Math.min.apply(null, y);
            var y2 = Math.max.apply(null, y);

            var marker = {
              id: this.tripList[index].name,
              position: {
                lat: parseInt(y1 + (y2 - y1) / 2),
                lng: parseInt(x1 + (x2 - x1) / 2)
              }
            };
            this.markers.push(marker);
          });
      }
      this.zoom = 0;
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
      this.$emit("update-profile");
    },
    reTripList() {
      http
        .get(`/tripPackage/${this.$route.params.hostNum}`)
        .then(({ data }) => {
          this.tripList = data;
        });
    }
  },
  computed: {
    ...mapGetters(["getUserNum"]),
    ...mapState({
      userNum: state => `${state.user.getUserNum}`
    }),
    mapConfig() {
      return {
        ...mapSettings,
        center: this.mapCenter,
        zoom: this.zoom
      };
    }
  },
  watch: {
    getUserNum: function() {
      this.$router.go(this.$router.currentRoute);
    }
  }
};
</script>

<style lang="scss" scoped>
.travel-map {
  height: 400px;
}
</style>