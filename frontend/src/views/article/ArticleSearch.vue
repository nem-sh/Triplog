<template>
  <v-container>
      
      <li v-for="article in articles" :key="article.num">
          {{article.num}}
          <br>
          {{article.content}}
          {{article.title}}
      </li>
      
  </v-container>
  
</template>

<script>
import axios from "axios";
export default {
    name:"ArticleSearch",
    data () {
        return {
            articles: [],
            keyword: "",
            selected:"",
        }
    } ,
   
   methods: {
       fetchData: function() {
           if (this.selected=="제목") {
            axios.get(`http://localhost:8080/api/search/searchArticle/${this.keyword}`)
            
            .then((response) => {
                console.log(response)
                this.articles = response.data
                
            })
            .catch((e) => {
                console.log(e)
                
                console.log(this.keyword);
            });
           }else if (this.selected == "작성자"){
               axios.get(`http://localhost:8080/api/search/searchUserArticle/${this.keyword}`)
               .then((response) => {
                   this.articles = response.data
               }).catch((e) => {
                   console.log(e)
               });
           }
     },

   },
   created(){
      this.keyword = this.$route.query.keyword
      this.selected = this.$route.query.selected
       this.fetchData()
   }
   
}
</script>

<style>

</style>