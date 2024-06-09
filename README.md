# Projet : Jeu d'Échecs SAE
## Réalisé par KERBADOU Islem, BUCHMULLER Nassim, PELLET Casimir, FROMENTIN Felix

Ce document décrit les fonctionnalités, l'installation et l'utilisation de l'application, ainsi que quelques informations sur l'architecture et les bonnes pratiques de développement adoptées.

## Fonctionnalités
### 1. Jeu d'Échecs de Base
- Règles de déplacements : L'application implémente toutes les règles de déplacement des pièces d'échecs (roi, dame, tour, fou, cavalier, pion).
- Limite de temps : Possibilité de définir une limite de temps pour chaque joueur. Si le temps imparti est dépassé, alors la partie est terminée. On a décidé de s'inspirer de chess.com ici et de non pas déplacerune pièce aléatoire au bout d'un certains temps.
- Interface graphique : L'interface graphique s'inspire de celle de chess.com, on a pris la liberté de la modifier sans pour autant trop s'en éloigner.

### 2. Mode de Jeu
- Joueur contre joueur (sur la même machine) : Deux joueurs peuvent jouer l'un contre l'autre sur la même machine, en alternant les tours. Il faut obligatoirement se connecter au préalable, puis choisir contre qui l'on souhaite jouer.
- 1v1 contre un Bot : Un mode de jeu contre l'ordinateur est disponible avec un bot faisant des coups valides aléatoires.
- Tournoi à élimination directe : Un mode tournoi permet de s'affronter à plusieurs avec un système d'élimination directe. Les joueurs s'affrontent de manière aléatoire avec un seul vainqueur.

### 3. Gestion des Joueurs et Statistiques
- Enregistrement des joueurs : Les joueurs peuvent être enregistrés dans un fichier avec leur nom, prénom, nombre de parties jouées et nombre de parties gagnées. On a utilisé le format CSV.
- Consultation des statistiques : Les statistiques des joueurs peuvent être consultées à partir de l'interface de l'application. On peut y consulter le nombre de parties jouées et gagnées.

### 4. Gestion des Parties
- Pause et reprise : Une partie en cours peut être mise en pause (enregistrée dans un fichier temporaire au format CSV) et rechargée plus tard pour être continuée.

