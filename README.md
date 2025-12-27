Système de Gestion Académique (Mini-Projet JavaFX)

Une une application desktop de gestion académique permettant de gérer : Filières - Étudiants (Élèves) - Dossiers administratifs - Cours

Architecture
L'application respecte le modèle MVC couplé au fichiers DAO (Data Access Object)
1. La Vue (View - .fxml): Interface graphique. Et pour la Navigation, On a utiliser Architecture **BorderPane** avec un menu latéral fixe (Filiere, etudiant, cours ...) et un chargement dynamique des vues au centre avec FXMLLoader.
2. Le Contrôleur (Controller - .java) : Pour lq gestions des interactions (click, events ...) et la logique de l'affichage evec ComboBox.
3. Le Modèle (Model - .java) : Pour les entites et les tables de la base de donnees.
4. La Couche DAO : Qui contient toute la logique SQL (CRUD) et la logique metier

Choix Techniques
JDBC (Java Database Connectivity) : Afin d'utiliser SQL brute et controller les transactions.
PostgreSQL : Choix basee sur mes preference, et sur l'existance de type UUID.
UUID : Pour les ids, c'est le standard pour les ids et il offre une couche de securite de plus.

Difficultés Rencontrées
1. L'obligation d'utiliser ResultSets, et les transformer.
2. L'utilisation de ComboBox est un peu difficile, Il ne faut pas oublier la methode ToString sinon l'affichage ne sera pas comme prevue.
3. Le Grand Problem c'est celui de la planification, Il faut savoir commencer par quoi et avoir une vision global, pour être capable de faire certain decisions au niveaux bas, et plannifier la totalite du projet avant de commencer blindly le codage.

Le projet est complet maintenant.