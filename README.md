# 🛠️ Gestion de Service IT - API de Support

Ce projet est une API REST de gestion de tickets de support informatique (IT Support), développée avec **Spring Boot**, **Spring Security**, et **JPA/Hibernate**. Elle permet aux utilisateurs de soumettre et suivre des tickets d'assistance, et aux administrateurs de gérer le cycle de vie de ces tickets.

---

## 🚀 Fonctionnalités Clés

*   **Gestion des Utilisateurs & Sécurité :**
    *   Inscription et connexion des utilisateurs.
    *   Authentification sans état (Stateless) basée sur des jetons **JWT (JSON Web Tokens)**.
    *   Gestion des rôles : `USER` (Utilisateur standard) et `ADMIN` (Administrateur).
*   **Gestion des Tickets :**
    *   Création de tickets de support par les utilisateurs.
    *   Consultation des détails d'un ticket.
    *   Visualisation de la liste des tickets :
        *   Un `USER` voit uniquement les tickets qu'il a créés.
        *   Un `ADMIN` a une visibilité globale sur l'ensemble des tickets du système.
    *   Mise à jour du statut des tickets par l'administrateur (`OPEN` ➡️ `IN_PROGRESS` ➡️ `RESOLVED`).
*   **Base de Données H2 intégrée :**
    *   Utilisation d'une base de données en mémoire pour un démarrage rapide sans configuration externe.
    *   Console d'administration H2 activée pour inspecter les données en temps réel.

---

## 🛠️ Stack Technique

*   **Langage :** Java 17
*   **Framework Principal :** Spring Boot 4.0.6
*   **Persistance :** Spring Data JPA / Hibernate
*   **Sécurité :** Spring Security & JWT (io.jsonwebtoken)
*   **Base de données :** H2 (en mémoire)
*   **Utilitaires :** Lombok

---

## 📁 Architecture du Projet

Le projet suit une architecture en couches standard pour une séparation claire des responsabilités :

```text
src/main/java/com/example/itsupport/
├── config/          # Configurations générales (ex. PasswordEncoder)
├── controller/      # Contrôleurs REST (Exposition des endpoints)
├── dto/             # Objets de transfert de données (Data Transfer Objects)
├── entity/          # Modèles de données JPA (User, Ticket, Role, Status)
├── exception/       # Gestionnaires d'exceptions personnalisées
├── repository/      # Interfaces d'accès aux données (Spring Data JPA)
├── security/        # Filtres et utilitaires pour l'authentification JWT & Security
└── service/         # Logique métier de l'application
```

---

## ⚡ Démarrage Rapide

### Prérequis
*   **Java Development Kit (JDK)** version 17 ou supérieure.
*   **Maven** 3.x (ou utilisez le wrapper `mvnw` inclus).

### Étapes d'installation et de lancement

1.  **Cloner le dépôt ou se positionner dans le dossier racine :**
    ```bash
    cd c:/Users/HP/Desktop/itsupport
    ```

2.  **Lancer l'application avec le wrapper Maven :**
    *   **Sur Windows (PowerShell/CMD) :**
        ```powershell
        ./mvnw.cmd spring-boot:run
        ```
    *   **Sur Linux/macOS :**
        ```bash
        ./mvnw spring-boot:run
        ```

3.  **Vérifier le bon fonctionnement :**
    L'application démarrera par défaut sur le port `8080`. Vous pouvez tester la connexion à l'URL :
    `http://localhost:8080`

---

## 🗄️ Console Base de Données (H2)

La console H2 vous permet d'inspecter les tables `users` et `tickets` directement depuis votre navigateur.

*   **URL d'accès :** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
*   **Configuration de connexion :**
    *   **Driver Class :** `org.h2.Driver`
    *   **JDBC URL :** `jdbc:h2:mem:itsupportdb`
    *   **User Name :** `sa`
    *   **Password :** `password`

---

## 📖 Guide de l'API REST

Tous les endpoints (sauf authentification et console H2) nécessitent de passer le jeton JWT dans l'en-tête HTTP sous la forme :
`Authorization: Bearer <votre_token_jwt>`

### 1. Authentification (`/api/auth`)

#### 📝 Inscription
*   **URL :** `POST /api/auth/register`
*   **Corps de la requête (JSON) :**
    ```json
    {
      "username": "jean_dupont",
      "password": "MotDePasseSecurise123",
      "role": "USER"
    }
    ```
*   **Réponse (JSON) :**
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
    ```

#### 🔑 Connexion
*   **URL :** `POST /api/auth/login`
*   **Corps de la requête (JSON) :**
    ```json
    {
      "username": "jean_dupont",
      "password": "MotDePasseSecurise123"
    }
    ```
*   **Réponse (JSON) :** Retourne le jeton d'accès JWT.

---

### 2. Gestion des Tickets (`/api/tickets`)

#### 🆕 Créer un ticket (`USER` uniquement)
*   **URL :** `POST /api/tickets`
*   **Corps de la requête (JSON) :**
    ```json
    {
      "title": "Écran défectueux",
      "description": "Mon écran secondaire clignote et s'éteint de manière aléatoire."
    }
    ```
*   **Réponse (JSON) :**
    ```json
    {
      "id": 1,
      "title": "Écran défectueux",
      "description": "Mon écran secondaire clignote et s'éteint de manière aléatoire.",
      "status": "OPEN",
      "creatorUsername": "jean_dupont",
      "createdAt": "2026-05-25T22:30:00",
      "updatedAt": "2026-05-25T22:30:00"
    }
    ```

#### 📋 Lister les tickets (`USER` ou `ADMIN`)
*   **URL :** `GET /api/tickets`
*   **Description :** 
    *   Si connecté en tant que `USER` : Retourne la liste des tickets que vous avez créés.
    *   Si connecté en tant qu'`ADMIN` : Retourne l'intégralité des tickets du système.
*   **Réponse (JSON) :** Liste d'objets tickets.

#### 🔍 Consulter un ticket par son ID
*   **URL :** `GET /api/tickets/{id}`
*   **Réponse (JSON) :** Détails du ticket demandé.

#### ⚙️ Mettre à jour le statut d'un ticket (`ADMIN` uniquement)
*   **URL :** `PUT /api/tickets/{id}/status?status={STATUS}`
*   **Paramètre de requête :** `status` peut prendre les valeurs `OPEN`, `IN_PROGRESS`, ou `RESOLVED`.
*   **Exemple :** `PUT /api/tickets/1/status?status=IN_PROGRESS`
*   **Réponse (JSON) :** Le ticket mis à jour avec son nouveau statut.

---

### 3. Gestion des Utilisateurs (`/api/users`)

#### 👤 Consulter son profil
*   **URL :** `GET /api/users/me`
*   **Réponse (JSON) :** Profil complet de l'utilisateur actuellement authentifié.

#### 👥 Lister tous les utilisateurs (`ADMIN` uniquement)
*   **URL :** `GET /api/users`
*   **Réponse (JSON) :** Liste complète des utilisateurs enregistrés dans la base.
