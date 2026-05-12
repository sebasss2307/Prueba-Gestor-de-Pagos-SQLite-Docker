# Portal de Recibos de Pago

Sistema web full-stack para la visualización y gestión de recibos de pago de colaboradores, con autenticación segura y base de datos SQLite embebida.

---

## Guía Rápida

### Requisitos Previos

**Solo necesitas tener instalado:**
- **Docker Desktop** → [Descargar aquí](https://www.docker.com/get-started)

**Eso es todo.** No necesitas instalar Java, Node.js, Maven, ni ninguna otra herramienta. Docker se encarga de todo.

### Para Usuarios de Windows

1. **Instala Docker Desktop** 
   - Descarga desde: https://www.docker.com/get-started
   - Ejecuta el instalador
   - Reinicia tu computadora si te lo pide
   
2. **Inicia Docker Desktop**
   - Busca "Docker Desktop" en el menú de inicio
   - Ábrelo y espera a que el ícono de la bandeja esté verde (puede tardar 1-2 minutos)
   
3. **Descarga o clona este proyecto**
   ```bash
   git clone <url-del-repositorio>
   cd PruebaTecnicaDocker
   ```
   
4. **Ejecuta el proyecto**
   - Doble clic en: `docker-start.bat`
   - Espera 2-3 minutos mientras se construyen las imágenes (solo la primera vez)
   **ADVERTENCIA:** Si el script no funciona o arroja un error a la primera ejecucion, intentar ejecutarlo de nuevo.
5. **Abre tu navegador**
   - Ve a: http://localhost:9000
   
6. **Inicia sesión con las credenciales de prueba** (ver abajo)

### Para Usuarios de Linux/Mac

```bash
# 1. Instala Docker y Docker Compose
# Ubuntu/Debian:
sudo apt-get update
sudo apt-get install docker.io docker-compose

# Mac (con Homebrew):
brew install --cask docker

# 2. Clona el proyecto
git clone <url-del-repositorio>
cd PruebaTecnica

# 3. Inicia Docker (si no está corriendo)
# En Mac: Abre Docker Desktop desde Applications
# En Linux: sudo systemctl start docker

# 4. Ejecuta el proyecto
docker-compose up --build -d

# 5. Abre tu navegador en:
# http://localhost:9000
```

### Credenciales de Prueba
| Usuario | Contraseña |
|---------|-----------|
| JuanPerezDelCampo001 | password123 |
| MariaLopezContreras002 | 1234secure |
| CarlosRodriguezTercero003 | abcDEF456 |

### Detener el Proyecto
- **Windows:** Doble clic en `docker-stop.bat`
- **Linux/Mac:** `docker-compose down`

### Nota Importante sobre la Primera Ejecución

**La primera vez que ejecutes el proyecto:**
- Docker descargará las imágenes base (Node.js, Java, Nginx) - ~500MB
- Compilará el backend (Maven descargará dependencias) - ~2-3 minutos
- Compilará el frontend (npm instalará paquetes) - ~2-3 minutos
- **Total: 5-7 minutos aproximadamente**

**Las siguientes ejecuciones serán mucho más rápidas** (10-20 segundos) porque Docker usa caché.
---

## Inicio Rápido con Docker

### Requisitos Previos
- **Docker Desktop** → [Descargar](https://www.docker.com/get-started)
- Docker Compose viene incluido con Docker Desktop

Verifica que Docker esté instalado:
```bash
docker --version
docker-compose --version
```

### Opción 1: Scripts Automáticos (Recomendado para Windows)

#### Iniciar el Proyecto
**Doble clic en:**
```
docker-start.bat
```

Este script:
- Verifica que Docker esté instalado y corriendo
- Construye las imágenes Docker
- Inicia los servicios en segundo plano
- Muestra las URLs de acceso

#### Detener el Proyecto
**Doble clic en:**
```
docker-stop.bat
```

Este script:
- Detiene todos los contenedores
- Mantiene la base de datos intacta

#### Limpiar Todo (Eliminar contenedores, imágenes y datos)
**Doble clic en:**
```
docker-clean.bat
```

**ADVERTENCIA:** Este script elimina la base de datos. Úsalo solo si quieres empezar desde cero.

**ADVERTENCIA:** Si el script no funciona a la primera ejecucion, intentar ejecutarlo de nuevo

### Opción 2: Comandos Manuales

```bash
# Construir e iniciar todos los servicios
docker-compose up --build -d

# Ver logs en tiempo real
docker-compose logs -f

# Detener los servicios (mantiene la base de datos)
docker-compose down

# Detener y eliminar volúmenes (elimina la base de datos)
docker-compose down -v
```

### Accede a la Aplicación
```
http://localhost:9000
```

### Credenciales de Prueba
| Usuario | Contraseña |
|---------|-----------|
| JuanPerezDelCampo001 | password123 |
| MariaLopezContreras002 | 1234secure |
| CarlosRodriguezTercero003 | abcDEF456 |

---

## Descripción del Proyecto

Portal web que permite a los colaboradores de una empresa acceder de forma segura a sus recibos de pago. El sistema implementa autenticación robusta con validación regex y hash SHA-256, almacenamiento persistente en SQLite, y una interfaz de usuario moderna y responsive construida con Vue 3 y Quasar Framework.

### Problema que Resuelve
- Centraliza la información de recibos de pago en un solo lugar
- Elimina la necesidad de enviar recibos por correo o papel
- Proporciona acceso 24/7 a los colaboradores
- Mantiene un historial completo de pagos

### Casos de Uso
- Colaborador consulta sus últimos 20 recibos de pago
- Colaborador visualiza el detalle completo de un recibo específico
- Colaborador verifica montos, descuentos y bonificaciones
- Sistema carga automáticamente datos desde archivos CSV

---

## Características Principales

### Seguridad
- **Autenticación SHA-256:** Contraseñas hasheadas de forma segura
- **Validación Regex:** Username con formato UpperCamelCase, mínimo 15 caracteres, termina en 3 dígitos
- **Tokens de Sesión:** Sistema de tokens UUID para gestión de sesiones
- **CORS Configurado:** Comunicación segura entre frontend y backend

### Funcionalidades
- **Login Seguro:** Validación de credenciales con feedback en tiempo real
- **Dashboard:** Vista general del colaborador autenticado
- **Lista de Recibos:** Visualización de los últimos 20 recibos ordenados por fecha
- **Detalle de Recibo:** Vista completa con desglose de:
  - Sueldo base
  - Bonificaciones (bono de producción)
  - Descuentos (salud, AFP, otros)
  - Sueldo líquido
- **Formato de Moneda:** Valores en pesos chilenos (CLP) con separador de miles
- **Interfaz Responsive:** Adaptable a desktop, tablet y móvil
- **Navegación Intuitiva:** Menú lateral con acceso rápido a todas las secciones

### Datos
- **Carga Automática:** Migración de datos desde CSV a SQLite al iniciar
- **Persistencia:** Base de datos SQLite embebida con volumen Docker

---

## Tecnologías Utilizadas

### Backend
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17 | Lenguaje de programación |
| **Spring Boot** | 3.5.14 | Framework para API REST |
| **Spring Data JPA** | 3.5.14 | ORM para acceso a datos |
| **SQLite** | 3.47.2.0 | Base de datos embebida |
| **Maven** | 3.9+ | Gestión de dependencias |
| **Lombok** | Latest | Reducción de código boilerplate |

### Frontend
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Vue.js** | 3.5.22 | Framework JavaScript progresivo |
| **Quasar Framework** | 2.16.0 | Framework UI con componentes Material Design |
| **Node.js** | 24 | Entorno de ejecución JavaScript |
| **Axios** | 1.16.0 | Cliente HTTP para API REST |
| **Vue Router** | 5.0.3 | Enrutamiento SPA |
| **Pinia** | 3.0.1 | Gestión de estado |
| **Nginx** | Alpine | Servidor web para producción |

### DevOps
| Tecnología | Propósito |
|------------|-----------|
| **Docker** | Contenedorización de aplicaciones |
| **Docker Compose** | Orquestación de servicios |

---

## Arquitectura Docker

El proyecto utiliza una arquitectura multi-contenedor con Docker Compose:

### Servicios

#### Backend (Spring Boot)
- **Puerto:** 8080
- **Imagen:** Multi-stage build con Maven y OpenJDK 17
- **Volumen:** Persistencia de base de datos SQLite
- **Health Check:** Endpoint `/api/auth/health`

#### Frontend (Vue + Quasar + Nginx)
- **Puerto:** 9000 (mapeado al 80 interno)
- **Imagen:** Multi-stage build con Node.js y Nginx
- **Proxy:** Nginx redirige `/api` al backend
- **Dependencia:** Espera a que el backend esté saludable

### Volúmenes
- `backend-data`: Almacena la base de datos SQLite de forma persistente

### Red
- `recibos-network`: Red bridge para comunicación entre servicios

---

## Comandos Docker

### Scripts Rápidos (Windows)

| Script | Descripción |
|--------|-------------|
| `docker-start.bat` | Inicia todos los servicios |
| `docker-stop.bat` | Detiene los servicios (mantiene datos) |
| `docker-clean.bat` | Elimina todo (contenedores, imágenes, datos) |

### Comandos Manuales

#### Inicio y Detención

```bash
# Construir e iniciar todos los servicios
docker-compose up --build

# Iniciar en segundo plano
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f backend
docker-compose logs -f frontend

# Detener servicios (mantiene la base de datos)
docker-compose down

# Detener y eliminar volúmenes (elimina la base de datos)
docker-compose down -v

# Reconstruir un servicio específico
docker-compose build backend
docker-compose build frontend

# Reiniciar un servicio
docker-compose restart backend
docker-compose restart frontend
```

### Gestión de Contenedores

```bash
# Ver contenedores en ejecución
docker-compose ps

# Ejecutar comandos dentro de un contenedor
docker-compose exec backend sh
docker-compose exec frontend sh

# Ver uso de recursos
docker stats
```

### Limpieza

```bash
# Eliminar contenedores detenidos
docker-compose rm

# Eliminar imágenes no utilizadas
docker image prune

# Limpieza completa del sistema Docker
docker system prune -a
```

---

## Desarrollo Local (Sin Docker)

Si prefieres ejecutar el proyecto sin Docker:

### Requisitos
- **Java 17+** → [Descargar](https://adoptium.net/)
- **Node.js 18+** → [Descargar](https://nodejs.org/)
- **Maven** (incluido en el proyecto con Maven Wrapper)

### Backend
```bash
cd backend
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

**Accede a:** http://localhost:9000

---

## Estructura del Proyecto

```
PruebaTecnica/
├── README.md                  ← Este archivo
├── .gitignore                ← Archivos ignorados
├── .dockerignore             ← Archivos ignorados por Docker
├── docker-compose.yml        ← Orquestación de servicios
│
├── backend/                   ← API Spring Boot
│   ├── Dockerfile            ← Imagen Docker del backend
│   ├── .dockerignore         ← Archivos ignorados
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/prueba/backend/
│   │   │   │   ├── config/
│   │   │   │   │   └── DataInitializer.java      # Carga datos CSV → SQLite
│   │   │   │   ├── controller/
│   │   │   │   │   ├── AuthController.java       # Endpoints de autenticación
│   │   │   │   │   └── ReciboController.java     # Endpoints de recibos
│   │   │   │   ├── dto/
│   │   │   │   │   ├── LoginRequest.java         # DTO de login
│   │   │   │   │   └── LoginResponse.java        # DTO de respuesta
│   │   │   │   ├── model/
│   │   │   │   │   ├── User.java                 # Entidad JPA Usuario
│   │   │   │   │   └── Recibo.java               # Entidad JPA Recibo
│   │   │   │   ├── repository/
│   │   │   │   │   ├── UserRepository.java       # Spring Data JPA
│   │   │   │   │   └── ReciboRepository.java     # Spring Data JPA
│   │   │   │   ├── service/
│   │   │   │   │   └── AuthService.java          # Lógica de autenticación
│   │   │   │   └── util/
│   │   │   │       └── ValidationUtil.java       # Validaciones y hash
│   │   │   └── resources/
│   │   │       ├── application.properties         # Configuración Spring
│   │   │       ├── Usuarios.csv                   # Datos de usuarios
│   │   │       └── Recibos de Pago.csv           # Datos de recibos
│   │   └── test/
│   ├── pom.xml                                    # Dependencias Maven
│   └── mvnw / mvnw.cmd                           # Maven Wrapper
│
└── frontend/                  ← SPA Vue + Quasar
    ├── Dockerfile            ← Imagen Docker del frontend
    ├── .dockerignore         ← Archivos ignorados
    ├── nginx.conf            ← Configuración Nginx
    ├── src/
    │   ├── pages/
    │   │   ├── DashboardPage.vue                  # Página principal
    │   │   ├── LoginPage.vue                      # Página de login
    │   │   ├── RecibosPage.vue                    # Lista de recibos
    │   │   ├── ReciboDetallePage.vue              # Detalle de recibo
    │   │   └── ErrorNotFound.vue                  # Página 404
    │   ├── layouts/
    │   │   └── AppLayout.vue                      # Layout principal con menú
    │   ├── services/
    │   │   └── api.js                             # Cliente HTTP Axios
    │   ├── router/
    │   │   ├── index.js                           # Configuración router
    │   │   └── routes.js                          # Definición de rutas
    │   └── stores/
    │       └── index.js                           # Store Pinia
    ├── package.json                               # Dependencias npm
    └── quasar.config.js                           # Configuración Quasar
```

---

## URLs de Acceso

### Con Docker
- **Aplicación Frontend:** http://localhost:9000
- **Backend API:** http://localhost:9000/api (proxy a través de Nginx)
- **Backend Directo:** http://localhost:8080/api

### Sin Docker
- **Aplicación Frontend:** http://localhost:9000
- **Backend API:** http://localhost:8080/api

---