# Etapa 1: Build
FROM node:24-alpine AS build

WORKDIR /app

# Copiar todo el código fuente
COPY . .

# Instalar dependencias
RUN npm install --legacy-peer-deps

# Build de producción
RUN npm run build

# Etapa 2: Runtime con Nginx
FROM nginx:alpine

# Copiar archivos compilados
COPY --from=build /app/dist/spa /usr/share/nginx/html

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer el puerto
EXPOSE 80

# Comando de inicio
CMD ["nginx", "-g", "daemon off;"]
