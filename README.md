# Pay My Buddy

## Description du projet
Pay My Buddy est une application web développée en Java permettant aux utilisateurs d'envoyer et de recevoir de l'argent de manière sécurisée. L'application repose sur une architecture en couches, comprenant une base de données relationnelle, une couche DAL pour l'accès aux données, et une interface web pour l'interaction utilisateur.

## Technologies utilisées
- **Backend :** Java (Spring Boot)
- **Base de données :** MySQL
- **Frontend :** HTML, CSS, Thymleaf
- **ORM :** Hibernate
- **Outils de collaboration :** GitHub, Notion, Figma

## Installation et configuration
### Prérequis
Avant de commencer, assure-toi d'avoir installé les outils suivants :
- JDK 17+
- MySQL Server
- Maven
- Un IDE (IntelliJ IDEA, Eclipse, VS Code...)

### Étapes d'installation
1. **Cloner le projet :**
   ```bash
   git clone https://github.com/Z093/Paymybuddy
   cd pay-my-buddy
   ```
2. **Configurer la base de données :**
   - Importer le fichier `schema.sql` dans MySQL
   - Modifier le fichier `application.properties` avec tes identifiants MySQL

3. **Lancer l'application :**
   ```bash
   mvn spring-boot:run
   ```
4. **Accéder à l'application :**
   Ouvrir un navigateur et aller sur `http://localhost:8080`

## Structure du projet
```
pay-my-buddy/
│── src/main/java/com/paymybuddy
│   ├── config        # Configuration de l'application
│   ├── controller    # Gestion des requêtes HTTP
│   ├── dto           # Objets de transfert de données (Data Transfer Objects)
│   ├── model         # Modèles de données (JPA Entities)
│   ├── repository    # Interface d'accès aux données (DAL)
│   ├── security      # Gestion de la sécurité et authentification
│   ├── service       # Logique métier
│   ├── PayMyBuddyApplication.java  # Point d'entrée de l'application
│
│── src/main/resources
│   │── schema.sql    # Script SQL pour la base de données
│   ├── static        # Fichiers CSS
│   ├── templates     # HTML Thymeleaf
│   ├── application.properties # Configuration
│
│── README.md         # Documentation du projet
```

## Diagrammes
### Diagramme UML
Le diagramme UML décrit l'architecture globale du projet et les interactions entre les différentes entités. 

![Diagramme UML](https://github.com/user-attachments/files/19314906/Diagramme%2BUML%2B-%2BPay%2BMy%2BBuddy.pdf)


### Modèle Physique de Données (MPD)
Le MPD représente la structure des tables et les relations dans la base de données.

![Modèle Physique de Données](https://github.com/user-attachments/assets/a28a93a4-258f-44fd-bbbb-ea86af308789)


## Fonctionnalités principales
- Création et connexion d'un compte utilisateur
- Ajout d'amis à son réseau
- Envoi d'argent entre amis
- Gestion d'un compte utilisateur


## Standards et bonnes pratiques
L’application suit les standards **WCAG** pour garantir une accessibilité optimale. Le design est basé sur les maquettes **Figma** fournies par l'équipe UX.

## Contact
Avinash - Chef de projet
Anja - UI/UX Designer
Guto - Développeur base de données

Merci de respecter les bonnes pratiques de code et la documentation fournie ! 🚀

