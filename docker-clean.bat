@echo off
echo ================================================
echo   Portal de Recibos de Pago
echo   Limpieza completa de Docker
echo ================================================
echo.
echo ADVERTENCIA: Esta accion eliminara:
echo   - Todos los contenedores del proyecto
echo   - Todos los volumenes (BASE DE DATOS)
echo   - Todas las imagenes del proyecto
echo.
echo Los datos de la base de datos se PERDERAN
echo.

set /p confirm="Estas seguro? (S/N): "
if /i not "%confirm%"=="S" (
    echo.
    echo Operacion cancelada
    pause
    exit /b 0
)

echo.
echo Deteniendo y eliminando contenedores...
docker-compose down -v

echo.
echo Eliminando imagenes del proyecto...
docker rmi recibos-backend:latest 2>nul
docker rmi recibos-frontend:latest 2>nul
docker rmi pruebatecnica-backend:latest 2>nul
docker rmi pruebatecnica-frontend:latest 2>nul

echo.
echo Limpiando imagenes huerfanas...
docker image prune -f

echo.
echo ================================================
echo   Limpieza completada
echo ================================================
echo.
echo Se han eliminado:
echo   - Contenedores del proyecto
echo   - Volumenes (base de datos)
echo   - Imagenes del proyecto
echo.
echo Para volver a iniciar el proyecto usa:
echo   docker-start.bat
echo.

pause
