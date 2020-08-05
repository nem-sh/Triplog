<template>
    <div>
        <h3>제목 검색결과</h3>
            <article-search-table-comp
            :propKeyword="this.keyword"
            :propOption="title"
            />
        <br>
        <h3>작성자 검색결과</h3>
            <article-search-table-comp
            :propKeyword="this.keyword"
            :propOption="writer"
            />
    </div>
</template>

<script>
import ArticleSearchTableComp from "@/components/search/ArticleSearchTableComp.vue";

export default {
    name: 'ArticleSearch',
    components: {
        ArticleSearchTableComp,
    },
    data: function () {
        return {
            keyword: "",
            title: "title",
            writer: "writer"
        };
    },

    created() {
      this.keyword = this.$route.query.keyword;
   },
}
</script>
