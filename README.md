# Lara Project

## Java
### Compilation
Les parties écrites dans le langage Java reposent sur le gestionnaire de module Maven. Vous pouvez compiler le code en réalisant :
```
mvn install clean
```
et exécuter une classe quelconque avec
```
java -cp target/laraproject-0.1-SNAPSHOT.jar org.lara.NOMDELACLASSE
```
### GUI - org.lara.gui.Main
Le GUI a été réalisé par Anna-Rose et effectue les fonctions basiques de notre interface. Il a été implémenté en utilsant les bibliothèques livrées avec Java JDK (Java AWT et JavaX Swing).
### NLP - org.lara.nlp.*
Le NLP a été réalisé par Louis et se décompose en 2 parties :
* la notion de context permet de convertir un fichier de conversation en deux listes de questions et de réponses (la class *org.lara.nlp.Cornell* l'implémente pour la base de dialogue Cornell)
* la notion de word2vec (implémentée grâce à Deeplearning4J) qui convertit les mots en vecteur depuis des phrases d'entraînement. Il embarque :
    - le modèle Word2Vec (*org.lara.nlp.dl4j.W2v*)
    - le modèle SequenceVectors (*org.lara.nlp.dl4j.Sv*)
    - le modèle Glove (*org.lara.nlp.dl4j.Glv*)
    - le modèle ParagraphVectors (une implémentation de *PV-DBOW* qui se base sur les modèles précédents pour convertir une phrase en vecteur)  (*org.lara.nlp.dl4j.Pv*)

## Python
### Compilation
Les bibliothèques nécessaires peuvent être installées via pip :
```
pip install -r requierments.txt
```
### Parser Facebook
Le parser Facebook a été développé par Riad et permet de traiter les données de exporter de Facebook au format JSON. Les paramètres d'entrée sont *delayBetween2Conv*, *nbMessages* et *fbConvFilename* a définir en fin de fichier.
### RNN
Le réseau de neuronne a été développé par Antoine en utilisant Keras. La version actuelle de ce modèle *seq2seq* entraîne le réseau de neurones sur les ensembles suivants :
- X : vecteurs à valeur entière (dans une plage prédéfinie) et de longueur fixée.
- Y : ces mêmes vecteurs sauf que la moitié d'entre eux ont vu leur coordonnée augmentée de 1 et les autres diminuée de 1.