LogFileWatcher
========================

A Java program that watches a log file (i.e. files that are always appended by other applications) and print its updates into screen.

![Screenshot](https://raw.githubusercontent.com/hlfernandez/LogFileWatcher/master/screenshots/screenshot.gif)

Requirements
------------
To run this implementation of the game of life you need:
  - Java 1.8+
  - Maven 2+
  - Git
  - [GC4S](https://github.com/hlfernandez/GC4S)

Installation
------------
### 1. Download
You can download the LogFileWatcher project directly from Github using the following command:
`git clone https://github.com/hlfernandez/LogFileWatcher.git`

Alternatively, you can [download a build jar] (https://raw.githubusercontent.com/hlfernandez/LogFileWatcher/master/builds/logfilewatcher-0.1-jar-with-dependencies.jar).

### 2. Build 

First, go to the LogFileWatcher project base directory and run the following command:
`mvn package`

After building you can find the distribution file `logfilewatcher-0.1-jar-with-dependencies.war` file in the `target` directory.

### 3. Launch
Type `java -jar logfilewatcher-0.1-jar-with-dependencies.war -l /path/to/log.file` to run the application.
