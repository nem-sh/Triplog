<template>
  <div id="main_wrap">
    <v-row dense>
      <v-col cols="12" md="2" align-self="center">
        <div style="width:100%; text-align:center" :style="{fontFamily : 'Nanum Gothic'}">제목</div>
      </v-col>
      <v-col cols="12" md="9">
        <v-text-field v-model="articleTitle" label="제목" id="title" ref="title"></v-text-field>
      </v-col>
    </v-row>
    <v-row dense>
      <v-col cols="12" md="2" align-self="center">
        <div style="width:100%; text-align:center" :style="{fontFamily : 'Nanum Gothic'}">장소</div>
      </v-col>
      <v-col cols="12" md="9">
        <v-dialog v-model="addressDialog" max-width="300">
          <template v-slot:activator="{ on, attrs }">
            <div>
              <v-btn small v-bind="attrs" v-on="on" :style="{fontFamily : 'Nanum Gothic'}">장소 찾기</v-btn>
              {{ place.name }}
            </div>
          </template>
          <FindPlace @childs-event="getPlace" />
        </v-dialog>
      </v-col>
    </v-row>

    <div class="text-center">
      <v-bottom-sheet v-model="sheet" hide-overlay>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            color="teal"
            dark
            v-bind="attrs"
            v-on="on"
            bottom
            fixed
          >
            이미지풀 열기
          </v-btn>
        </template>
        <v-sheet class="text-center" height="320px">
          <v-row>
            <v-col cols="3">
              <v-file-input
                class="ml-12"
                dense
                label="이미지 업로드"
                multiple
                accept="image/*"
                prepend-icon="mdi-camera"
                @change="onChangeMultipleImages"
                v-model="uploadImgs"
              ></v-file-input>
            </v-col>
            <v-spacer></v-spacer>
          <v-btn
            class="mr-12 mt-4"
            text
            color="teal"
            @click="sheet = !sheet"
          >close</v-btn>
          </v-row>
          <v-row class="fill-height" v-if="imgPool.length == 0" justify="center" align="center">
          <h3>이미지를 업로드 한 후 에디터로 끌어다 놓으세요.</h3>
          </v-row>
          <v-row>
            <v-sheet class="mx-auto" max-width="1300">
              <v-slide-group multiple show-arrows="mobile">
                <v-slide-item v-for="(item, index) in imgPool" :key="index">
                  <v-card class="ma-4" width="150" :id="index" elevation="0">
                      <img
                        :src="returnImageURL(item)"
                        width="150"
                        height="200"
                        @dragend="dragEnd"
                      />
                  </v-card>
                </v-slide-item>
              </v-slide-group>
            </v-sheet>
          </v-row>
        </v-sheet>
    </v-bottom-sheet>
    </div>
    
    <v-btn v-if="existChatbot && hidden" @click="useChatbot">
      <v-icon>fas fa-robot</v-icon>
    </v-btn>
    <v-btn v-if="!hidden" @click="useChatbot">접기</v-btn>
    <v-row v-if="useChatbotImg">
      <v-sheet class="mx-auto" max-width="1000">
        <v-slide-group multiple show-arrows="mobile">
          <v-slide-item v-for="(item, index) in chatbotImg" :key="index">
            <v-card class="ma-4" width="200" :id="index">
              <img :src="item.media" width="200" height="300" @dragend="dragEnd" />
              <p>{{item.comment}}</p>
            </v-card>
          </v-slide-item>
        </v-slide-group>
      </v-sheet>
    </v-row>
    <br />

    <input ref="imageInput" type="file" accept="image/*" hidden @change="onChangeImages" />

    <br />
    <v-sheet elevation="3" class="pa-0">
      <v-toolbar dense color="elevation-0">
        <v-col cols="2">
          <v-select
            :items="fontSizeItems"
            v-model="fontSizeValue"
            dense
            menu-props="auto"
            prepend-inner-icon="mdi-format-size"
            class="mt-3 mr-2"
            color="cyan darken-2"
          />
        </v-col>

        <v-col cols="2">
          <v-select
            :items="fontItems"
            v-model="fontValue"
            dense
            prepend-inner-icon="mdi-format-font"
            class="mt-3 ml-2"
            menu-props="auto"
            :style="{fontFamily : fontValue}"
            color="cyan darken-2"
          />
        </v-col>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="addQuoteIntoEditor"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-quote-open</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">인용구</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="execValue('formatBlock', true, '<blockquote>')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-quote-open</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">문단으로 감싸기</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('strikeThrough')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-quote-open</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">가로줄 추가</span>
        </v-tooltip>

        <v-tooltip bottom>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              @click="exec('removeFormat')"
              label
              icon
              color="cyan darken-2"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-quote-open</v-icon>
            </v-btn>
          </template>
          <span :style="{fontFamily : 'SunFlower'}">포멧 제거</span>
        </v-tooltip>

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

        <v-menu open-on-hover offset-y>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              label
              :style="{backgroundColor : HiliteColor, color : fontColor}"
              v-on="on"
              v-bind="attrs"
              icon
            >
              <v-icon>mdi-marker</v-icon>
            </v-btn>
          </template>

          <v-list>
            <v-list-item>
              <v-color-picker
                hide-inputs
                show-swatches
                swatches-max-height="100px"
                v-model="HiliteColorValue"
              ></v-color-picker>
            </v-list-item>
          </v-list>
        </v-menu>

        <v-menu open-on-hover offset-y>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              class="mr-1"
              icon
              label
              :style="{backgroundColor : HiliteColor, color : fontColor}"
              v-on="on"
              v-bind="attrs"
            >
              <v-icon>mdi-format-color-text</v-icon>
            </v-btn>
          </template>

          <v-list>
            <v-list-item>
              <v-color-picker
                hide-inputs
                show-swatches
                swatches-max-height="100px"
                v-model="fontColorValue"
              ></v-color-picker>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-toolbar>

    <v-toolbar dense color="elevation-0">
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
      <v-divider vertical></v-divider>

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
    </v-toolbar>
    <br>
    <v-divider></v-divider>
    <v-sheet height="650">
        <iframe
          v-if="!$route.params.articleNum"
          id="editor"
          :src="editorSrc"
          frameborder="0"
          scrolling="auto"
          style="width:100%; height:100%"
        ></iframe>
        <iframe
          v-if="realContent"
          id="editor"
          :srcdoc="realContent"
          frameborder="0"
          scrolling="auto"
          style="width:100%; height:100%"
        ></iframe>
      </v-sheet>
    </v-sheet>

    <v-row>
      <v-col>
        <v-btn
          v-if="!$route.params.articleNum"
          tile
          outlined
          @click="temp"
          :style="{fontFamily : 'Nanum Gothic'}"
        >임시 저장</v-btn>
      </v-col>
      <v-col align="right">
        <v-btn
          v-if="!$route.params.articleNum"
          tile
          outlined
          @click="regist"
          :style="{fontFamily : 'Nanum Gothic'}"
        >등록</v-btn>
        <v-btn v-else tile outlined @click="regist" :style="{fontFamily : 'Nanum Gothic'}">수정</v-btn>
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
import { mapGetters, mapState } from 'vuex';
import FindPlace from "@/components/GoogleMap/FindPlace.vue";

export default {
  name: "ArticleWriteComp",
  components: {
    FindPlace,
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
      prefix: '<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" /><title>Editor</title></head><body>',
      suffix: '</body></html>',
      tempPrefix: '<html>\
    <head>\
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />\
        <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />\
        <title>Editor</title>\
        <script type="text/javascript">\
\
            window.onload = function() {\
                if (document.body && document.body.contentEditable != undefined && window.ActiveXObject) {\
                    document.body.contentEditable = "true";\
                } else {\
                    document.designMode = "on";\
                }\
\
                const config = { attributes: false, childList: true, subtree: true };\
\
                const callback = function(mutationsList, observer) {\
                    for(let mutation of mutationsList) {\
                        if (mutation.type === "childList") {\
                            if((mutation.addedNodes.length > 0) && (mutation.addedNodes[0].tagName == "IMG")) {\
                                var imgNode = mutation.addedNodes[0];\
                                var parent = imgNode.parentElement;\
                                var newDiv = document.createElement("div");\
                                newDiv.setAttribute("class", "resizable");\
                                newDiv.setAttribute("contentEditable", "false");\
                                newDiv.setAttribute("draggable", "true");\
                                var newImgNode = document.createElement("img");\
                                newImgNode.setAttribute("src", imgNode.src);\
                                newImgNode.setAttribute("style", "width:100%; height:100%");\
                                newImgNode.setAttribute("draggable", "false");\
                                newDiv.appendChild(newImgNode);\
                                if(parent == null) {\
                                    imgNode.remove();\
                                    mutation.previousSibling.appendChild(newDiv);\
                                    newDiv.remove();\
                                }\
                                else {\
                                    parent.replaceChild(newDiv, imgNode);\
                                }\
                            }\
                        }\
                    }\
                };\
\
                const observer = new MutationObserver(callback);\
\
                observer.observe(document, config);\
            }\
    </sc', 
      tempPrefix2 : 'ript>\
        <link href="https://fonts.googleapis.com/css2?family=East+Sea+Dokdo&family=Gaegu&family=Hi+Melody&family=Nanum+Gothic&family=Nanum+Myeongjo&family=Nanum+Pen+Script&family=Poor+Story&family=Sunflower:wght@300&family=Yeon+Sung&display=swap"\
        rel="stylesheet">\
    </head>\
    <body id="editorBody">',
      editorSrc: "../editor.html",
      addressDialog: false,
      place: {
        name: "",
        lat: 0,
        lng: 0
        },
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
      ImgPath: "",
      editorHtmlFile: null,
      fontColorValue: {
        r: 0,
        g: 0,
        b: 0,
        a: 1
      },
      HiliteColorValue: {
        r: 255,
        g: 255,
        b: 255,
        a: 1
      },
      sheet: false,
      chatbotImg:[],
      existChatbot: false,
      useChatbotImg:false,
      hidden:true,
      realContent: "",
      article: {},
    };
  },
  created() {
    // this.start();
    http.get(`chatbot/${this.getUserNum}`)
    .then(({data})=>{
      this.chatbotImg = data;
      this.existChatbot = true;
    })
    if(this.$route.params.articleNum){
      http
        .get(`/article/update/${this.$route.params.articleNum}`)
        .then(({ data }) => {
          this.articleTitle = data.title;
          this.articleContent = data.content;
          this.place.name = data.place;
          this.place.lat = data.lat;
          this.place.lng = data.lng;  
          this.article = data;
          console.log(this.article);
          this.openContentFile();
        });
    }
  },
  mounted() {
    if (window.localStorage.getItem("isSaved") == "true") {
      this.confirmLoadData();
    }
  },
  methods: {
    addQuoteIntoEditor: function() {
      var quoteDiv = 
      '<div style = "width:100%; display:block;">\
        <div style = "width:100%; text-align:center;">\
          <img src="../../template/image/quote-open.png">\
        </div>\
        <div style = "width:100%; text-align:center;">\
          <h1 class="textH" data-text="내용을 입력하세요."></div>\
        </div>\
        <div style = "width:100%; text-align:center;">\
          <h5 class="textH" data-text="출처" style="color:gray;"></div>\
        </div>\
        <div style = "width:100%; text-align:center;">\
          <img src="../../template/image/quote-close.png">\
        </div>\
      </div>';
      this.execValue("insertHTML", false, quoteDiv);
    },
    dragEnd: function(event) {
      if(event.dataTransfer.dropEffect == "copy") {
        this.imgPool.splice(event.target.parentElement.getAttribute("id"), 1);
      }
    },
    createFileByInnerEditorTextAndReturnImgFileArr: async function() {
      var innerIframe = document.getElementById("editor").contentWindow.document
        .body.innerHTML;
      var content = this.prefix + innerIframe + this.suffix;

      var parsedDoc = new DOMParser().parseFromString(content, 'text/html');
      var imgTags = parsedDoc.getElementsByTagName('img');
      var imgFiles = [];
      var key;
      for (key = 0; key < imgTags.length; key++) {
        var imgSrc = imgTags[key].src;
        let blob = await fetch(imgSrc).then(r => r.blob());
        var imgFileName;
        if(imgSrc.split('/').reverse()[0].split('.').length > 1) {
          imgFileName = imgSrc.split('/').reverse()[0];
        }else{
          imgFileName = imgSrc.split('/').reverse()[0] + "." + blob.type.split('/').reverse()[0];
        }
        var newFile = new File([blob], imgFileName, {
          type: blob.type,
        });
        imgTags[key].src = "../../articleImage/" + newFile.name;
        imgFiles.push(newFile);
      }

      var imgWrappers = parsedDoc.getElementsByClassName("resizable");
      for (key = 0; key < imgWrappers.length; key++) {
        imgWrappers[key].style.resize = "none";
      }

      content = parsedDoc.documentElement.innerHTML;
      var fileName = this.getUserNum + "_" + this.articleTitle + ".html";
      this.editorHtmlFile = new File([content], fileName, {
        type: "text/html",
      });

      return imgFiles;
    },
    createTempFileByInnerEditorTextAndReturnImgFileArr: async function() {
      var innerIframe = document.getElementById("editor").contentWindow.document
        .body.innerHTML;
      var content = this.tempPrefix + this.tempPrefix2 + innerIframe + this.suffix;

      var parsedDoc = new DOMParser().parseFromString(content, 'text/html');
      var imgTags = parsedDoc.getElementsByTagName('img');
      var imgFiles = [];
      var key;
      for (key = 0; key < imgTags.length; key++) {
        var imgSrc = imgTags[key].src;
        let blob = await fetch(imgSrc).then(r => r.blob());
        var imgFileName = imgSrc.split('/').reverse()[0] + "." + blob.type.split('/').reverse()[0];
        var newFile = new File([blob], imgFileName, {
          type: blob.type,
        });
        imgTags[key].src = "../../articleImage/" + newFile.name;
        imgFiles.push(newFile);
      }

      content = parsedDoc.documentElement.innerHTML;
      var fileName = this.getUserNum + "_" + this.articleTitle + ".html";
      this.editorHtmlFile = new File([content], fileName, {
        type: "text/html",
      });

      return imgFiles;
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
      window.localStorage.removeItem("tempHtmlFileName");
    },
    loadData() {
      this.editorSrc = "../../content/temp/" + window.localStorage.getItem("tempHtmlFileName");
      this.dialog = false;
    },
    confirmLoadData() {
      this.dialog = true;
    },
    storeInLocal() {
      if (window.localStorage && (this.articleContent || this.articleTitle)) {
        window.localStorage.setItem("isSaved", "true");
        this.tempRegistHandler();
      } else if (!window.localStorage) {
        console.log("Error" + ": Your browser not support");
      }
    },
    temp() {
      this.storeInLocal();
    },
    tempRegistHandler: async function() {
     var imgFiles = await this.createTempFileByInnerEditorTextAndReturnImgFileArr();
     var formData = new FormData();
     formData.append('files', this.editorHtmlFile);
     for (const key in imgFiles) {
       formData.append('files', imgFiles[key]);
     }
    http3
      .post(`/article/tempFiles`, formData).then(({ data }) => {
        window.localStorage.setItem("tempHtmlFileName", data[0]);
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
    }
    else {
      this.$route.params.articleNum == null ? this.registHandler() : this.updateHandler();
    }
   },
   registHandler: async function() {
     var imgFiles = await this.createFileByInnerEditorTextAndReturnImgFileArr();
     var formData = new FormData();
     formData.append('files', this.editorHtmlFile);
     for (const key in imgFiles) {
       formData.append('files', imgFiles[key]);
     }
    http3
      .post(`/article/files`, formData).then(({ data }) => {
        http
          .post(`/article`, {
            thumbnail : data[1],
            title: this.articleTitle,
            content: data[0],
            created_at: new Date(),
            user_num: this.getUserNum,
            userNickname: this.getProfile,
            place: this.place.name,
            lat: this.place.lat,
            lng: this.place.lng
          }).then(({ data }) => {
            let msg = "등록 처리시 문제가 발생했습니다.";
            if (data === "success") {
                this.registSuccess = true;
                msg = "등록이 완료되었습니다.";
              }
              this.alertMsg = msg;
              this.alert = true;
              this.registSuccess = true;
              // clearInterval(this.polling);
              this.storeClean();
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
    updateHandler: async function() {
      var imgFiles = await this.createFileByInnerEditorTextAndReturnImgFileArr();
      var formData = new FormData();
      formData.append('files', this.editorHtmlFile);
      for (const key in imgFiles) {
        formData.append('files', imgFiles[key]);
      }
      http3
      .post(`/article/files`, formData).then(({ data }) => {
        http
          .put(`/article/${this.article.num}`, {
            num: this.article.num,
            user_num: this.article.user_num,
            trippackage_num: this.article.trippackage_num,
            title: this.articleTitle,
            place: this.place.name,
            content: data[0],
            thumbnail: data[1],
            temp: this.article.temp,
            created_at: this.article.created_at,
            date_start: this.article.date_start,
            date_end: this.article.date_end,
            likeCount: this.article.likeCount,
            views: this.article.views,
            userNickname: this.article.userNickname,
            lat: this.place.lat,
            lng: this.place.lng,
          })
          .then(({ data }) => {
            let msg = "수정 처리시 문제가 발생했습니다.";
            if (data === "success") {
              this.registSuccess = true;
              msg = "수정이 완료되었습니다.";
            }
            this.alertMsg = msg;
            this.alert = true;
            this.registSuccess = true;
            this.storeClean();
            this.$router.push(`/article/list/${this.getUserNum}`);
          })
          .catch(() => {
            this.alertMsg = "수정 처리시 에러가 발생했습니다.";
            this.alert = true;
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
    openContentFile: function() {
      var url = "../../content/registered/" + this.articleContent;
      var xhr = new XMLHttpRequest();
      xhr.responseType = "text";

      var setRealContent = val => {
        var body = val.split('<body>');
        var content = this.tempPrefix + this.tempPrefix2 + body[1];
        this.realContent = (content);
      };
      
      xhr.onload = function(e) {
        var resp = xhr.responseText || e.target.responseText;
        setRealContent(resp);
      };

      xhr.open("GET", url);
      xhr.send();
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
    getPlace(place){
      this.addressDialog = place.addressDialog;
      this.place = place;
    },
    returnImageURL(file) {
      return URL.createObjectURL(file);
    },
    useChatbot(){
      this.hidden = !this.hidden;
      this.useChatbotImg = !this.useChatbotImg;
      console.log(this.hidden)
      console.log(this.useChatbotImg)
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
    },
    
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
