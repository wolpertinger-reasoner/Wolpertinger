# Wolpertinger

## Building

In order to build Wolpertinger (Standalone and Protege-plugin), ensure that you have [Git](https://git-scm.com/downloads) and [Maven](https://maven.apache.org/download.cgi) installed.

Clone a copy of the repo:

```
git clone https://github.com/wolpertinger-reasoner/Wolpertinger.git
```

Change to the directory:

```
cd Wolpertinger
```

Run maven to build the artifacts:

```
mvn clean package
```

## Installing

For the plug-in, it remains to copy the JAR-file from the target directory into your plugins directory of the local Protege installation.

