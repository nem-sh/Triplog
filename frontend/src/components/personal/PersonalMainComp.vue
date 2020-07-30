<template>
  <v-app>
    <br />
    <br />
    <br />
    <div class="mx-auto bg" style="width:1200px; height:500px;  
   border-radius:10px;">
      <v-container style="height: 100%; opacity:1; ">
        <v-row style="height: 100%; width: 100%;  margin :0;">
          <v-col
            cols="3"
            style="height : 100%; back; background-color: rgba( 255, 255, 255, 0.6 ); border-radius:10px;"
          >
            <v-hover v-slot:default="{ hover }">
              <div class="mx-auto" width="100%" height="100%">
                <v-img
                  :aspect-ratio="10/10"
                  src="@/assets/profile_init.png"
                  style="align-items:flex-end; display : flex;"
                >
                  <div
                    v-if="hover & isMyBlog"
                    class="black white--text"
                    style="height : 50px; opacity: 0.5; text-align: center; line-height: 50px;"
                  >프로필수정</div>
                </v-img>
                <v-card-text class="pt-6" style="position: relative;">
                  <h3 class="display-1 orange--text mb-2">{{hostNickName}}</h3>
                  <div class="font-weight-light grey--text title mb-2">{{hostEmail}}</div>
                  <div class="font-weight-light title mb-2">안녕하세요 방가루^_^</div>

                  <div
                    v-if="isMyBlog"
                    class="font-weight-light grey--text title mb-2"
                    style="text-align: center;"
                  >나의 이웃 | 좋아요 목록</div>
                  <div v-else>
                    <div
                      v-if="true"
                      class="font-weight-light grey--text title mb-2"
                      style="text-align: center;"
                    >이웃 신청</div>
                    <div v-esle>이웃 친구</div>
                  </div>
                </v-card-text>
              </div>
            </v-hover>
          </v-col>
          <v-col
            cols="9"
            style="height : 100%; display:flex; justify-content:flex-end; align-items:flex-end;"
          >
            <h1 style="margin-bottom: 30px; margin-right: 30px;">여행가요 blog</h1>
          </v-col>
        </v-row>
      </v-container>
    </div>
  </v-app>
</template>

<script>
export default {
  name: "PersonalMainComp",
  props: {
    hostNum: { type: Number },
    hostNickName: { type: String },
    hostEmail: { type: String },
    isMyBlog: { type: Boolean }
  }
};
</script>

<style>
.bg {
  position: relative;
  z-index: 1;
}
.bg::after {
  width: 100%;
  height: 100%;
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
  opacity: 0.6;
  background-image: url("http://localhost:8080/img/mountain.jpg");
  background-size: cover;
  border-radius: 10px;
}
</style>