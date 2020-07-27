import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
// import error from "../components/error.vue";
import APIError from "../components/error/APIError.vue";
import ArticleWrite from "../views/article/ArticleWrite.vue"
// import store from "../store/modules/user.js";
import ArticleSearch from "../views/article/ArticleSearch.vue";
import ArticleInfo from "../views/article/ArticleInfo.vue"
import ArticleList from "../views/article/ArticleList.vue"
import ArticleModify from "../views/article/ArticleModify.vue"
import Like from "../views/likey/Like.vue"
import temp from "../views/temporaryArticle/temp.vue"
Vue.use(VueRouter);

// const requireAuth = () => (to, from, next) => {
//   if (store.state.profilename !== '') {
//     return next();
//   }
//   next('/');
// };

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/like",
    name: "Like",
    component: Like
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue")
  },
  {
    path:"/article/searchArticle",
    name:"ArticleSearch",
    component: ArticleSearch
  },
  
  {
    path: "/apierror/:errorCode",
    name: "APIError",
    component: APIError
  },
  // {
  //   path: "/*",
  //   name: "error",
  //   component: error
  // },
  {
    path: "/article/write",
    name: "articleWrite",
    component: ArticleWrite
  },
  {
    path: "/article/detail",
    name: "articleDetail",
    component: ArticleInfo
  },
  {
    path: "/article/list",
    name: "articleList",
    component: ArticleList
  },
  {
    path: "/article/modify",
    name: "articleModify",
    component: ArticleModify
  },
  {
    path: "/tempArticle",
    name: "tempArticle",
    component: temp
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
