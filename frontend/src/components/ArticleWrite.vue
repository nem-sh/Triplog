<template>
  <div id="main_wrap">
    hi
  </div>
</template>

<script>
export default {
  name: "login",
  data() {
    return {
      visablelogin: true,
      email: "",
      password: "",
      customer: {
        nickname: "",
        cname: "",
        email: "",
        password: ""
      },
      submitted: false,
      alert: false,
      alertMsg: "",
      password_confirm: "",
    };
  },
 
};
</script>
