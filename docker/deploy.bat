@echo off
chcp 65001 > nul
setlocal enabledelayedexpansion

:: ==============================================
:: SimpleIAST Deployment Script for Windows
:: Version: 2.0.3 (MySQL root password option)
:: Description: Automated deployment for SimpleIAST with flexible database options
:: ==============================================

:: Color codes
for /F "tokens=1,2 delims=#" %%a in ('"prompt #$H#$E# & echo on & for %%b in (1) do rem"') do (
  set "DEL=%%a"
)

echo.
echo ==============================================
echo      SimpleIAST Deployment Script
echo ==============================================
echo.

:: Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker is not installed. Please install Docker first.
    exit /b 1
)

:: Check if Docker Compose is installed
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker Compose is not installed. Please install Docker Compose first.
    exit /b 1
)

echo [INFO] Docker and Docker Compose are available

:: Initialize variables
set mysql_mode=
set redis_mode=
set mysql_host=
set mysql_port=
set mysql_database=
set mysql_user=
set mysql_password=
set mysql_root_password=
set redis_host=
set redis_port=
set redis_password=
set auto_mysql_password=
set auto_redis_password=
set chars=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$^&*()
set mysql_image=mysql:5.7.34
set mysql_platform=

:: MySQL deployment mode selection
echo.
echo === MySQL Database Configuration ===
echo 1) Use external MySQL database
echo 2) Automatically create MySQL container
set /p mysql_choice=Select MySQL deployment mode (1/2):

if "!mysql_choice!"=="1" goto external_mysql
if "!mysql_choice!"=="2" goto auto_mysql
echo [ERROR] Invalid choice. Exiting.
exit /b 1

:external_mysql
set mysql_mode=external
echo [INFO] Using external MySQL database
set /p mysql_host=Enter MySQL host:
set /p mysql_port=Enter MySQL port (default: 3306):
if "!mysql_port!"=="" set mysql_port=3306
set /p mysql_database=Enter MySQL database name:
set /p mysql_user=Enter MySQL username:
set /p mysql_password=Enter MySQL password:
goto redis_config

:auto_mysql
set mysql_mode=containerized
echo [INFO] Automatically creating MySQL container

:: Generate random password for MySQL user
set "auto_mysql_password="
for /L %%i in (1,1,12) do (
    set /a "rand=!random! %% 72"
    call set "auto_mysql_password=!auto_mysql_password!!chars:~!rand!,1!"
)

:: Allow custom root password
set /p custom_root=Do you want to set a custom MySQL root password? (y/n) [n]: 
if /i "!custom_root!"=="y" (
    set /p mysql_root_password=Enter MySQL root password: 
) else (
    set "mysql_root_password="
    for /L %%i in (1,1,12) do (
        set /a "rand=!random! %% 72"
        call set "mysql_root_password=!mysql_root_password!!chars:~!rand!,1!"
    )
    echo [SUCCESS] MySQL root password generated: !mysql_root_password!
)

set mysql_host=mysql
set mysql_port=3306
set mysql_database=iast
set mysql_user=iast
set mysql_password=!auto_mysql_password!

echo [SUCCESS] MySQL user password generated: !auto_mysql_password!

:: Add permission grants to existing init.sql file
echo [INFO] Adding permission grants to MySQL initialization file...
(
    echo.
    echo -- SimpleIAST Database Permissions Configuration
    echo -- Grant all privileges to iast user on iast database
    echo GRANT ALL PRIVILEGES ON !mysql_database!.* TO '!mysql_user!'@'%%';
    echo FLUSH PRIVILEGES;
) >> .\mysql\init.sql

echo [SUCCESS] Permission grants added to init.sql
goto redis_config

:redis_config
:: Redis deployment mode selection
echo.
echo === Redis Configuration ===
echo 1) Use external Redis
echo 2) Automatically create Redis container
set /p redis_choice=Select Redis deployment mode (1/2):

if "!redis_choice!"=="1" goto external_redis
if "!redis_choice!"=="2" goto auto_redis
echo [ERROR] Invalid choice. Exiting.
exit /b 1

:external_redis
set redis_mode=external
echo [INFO] Using external Redis
set /p redis_host=Enter Redis host:
set /p redis_port=Enter Redis port (default: 6379):
if "!redis_port!"=="" set redis_port=6379
set /p redis_password=Enter Redis password:
goto create_config

:auto_redis
set redis_mode=containerized
echo [INFO] Automatically creating Redis container

:: Generate random password
set "auto_redis_password="
for /L %%i in (1,1,16) do (
    set /a "rand=!random! %% 72"
    call set "auto_redis_password=!auto_redis_password!!chars:~!rand!,1!"
)

set redis_host=redis
set redis_port=6379
set redis_password=!auto_redis_password!

echo [SUCCESS] Redis password generated: !auto_redis_password!
goto create_config

:create_config
echo [INFO] Creating environment configuration...

(
echo # SimpleIAST Environment Configuration
echo # Generated on !date! !time!
echo MYSQL_MODE=!mysql_mode!
echo MYSQL_HOST=!mysql_host!
echo MYSQL_PORT=!mysql_port!
echo MYSQL_DATABASE=!mysql_database!
echo MYSQL_USER=!mysql_user!
echo MYSQL_PASSWORD=!mysql_password!
echo REDIS_MODE=!redis_mode!
echo REDIS_HOST=!redis_host!
echo REDIS_PORT=!redis_port!
echo REDIS_PASSWORD=!redis_password!
) > .env

echo [SUCCESS] Environment configuration saved to .env file

echo [INFO] Generating Docker Compose configuration...

:: Start docker-compose file
(
echo version: '0.4.0'
echo services:
) > docker-compose.yml

:: Add Redis service if containerized
if "!redis_mode!"=="containerized" (
    (
    echo   redis:
    echo     image: redis:latest
    echo     environment:
    echo       - TZ=Asia/Shanghai
    echo     command: ["redis-server", "--requirepass", "!redis_password!"]
    echo     restart: always
    echo     networks:
    echo       - iast-network
    ) >> docker-compose.yml
)

:: Add MySQL service if containerized
if "!mysql_mode!"=="containerized" (
    (
    echo   mysql:
    echo     image: !mysql_image!
    if not "!mysql_platform!"=="" echo     !mysql_platform!
    echo     environment:
    echo       - MYSQL_ROOT_PASSWORD=!mysql_root_password!
    echo       - MYSQL_USER=!mysql_user!
    echo       - MYSQL_PASSWORD=!mysql_password!
    echo       - MYSQL_DATABASE=!mysql_database!
    echo       - TZ=Asia/Shanghai
    echo     volumes:
    echo       - .\mysql\init.sql:/docker-entrypoint-initdb.d/init.sql
    echo     command:
    echo         - --default-authentication-plugin=mysql_native_password
    echo         - --character-set-server=utf8mb4
    echo         - --collation-server=utf8mb4_unicode_ci
    echo     restart: always
    echo     networks:
    echo       - iast-network
    ) >> docker-compose.yml
)

:: Add backend service
(
    echo   backend:
    echo     image: openjdk:11
    echo     container_name: iast-server
    echo     environment:
    echo       - TZ=Asia/Shanghai
    echo       - SPRING_DATASOURCE_URL=jdbc:mysql://!mysql_host!:!mysql_port!/!mysql_database!?useUnicode=true^&characterEncoding=utf8^&useSSL=false^&allowPublicKeyRetrieval=true
    echo       - SPRING_DATASOURCE_USERNAME=!mysql_user!
    echo       - SPRING_DATASOURCE_PASSWORD=!mysql_password!
    echo       - SPRING_REDIS_HOST=!redis_host!
    echo       - SPRING_REDIS_PORT=!redis_port!
    echo       - SPRING_REDIS_PASSWORD=!redis_password!
    echo     command: java -jar /app/app.jar
    echo     volumes:
    echo       - .\backend\:/app/
) >> docker-compose.yml

:: Add depends_on based on selected services
set depends_on=
if "!mysql_mode!"=="containerized" set depends_on=!depends_on!^
^
      - mysql
if "!redis_mode!"=="containerized" set depends_on=!depends_on!^
^
      - redis

if not "!depends_on!"=="" (
    (
    echo     depends_on:!depends_on!
    ) >> docker-compose.yml
)

:: Continue backend service configuration
(
echo     ports:
echo       - "81:8989"
echo     restart: always
echo     networks:
echo       - iast-network
) >> docker-compose.yml

:: Add frontend service
(
    echo   frontend:
    echo     image: ziizhuwy/simpleiast-frontend:latest
    echo     container_name: iast-view
    echo     volumes:
    echo       - .\frontend\web\dist:/usr/share/nginx/html
    echo       - .\frontend\nginx\nginx.conf:/etc/nginx/nginx.conf
    echo       - .\logs\:/var/log/nginx/
    echo     environment:
    echo       - TZ=Asia/Shanghai
    echo     ports:
    echo       - "8443:80"
    echo     depends_on:
    echo       - backend
    echo     networks:
    echo       - iast-network
    echo.
    echo networks:
    echo   iast-network:
    echo     driver: bridge
) >> docker-compose.yml

echo [SUCCESS] Docker Compose file created

:: ============================
:: Start services
:: ============================
echo [INFO] Starting services with Docker Compose...
docker-compose up -d

if %errorlevel% equ 0 (
    echo [SUCCESS] All services started successfully!
    echo.
    echo ==============================================
    echo            DEPLOYMENT COMPLETE
    echo ==============================================
    echo.
    echo Deployment Summary:
    echo MySQL: !mysql_mode!
    echo Redis: !redis_mode!
    echo.
    if "!mysql_mode!"=="containerized" (
        echo MySQL Root Credentials:
        echo   Root Password: !mysql_root_password!
        echo.
        echo MySQL User Credentials:
        echo   Host:     !mysql_host!
        echo   Port:     !mysql_port!
        echo   Database: !mysql_database!
        echo   Username: !mysql_user!
        echo   Password: !mysql_password!
        echo.
    )
    if "!redis_mode!"=="containerized" (
        echo Redis Credentials:
        echo   Host:     !redis_host!
        echo   Port:     !redis_port!
        echo   Password: !redis_password!
        echo.
    )
    echo Application URLs:
    echo Frontend: http://localhost:8443
    echo Backend:  http://localhost:81
    echo.
    echo ==============================================
    echo Management Commands:
    echo   docker-compose down      # Stop services
    echo   docker-compose logs      # View logs
    echo   docker-compose restart   # Restart services
    echo ==============================================
) else (
    echo [ERROR] Failed to start services. Check Docker logs for details.
    exit /b 1
)

endlocal