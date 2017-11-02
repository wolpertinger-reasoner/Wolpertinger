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

For the plug-in, it remains to copy the JAR-file from the target directory into your plugins directory of the local Protege installation. You need to have Clingo (https://github.com/potassco/clingo) in your machine for doing reasoning tasks.

# 3-Coloring Examples

We use an ontology that represent the 3-Coloring problem to show how to ask for models. The ontology is available in the sub-repository Ontologies\3Coloring. The core axioms of the problem encoding can be found in 3Coloring.owl. Consider if we want to find a solution for graph G = (V, E):
* introduce a new individual _v\_i_ for each vertex _v_ in V as an instance of concept Node.
* for each edge (u,v) in E, insert an object property assertions "_edge v\_i_" in _u\_i_.

We provide an instance example using a graph with 4 vertices and 4 edges in 3Coloring-4Nodes.owl. We can ask Wolpertinger for 3 fixed-domain models of the ontology using command:

```
java -jar [wolpertinger-path] --models=3 --abox=3coloring-models [ontology-path]
```

\* the ontology-path is an URI. for example, in windows machines:
```
java -jar C:\wolpertinger.jar --models=3 --abox=3coloring-models "file:/C:/Ontologies/3Coloring/3Coloring-4Nodes.owl"
```

The ontologies that represent models will be written in the folder 3coloring-models. One can derive the solution from each model by looking at the instances of RedNode, BlueNode, and GreenNode. If individual  _v\_i_ is an instance of RedNode, then we give red color to vertex _v_, etc.