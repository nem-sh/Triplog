<template>
<v-container>
    <br>
    <br>
    <h3>제목 검색결과</h3>
     <v-simple-table>
         
         <thead style="text-align:center;">
             <tr>
                <th style="text-align:center;">썸네일</th>
                 <th style="text-align:center;">제목</th>
                 <th style="text-align:center;">작성자</th>
                 <th style="text-align:center;">작성일</th>
             </tr>
         </thead>
         <tbody>
             <tr v-for="item in articles_title" :key="item.num" @click="clickTableRow(item)" >
                
                 <td>
                     <v-img v-if="item.thumbnail" :src="require(`@/assets/blogImage/${item.thumbnail}`)" style="width: 100px; height:100px; margin-right:0;" ></v-img>
                    <v-img v-else :src="require(`@/assets/blogImage/noimage.png`)" style="width: 100px; height:100px; margin-right:0;" ></v-img>
                 </td>
                 <td style="width: 200px; text-align:center;"> {{item.title}}</td>
                 <td style="width: 200px; text-align:center;">{{item.nickName}}</td>
                 <td style="width: 200px; text-align:center;">{{item.created_at | moment('YYYY-MM-DD') }}</td>
             </tr>

         </tbody>
         
    </v-simple-table>
    <br>
    <h3>작성자 검색결과</h3>
    <v-simple-table>
        
        <thead>
             <tr>
                <th style="text-align:center;">썸네일</th>
                 <th style="text-align:center;">제목</th>
                 <th style="text-align:center;">작성자</th>
                 <th style="text-align:center;">작성일</th>
             </tr>
         </thead>
         <tbody>
             <tr v-for="item_auth in articles_auth" :key="item_auth.num"  @click="clickTableRow(item_auth)">
                 
                 <td style="text-aligh:center;">
                     <v-img v-if="item_auth.thumbnail" :src="require(`@/assets/blogImage/${item_auth.thumbnail}`)" class="thumb" style="width: 100px; height:100px; margin-right:0;"></v-img>
                    <v-img v-else :src="require(`@/assets/blogImage/noimage.png`)" style="width: 100px; height:100px; margin-right:0;"></v-img>
                 </td>
                 <td style="width: 200px; text-align:center;"> {{item_auth.title}}</td>
                 <td style="width: 200px; text-align:center;">{{item_auth.nickName}}</td>
                 <td style="width: 200px; text-align:center;">{{item_auth.created_at  | moment('YYYY-MM-DD')}}</td>
             </tr>
             
         </tbody>
        
    </v-simple-table>
    
</v-container>
</template>

<script>
import axios from "axios";


export default {
    name:"ArticleSearch",
    components: {
    
    },
    data () {
        return {
            articles_title: [],
            articles_auth: [],
            keyword: "",
            auth: [],
            user_nickname: "",
        }
    } ,
   methods: {
       fetchData: function() {
            axios.get(`http://localhost:8080/api/search/searchArticle/${this.keyword}`)
            .then((response) => {
                console.log(response)
                this.articles_title = response.data
            })
            .catch((e) => {
                console.log(e)
            });
            axios.get(`http://localhost:8080/api/search/searchUserArticle/${this.keyword}`)
            .then((response)=>{
                this.articles_auth = response.data
            }).catch((e)=>{
                console.log(e)
            })
      
     },
    
     clickTableRow: function(obj) {
         this.$router.push(`/article/detail/${obj.num}`);
     },
    
   },
    
   created() {
      this.keyword = this.$route.query.keyword
      
       this.fetchData()
   },
   
   
}
</script>

<style>
div.v-image.v-responsive.theme--light {
    margin-right: 2px;
}
</style>