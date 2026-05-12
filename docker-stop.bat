@echo off
echo ================================================
echo   Portal de Recibos de Pago
echo   Deteniendo servicios Docker
echo ================================================
echo.

REM Verificar que Docker esté instalado
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker no esta instalado
    pause
    exit /b 1
)

echo Deteniendo servicios...
echo.

docker-compose down

if %errorlevel% equ 0 (
    echo.
    echo ================================================
    echo   Servicios detenidos correctamente
    echo ================================================
    echo.
    echo Los contenedores han sido detenidos y eliminados
    echo La base de datos se mantiene en el volumen Docker
    echo.
    echo Para iniciar nuevamente usa:
    echo   docker-start.bat
    echo.
    echo Para eliminar TODOS los datos (incluyendo la BD):
    echo   docker-clean.bat
    echo.
) else (
    echo.
    echo ERROR: Hubo un problema al detener los servicios
    echo.
)

pause
