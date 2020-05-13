# Lara Project

## Java
### Compilation
Les parties écrites dans le langage Java reposent sur le gestionnaire de module Maven. Vous pouvez installer les dépendances en réalisant :
```
mvn install clean
```
### GUI - org.lara.gui
Le GUI a été réalisé par Anna-Rose et effectue les fonctions basiques de notre interface. Il a été implémenté en utilsant les bibliothèques livrées avec Java JDK (Java AWT et JavaX Swing).
### NLP - org.lara.nlp
Le NLP a été réalisé par Louis et se décompose en 2 parties :
* la notion de context permet de convertir un fichier de conversation en deux listes de questions et de réponses (la class *org.lara.nlp.Cornell* l'implémente pour la base de dialogue Cornell)
* la notion de word2vec (implémentée grâce à Deeplearning4J) qui convertit les mots en vecteur depuis des phrases d'entraînement. Il embarque :
    - le modèle Word2Vec (*org.lara.nlp.dl4j.W2v*)
    - le modèle SequenceVectors (*org.lara.nlp.dl4j.Sv*)
    - le modèle Glove (*org.lara.nlp.dl4j.Glv*)
    - le modèle ParagraphVectors (une implémentation de *PV-DBOW* qui se base sur les modèles précédents pour convertir une phrase en vecteur)  (*org.lara.nlp.dl4j.Pv*)
### RNN2Java - org.lara.rnn
Système de socket réalisé par Louis permettant la communication entre le RNN écrit en Python et l'application Java. Pour l'utiliser, il est nécessaire de compiler avec _protobuf_. Pour installer protobuf
```
sudo apt install protobuf-compiler
```
et pour compiler notre API
```
make
```

## Python
### Compilation
Les bibliothèques nécessaires peuvent être installées via pip :
```
pip install -r requirments.txt
```
### Parser Facebook
Le parser Facebook a été développé par Riad et permet de traiter les données de exporter de Facebook au format JSON. Les paramètres d'entrée sont *delayBetween2Conv*, *nbMessages* et *fbConvFilename* a définir en fin de fichier.
### RNN
Le réseau de neuronne a été développé par Louis en utilisant Keras. Il exploit le modèle Seq2Seq.
### Serveur pour la discussion avec l'application Java
Il est nécessaire de lancer ce serveur avant de lancer l'application Java :
```
python python/main.py
```

## Execution du projet
```
mvn javafx:run
```
