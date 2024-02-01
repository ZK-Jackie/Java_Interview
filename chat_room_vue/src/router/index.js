import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeViewBeta.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/RegisterViewBeta.vue')
  },
  {
    path: '/user',
    name: 'user',
    component: () => import('../views/UserView.vue')
  },
  {
    path: '/room',
    name: 'room',
    component: () => import('../views/RoomView.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
