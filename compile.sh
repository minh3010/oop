#!/bin/bash

echo "=========================================="
echo "  COMPILE HỆ THỐNG QUẢN LÝ RẠP PHIM"
echo "=========================================="

# Kiểm tra Java
if ! command -v javac &> /dev/null
then
    echo "❌ Java compiler (javac) chưa được cài đặt!"
    echo "Vui lòng cài đặt Java JDK trước khi tiếp tục."
    echo ""
    echo "Ubuntu/Debian: sudo apt install default-jdk"
    echo "macOS: brew install openjdk"
    exit 1
fi

echo "✓ Tìm thấy Java compiler"
javac -version

echo ""
echo "Đang compile các file Java..."

# Compile tất cả các file
javac cinema/model/*.java cinema/manager/*.java cinema/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compile thành công!"
    echo ""
    echo "Để chạy chương trình, sử dụng lệnh:"
    echo "  java cinema.Main"
    echo ""
    echo "Hoặc chạy script:"
    echo "  ./run.sh"
else
    echo "❌ Compile thất bại!"
    exit 1
fi
