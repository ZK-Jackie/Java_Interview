import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeViewBeta from '../views/HomeViewBeta.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeViewBeta,
    meta:{
      index: 1
    }
  },
  {
    path: '/login',
    name: 'login',
    props: true,
    component: () => import('../views/LoginView.vue'),
    meta:{
      index: 2
    }
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/RegisterViewBeta.vue'),
    meta:{
      index: 2
    }
  },
  {
    path: '/user',
    name: 'user',
    component: () => import('../views/UserView.vue'),
    meta:{
      index: 2
    }
  },
  {
    path: '/daily',
    name: 'dailyNews',
    component: () => import('../views/adminviews/DailynewsView.vue'),
    meta:{
      index: 2
    }
  },
  {
    path: '/log',
    name: 'log',
    component: () => import('../views/adminviews/LogView.vue'),
    meta:{
      index: 2
    }
  },
  {
    path: '/dept',
    name: 'dept',
    component: () => import('../views/adminviews/DeptView.vue'),
    meta:{
      index: 2
    }
  },
  {
    path: '/emp',
    name: 'emp',
    component: () => import('../views/adminviews/EmpView.vue'),
    meta:{
      index: 2
    }
  },
  {
    path: '/product',
    name: 'product',
    component: () => import('../views/adminviews/ProductView.vue'),
    meta:{
      index: 2
    }
  },
  {
    path: '/order',
    name: 'order',
    component: () => import('../views/empviews/OrderView.vue'),
  },
  {
    path: '/member',
    name: 'member',
    component: () => import('../views/empviews/CustomerView.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
