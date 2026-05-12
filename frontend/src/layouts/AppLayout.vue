<template>
  <q-layout view="lHh Lpr lFf">
    <!-- ============================================
         HEADER CORPORATIVO
         ============================================
         Header empresarial con diseño limpio y profesional
    -->
    <q-header elevated class="bg-white text-primary shadow-md">
      <q-toolbar class="q-py-sm" style="min-height: 64px;">
        <!-- Botón de menú -->
        <q-btn
          flat
          dense
          round
          icon="menu"
          color="primary"
          @click="leftDrawerOpen = !leftDrawerOpen"
          class="corporate-btn"
        />
        
        <!-- Título corporativo -->
        <q-toolbar-title class="flex items-center">
          <div class="text-h6 text-weight-bold" style="color: #1e3a8a;">
            Portal de Recibos
          </div>
        </q-toolbar-title>
        
        <!-- Información del usuario -->
        <div class="row items-center q-gutter-sm">
          <div class="gt-sm text-right q-mr-sm">
            <div class="text-body2 text-weight-medium" style="color: #1e3a8a;">
              {{ user?.nombreCompleto || user?.username }}
            </div>
            <div class="text-caption" style="color: #64748b;">
              Usuario activo
            </div>
          </div>
          <q-avatar size="42px" style="background-color: #1e40af;" text-color="white">
            <span class="text-weight-bold">{{ getInitials() }}</span>
          </q-avatar>
        </div>
      </q-toolbar>
      
      <!-- Línea divisoria sutil -->
      <div style="height: 1px; background-color: #e2e8f0;"></div>
    </q-header>

    <!-- ============================================
         DRAWER LATERAL CORPORATIVO
         ============================================
         Menú lateral con diseño empresarial limpio
    -->
    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      class="bg-white"
      :width="280"
    >
      <!-- Logo en el drawer -->
      <div class="q-pa-lg" style="background-color: #f8fafc; border-bottom: 1px solid #e2e8f0;">
        <img 
          src="~assets/1.-Logo-MicroeDoc-color-2.png" 
          alt="Logo" 
          style="width: 100%; height: auto; object-fit: contain;"
        />
      </div>

      <!-- Navegación -->
      <q-list padding class="q-mt-sm">
        <!-- Dashboard -->
        <q-item
          clickable
          v-ripple
          to="/dashboard"
          active-class="menu-item-active"
          class="q-mb-xs q-mx-sm corporate-menu-item"
          style="border-radius: 8px;"
        >
          <q-item-section avatar>
            <q-icon name="dashboard" size="24px" />
          </q-item-section>
          <q-item-section>
            <q-item-label class="text-weight-medium text-body2">Dashboard</q-item-label>
            <q-item-label caption class="text-caption">Vista general</q-item-label>
          </q-item-section>
        </q-item>

        <!-- Mis Recibos -->
        <q-item
          clickable
          v-ripple
          to="/recibos"
          active-class="menu-item-active"
          class="q-mb-xs q-mx-sm corporate-menu-item"
          style="border-radius: 8px;"
        >
          <q-item-section avatar>
            <q-icon name="receipt" size="24px" />
          </q-item-section>
          <q-item-section>
            <q-item-label class="text-weight-medium text-body2">Mis Recibos</q-item-label>
            <q-item-label caption class="text-caption">Historial completo</q-item-label>
          </q-item-section>
        </q-item>

        <q-separator class="q-my-md q-mx-md" />

        <!-- Cerrar Sesión -->
        <q-item
          clickable
          v-ripple
          @click="handleLogout"
          class="q-mx-sm corporate-menu-item"
          style="border-radius: 8px;"
        >
          <q-item-section avatar>
            <q-icon name="logout" size="24px" color="grey-7" />
          </q-item-section>
          <q-item-section>
            <q-item-label class="text-weight-medium text-body2 text-grey-8">
              Cerrar Sesión
            </q-item-label>
            <q-item-label caption class="text-caption">Salir del sistema</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>

      <!-- Footer corporativo -->
      <div class="absolute-bottom q-pa-md text-center" style="background-color: #f8fafc; border-top: 1px solid #e2e8f0;">
        <div class="text-caption text-weight-medium" style="color: #64748b;">
          Portal de Recibos
        </div>
        <div class="text-caption" style="color: #94a3b8;">
          Versión 1.0.0 | © 2026
        </div>
      </div>
    </q-drawer>

    <!-- ============================================
         CONTENEDOR DE PÁGINAS
         ============================================
         Área principal con fondo corporativo
    -->
    <q-page-container class="corporate-bg">
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup>
/**
 * ============================================
 * LAYOUT CORPORATIVO
 * ============================================
 * Layout empresarial con diseño formal y profesional
 */

import { ref, onMounted } from 'vue';
import { useQuasar } from 'quasar';
import api from 'src/services/api';

const $q = useQuasar();
const user = ref(null);
const leftDrawerOpen = ref(false);

/**
 * ============================================
 * CARGAR INFORMACIÓN DEL USUARIO
 * ============================================
 */
onMounted(() => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    try {
      user.value = JSON.parse(userStr);
    } catch (error) {
      console.error('Error al parsear usuario:', error);
    }
  }
});

/**
 * ============================================
 * OBTENER INICIALES DEL USUARIO
 * ============================================
 * Retorna las iniciales para el avatar
 */
const getInitials = () => {
  if (!user.value) return 'U';
  
  const nombres = user.value.nombres || '';
  const apellido = user.value.primerApellido || '';
  
  const inicial1 = nombres.charAt(0).toUpperCase();
  const inicial2 = apellido.charAt(0).toUpperCase();
  
  return inicial1 + inicial2 || 'U';
};

/**
 * ============================================
 * MANEJAR CIERRE DE SESIÓN
 * ============================================
 */
const handleLogout = () => {
  $q.dialog({
    title: 'Cerrar Sesión',
    message: '¿Está seguro que desea cerrar sesión?',
    cancel: {
      label: 'Cancelar',
      color: 'grey-7',
      flat: true,
      noCaps: true
    },
    ok: {
      label: 'Cerrar Sesión',
      color: 'primary',
      unelevated: true,
      noCaps: true
    },
    persistent: true,
  }).onOk(async () => {
    localStorage.clear();

    try {
      await api.post('/auth/logout');
    } catch (error) {
      console.error('Error al cerrar sesión en el servidor:', error);
    }

    $q.notify({
      type: 'info',
      message: 'Sesión cerrada correctamente',
      position: 'top',
      icon: 'logout',
      color: 'primary'
    });

    setTimeout(() => {
      window.location.href = '/';
    }, 300);
  });
};
</script>

<style scoped>
/* ============================================
   ESTILOS CORPORATIVOS DEL LAYOUT
   ============================================ */

/* Menú corporativo */
.corporate-menu-item {
  transition: all 0.2s ease;
  color: #1e3a8a;
}

.corporate-menu-item:not(.q-router-link--active):hover {
  background-color: #f1f5f9;
}

/* Estado activo con fondo naranjo claro */
.menu-item-active {
  background-color: #fff7ed !important;
  color: #f97316 !important;
  border-left: 4px solid #f97316;
}

.menu-item-active .q-icon {
  color: #f97316 !important;
}

.menu-item-active .q-item__label {
  color: #f97316 !important;
}

.menu-item-active .q-item__label--caption {
  color: #fb923c !important;
}

/* Header con sombra sutil */
.q-header {
  backdrop-filter: blur(10px);
}

/* Drawer con borde sutil */
.q-drawer {
  border-right: 1px solid #e2e8f0;
}
</style>
