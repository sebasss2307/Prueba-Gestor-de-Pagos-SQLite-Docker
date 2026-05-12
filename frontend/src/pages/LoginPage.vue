<template>
  <div class="flex flex-center bg-gradient" style="min-height: 100vh; width: 100%">
    <q-card class="login-card q-pa-lg shadow-10">
      <!-- Encabezado -->
      <q-card-section class="text-center">
        <img 
          src="~assets/1.-Logo-MicroeDoc-color-2.png" 
          alt="Logo" 
          style="height: 80px; margin-bottom: 16px;"
        />
        <div class="text-h5 text-weight-bold">Iniciar Sesión</div>
        <div class="text-grey-7 q-mt-sm">Ingresa tus credenciales para continuar</div>
      </q-card-section>

      <!-- Formulario -->
      <q-card-section>
        <q-form @submit.prevent="login" class="q-gutter-md">
          <!-- Campo de usuario -->
          <div>
            <q-input
              v-model="username"
              filled
              label="Usuario"
              placeholder="Ej: JuanPerezDelCampo001"
              :disable="loading"
              lazy-rules
              :rules="[
                val => (val && val.length > 0) || 'El usuario es requerido',
                val => USERNAME_REGEX.test(val) || 'Formato de usuario inválido'
              ]"
            >
              <template v-slot:prepend>
                <q-icon name="person" />
              </template>
            </q-input>
            
            <!-- Validación en tiempo real -->
            <div 
              v-if="username && usernameValidation.message" 
              class="text-caption q-mt-xs q-ml-sm"
              :class="`text-${validationColor}`"
            >
              {{ usernameValidation.message }}
            </div>
            
            <div class="text-caption text-grey-7 q-mt-xs q-ml-sm">
              Formato: UpperCamelCase, 15+ caracteres, termina con 3 dígitos
            </div>
          </div>

          <!-- Campo de contraseña -->
          <q-input
            v-model="password"
            filled
            :type="isPwd ? 'password' : 'text'"
            label="Contraseña"
            placeholder="Ingresa tu contraseña"
            :disable="loading"
            @keyup.enter="handleEnter"
            lazy-rules
            :rules="[
              val => (val && val.length > 0) || 'La contraseña es requerida'
            ]"
          >
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
            <template v-slot:append>
              <q-icon
                :name="isPwd ? 'visibility_off' : 'visibility'"
                class="cursor-pointer"
                @click="isPwd = !isPwd"
              />
            </template>
          </q-input>

          <!-- Botón de login -->
          <q-btn
            type="submit"
            label="Ingresar"
            color="primary"
            size="lg"
            class="full-width"
            :loading="loading"
            :disable="loading || !usernameValidation.valid"
          >
            <template v-slot:loading>
              <q-spinner-hourglass class="on-left" />
              Ingresando...
            </template>
          </q-btn>
        </q-form>
      </q-card-section>

      <!-- Usuarios de prueba -->
      <q-card-section class="text-center">
        <q-separator class="q-mb-md" />
        <div class="text-caption text-grey-7">
          <strong>Usuarios de pueba:</strong><br>
          JuanPerezDelCampo001 / password123<br>
          MariaLopezContreras002 / 1234secure<br>
          CarlosRodriguezTercero003 / abcDEF456
        </div>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useQuasar } from 'quasar';
import api from 'src/services/api';

const router = useRouter();
const $q = useQuasar();

const username = ref('');
const password = ref('');
const loading = ref(false);
const isPwd = ref(true);

const USERNAME_REGEX = /^[A-Z][a-zA-Z]{11,}\d{3}$/;

// Validación en tiempo real
const usernameValidation = computed(() => {
  const user = username.value;
  
  if (!user) {
    return { valid: null, message: '' };
  }
  
  if (user.length < 15) {
    return {
      valid: false,
      message: `Mínimo 15 caracteres (actual: ${user.length})`
    };
  }
  
  if (!user.charAt(0).match(/[A-Z]/)) {
    return {
      valid: false,
      message: 'Debe comenzar con letra mayúscula'
    };
  }
  
  const lastThree = user.slice(-3);
  if (!lastThree.match(/^\d{3}$/)) {
    return {
      valid: false,
      message: 'Debe terminar con 3 dígitos'
    };
  }
  
  if (!USERNAME_REGEX.test(user)) {
    return {
      valid: false,
      message: 'Formato UpperCamelCase inválido'
    };
  }
  
  return {
    valid: true,
    message: 'Formato válido ✓'
  };
});

const validationColor = computed(() => {
  if (usernameValidation.value.valid === null) return 'grey-7';
  return usernameValidation.value.valid ? 'positive' : 'negative';
});

// Función de login
const login = async () => {
  if (!username.value || !password.value) {
    $q.notify({
      type: 'warning',
      message: 'Por favor ingresa usuario y contraseña',
      position: 'top',
    });
    return;
  }

  if (!usernameValidation.value.valid) {
    $q.notify({
      type: 'warning',
      message: 'El formato del usuario no es válido',
      caption: usernameValidation.value.message,
      position: 'top',
    });
    return;
  }

  loading.value = true;

  try {
    const response = await api.post('/auth/login', {
      username: username.value,
      password: password.value,
    });

    localStorage.setItem('user', JSON.stringify(response.data));
    localStorage.setItem('token', response.data.token);

    $q.notify({
      type: 'positive',
      message: `¡Bienvenido ${response.data.nombres}!`,
      caption: response.data.nombreCompleto,
      position: 'top',
      icon: 'check_circle',
    });

    setTimeout(() => {
      router.push('/dashboard');
    }, 500);

  } catch (error) {
    console.error('Error en login:', error);

    const errorMessage = error.response?.data?.error || 'Error al iniciar sesión';

    $q.notify({
      type: 'negative',
      message: 'Error de autenticación',
      caption: errorMessage,
      position: 'top',
      icon: 'error',
    });

  } finally {
    loading.value = false;
  }
};

const handleEnter = () => {
  login();
};
</script>

<style scoped>
/* ============================================
   ESTILOS DEL LOGIN CORPORATIVO
   ============================================ */
.bg-gradient {
  background: linear-gradient(135deg, #1e3a8a 0%, #1e40af 100%);
  position: relative;
}

.bg-gradient::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    linear-gradient(to right, rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(to bottom, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 40px 40px;
}

.login-card {
  width: 100%;
  max-width: 480px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

@media (max-width: 600px) {
  .login-card {
    margin: 16px;
  }
}
</style>
