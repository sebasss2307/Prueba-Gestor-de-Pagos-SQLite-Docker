const routes = [
  // Login sin layout
  {
    path: '/',
    component: () => import('pages/LoginPage.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/login',
    component: () => import('pages/LoginPage.vue'),
    meta: { requiresAuth: false },
  },

  // Rutas con layout (autenticadas)
  {
    path: '/',
    component: () => import('layouts/AppLayout.vue'),
    children: [
      {
        path: 'dashboard',
        component: () => import('pages/DashboardPage.vue'),
        meta: { requiresAuth: true },
      },
      {
        path: 'home',
        redirect: '/dashboard',
        meta: { requiresAuth: true },
      },
      {
        path: 'recibos',
        component: () => import('pages/RecibosPage.vue'),
        meta: { requiresAuth: true },
      },
      {
        path: 'recibos/:username/:nroRecibo',
        name: 'recibo-detalle',
        component: () => import('pages/ReciboDetallePage.vue'),
        meta: { requiresAuth: true },
      },
    ],
  },

  // 404
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
