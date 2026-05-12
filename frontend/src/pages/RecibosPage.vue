<template>
  <!-- ============================================
       PÁGINA DE RECIBOS DE PAGO
       ============================================
       Muestra los últimos 20 recibos del usuario autenticado
  -->
  <div class="q-pa-md" style="min-height: 100vh; width: 100%">
    <div class="q-pa-md">
      <!-- ============================================
           ENCABEZADO CORPORATIVO
           ============================================
      -->
      <div class="q-mb-lg">
        <div class="text-h4 text-weight-bold" style="color: #1e40af;">
          <q-icon name="receipt_long" class="q-mr-sm" />
          Mis Recibos de Pago
        </div>
        <div class="text-subtitle1 text-grey-7">
          Últimos 20 recibos de {{ user?.nombreCompleto }}
        </div>
      </div>

      <!-- ============================================
           LOADING
           ============================================
      -->
      <div v-if="loading" class="flex flex-center q-pa-xl">
        <q-spinner-hourglass size="50px" color="primary" />
      </div>

      <!-- ============================================
           LISTA DE RECIBOS
           ============================================
      -->
      <div v-else-if="recibos.length > 0">
        <q-card class="shadow-2" style="border-radius: 12px;">
          <q-list separator>
            <q-item
              v-for="recibo in recibos"
              :key="recibo.periodo"
              clickable
              @click="verDetalle(recibo)"
              class="recibo-item"
            >
              <!-- Avatar naranjo -->
              <q-item-section avatar>
                <q-avatar style="background-color: #fff7ed; color: #f97316;">
                  <q-icon name="receipt" />
                </q-avatar>
              </q-item-section>

              <!-- Información del recibo -->
              <q-item-section>
                <q-item-label class="text-weight-bold" style="color: #1e40af;">
                  {{ formatPeriodo(recibo.periodo) }}
                </q-item-label>
                <q-item-label caption>
                  <q-icon name="event" size="14px" class="q-mr-xs" style="color: #64748b;" />
                  Fecha de pago: {{ formatFecha(recibo.fechaPago) }}
                </q-item-label>
              </q-item-section>

              <!-- Monto en negro -->
              <q-item-section side>
                <q-item-label class="text-h6 text-weight-bold" style="color: #1e293b;">
                  {{ formatMoneda(recibo.sueldoLiquido) }}
                </q-item-label>
                <q-item-label caption class="text-right" style="color: #64748b;">
                  Sueldo Líquido
                </q-item-label>
              </q-item-section>

              <!-- Icono de navegación -->
              <q-item-section side>
                <q-icon name="chevron_right" color="grey-5" />
              </q-item-section>
            </q-item>
          </q-list>
        </q-card>
      </div>

      <!-- ============================================
           SIN RECIBOS
           ============================================
      -->
      <div v-else class="flex flex-center q-pa-xl">
        <q-card class="text-center q-pa-xl">
          <q-icon name="inbox" size="80px" color="grey-5" />
          <div class="text-h6 q-mt-md text-grey-7">
            No hay recibos disponibles
          </div>
        </q-card>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * ============================================
 * PÁGINA DE RECIBOS DE PAGO
 * ============================================
 * Muestra los últimos 20 recibos del usuario autenticado
 */

import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useQuasar } from 'quasar';
import api from 'src/services/api';

const router = useRouter();
const $q = useQuasar();
const user = ref(null);
const recibos = ref([]);
const loading = ref(true);

/**
 * ============================================
 * CARGAR DATOS AL MONTAR EL COMPONENTE
 * ============================================
 */
onMounted(async () => {
  // Cargar usuario desde localStorage
  const userStr = localStorage.getItem('user');
  if (!userStr) {
    router.push('/login');
    return;
  }

  try {
    user.value = JSON.parse(userStr);
    await cargarRecibos();
  } catch (error) {
    console.error('Error al cargar datos:', error);
    router.push('/login');
  }
});

/**
 * ============================================
 * CARGAR RECIBOS DESDE EL BACKEND
 * ============================================
 */
const cargarRecibos = async () => {
  loading.value = true;
  
  try {
    const response = await api.get(`/recibos/${user.value.username}`);
    recibos.value = response.data;
  } catch (error) {
    console.error('Error al cargar recibos:', error);
    $q.notify({
      type: 'negative',
      message: 'Error al cargar recibos',
      caption: error.response?.data?.error || 'Error desconocido',
      position: 'top',
    });
  } finally {
    loading.value = false;
  }
};

/**
 * ============================================
 * VER DETALLE DE UN RECIBO
 * ============================================
 */
const verDetalle = (recibo) => {
  console.log('Navegando a detalle de recibo:', recibo);
  console.log('Username:', recibo.username);
  console.log('Nro Recibo:', recibo.nroRecibo);
  
  router.push({
    name: 'recibo-detalle',
    params: {
      username: recibo.username,
      nroRecibo: recibo.nroRecibo
    }
  });
};

/**
 * ============================================
 * FORMATEAR PERIODO
 * ============================================
 * Convierte "04-2025" a "Abril 2025"
 */
const formatPeriodo = (periodo) => {
  if (!periodo) return '';
  const [month, year] = periodo.split('-');
  const meses = [
    'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
  ];
  return `${meses[parseInt(month) - 1]} ${year}`;
};

/**
 * ============================================
 * FORMATEAR FECHA
 * ============================================
 * Convierte "2026-01-31" a "31 de Enero de 2026"
 */
const formatFecha = (fecha) => {
  const date = new Date(fecha + 'T00:00:00');
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

/**
 * ============================================
 * FORMATEAR MONEDA
 * ============================================
 * Convierte 1007667 a "$1.007.667"
 */
const formatMoneda = (valor) => {
  return new Intl.NumberFormat('es-CL', {
    style: 'currency',
    currency: 'CLP',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(valor);
};
</script>

<style scoped>
.recibo-item {
  transition: background-color 0.2s;
}

.recibo-item:hover {
  background-color: rgba(0, 0, 0, 0.03);
}
</style>
