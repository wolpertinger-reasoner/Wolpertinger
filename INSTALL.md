## Install

Clone the git repository, via:

```
git clone https://github.com/wolpertinger-reasoner/Wolpertinger.git
```

Run Maven to compile and package Wolpertinger:

```
mvn package
```

## Use Wolpertinger

```
java -jar wolpertinger.jar --help
```

Use the Sudoku example Ontology:
```
java -jar wolpertinger.jar --translate Sudoku-9x9.owl
```
