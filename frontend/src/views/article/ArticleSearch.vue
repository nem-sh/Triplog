<template>
  <v-container>
      
      <li :v-for="article in articles">
          {{ article }}
      </li>
      아티클
      
  </v-container>
  
</template>

<script>
import axios from "axios";
export default {
    name:"ArticleSearch",
    data () {
        return {
            articles: Object,
            keyword: null,
        }
    } ,
   
   methods: {
       fetchData: function() {
            axios.get(`http://localhost:8080/api/article/searchArticle/${this.keyword}`)
            .then(function(response) {
                this.articles = response
            })
            .catch((e) => {
                console.log(e)
                console.log("reefklsdanfkl;asdjfkl;asj")
                console.log(this.keyword);
            });
     },

   },
   created(){
      this.keyword = this.$route.query.keyword
       this.fetchData()
   }
   
}
</script>

<style>

</style>