# ImagePipeline
Fast editing GUI open source tool for large amount of images.

## Introduction
A large number of images can be processed at once. Multi-threading allows for fast loading, editing, and saving of images, and converts large amounts of image data in a short period of time.
This program is still in the development stage and currently supports only image resizing as an editing method, but we expect to implement various other editing methods in the future.

## Design Concepts
The pipeline is created by defining the various edits as modules and arranging the modules in series. The pipeline then passes the loaded images to perform batch editing on a large number of images.

## Technical Policies
The GUI and pipeline execution parts are implemented using Java. On the other hand, the image editing process uses OpenCV, which allows for scalability in image processing.
