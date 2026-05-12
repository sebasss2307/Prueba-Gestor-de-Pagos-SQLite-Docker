import { defineRouter } from '#q-app/wrappers'
import {
  createRouter,
  createMemoryHistory,
  createWebHistory,
  createWebHashHistory,
} from 'vue-router'
import routes from './routes'

export default defineRouter((/* { store, ssrContext } */) => {
  const createHistory = process.env.SERVER
    ? createMemoryHistory
    : process.env.VUE_ROUTER_MODE === 'history'
      ? createWebHistory
      : createWebHashHistory

  const Router = createRouter({
    scrollBehavior: () => ({ left: 0, top: 0 }),
    routes,
    history: createHistory(process.env.VUE_ROUTER_BASE),
  })

  // Protección de rutas
  Router.beforeEach((to, from, next) => {
    const user = localStorage.getItem('user');
    const requiresAuth = to.meta.requiresAuth;

    if (requiresAuth && !user) {
      next('/');
      return;
    }
    
    if ((to.path === '/' || to.path === '/login') && user) {
      next('/dashboard');
      return;
    }
    
    next();
  });

  return Router
})
