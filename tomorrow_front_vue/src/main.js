import Vue from 'vue';
import App from './App.vue'
import Home from './components/Home.vue';
import Login from './components/Login.vue';
import "./assets/style.css";
import VueRouter from 'vue-router';

Vue.use(VueRouter);


const routes = [
  { path: '/home/:userId', component: Home},
  { path: '/', component: Login}
];

const router = new VueRouter({
  mode: 'history',
  routes
});

new Vue({
  router, 
  render: h => h(App)
}).$mount('#app')


