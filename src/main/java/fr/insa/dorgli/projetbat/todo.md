# TODOS

*(regarder les "TODO" dans les différents fichiers)*

#### Notes

* La classe Test est un programme "principal" pour faire des tests avec les classes et compagnie, sans toucher au reste du programme
* La classe Deserialize permet de créer une hiérarchie d'objets via une interface interactive très rustique
  A terme, il faudrait séparer la partie interactive d'une future partie automatique (sur des fichiers de sauvegarde)

## Urgents

- [ ] Normaliser les constructeurs qui se ressemblent
  Typiquement, pour certaines classes, "nom" arrive en dernier argument tandis qu'il est premier pour d'autres classes...

## Importants

Dans l'ordre :

- [ ] Faire les classes de base
  - [x] L'environnement de la classe Mur a l'air complet
  - [ ] Piece, Sol, Plafond, RevetementSolPlafond, OuvertureNiveaux, TypeOuvertureNiveaux
  - [ ] EtageAppart (à garder ou supprimer ??), Niveau, Appart
  - [ ] Batiment
- [ ] Faire une classe principale / Main / App et une fonction principale, mais en gardant quand même Test
- [ ] Lister les objets et faire des IDs
  - [ ] Faire une liste de tous les objets par ID et les regroupper par type dans des map (id -> objet)
  - [ ] Faire des méthodes permettant de manipuler les objets à partir d'ID / retrouver l'ID d'un objet
  - [ ] Afficher l'ID de l'objet en question dans toStringShort, sans avoir à le stocker dans l'objet en question
- [ ] Faire une *mini* interface interactive (TUI) pour manipuler de manière basique la hiérarchie des objets du batiment
  - *(en cours)*
- [ ] Calculer un devis
  - [ ] Calculer le prix de chaque sous-composant de manière récursive
    - [ ] Interface *Couts* ?
  - Est-ce que le devis mérite une classe, pour avoir plusieurs devis en parallèle ?
- [ ] Sauvegardes dans un fichier
  - [ ] Classe à part pour dé/serialiser ? ou chaque classe le fait elle-même ?
  - [ ] Sauvegarder une session
  - [ ] Charger une session
  - [ ] Choisir un dossier de sauvegarde
  - [ ] tar ? zip ? dossier ?? fichier unique ????

## Secondaires

- [ ] Faire une classe `Config` 😎
- [ ] Rendre le logging optionnel / mode verbeux
- [ ] Sauvegarder / expotrer un devis
  - [ ] Sous forme de texte
  - [ ] Mis joliment en forme (?)
