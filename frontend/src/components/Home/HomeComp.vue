<template>
<v-container>
  <v-carousel
    cycle
    interval="6000"
    height="400"
    hide-delimiters
    >
      <v-carousel-item
        v-for="(item,i) in items"
        :key="i"
        :src="require(`@/assets/mainImage/${item.src}`)"
        >
        <v-row
            class="fill-height"
            align="center"
            justify="center"
            >
            <div style="text-align:center; width: 80%">
                <h1>{{ item.h1msg }}</h1>
                <h3>{{ item.h2msg }}</h3>
            </div>
        </v-row>
      </v-carousel-item>
      
    </v-carousel>
    <v-sheet
        height="180"
        align="center"
        justify="center"
        >
        <v-row 
            class="fill-height"
            align="center"
            justify="center"
            >
            <div style="text-align:center;">
                <h1>TRIPLOG는 사용자 친화적인 블로그입니다.</h1>
                <br>
                <h3>당신은 여러 에디터를 상황, 취향에 맞게 제공받을 수 있고 이를 통해 손쉽게 글을 작성할 수 있습니다.</h3>
            </div>
        </v-row>
    </v-sheet>
    <v-container
        align="center"
        justify="center"
        >
        <h2>많은 좋아요를 받은 게시물들</h2>
        <like-sort-list-comp></like-sort-list-comp>
    </v-container>
</v-container>
</template>

<script>
import LikeSortListComp from "@/components/Home/LikeSortListComp.vue";
export default {
    name: "HomeComp",
    components: {
        LikeSortListComp
    },
    data () {
        return {
            items: [
                {
                src: 'beach-2836300_1280.jpg',
                h1msg: "TRIPLOG에서 당신의 여행을 기록하세요",
                h2msg: "전세계의 TRIPLOGGER들이 당신의 여행에 함께할 것입니다.",
                },
                {
                src: 'volkswagen-569315_1920.jpg',
                h1msg: "TRIPLOGGER 사이에서 가장 뜨거운 게시물 보기",
                h2msg: "많은 사랑을 받고 있는 게시물들을 만나보세요. 많은 사랑을 받고 있는 데는 그럴만한 이유가 있을 거예요. 아주 흥미로울 것입니다.",
                },
                {
                src: 'hiking-1031383_1920.jpg',
                h1msg: "여러 TRIPLOGGER들이 다녀간 게시물들을 만나보세요",
                h2msg: "많은 조회수를 기록한 게시물들에서 TRIPLOGGER들의 발자취를 따라가보세요. 새로운 여행을 발견할 수도 있을 것입니다.",
                },
            ],
        }
    }
};
</script>