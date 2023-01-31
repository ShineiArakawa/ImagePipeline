#!/bin/bash

cd lib
unzip opencv-4.6.0.zip

cd opencv-4.6.0
mkdir build

cd build
cmake ..
make -j

cd ../../../
