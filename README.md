# ImagePipeline
Fast editing GUI open source tool for large amount of images at one time.

## Introduction
A large number of images can be processed at once. Multi-threading allows for fast loading, editing, and saving of images, and converts large amounts of image data in a short period of time.
This program is still in the development stage and currently supports only image resizing as an editing method, but we expect to implement various other editing methods in the future.

## Design Concepts
The pipeline is created by defining the various edits as modules and arranging the modules in series. The pipeline then passes the loaded images to perform batch editing on a large number of images.

## Technical Policies
The GUI and pipeline execution parts are implemented using Java. On the other hand, the image editing process uses OpenCV, which allows for scalability in image processing.

## Requirements
The following libraries are necessary to build this program.  

- Apache ant
- Java Development Kit
- cmake

## How to build
First, clone this repository and build OpenCV. In this example, we are going to build OpenCV library in the ```lib``` folder.
If you want to use another version of OpenCV, you need to build following the instrunctions on [Link](https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html#install-opencv-3-x-under-linux). The above site shows how to build OpenCV for Java using CMake-gui, but the CMakeLists.txt file in the built-in OpenCV has been rewritten to built without using CMake-gui.

```bash
git clone https://github.com/ShineiArakawa/ImagePipeline.git
cd ImagePipeline

cd lib
unzip opencv-4.6.0.zip
cd opencv-4.6.0
mkdir build
cd build
cmake ..
make -j # "-j" is optional
```

Then, run ```ant``` to compile Java sources.
If you use another version of OpenCV, you need to edit  ```OPENCV_JAR_DIR``` and ```OPENCV_LIB_DIR``` in ```build.properties```.
```bash
cd ImagePipeline

cp build.properties_template build.properties
ant run
```
