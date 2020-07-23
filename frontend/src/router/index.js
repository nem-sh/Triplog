import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
// import error from "../components/error.vue";

import APIError from "../components/error/APIError.vue";
import ArticleWrite from "../views/article/ArticleWrite.vue"
// import store from "../store/modules/user.js";

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
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue")
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
    path: "/write",
    name: "write",
    component: ArticleWrite
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
