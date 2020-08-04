<template>
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
            <article-search-comp
            v-for="item in items" 
            :key="item.num"
            :propTitle="item.title"
            :propUserNickname="item.userNickname"
            :propThumbnail="item.thumbnail"
            :propCreated_at="item.created_at"
            :propNum="item.num"
            />
        </tbody>
    </v-simple-table>
</template>

<script>
import http from "@/util/http-common";
import ArticleSearchComp from "@/components/search/ArticleSearchComp.vue";

export default {
    name:"ArticleSearchTableComp",
    components: {
        ArticleSearchComp,
    },
    props: {
        propKeyword: {type: String},
        propOption: {type: String},
    },
    data () {
        return {
            items: [],
            keyword: this.propKeyword,
            option: this.propOption,
        }
    } ,
   methods: {
       fetchData: function() {
           if (this.option == 'title') {
                http.get(`/search/searchArticle/${this.keyword}`)
                .then((response) => {
                    console.log(response)
                    this.items = response.data
                })
                .catch((e) => {
                    console.log(e)
                });

           } else {
               http.get(`/search/searchUserArticle/${this.keyword}`)
                .then((response)=>{
                    console.log(response)
                    this.items = response.data
                }).catch((e)=>{
                    console.log(e)
                })
           }
      
     },
   },
    
   created() {
      this.fetchData()
   },
}
</script>