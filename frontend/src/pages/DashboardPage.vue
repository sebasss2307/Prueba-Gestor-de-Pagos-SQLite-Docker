<template>
  <q-page class="q-pa-md">
    <div class="q-pa-md fade-in" style="max-width: 1400px; margin: 0 auto;">
      <!-- ============================================
           ENCABEZADO CORPORATIVO
           ============================================
      -->
      <div class="q-mb-lg">
        <div class="text-h4 corporate-title q-mb-xs">
          Dashboard
        </div>
        <div class="text-body1 corporate-text">
          Bienvenido, <span class="text-weight-medium" style="color: #1e3a8a;">{{ user?.nombreCompleto }}</span>
        </div>
      </div>

      <!-- ============================================
           TARJETAS DE MÉTRICAS CORPORATIVAS
           ============================================
      -->
      <div class="row q-col-gutter-md q-mb-lg">
        <!-- Total de Recibos - AZUL -->
        <div class="col-12 col-md-4">
          <q-card flat bordered class="corporate-card" style="border-radius: 12px;">
            <q-card-section class="q-pa-lg">
              <div class="row items-center">
                <div class="col">
                  <div class="text-caption text-weight-medium text-uppercase" style="color: #64748b; letter-spacing: 0.05em;">
                    Total de Recibos
                  </div>
                  <div class="text-h3 text-weight-bold q-mt-sm" style="color: #1e293b;">
                    {{ totalRecibos }}
                  </div>
                  <div class="text-caption q-mt-xs" style="color: #94a3b8;">
                    Recibos registrados
                  </div>
                </div>
                <div class="col-auto">
                  <q-avatar size="64px" style="background-color: #eff6ff;">
                    <q-icon name="receipt" size="32px" style="color: #1e40af;" />
                  </q-avatar>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>

        <!-- Último Pago - NARANJO -->
        <div class="col-12 col-md-4">
          <q-card flat bordered class="corporate-card" style="border-radius: 12px;">
            <q-card-section class="q-pa-lg">
              <div class="row items-center">
                <div class="col">
                  <div class="text-caption text-weight-medium text-uppercase" style="color: #64748b; letter-spacing: 0.05em;">
                    Último Pago
                  </div>
                  <div v-if="ultimoRecibo">
                    <div class="text-h4 text-weight-bold q-mt-sm" style="color: #1e293b;">
                      {{ formatMoneda(ultimoRecibo.sueldoLiquido) }}
                    </div>
                    <div class="text-caption q-mt-xs" style="color: #94a3b8;">
                      {{ formatPeriodo(ultimoRecibo.periodo) }}
                    </div>
                  </div>
                  <div v-else class="text-h5 q-mt-sm text-grey-6">Sin datos</div>
                </div>
                <div class="col-auto">
                  <q-avatar size="64px" style="background-color: #fff7ed;">
                    <q-icon name="payments" size="32px" style="color: #f97316;" />
                  </q-avatar>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>

        <!-- Promedio Mensual - AZUL -->
        <div class="col-12 col-md-4">
          <q-card flat bordered class="corporate-card" style="border-radius: 12px;">
            <q-card-section class="q-pa-lg">
              <div class="row items-center">
                <div class="col">
                  <div class="text-caption text-weight-medium text-uppercase" style="color: #64748b; letter-spacing: 0.05em;">
                    Promedio Mensual
                  </div>
                  <div class="text-h4 text-weight-bold q-mt-sm" style="color: #1e293b;">
                    {{ formatMoneda(promedioMensual) }}
                  </div>
                  <div class="text-caption q-mt-xs" style="color: #94a3b8;">
                    Basado en {{ totalRecibos }} recibos
                  </div>
                </div>
                <div class="col-auto">
                  <q-avatar size="64px" style="background-color: #eff6ff;">
                    <q-icon name="trending_up" size="32px" style="color: #1e40af;" />
                  </q-avatar>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>

      <!-- ============================================
           TABLA DE ÚLTIMOS RECIBOS
           ============================================
      -->
      <q-card flat bordered style="border-radius: 12px;">
        <!-- Header -->
        <q-card-section class="q-pa-lg" style="background-color: #f8fafc; border-bottom: 1px solid #e2e8f0;">
          <div class="row items-center">
            <div class="col">
              <div class="text-h6 text-weight-bold" style="color: #1e3a8a;">
                <q-icon name="history" class="q-mr-sm" />
                Últimos 5 Recibos
              </div>
              <div class="text-caption q-mt-xs" style="color: #64748b;">
                Historial reciente de pagos
              </div>
            </div>
            <div class="col-auto">
              <q-btn
                label="Ver Todos"
                icon-right="arrow_forward"
                color="primary"
                @click="router.push('/recibos')"
                unelevated
                no-caps
                class="corporate-btn"
              />
            </div>
          </div>
        </q-card-section>

        <!-- Loading -->
        <q-card-section v-if="loading" class="text-center q-pa-xl">
          <q-spinner color="primary" size="48px" />
          <div class="text-body2 text-grey-7 q-mt-md">Cargando recibos...</div>
        </q-card-section>

        <!-- Lista de recibos -->
        <q-card-section v-else-if="recibos.length > 0" class="q-pa-none">
          <q-list separator>
            <q-item
              v-for="(recibo, index) in recibos.slice(0, 5)"
              :key="recibo.nroRecibo"
              clickable
              @click="verDetalle(recibo)"
              class="corporate-list-item"
              :style="{ animationDelay: `${index * 0.05}s` }"
              style="animation: fadeIn 0.3s ease-out both; min-height: 80px;"
            >
              <!-- Número de recibo - NARANJO -->
              <q-item-section avatar>
                <q-avatar size="48px" style="background-color: #fff7ed; color: #f97316;">
                  <div class="text-weight-bold">#{{ recibo.nroRecibo }}</div>
                </q-avatar>
              </q-item-section>

              <!-- Información -->
              <q-item-section>
                <q-item-label class="text-body1 text-weight-medium" style="color: #1e3a8a;">
                  {{ formatPeriodo(recibo.periodo) }}
                </q-item-label>
                <q-item-label caption class="text-caption" style="color: #64748b;">
                  <q-icon name="event" size="14px" class="q-mr-xs" />
                  {{ formatFecha(recibo.fechaPago) }}
                </q-item-label>
              </q-item-section>

              <!-- Monto -->
              <q-item-section side class="text-right">
                <q-item-label class="text-h6 text-weight-bold" style="color: #1e293b;">
                  {{ formatMoneda(recibo.sueldoLiquido) }}
                </q-item-label>
                <q-item-label caption class="text-caption" style="color: #94a3b8;">
                  Sueldo Líquido
                </q-item-label>
              </q-item-section>

              <!-- Icono -->
              <q-item-section side>
                <q-icon name="chevron_right" size="20px" color="grey-5" />
              </q-item-section>
            </q-item>
          </q-list>
        </q-card-section>

        <!-- Sin recibos -->
        <q-card-section v-else class="text-center q-pa-xl">
          <q-icon name="inbox" size="80px" color="grey-4" />
          <div class="text-h6 text-grey-7 q-mt-md">No hay recibos disponibles</div>
          <div class="text-caption text-grey-6 q-mt-sm">
            Los recibos aparecerán aquí cuando estén disponibles
          </div>
        </q-card-section>
      </q-card>
    </div>
  </q-page>
</template>

<script setup>
/**
 * ============================================
 * DASHBOARD CORPORATIVO
 * ============================================
 */

import { ref, computed, onMounted } from 'vue';
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
 * COMPUTED PROPERTIES
 * ============================================
 */
const totalRecibos = computed(() => recibos.value.length);

const ultimoRecibo = computed(() => {
  return recibos.value.length > 0 ? recibos.value[0] : null;
});

const promedioMensual = computed(() => {
  if (recibos.value.length === 0) return 0;
  const suma = recibos.value.reduce((acc, recibo) => acc + recibo.sueldoLiquido, 0);
  return suma / recibos.value.length;
});

/**
 * ============================================
 * LIFECYCLE
 * ============================================
 */
onMounted(async () => {
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
 * MÉTODOS
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

const verDetalle = (recibo) => {
  router.push({
    name: 'recibo-detalle',
    params: {
      username: recibo.username,
      nroRecibo: recibo.nroRecibo
    }
  });
};

const formatPeriodo = (periodo) => {
  const [month, year] = periodo.split('-');
  const meses = [
    'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
  ];
  return `${meses[parseInt(month) - 1]} ${year}`;
};

const formatFecha = (fecha) => {
  const date = new Date(fecha + 'T00:00:00');
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

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
/* ============================================
   ESTILOS CORPORATIVOS
   ============================================ */

.corporate-list-item {
  transition: background-color 0.2s ease;
}

.corporate-list-item:hover {
  background-color: #f8fafc;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
