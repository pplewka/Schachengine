# Baguette

A UCI-compatible chess engine

##Dependencies

- Java (>=11)

- Maven

- (Git)


### Supported OS

- Linux (tested under Ubuntu 19.04)

- Windows (tested under Windows 10)

### Supported GUI

- [Arena](http://www.playwitharena.de/)

## Installation (for Ubuntu)

### Dependencies

For Ubuntu 19.04:

`sudo apt install openjdk-11-jdk git maven`

### Compilation

`git clone https://github.com/pplewka/Schachengine`

`cd Schachengine`

`mvn package`

The compiled *.jar file will be in the target directory

### Installation in Arena

Install and run the latest Arena version from their [website](http://www.playwitharena.de/)
(Note: under some Ubuntu versions you have to run `sudo apt install libgtk2.0-0` to start arena)

To add Baguette to Arena click "Engines" (or "Motoren" in the german version) -> "Install new engine...".

Select the *.jar file in the Schachengine/target folder.

Now you have to configure the engine: "Engines" -> "Manage"

In the "Details" section there should be an entry on the left called "new engine".
Configure it with the following parameters:

Name: "Baguette"

Author: "Alessio Ragusa, Matthias Sennikow, Patrick Plewka"

Commandline: <select the compiled *.jar file in the Schachengine/target directory>

Country: "Germany"

Type: "UCI"

If at any point Arena asks you to set the executable bit for the engine, simply accept.

## Installation (for Windows 10)

### Dependencies

- Java 11 ([Download](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html))
- Maven for building the project

### Compilation

`git clone https://github.com/pplewka/Schachengine`

`cd Schachengine`

`mvn package`

The compiled *.jar file will be in the target directory

### Installation in Arena

Install and run the latest Arena version from their [website](http://www.playwitharena.de/)

To add Baguette to Arena click "Engines" (or "Motoren" in the german version) -> "Install new engine...".

Select the *.jar file in the Schachengine/target folder.

Now you have to configure the engine: "Engines" -> "Manage"

In the "Details" section there should be an entry on the left called "new engine".
Configure it with the following parameters:

Name: "Baguette"

Author: "Alessio Ragusa, Matthias Sennikow, Patrick Plewka"

Commandline: <select the compiled *.jar file in the Schachengine/target directory>

Country: "Germany"

Type: "UCI"

If at any point Arena asks you to set the executable bit for the engine, simply accept.

##Setting options
Changing options is supported only at startup.

This means in Arena you have to restart the engine after changing an option.

## Misc

[time tracking](
https://docs.google.com/spreadsheets/d/1IdjtzDdD-qunN6sGhvGweS9n2EE_SIZ2V6HmBaCfqOE/edit?usp=drivesdk)
