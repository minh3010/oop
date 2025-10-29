#!/bin/bash

echo "=========================================="
echo "  HỆ THỐNG QUẢN LÝ RẠP PHIM"
echo "=========================================="

# Kiểm tra Java
if ! command -v java &> /dev/null
then
    echo "❌ Java chưa được cài đặt!"
    echo "Vui lòng cài đặt Java JDK trước khi tiếp tục."
    exit 1
fi

# Kiểm tra đã compile chưa
if [ ! -f "cinema/Main.class" ]; then
    echo "Chưa compile! Đang compile..."
    ./compile.sh
    if [ $? -ne 0 ]; then
        exit 1
    fi
fi

echo ""
echo "Đang khởi động hệ thống..."
echo ""

# Chạy chương trình
java cinema.Main
