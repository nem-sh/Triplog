<template>
  <div id="main_wrap" >
    <v-simple-table style="width: 80%;">
      <template v-slot:default>
        <tbody>
          <tr>
              <th class="text-left">TITLE</th>
              <td>
                <v-text-field
                  type="text"
                  class="form-control"
                  id="title"
                  ref="title"
                  label="제목을 입력하세요"
                  v-model="articleTitle"
                />
              </td>
          </tr>
          <tr>
              <th class="text-left">Content</th>
              <td>
              <v-textarea
               label="내용을 입력하세요"
               id="content"
               ref="content"
               v-model="articleContent"
              >
              </v-textarea>
              </td>
          </tr>
          
          <!-- <tr>
            <td colspan="2">
            <div>
              <v-btn @click="temp">임시 저장</v-btn>
              <v-btn @click="regist">등록</v-btn>
            </div>
            </td>
          </tr> -->
        </tbody>
        <v-row>
          <v-col>
            <v-btn @click="temp">임시 저장</v-btn>
          </v-col>
          <v-col>
            <v-btn @click="regist">등록</v-btn>
          </v-col>
        </v-row>
      </template>
    </v-simple-table>
  </div>
</template>

<script>
export default {
  name: "login",
  data() {
    return {
      articleContent: "",
      articleTitle: "",
    };
  },
 methods: {
   temp() {

   },
   regist() {

   },
 },
};
</script>
