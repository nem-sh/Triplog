<template>
  <div>
    <PersonalMainComp
        :hostNum = "item.num"
        :hostNickName = "item.nickname"
    />
  </div>
</template>

<script>
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
    };
  },
  created() {
    http
      .get(`/users/get/${this.$route.params.hostNum}`)
      .then(({ data }) => {
        this.item = data;
        console.dir(data);
      });
  },
};
</script>
