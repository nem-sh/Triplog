<template>
  <div>
    <PersonalMainComp
      :hostNum="item.num"
      :hostNickName="item.nickname"
      :hostEmail="item.email"
      :isMyBlog="isMyBlog"
    />
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import PersonalMainComp from "@/components/personal/PersonalMainComp.vue";
import http from "@/util/http-common";

export default {
  name: "PersonalMain",
  components: {
    PersonalMainComp
  },
  data: function() {
    return {
      item: {},
      isMyBlog: false
    };
  },
  created() {
    http.get(`/users/get/${this.$route.params.hostNum}`).then(({ data }) => {
      this.item = data;
      console.dir(data);
      if (this.getUserNum == this.item.num) {
        this.isMyBlog = true;
      }
    });
  },
  computed: {
    ...mapGetters(["getUserNum"]),
    ...mapState({
      userNum: state => `${state.user.getUserNum}`
    })
  }
};
</script>
