# Wolpertinger - A Bounded Model Reasoner for OWL DL (SROIQ)

Wolpertinger is a simple tool, which translates OWL DL ontologies into an answer
set program accepted by gringo (version 4.4), respectively clingo (4.4) (http://potassco.sourceforge.net).

Each obtained answer set, when using clasp or clingo, corresponds to a so-called bounded model.
That is, a model where the domain is induced by exactly those individuals
occurring within the ontology.

## Install

Clone the repository to your machine, via:

```
git clone https://github.com/wolpertinger-reasoner/Wolpertinger.git
```

Run Maven to compile Wolpertinger:

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
