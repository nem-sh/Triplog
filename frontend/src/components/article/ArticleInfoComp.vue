<template>
  <div>
    <v-simple-table>
      <template v-slot:default borderless>
        <tbody>
            <tr>
                <th class="text-left">제목</th>
                <td class="text-left">{{articleTitle}}</td>
            </tr>
            <tr>
                <th class="text-left">장소</th>
                <td class="text-left">{{articlePlace}}</td>
            </tr>
            <tr>
                <th class="text-left">일정 시작 날짜</th>
                <td class="text-left">{{getFormatDate(articleDateStart)}}</td>
            </tr>
            <tr>
                <th class="text-left">일정 종료 날짜</th>
                <td class="text-left">{{getFormatDate(articleDateEnd)}}</td>
            </tr>
            <tr>
                <th class="text-left">작성일</th>
                <td class="text-left">{{getFormatDate(articleCreateAt)}}</td>
            </tr>
            <tr>
                <td colspan="2">
                    <v-p>
                        {{articleContent}}
                    </v-p>
                </td>
            </tr>
        </tbody>
      </template>
    </v-simple-table>
    <br />
    <br />
    <div class="text-center">
        <v-btn to="/noticeList">수정</v-btn>
        <v-btn to="/noticeList">삭제</v-btn>
        <v-btn to="/noticeList">목록</v-btn>
    </div>
  </div>
</template>

<script>
import moment from "moment";
export default {
  name: "articleInfoComp",
  props: {
    articleTitle: { type: String },
    articlePlace: { type: String },
    articleDateStart: { type: String },
    articleDateEnd: { type: String },
    articleCreateAt: { type: String },
    articleContent: {type: String}
  },
  methods: {
    getFormatDate(regtime) {
      return moment(new Date(regtime)).format("YYYY.MM.DD HH:mm:ss");
    }
  }
};
</script>
