#!/bin/bash

FILE1=./open-wbcrypto
if [ -f "$FILE1" ]; then
    echo "$FILE1 exist"
else
    echo "$FILE1 does not exist"
    exit 1
fi

cd $FILE1
mkdir build && cd build
cmake ..
make
cd ../..

FILE2=./libcrypto.so
if [ -f "$FILE2" ]; then
    rm -f $FILE2
fi

cp -r $FILE1/build/out/libcrypto.so ./