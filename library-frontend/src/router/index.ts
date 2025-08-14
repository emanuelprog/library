import { defineRouter } from '#q-app/wrappers';
import {
  createMemoryHistory,
  createRouter,
  createWebHashHistory,
  createWebHistory,
} from 'vue-router';
import routes from './routes';
import { useUserStore } from 'src/stores/userStore';

export default defineRouter(function () {
  const createHistory = process.env.SERVER
    ? createMemoryHistory
    : (process.env.VUE_ROUTER_MODE === 'history' ? createWebHistory : createWebHashHistory);

  const Router = createRouter({
    scrollBehavior: () => ({ left: 0, top: 0 }),
    routes,
    history: createHistory(process.env.VUE_ROUTER_BASE),
  });

  const userStore = useUserStore();

  Router.beforeEach((to, from, next) => {
    const { selectedUser } = userStore;

    if (to.meta.requiresUser && !userStore.selectedUser) {
      return next('/login');
    }

    if (to.meta.allowedRoles && Array.isArray(to.meta.allowedRoles)) {
      if (!selectedUser?.role || !to.meta.allowedRoles.includes(selectedUser.role)) {
        return next('/home');
      }
    }

    next();
  });

  return Router;
});
