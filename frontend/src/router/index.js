import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
   {
      path: '/investment',    // 👈 기존 '/about'을 '/investment'로 수정!
      name: 'investment',     // 👈 기존 'about'을 'investment'로 수정!
      component: () => import('../views/AboutView.vue')
    }
  ],
})

export default router
