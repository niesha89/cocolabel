# cocolabel
This project provides a web labeling application. It has both web pages and backend.

Web pages of this project is mainly based on imglab : https://github.com/NaturalIntelligence/imglab, which is a web labeling project without any backend.
The backend is written based on spring boot : https://github.com/spring-projects/spring-boot.

## Features

* Cocolabel is platform independent, runs on JDK8+. It requires minimal CPU and memory.
* No databases are required.
* Uploading images is not supported currently, and only images on server can be read.
* All the settings and label files will be saved on file system of your server.


## How to install

- To install locally, compile command like this:
* mvn package
- Find the built jar in target folder, then run this:
* java -jar cocolabel-0.1.jar


## How to use
Visit http://localhost:9999/ and you will see the web page.
1. Move your mouse to the lefttop and you will see a dropdown menu, click "Project".
2. Type your source directory where the pictures locate
3. Type your workspace directory where you would like to place your labeled file.
4. Click switch button and you will see the thumbnails on the bottom.
5. Move your mouse to the lefttop and you will see a dropdown menu, click "Settings".
6. Predefine all the potential categories related to your current work, then click "Save" button
7. Click any thumbnail, then you can use tool kit on the left side to draw a rectangle, zoom in/out or move any rectangle
8. You can modify the category and label name of any rectangle on the right of the page after you click a certain rectangle.
9. If you switch a picture either by clicking another thumbnail or using "ALT" + left/right, all the labels will be saved automatically to your workspace directory.

## Built with

This application is built upon following technology stack.

* [jQuery](https://jquery.com)
* [Bootstrap](https://getbootstrap.com)
* [Riot.js](https://github.com/riot/riot) - Simple and elegant component-based UI library
* [SVG.js](http://svgjs.com)
* [imglab](https://github.com/NaturalIntelligence/imglab)
* [Spring boot](https://github.com/spring-projects/spring-boot)
* [Fastjson](https://github.com/alibaba/fastjson)
* [Freemarker](https://github.com/apache/freemarker)


## Contributors

* Nie Sha
