@echo off
echo ================================================
echo   Portal de Recibos de Pago
echo   Iniciando con Docker
echo ================================================
echo.

REM Verificar que Docker esté instalado
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker no esta instalado
    echo.
    echo Descarga Docker Desktop desde:
    echo https://www.docker.com/get-started
    echo.
    pause
    exit /b 1
)

REM Verificar que Docker esté corriendo
docker ps >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker no esta corriendo
    echo.
    echo Por favor inicia Docker Desktop y vuelve a intentar
    echo.
    pause
    exit /b 1
)

echo Docker detectado correctamente
echo.
echo Limpiando contenedores anteriores...
docker-compose down 2>nul
docker rm -f recibos-backend recibos-frontend 2>nul

echo.
echo Construyendo e iniciando servicios...
echo Esto puede tomar varios minutos la primera vez
echo.

REM Construir e iniciar los servicios
docker-compose up --build -d

if %errorlevel% equ 0 (
    echo.
    echo ================================================
    echo   Servicios iniciados correctamente
    echo ================================================
    echo.
    echo Backend:  http://localhost:8080
    echo Frontend: http://localhost:9000
    echo.
    echo Abre tu navegador en: http://localhost:9000
    echo.
    echo Para ver los logs en tiempo real:
    echo   docker-compose logs -f
    echo.
    echo Para detener los servicios usa:
    echo   docker-stop.bat
    echo.
) else (
    echo.
    echo ERROR: Hubo un problema al iniciar los servicios
    echo.
    echo Revisa los logs con:
    echo   docker-compose logs
    echo.
)

pause
