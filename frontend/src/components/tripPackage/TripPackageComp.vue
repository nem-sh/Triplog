<template>
  <div>
    <v-container>
      <v-row>
        <v-col>
          <v-textarea
            auto-grow
            outlined
            rows="1"
            row-height="5"
            label="여행 묶음 이름을 입력하세요"
            v-model="TripName"
          ></v-textarea>
        </v-col>
        <v-col>
          <v-btn @click="addTripPackage">여행 묶음 추가</v-btn>
        </v-col>
      </v-row>
      <TripList :num=0 :userNum="userNum" name="default" />
      <TripList
        v-for="(item, index) in tripList"
        :key="`${index}_tripList`"
        :num="item.num"
        :userNum="item.userNum"
        :name="item.name"
      />
    </v-container>
  </div>
</template>

<script>
import http from "@/util/http-common";
import TripList from "@/components/tripPackage/TripList.vue";

export default {
  name: "TripPackageComp",
  components: {
    TripList
  },
  data: function() {
    return {
      TripName: "",
      tripList: [],
      userNum: 0,
    };
  },
  created() {
    http.get(`/tripPackage/${this.$route.params.hostNum}`).then(({ data }) => {
      this.tripList = data;
    });
    this.userNum = Number(this.$route.params.hostNum);
  },
  methods: {
    addTripPackage() {
      http
        .post("/tripPackage/", {
          userNum: this.$route.params.hostNum,
          name: this.TripName
        })
        .catch(error => {
          console.error(error);
        });
    }
  }
};
</script>

<style>
</style>