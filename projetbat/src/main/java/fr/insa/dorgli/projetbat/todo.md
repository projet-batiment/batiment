# TODOS

<span style="color: red; text-decoration: underline; font-style: italic; font-weight: bold">TOUJOURS se référer au <a href="https://github.com/projet-batiment/batiment/blob/master/projetbat/src/main/java/fr/insa/dorgli/projetbat/todo.md">fichier TODO de la branche MASTER</a></span>

*(regarder aussi les "TODO" dans les différents fichiers)*

## Sommaire des trucs à faire

À faire en premier(s) :

* Finioler l'OOP (manipuler les objets et le IDs)
* Devis
* Sauvegarde (serde)
* GUI (interface graphique)

Secondaire :

* Géométrie, aires, intersections d'ouvertures et de revêtements

## Importants

Dans l'ordre :

- [x] OOP: Lister les objets et faire des IDs
  - [x] Faire une liste de tous les objets par ID et les regrouper par type dans des map (id -> objet)
  - [ ] Faire des méthodes permettant de manipuler les objets à partir d'ID / retrouver l'ID d'un objet
  - [ ] Afficher l'ID de l'objet en question dans toStringShort, sans avoir à le stocker dans l'objet en question

- [ ] Calculer un devis
  - [ ] Calculer le prix de chaque sous-composant de manière récursive
    - [ ] (Interface *CalculerPrix* ? *Aire* ?)
    - Est-ce que le devis mérite une classe, pour avoir plusieurs devis en parallèle ?
    - [ ] Sauvegarder / expotrer un devis
      - [ ] Sous forme de texte
      - [ ] Mis joliment en forme (si vraiment vraiment le temps ?)

- [ ] Sauvegardes dans un fichier
  - [ ] Classe à part pour dé/serialiser ? ou chaque classe le fait elle-même ?
    en cours...
  - [ ] Sauvegarder une session
  - [x] Charger une session
  - [x] Choisir un dossier de sauvegarde
  - [x] ~~tar ? zip ? dossier ?? fichier unique ????~~
    => pour l'instant la sauvegarde se fait dans une sorte de CVS boosté maison en `.batm`

- [ ] Interface graphique (GUI)
  - [ ] Afficher une fenêtre
  - [ ] Dessiner bêtement les objets
  - [ ] Ajouter un volet d'un côté ou de l'aautre pour éditer à la main les propriétés des objets
  - [ ] Rendre les objets cliquables

## Secondaires

- [ ] Géométrie plus permissive
  - [ ] Passer d'un système maison à des objets de `java.awt`, avec des `Area` et des `Point` en particulier
  - [ ] Rendre utilisable des formes polygonales plus que rectangulaires
  - [ ] Gérer les intersections de revêtements et d'ouvertures

## Passés

- [x] Normaliser les constructeurs qui se ressemblent
  Typiquement, pour certaines classes, "nom" arrive en dernier argument tandis qu'il est premier pour d'autres classes...

- [x] Faire les classes de base
    => Terminé 🥳
  - [x] L'environnement de la classe Mur a l'air complet
  - [x] Piece, Sol, Plafond, RevetementSolPlafond, OuvertureNiveaux, TypeOuvertureNiveaux
  - [x] ~~EtageAppart (à garder ou supprimer ??)~~, Niveau, Appart
  - [x] Batiment
- [x] Faire une classe principale / Main / App et une fonction principale, ~~mais en gardant quand même Test~~
- [x] ~~Faire une *mini* interface interactive (TUI) pour manipuler de manière basique la hiérarchie des objets du batiment~~
  - ~~*(en cours)*~~
  => interface TUI abandonnée car sauvegarde (serde)

- [x] Faire une classe `Config` 😎
  => passe aussi par la classe `Objects`
- [x] Rendre le logging optionnel / mode verbeux
  => voir `TUI` et la couleur
