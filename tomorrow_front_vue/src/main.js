// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import Home from './components/Home'
import Login from './components/Login.vue';
import GoogleAuth from 'vue-google-auth'
import VueRouter from 'vue-router';
import  './assets/style.css';

Vue.use(VueRouter);

const routes = [
  { path: '/home/:userId', component: Home},
  { path: '/', component: Login}
];

const router = new VueRouter({
  mode: 'history',
  routes
});


// Client ID and API key from the Developer Console
const CLIENT_ID = '615577723874-v1d83mvihgvvsnkjo66gcla0p4p7a6tg.apps.googleusercontent.com';
const API_KEY = 'AIzaSyAr6I3WK1A79Clya3XY-OypP2DvHSkk_1g';
const SCOPES = 'https://www.googleapis.com/auth/calendar.readonly';

Vue.use(GoogleAuth, { client_id: '615577723874-v1d83mvihgvvsnkjo66gcla0p4p7a6tg.apps.googleusercontent.com' })
Vue.googleAuth().load()

Vue.component('app-home', Home);
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  router, 
  el: '#app',
  components: { App },
  template: '<App/>'
})
