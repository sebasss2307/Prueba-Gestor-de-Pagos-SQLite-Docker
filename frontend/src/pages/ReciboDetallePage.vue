<template>
  <!-- ============================================
       PÁGINA DE DETALLE DE RECIBO
       ============================================
       Muestra el detalle completo de un recibo de pago
  -->
  <div class="q-pa-md" style="min-height: 100vh; width: 100%">
    <div class="q-pa-md">
      <!-- ============================================
           BOTÓN VOLVER
           ============================================
      -->
      <q-btn
        label="Volver a Recibos"
        icon="arrow_back"
        color="primary"
        flat
        @click="router.push('/recibos')"
        class="q-mb-md"
      />

      <!-- ============================================
           LOADING
           ============================================
      -->
      <div v-if="loading" class="flex flex-center q-pa-xl">
        <q-spinner-hourglass size="50px" color="primary" />
      </div>

      <!-- ============================================
           DETALLE DEL RECIBO
           ============================================
      -->
      <div v-else-if="recibo" class="row justify-center">
        <div class="col-12 col-md-10 col-lg-8">
          <q-card class="shadow-10">
            <!-- Encabezado con azul -->
            <q-card-section style="background-color: #1e40af;" class="text-white">
              <div class="text-h5 text-weight-bold text-center">
                Recibo de Pago #{{ recibo.nroRecibo }}
              </div>
              <div class="text-h6 text-center q-mt-sm">
                {{ formatPeriodo(recibo.periodo) }}
              </div>
            </q-card-section>

            <q-separator />

            <!-- Información del colaborador -->
            <q-card-section>
              <div class="text-h6 text-weight-bold q-mb-md">
                <q-icon name="person" class="q-mr-sm" />
                Información del Colaborador
              </div>
              
              <q-list>
                <q-item>
                  <q-item-section>
                    <q-item-label caption>Usuario</q-item-label>
                    <q-item-label>{{ recibo.username }}</q-item-label>
                  </q-item-section>
                </q-item>

                <q-item>
                  <q-item-section>
                    <q-item-label caption>Fecha de Pago</q-item-label>
                    <q-item-label>{{ formatFecha(recibo.fechaPago) }}</q-item-label>
                  </q-item-section>
                </q-item>

                <q-item>
                  <q-item-section>
                    <q-item-label caption>Detalle</q-item-label>
                    <q-item-label>{{ recibo.detalle }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-card-section>

            <q-separator />

            <!-- Desglose de pago -->
            <q-card-section>
              <div class="text-h6 text-weight-bold q-mb-md">
                <q-icon name="payments" class="q-mr-sm" />
                Desglose de Pago
              </div>

              <q-list>
                <!-- Sueldo Base -->
                <q-item>
                  <q-item-section>
                    <q-item-label>Sueldo Base</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label class="text-weight-bold" style="color: #1e293b;">
                      {{ formatMoneda(recibo.sueldoBase) }}
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <!-- Bono de Producción - NARANJO -->
                <q-item>
                  <q-item-section>
                    <q-item-label>Bono de Producción</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label class="text-weight-bold" style="color: #1e293b;">
                      + {{ formatMoneda(recibo.bonoProduccion) }}
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <q-separator class="q-my-sm" />

                <!-- Total Bruto -->
                <q-item>
                  <q-item-section>
                    <q-item-label class="text-weight-medium">Total Bruto</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label class="text-weight-bold" style="color: #1e293b;">
                      {{ formatMoneda(recibo.sueldoBase + recibo.bonoProduccion) }}
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <q-separator class="q-my-md" />

                <!-- Descuentos - AZUL -->
                <q-item-label header style="color: #1e40af;">Descuentos</q-item-label>

                <q-item>
                  <q-item-section>
                    <q-item-label>Descuento Salud</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label style="color: #1e293b;">
                      - {{ formatMoneda(recibo.descuentoSalud) }}
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <q-item>
                  <q-item-section>
                    <q-item-label>Descuento AFP</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label style="color: #1e293b;">
                      - {{ formatMoneda(recibo.descuentoAfp) }}
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <q-item>
                  <q-item-section>
                    <q-item-label>Otros Descuentos</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label style="color: #1e293b;">
                      - {{ formatMoneda(recibo.otrosDescuentos) }}
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <q-separator class="q-my-sm" />

                <!-- Total Descuentos -->
                <q-item>
                  <q-item-section>
                    <q-item-label class="text-weight-medium">Total Descuentos</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label class="text-weight-bold" style="color: #1e293b;">
                      - {{ formatMoneda(recibo.descuentoSalud + recibo.descuentoAfp + recibo.otrosDescuentos) }}
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <q-separator class="q-my-md" />

                <!-- Sueldo Líquido - NARANJO -->
                <q-item style="background-color: #fff7ed;">
                  <q-item-section>
                    <q-item-label class="text-h6 text-weight-bold" style="color: #f97316;">
                      Sueldo Líquido
                    </q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label class="text-h5 text-weight-bold" style="color: #1e293b;">
                      {{ formatMoneda(recibo.sueldoLiquido) }}
                    </q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-card-section>

            <q-separator />

            <!-- Resumen numérico -->
            <q-card-section>
              <div class="row q-col-gutter-md">
                <div class="col-3 text-center">
                  <div class="text-caption text-grey-7">Sueldo Base</div>
                  <div class="text-body1 text-weight-bold" style="color: #1e293b;">
                    {{ formatNumero(recibo.sueldoBase) }}
                  </div>
                </div>
                <div class="col-3 text-center">
                  <div class="text-caption text-grey-7">Bonos</div>
                  <div class="text-body1 text-weight-bold" style="color: #1e293b;">
                    {{ formatNumero(recibo.bonoProduccion) }}
                  </div>
                </div>
                <div class="col-3 text-center">
                  <div class="text-caption text-grey-7">Descuentos</div>
                  <div class="text-body1 text-weight-bold" style="color: #1e293b;">
                    {{ formatNumero(recibo.descuentoSalud + recibo.descuentoAfp + recibo.otrosDescuentos) }}
                  </div>
                </div>
                <div class="col-3 text-center">
                  <div class="text-caption text-grey-7">Líquido</div>
                  <div class="text-body1 text-weight-bold" style="color: #1e293b;">
                    {{ formatNumero(recibo.sueldoLiquido) }}
                  </div>
                </div>
              </div>
            </q-card-section>

            <!-- Botones de acción -->
            <q-card-actions align="center" class="q-pa-md">
              <q-btn
                label="Volver"
                color="primary"
                icon="arrow_back"
                @click="router.push('/recibos')"
                outline
                class="q-px-lg"
              />
            </q-card-actions>
          </q-card>
        </div>
      </div>

      <!-- ============================================
           ERROR
           ============================================
      -->
      <div v-else class="flex flex-center q-pa-xl">
        <q-card class="text-center q-pa-xl">
          <q-icon name="error" size="80px" color="negative" />
          <div class="text-h6 q-mt-md">
            Recibo no encontrado
          </div>
          <q-btn
            label="Volver a Recibos"
            color="primary"
            @click="router.push('/recibos')"
            class="q-mt-md"
          />
        </q-card>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * ============================================
 * PÁGINA DE DETALLE DE RECIBO
 * ============================================
 * Muestra el detalle completo de un recibo de pago
 */

import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useQuasar } from 'quasar';
import api from 'src/services/api';

const router = useRouter();
const route = useRoute();
const $q = useQuasar();
const recibo = ref(null);
const loading = ref(true);

/**
 * ============================================
 * CARGAR DETALLE DEL RECIBO
 * ============================================
 */
onMounted(async () => {
  const { username, nroRecibo } = route.params;

  if (!username || !nroRecibo) {
    router.push('/recibos');
    return;
  }

  try {
    const response = await api.get(`/recibos/${username}/${nroRecibo}`);
    recibo.value = response.data;
  } catch (error) {
    console.error('Error al cargar recibo:', error);
    $q.notify({
      type: 'negative',
      message: 'Error al cargar el recibo',
      caption: error.response?.data?.error || 'Error desconocido',
      position: 'top',
    });
  } finally {
    loading.value = false;
  }
});

/**
 * ============================================
 * FORMATEAR PERIODO
 * ============================================
 * Convierte "04-2025" a "Abril 2025"
 */
const formatPeriodo = (periodo) => {
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
 */
const formatMoneda = (valor) => {
  return new Intl.NumberFormat('es-CL', {
    style: 'currency',
    currency: 'CLP',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(valor);
};

/**
 * ============================================
 * FORMATEAR NÚMERO CON SEPARADOR DE MILES
 * ============================================
 * Sin decimales, solo separador de miles
 */
const formatNumero = (valor) => {
  return new Intl.NumberFormat('es-CL', {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(valor);
};
</script>

<style scoped>
/* Estilos adicionales si son necesarios */
</style>
