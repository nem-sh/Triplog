<template>
    <v-card class="ma-4" width="200" @click="clickTableRow()">
        <v-scale-transition> 
            <v-img v-if="thumbnail" :src="`../../articleImage/${thumbnail}`"
            height="200px">
            </v-img>
            <v-img v-else :src="`../../articleImage/noimage.png`"
            class="align-end"
            >
            </v-img>
            <template v-slot:placeholder>
                <v-row
                    class="fill-height ma-0"
                    align="center"
                    justify="center"
                >
                    <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                </v-row>
            </template>
        </v-scale-transition>
        
        <v-card-title>
            {{this.title}}
        </v-card-title>
        <v-card-subtitle>
            {{this.created_at | moment('YYYY-MM-DD')}}
        </v-card-subtitle>
    </v-card>
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