<template>
    <tr @click="clickTableRow()">
        <td>
        <v-img v-if="thumbnail" :src="require(`@/assets/articleImage/${thumbnail}`)" style="width: 100px; height:100px; margin-right:0;" ></v-img>
        <v-img v-else :src="require(`@/assets/articleImage/noimage.png`)" style="width: 100px; height:100px; margin-right:0;" ></v-img>
        </td>
        <td style="width: 200px; text-align:center;"> {{title}}</td>
        <td style="width: 200px; text-align:center;">{{userNickname}}</td>
        <td style="width: 200px; text-align:center;">{{created_at | moment('YYYY-MM-DD') }}</td>
    </tr>
</template>

<script>
export default {
    name: "ArticleSearchComp",
    props: {
        propCreated_at: { type: String },
        propUserNickname: { type: String },
        propTitle: { type: String },
        propThumbnail: { type: String },
        propNum: {type: Number},
    },
    data() {
        return {
            created_at: this.propCreated_at,
            userNickname: this.propUserNickname,
            title: this.propTitle,
            thumbnail: this.propThumbnail,
            num: this.propNum,
        };
    },
    methods: {
        clickTableRow: function() {
            this.$router.push(`/article/detail/${this.num}`);
        },
    },
}
</script>