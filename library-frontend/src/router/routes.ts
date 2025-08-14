import type { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/',
    component: () => import('layouts/AuthLayout.vue'),
    children: [
      { path: 'login', component: () => import('pages/Login/LoginPage.vue'), meta: { public: true }, }
    ],
  },
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: 'home', component: () => import('pages/Home/HomePage.vue'), meta: { requiresUser: true } },
      {
        path: 'generos',
        component: () => import('pages/Genre/GenrePage.vue'),
        meta: { requiresUser: true, allowedRoles: ['ROLE_WRITER'] }
      },
      {
        path: 'autores',
        component: () => import('pages/Author/AuthorPage.vue'),
        meta: { requiresUser: true, allowedRoles: ['ROLE_WRITER'] }
      },
      {
        path: 'livros',
        component: () => import('pages/Book/BookPage.vue'),
        meta: { requiresUser: true, allowedRoles: ['ROLE_WRITER', 'ROLE_READER'] }
      }],
  },
];

export default routes;
