<template>
  <div id="main_wrap">
    <v-row dense>
      <v-col cols="12" md="2" align-self="center">
        <div style="width:100%; text-align:center" :style="{fontFamily : 'SunFlower'}">제목</div>
      </v-col>
      <v-col cols="12" md="9">
        <v-text-field v-model="articleTitle" label="제목" id="title" ref="title"></v-text-field>
      </v-col>
    </v-row>
    <v-row dense>
      <v-col cols="12" md="2" align-self="center">
        <div style="width:100%; text-align:center" :style="{fontFamily : 'SunFlower'}">장소</div>
      </v-col>
      <v-col cols="12" md="9">
        <v-dialog v-model="addressDialog" persistent max-width="500">
          <template v-slot:activator="{ on, attrs }">
            <div>
              <v-btn small v-bind="attrs" v-on="on" :style="{fontFamily : 'SunFlower'}">주소 찾기</v-btn>
              {{ address.address }}
            </div>
          </template>
          <vue-daum-postcode @complete="address = $event; addressDialog = false;" />
        </v-dialog>
      </v-col>
    </v-row>

    <v-row style="height: 300px;" v-if="imageUrl">
      <v-col cols="12" md="2" align-self="center"></v-col>
      <v-col cols="12" md="9">
        <v-sheet outlined height="300" align="center">
          <v-img v-if="imageUrl" :src="imageUrl" class="img" aspect-ratio="1.7" contain>
            <template v-slot:placeholder>
              <v-row class="fill-height ma-0" align="center" justify="center">
                <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
              </v-row>
            </template>
          </v-img>
        </v-sheet>
      </v-col>
    </v-row>

    <v-file-input
      multiple
      label="Images"
      accept="image/*"
      prepend-icon="mdi-camera"
      @change="onChangeMultipleImages"
      v-model="uploadImgs"
    ></v-file-input>

    <v-row>
      <v-sheet class="mx-auto" max-width="1000">
        <v-slide-group multiple show-arrows="mobile">
          <v-slide-item v-for="(item, index) in imgPool" :key="index">
            <v-card class="ma-4" width="200">
              <img
                :src="returnImageURL(item)"
                @dragend="drag(index)"
                draggable="true"
                width="200"
                height="300"
              />
            </v-card>
          </v-slide-item>
        </v-slide-group>
      </v-sheet>
    </v-row>

    <br />

    <input ref="imageInput" type="file" accept="image/*" hidden @change="onChangeImages" />

    <br />
    <v-sheet elevation="3" outlined>
      <v-sheet class="ma-1">
        <v-row>
          <v-col>
            <v-tooltip bottom>
              <template v-slot:activator="{on, attrs}">
                <v-btn
                  class="mr-1"
                  @click="exec('bold')"
                  label
                  icon
                  color="cyan darken-2"
                  v-on="on"
                  v-bind="attrs"
                >
                  <v-icon>mdi-format-bold</v-icon>
                </v-btn>
              </template>
              <span :style="{fontFamily : 'SunFlower'}">진하게</span>
            </v-tooltip>

            <v-tooltip bottom>
              <template v-slot:activator="{on, attrs}">
                <v-btn
                  class="mr-1"
                  @click="exec('italic')"
                  label
                  icon
                  color="cyan darken-2"
                  v-on="on"
                  v-bind="attrs"
                >
                  <v-icon>mdi-format-italic</v-icon>
                </v-btn>
              </template>
              <span :style="{fontFamily : 'SunFlower'}">기울임</span>
            </v-tooltip>

            <v-tooltip bottom>
              <template v-slot:activator="{on, attrs}">
                <v-btn
                  class="mr-1"
                  @click="exec('underline')"
                  label
                  icon
                  color="cyan darken-2"
                  v-on="on"
                  v-bind="attrs"
                >
                  <v-icon>mdi-format-underline</v-icon>
                </v-btn>
              </template>
              <span :style="{fontFamily : 'SunFlower'}">밑줄</span>
            </v-tooltip>

            <v-tooltip bottom>
              <template v-slot:activator="{on, attrs}">
                <v-btn
                  class="mr-1"
                  @click="exec('subscript')"
                  label
                  icon
                  color="cyan darken-2"
                  v-on="on"
                  v-bind="attrs"
                >
                  <v-icon>mdi-format-subscript</v-icon>
                </v-btn>
              </template>
              <span :style="{fontFamily : 'SunFlower'}">아래첨자</span>
            </v-tooltip>

            <v-tooltip bottom>
              <template v-slot:activator="{on, attrs}">
                <v-btn
                  class="mr-1"
                  @click="exec('superscript')"
                  label
                  icon
                  color="cyan darken-2"
                  v-on="on"
                  v-bind="attrs"
                >
                  <v-icon>mdi-format-superscript</v-icon>
                </v-btn>
              </template>
              <span :style="{fontFamily : 'SunFlower'}">윗첨자</span>
            </v-tooltip>

            <v-tooltip bottom>
              <template v-slot:activator="{on, attrs}">
                <v-btn
                  class="mr-1"
                  @click="showColorPickerHil = ! showColorPickerHil"
                  icon
                  label
                  :style="{color : HiliteColor}"
                  v-on="on"
                  v-bind="attrs"
                >
                  <v-icon>mdi-marker</v-icon>
                </v-btn>
              </template>
              <span :style="{fontFamily : 'SunFlower'}">글자 배경색</span>
            </v-tooltip>

            <v-color-picker
              v-show="showColorPickerHil"
              hide-inputs
              show-swatches
              swatches-max-height="100px"
              v-model="HiliteColorValue"
            ></v-color-picker>

            <v-tooltip bottom>
              <template v-slot:activator="{on, attrs}">
                <v-btn
                  class="mr-1"
                  @click="showColorPicker = ! showColorPicker"
                  icon
                  label
                  :style="{color : fontColor}"
                  v-on="on"
                  v-bind="attrs"
                >
                  <v-icon>mdi-palette</v-icon>
                </v-btn>
              </template>
              <span :style="{fontFamily : 'SunFlower'}">글자색</span>
            </v-tooltip>

            <v-color-picker
              v-show="showColorPicker"
              hide-inputs
              show-swatches
              swatches-max-height="100px"
              v-model="fontColorValue"
            ></v-color-picker>
          </v-col>
          <v-col>
            <!-- <v-hover v-slot:default="{hoverSlide}"> -->
            <!-- <v-btn icon>
                <v-icon>mdi-format-size</v-icon>
            </v-btn>-->
            <v-select
              :items="fontSizeItems"
              label="Font Size"
              dense
              outlined
              menu-props="auto"
              color="cyan darken-2"
              prepend-inner-icon="mdi-format-size"
              v-model="fontSizeValue"
            />

            <!-- </v-hover> -->
          </v-col>
          <v-col>
            <v-select
              :items="fontItems"
              label="Font Style"
              dense
              outlined
              menu-props="auto"
              prepend-inner-icon="mdi-format-font"
              color="cyan darken-2"
              v-model="fontValue"
              :style="{fontFamily : fontValue}"
            />
          </v-col>
        </v-row>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('justifyLeft')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-align-left</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">왼쪽정렬</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('justifyCenter')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-align-center</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">가운데정렬</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('justifyRight')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-align-right</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">오른쪽정렬</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('justifyFull')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-align-justify</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">양쪽정렬</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('insertHorizontalRule')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-align-middle</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">구분선</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('insertOrderedList')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-list-numbered</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">순서목록</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('insertunorderedList')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-list-bulleted</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">목록</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('insertParagraph')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-textdirection-l-to-r</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">문단삽입</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('indent')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-indent-increase</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">들여쓰기</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('outdent')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-indent-decrease</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">내어쓰기</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('undo')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-keyboard-return</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">실행취소</span>
        </v-tooltip>
        <v-container></v-container>

        <v-divider></v-divider>
      </v-sheet>
      <v-sheet height="500">
        <iframe
          id="editor"
          src="../editor.html"
          frameborder="0"
          scrolling="auto"
          style="width:100%; height:100%"
        ></iframe>
      </v-sheet>
    </v-sheet>

    <v-row>
      <v-col>
        <v-btn tile outlined @click="temp" :style="{fontFamily : 'SunFlower'}">임시 저장</v-btn>
      </v-col>
      <v-col align="right">
        <v-btn tile outlined @click="regist" :style="{fontFamily : 'SunFlower'}">등록</v-btn>
      </v-col>
    </v-row>
    <br />
    <br />
    <v-dialog v-model="dialog" max-width="350">
      <v-card>
        <v-card-title class="headline">임시 저장된 게시글이 있습니다.</v-card-title>

        <v-card-text>불러오시겠습니까?</v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn color="blue darken-1" text @click="deleteData">취소</v-btn>

          <v-btn color="blue darken-1" text @click="loadData">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import http from "@/util/http-common";
import http3 from "@/util/http-common3";
import { mapGetters, mapState } from "vuex";
import { VueDaumPostcode } from "vue-daum-postcode";

export default {
  name: "ArticleWriteComp",
  components: {
    VueDaumPostcode
  },
  data() {
    return {
      articleContent: "",
      articleTitle: "",
      articlePlace: "",
      alert: false,
      alertMsg: "",
      registSuccess: false,
      imageFileName: null,
      imageUrl: null,
      fileInfo: "",
      polling: null,
      dialog: false,
      editorHtmlPath: "./assets/editor/editor.html",
      prefix:
        '<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" /><title>Editor</title></head><body>',
      suffix: "</body></html>",
      uploadImgs: [],
      imgPool: [],
      multFlag: false,
      fontItems: [
        { text: "고딕", value: "null" },
        { text: "굴림", value: "굴림" },
        { text: "돋움", value: "돋움" },
        { text: "궁서", value: "궁서" },
        { text: "Nanum Gothic", value: "Nanum Gothic" },
        { text: "Gaegu", value: "Gaegu" },
        { text: "Nanum Myeongjo", value: "Nanum Myeongjo" },
        { text: "Sunflower", value: "Sunflower" },
        { text: "Poor Story", value: "Poor Story" },
        { text: "Yeon Sung", value: "Yeon Sung" },
        { text: "East Sea Dokdo", value: "East Sea Dokdo" },
        { text: "Hi Melody", value: "Hi Melody" },
        { text: "Nanum Pen Script", value: "Nanum Pen Script" },
        { text: "Arial", value: "Arial" },
        { text: "Georgia", value: "Georgia" },
        { text: "Times New Roman", value: "Times New Roman" },
        { text: "Verdana", value: "Verdana" }
      ],
      fontSizeItems: [
        { text: "xsmall", value: "1" },
        { text: "small", value: "2" },
        { text: "medium", value: "3" },
        { text: "large", value: "4" },
        { text: "xlarge", value: "5" },
        { text: "2xlarge", value: "6" },
        { text: "big", value: "7" }
      ],
      fontSizeValue: "3",
      fontValue: "null",
      address: {},
      addressDialog: false,
      showColorPicker: false,
      fontColorValue: {
        r: 0,
        g: 0,
        b: 0,
        a: 1
      },
      showColorPickerHil: false,
      HiliteColorValue: {
        r: 0,
        g: 170,
        b: 255,
        a: 1
      }
    };
  },
  created() {
    // this.start();
  },
  mounted() {
    if (window.localStorage.getItem("isSaved") == "true") {
      this.confirmLoadData();
    }
  },
  methods: {
    drag: function(idx) {
      this.imgPool.splice(idx, 1);
    },

    createFileByInnerEditorText: function() {
      var innerIframe = document.getElementById("editor").contentWindow.document
        .body.innerHTML;
      var content = this.prefix + innerIframe + this.suffix;
      var fileName = this.getUserNum + "_" + this.articleTitle + ".html";
      var file = new File([content], fileName, {
        type: "text/html"
      });
      return file;
    },
    exec: function(option) {
      this.editorDocument().execCommand(option, false, true);
    },
    execValue: function(option, showUI, arg) {
      this.editorDocument().execCommand(option, showUI, arg);
    },
    editorDocument: function() {
      return (
        document.getElementById("editor").contentDocument ||
        document.getElementById("editor").contentWindow.document
      );
    },
    deleteData() {
      this.storeClean();
      this.dialog = false;
    },
    storeClean() {
      window.localStorage.removeItem("isSaved");
      window.localStorage.removeItem("textval");
      window.localStorage.removeItem("articleTitle");
    },
    loadData() {
      this.articleContent = window.localStorage.getItem("textval");
      this.articleTitle = window.localStorage.getItem("articleTitle");
      this.dialog = false;
    },
    confirmLoadData() {
      this.dialog = true;
    },
    storeInLocal() {
      if (window.localStorage && (this.articleContent || this.articleTitle)) {
        window.localStorage.setItem("isSaved", "true");
        window.localStorage.setItem("textval", this.articleContent);
        window.localStorage.setItem("articleTitle", this.articleTitle);
      } else if (!window.localStorage) {
        console.log("Error" + ": Your browser not support");
      }
    },
    temp() {
      this.storeInLocal();
      this.$router.push(`/article/list/${this.getUserNum}`);
    },
    start: function() {
      this.polling = setInterval(() => {
        this.storeInLocal();
      }, 60000);
    },
    regist() {
      let err = true;
      let msg = "";
      !this.articleTitle &&
        ((msg = "제목을 입력해주세요"),
        (err = false),
        this.$refs.title.focus());

      if (!err) {
        this.alertMsg = msg;
        this.alert = true;
      } else {
        this.registHandler();
      }
    },
    registHandler() {
      var contentFile = this.createFileByInnerEditorText();
      var formData = new FormData();
      formData.append("files", contentFile);
      formData.append("files", this.fileInfo);
      http3
        .post(`/article/files`, formData)
        .then(({ data }) => {
          http
            .post(`/article`, {
              thumbnail: data[1],
              title: this.articleTitle,
              content: data[0],
              created_at: new Date(),
              user_num: this.getUserNum,
              userNickname: this.getProfile,
              place: this.address.address
            })
            .then(({ data }) => {
              let msg = "등록 처리시 문제가 발생했습니다.";
              if (data === "success") {
                this.registSuccess = true;
                msg = "등록이 완료되었습니다.";
              }
              this.alertMsg = msg;
              this.alert = true;
              this.registSuccess = true;
              // clearInterval(this.polling);
              // this.storeClean();
              this.$router.push(`/article/list/${this.getUserNum}`);
            })
            .catch(e => {
              if (e.request.status === 404) {
                this.alertMsg = "등록 처리시 에러가 발생했습니다.";
                this.alert = true;
              } else {
                this.$router.push(`/apierror/${e.request.status}/`);
              }
              console.log(e.request.status);
            });
        })
        .catch(e => {
          if (e.request.status === 404) {
            this.alertMsg = "등록 처리시 에러가 발생했습니다.";
            this.alert = true;
          } else {
            this.$router.push(`/apierror/${e.request.status}/`);
          }
          console.log(e.request.status);
        });
    },
    onChangeMultipleImages() {
      this.multFlag = true;
      for (const key in this.uploadImgs) {
        this.imgPool.push(this.uploadImgs[key]);
      }
    },
    onChangeImages(e) {
      const file = e.target.files[0];
      this.fileInfo = file;
      this.imageFileName = file.name;
      this.imageUrl = URL.createObjectURL(file);
      this.uploadImgURLs.push(file);
    },
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    returnImageURL(file) {
      return URL.createObjectURL(file);
    }
  },
  computed: {
    ...mapGetters([
      "isAuthenticated",
      "isProfileLoaded",
      "getProfile",
      "getRealName",
      "getEmail",
      "getUserNum"
    ]),
    ...mapState({
      authLoading: state => state.auth.status === "loading",
      uname: state => `${state.user.getProfile}`,
      userEmail: state => `${state.user.getEmail}`,
      userNum: state => `${state.user.getUserNum}`
    }),
    loading: function() {
      return this.authStatus === "loading" && !this.isAuthenticated;
    },
    fontColor: function() {
      return (
        "rgba(" +
        this.fontColorValue.r +
        "," +
        this.fontColorValue.g +
        "," +
        this.fontColorValue.b +
        ",1)"
      );
    },
    HiliteColor: function() {
      return (
        "rgba(" +
        this.HiliteColorValue.r +
        "," +
        this.HiliteColorValue.g +
        "," +
        this.HiliteColorValue.b +
        ",1)"
      );
    }
  },
  watch: {
    fontValue: function(newVal) {
      this.execValue("fontName", false, newVal);
    },
    fontSizeValue: function(newVal) {
      this.execValue("fontSize", false, newVal);
    },
    fontColorValue: function(newVal) {
      this.execValue(
        "foreColor",
        false,
        "rgba(" + newVal.r + "," + newVal.g + "," + newVal.b + ",1)"
      );
    },
    HiliteColorValue: function(newVal) {
      this.execValue(
        "hiliteColor",
        false,
        "rgba(" + newVal.r + "," + newVal.g + "," + newVal.b + ",1)"
      );
    }
  }
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=East+Sea+Dokdo&family=Gaegu&family=Hi+Melody&family=Nanum+Gothic&family=Nanum+Myeongjo&family=Nanum+Pen+Script&family=Poor+Story&family=Sunflower:wght@300&family=Yeon+Sung&display=swap");

.v-select--reveal {
  opacity: 0.5;
  bottom: 0;
}
</style>