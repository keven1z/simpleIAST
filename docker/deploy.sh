#!/bin/bash

# ==============================================
# SimpleIAST Deployment Script
# Version: 2.0.3 (ARM Mac + MySQL root password option)
# Description: Automated deployment for SimpleIAST with flexible database options
# ==============================================

set -e

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_status() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# Generate random password
generate_password() {
    LC_ALL=C tr -dc 'A-Za-z0-9!@#$%&*()' </dev/urandom | head -c 12; echo
}

# Validate port
validate_port() {
    local port=$1
    if [[ ! $port =~ ^[0-9]+$ ]] || [ $port -lt 1 ] || [ $port -gt 65535 ]; then
        return 1
    fi
    return 0
}

# Ask yes/no
ask_yes_no() {
    local prompt="$1 (y/n) [${2:-n}]: "
    local default="${2:-n}"
    local answer
    read -p "$prompt" answer
    answer=${answer:-$default}
    case "$answer" in
        [Yy]* ) return 0;;
        [Nn]* ) return 1;;
        * )
            print_error "Please answer y or n."
            ask_yes_no "$1" "$2"
            ;;
    esac
}

# Banner
echo "=============================================="
echo "      SimpleIAST Deployment Script v2.0.3"
echo "=============================================="
echo ""

# Check Docker
if ! command -v docker &> /dev/null; then
    print_error "Docker is not installed. Please install Docker first."
    exit 1
fi
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi
print_status "Docker and Docker Compose are available"

# Init variables
mysql_mode=""
redis_mode=""
mysql_host=""
mysql_port=""
mysql_database=""
mysql_user=""
mysql_password=""
mysql_root_password=""
redis_host=""
redis_port=""
redis_password=""

# Detect architecture
mysql_image="mysql:5.7.34"
mysql_platform=""
if [[ $(uname -s) == "Darwin" && $(uname -m) == "arm64" ]]; then
    print_warning "Detected macOS ARM64 (Apple Silicon), using mysql:8.0-oracle"
    mysql_image="mysql:8.0-oracle"
    mysql_platform="    platform: linux/arm64"
fi

# ============================
# MySQL configuration
# ============================
echo ""
echo "=== MySQL Database Configuration ==="
if ask_yes_no "Use external MySQL database?" "n"; then
    mysql_mode="external"
    print_status "Using external MySQL database"
    read -p "Enter MySQL host: " mysql_host
    read -p "Enter MySQL port (default: 3306): " mysql_port
    mysql_port=${mysql_port:-3306}
    while ! validate_port "$mysql_port"; do
        print_error "Invalid port number."
        read -p "Enter MySQL port (default: 3306): " mysql_port
        mysql_port=${mysql_port:-3306}
    done
    read -p "Enter MySQL database name: " mysql_database
    read -p "Enter MySQL username: " mysql_user
    read -s -p "Enter MySQL password: " mysql_password
    echo
else
    mysql_mode="containerized"
    print_status "Automatically creating MySQL container"

    # 允许用户自定义 root 密码
    if ask_yes_no "Do you want to set a custom MySQL root password?" "n"; then
        read -s -p "Enter MySQL root password: " mysql_root_password
        echo
    else
        mysql_root_password=$(generate_password)
        print_success "MySQL root password generated: $mysql_root_password"
    fi

    mysql_password=$(generate_password)
    mysql_host="mysql"
    mysql_port="3306"
    mysql_database="iast"
    mysql_user="iast"
    print_success "MySQL user password generated: $mysql_password"
fi

# ============================
# Redis configuration
# ============================
echo ""
echo "=== Redis Configuration ==="
if ask_yes_no "Use external Redis?" "n"; then
    redis_mode="external"
    print_status "Using external Redis"
    read -p "Enter Redis host: " redis_host
    read -p "Enter Redis port (default: 6379): " redis_port
    redis_port=${redis_port:-6379}
    while ! validate_port "$redis_port"; do
        print_error "Invalid port number."
        read -p "Enter Redis port (default: 6379): " redis_port
        redis_port=${redis_port:-6379}
    done
    read -s -p "Enter Redis password: " redis_password
    echo
else
    redis_mode="containerized"
    print_status "Automatically creating Redis container"
    redis_password=$(generate_password)
    redis_host="redis"
    redis_port="6379"
    print_success "Redis password generated: $redis_password"
fi

# ============================
# Write .env
# ============================
print_status "Creating environment configuration..."
cat > .env << EOF
# SimpleIAST Environment Configuration
# Generated on $(date)
MYSQL_MODE=$mysql_mode
MYSQL_HOST=$mysql_host
MYSQL_PORT=$mysql_port
MYSQL_DATABASE=$mysql_database
MYSQL_USER=$mysql_user
MYSQL_PASSWORD=$mysql_password
MYSQL_ROOT_PASSWORD=$mysql_root_password
REDIS_MODE=$redis_mode
REDIS_HOST=$redis_host
REDIS_PORT=$redis_port
REDIS_PASSWORD=$redis_password
EOF
print_success "Environment configuration saved to .env file"

# ============================
# docker-compose.yml
# ============================
print_status "Generating Docker Compose configuration..."
cat > docker-compose.yml << EOF
name: simpleIAST
services:
EOF

# Redis
if [ "$redis_mode" = "containerized" ]; then
    cat >> docker-compose.yml << EOF
  redis:
    image: redis:7.2-alpine
    environment:
      - TZ=Asia/Shanghai
    command: ["redis-server", "--requirepass", "$redis_password"]
    restart: always
    networks:
      - iast-network
EOF
fi

# MySQL
if [ "$mysql_mode" = "containerized" ]; then
    # Add permission grants to existing init.sql file
    print_status "Adding permission grants to MySQL initialization file..."
    cat >> ./mysql/init.sql << EOL

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON ${mysql_database}.* TO '${mysql_user}'@'%';
FLUSH PRIVILEGES;
EOL
    
    cat >> docker-compose.yml << EOF
  mysql:
    image: $mysql_image
$(echo "$mysql_platform")
    environment:
      - MYSQL_ROOT_PASSWORD=$mysql_root_password
      - MYSQL_USER=$mysql_user
      - MYSQL_PASSWORD=$mysql_password
      - MYSQL_DATABASE=$mysql_database
      - TZ=Asia/Shanghai
    volumes:
      - ./mysql/init.sql:/docker-entrypoint-initdb.d/init.sql
    command:
        - --default-authentication-plugin=mysql_native_password
        - --character-set-server=utf8mb4
        - --collation-server=utf8mb4_unicode_ci
    restart: always
    networks:
      - iast-network
EOF
fi

# Backend
cat >> docker-compose.yml << EOF
  backend:
    image: eclipse-temurin:11-jdk
    container_name: iast-server
    environment:
      - TZ=Asia/Shanghai
      - SPRING_DATASOURCE_URL=jdbc:mysql://${mysql_host}:${mysql_port}/${mysql_database}?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true
      - SPRING_DATASOURCE_USERNAME=$mysql_user
      - SPRING_DATASOURCE_PASSWORD=$mysql_password
      - SPRING_REDIS_HOST=$redis_host
      - SPRING_REDIS_PORT=$redis_port
      - SPRING_REDIS_PASSWORD=$redis_password
    command: java -jar /app/app.jar
    volumes:
      - ./backend/:/app/
EOF

if [ "$mysql_mode" = "containerized" ] || [ "$redis_mode" = "containerized" ]; then
    cat >> docker-compose.yml << EOF
    depends_on:
EOF
    [ "$mysql_mode" = "containerized" ] && echo "      - mysql" >> docker-compose.yml
    [ "$redis_mode" = "containerized" ] && echo "      - redis" >> docker-compose.yml
fi

cat >> docker-compose.yml << EOF
    ports:
      - "81:8989"
    restart: always
    networks:
      - iast-network
EOF

# Frontend
cat >> docker-compose.yml << EOF
  frontend:
    build:
      context: .
      dockerfile: Dockerfile.frontend
    container_name: iast-view
    volumes:
      - ./frontend/web/dist:/usr/share/nginx/html
      - ./frontend/nginx/nginx.conf:/etc/nginx/nginx.conf
      - ./logs/:/var/log/nginx/
    environment:
      - TZ=Asia/Shanghai
    ports:
      - "8443:80"
    depends_on:
      - backend
    networks:
      - iast-network

networks:
  iast-network:
    driver: bridge
EOF
print_success "Docker Compose file created"

# ============================
# Start services
# ============================
print_status "Starting services with Docker Compose..."
docker-compose up -d

if [ $? -eq 0 ]; then
    print_success "All services started successfully!"
    echo ""
    echo "=============================================="
    echo "           DEPLOYMENT COMPLETE"
    echo "=============================================="
    echo ""
    echo "Deployment Summary:"
    echo "MySQL: $mysql_mode"
    echo "Redis: $redis_mode"
    echo ""
    if [ "$mysql_mode" = "containerized" ]; then
        echo "MySQL Root Credentials:"
        echo "  Root Password: $mysql_root_password"
        echo ""
        echo "MySQL User Credentials:"
        echo "  Host:     $mysql_host"
        echo "  Port:     $mysql_port"
        echo "  Database: $mysql_database"
        echo "  Username: $mysql_user"
        echo "  Password: $mysql_password"
        echo ""
    fi
    if [ "$redis_mode" = "containerized" ]; then
        echo "Redis Credentials:"
        echo "  Host:     $redis_host"
        echo "  Port:     $redis_port"
        echo "  Password: $redis_password"
        echo ""
    fi
    echo "Application URLs:"
    echo "Frontend: http://localhost:8443"
    echo "Backend:  http://localhost:81"
    echo ""
    echo "=============================================="
    echo "Management Commands:"
    echo "  docker-compose down      # Stop services"
    echo "  docker-compose logs      # View logs"
    echo "  docker-compose restart   # Restart services"
    echo "=============================================="
else
    print_error "Failed to start services. Check Docker logs for details."
    exit 1
fi
