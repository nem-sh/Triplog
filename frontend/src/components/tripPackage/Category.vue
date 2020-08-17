<template>
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
          <GoogleMapLine
            v-for="line in lines"
            :key="line.id"
            :path.sync="line.path"
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
</template>

<script>
import http from "@/util/http-common";
import ArticleListComp from "@/components/article/ArticleListComp.vue";
import GoogleMapLoader from "@/components/GoogleMap/GoogleMapLoader.vue";
import GoogleMapMarker from "@/components/GoogleMap/GoogleMapMarker.vue";
import GoogleMapLine from "@/components/GoogleMap/GoogleMapLine.vue";

import { mapSettings } from "@/constants/mapSettings";

export default {
  name: "Category",
  components: {
    ArticleListComp,
    GoogleMapLoader,
    GoogleMapMarker,
    GoogleMapLine
  },
  props: {
    num: { type: Number },
    userNum: { type: Number },
    name: { type: String }
  },
  data: function() {
    return {
      items: [],
      markers: [],
      lines: [],
      mapCenter: { lat: 0, lng: 0 },
      zoom: 1
    };
  },
  created() {
    http.get(`/article/tripPackage/${this.num}`).then(({ data }) => {
      this.items = data;
      var markers = new Array(this.items.length);
      var y = new Array(this.items.length);
      var x = new Array(this.items.length);

      for (let index = 0; index < this.items.length; index++) {
        markers[index] = {
          id: this.items[index].place,
          position: {
            lat: this.items[index].lat,
            lng: this.items[index].lng
          }
        };
        y[index] = this.items[index].lat;
        x[index] = this.items[index].lng;
      }
      this.markers = markers;
      
      var lines = new Array(this.items.length - 1);
      for (let index = 0; index < this.markers.length - 1; index++) {
        lines[index] = {
          id: index + 1,
          path: [this.markers[index].position, this.markers[index + 1].position]
        };
      }
      this.lines = lines;
      var x1 = Math.min.apply(null, x);
      var x2 = Math.max.apply(null, x);
      var y1 = Math.min.apply(null, y);
      var y2 = Math.max.apply(null, y);

      this.mapCenter.lat = parseInt(y1 + (y2 - y1) / 2);
      this.mapCenter.lng = parseInt(x1 + (x2 - x1) / 2);
      this.zoom = 4;
    });
  },
  computed: {
    mapConfig() {
      return {
        ...mapSettings,
        center: this.mapCenter,
        zoom: this.zoom
      };
    }
  },
};
</script>

<style lang="scss" scoped>
.travel-map {
  height: 400px;
}
</style>