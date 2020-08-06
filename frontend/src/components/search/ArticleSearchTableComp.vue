<template>
    <v-row>
        <v-sheet class="mx-auto" max-width="900">
            <v-slide-group class="pa-4">
                <v-slide-item v-for="item in items" :key="item.num">
                    <article-search-comp
                    :propTitle="item.title"
                    :propUserNickname="item.userNickname"
                    :propThumbnail="item.thumbnail"
                    :propCreated_at="item.created_at"
                    :propNum="item.num"
                    />
                </v-slide-item>
            </v-slide-group>
        </v-sheet>
    </v-row>
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