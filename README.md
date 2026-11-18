# SEG 3502 — Laboratoire 9 — Sécurité de l'API

Ce projet est l'API REST de conversion de température du laboratoire 9. Il répond à **l'exercice évalué de la section 3** de `SpringSecurity.pdf` : authentification en mémoire, deux utilisateurs et HTTP Basic obligatoire sur toutes les requêtes.

Aucune interface utilisateur ni implémentation JWT n'est demandée.

## Prérequis

- JDK 21
- Windows, macOS ou Linux

## Démarrer l'API

Dans le dossier du projet :

**Windows (CMD ou PowerShell) :**

```cmd
.\gradlew.bat bootRun
```

**macOS ou Linux :**

```bash
./gradlew bootRun
```

L'API répond sur `http://localhost:8080`.

## Utilisateurs (In Memory)

| Nom d'utilisateur | Mot de passe | Rôle |
| --- | --- | --- |
| `user1` | `pass1` | `USER` |
| `user2` | `pass2` | `USER` |

Les mots de passe sont encodés avec BCrypt en mémoire. Les identifiants sont définis en clair dans le code **uniquement parce qu'ils sont imposés par cet exercice pédagogique** ; en production, ils devraient être configurés de façon sécurisée.

## Points de terminaison

Les deux conversions exigent une authentification **Basic Auth à chaque requête** :

- `GET /temperature-converter/celsius-fahrenheit/{celsius}`
- `GET /temperature-converter/fahrenheit-celsius/{fahrenheit}`

### Vérification avec curl

Dans un terminal, lorsque le serveur est en cours d'exécution :

```cmd
REM Réussite : 200 OK, résultat 122.0
curl -i -u user1:pass1 http://localhost:8080/temperature-converter/celsius-fahrenheit/50

REM Réussite : 200 OK, résultat 100.0
curl -i -u user2:pass2 http://localhost:8080/temperature-converter/fahrenheit-celsius/212

REM Échec attendu : 401 Unauthorized (pas d'identifiants)
curl -i http://localhost:8080/temperature-converter/celsius-fahrenheit/50

REM Échec attendu : 401 Unauthorized (mauvais mot de passe)
curl -i -u user1:incorrect http://localhost:8080/temperature-converter/celsius-fahrenheit/50
```

Dans **Postman ou Insomnia** : choisir l'onglet *Authorization*, sélectionner *Basic Auth* et saisir l'un des deux comptes avant d'envoyer une requête GET.

## Exécuter les tests automatisés

**Windows :**

```cmd
.\gradlew.bat test
```

**macOS ou Linux :**

```bash
./gradlew test
```

Les tests d'intégration vérifient les conversions pour `user1` et `user2`, l'absence d'identifiants, les mauvais mots de passe et les noms d'utilisateurs inconnus.

## Fichiers principaux

- `src/main/kotlin/seg3x02/tempconverterapi/security/SecurityConfig.kt` : configuration de Spring Security, HTTP Basic stateless, authentification en mémoire et BCrypt.
- `src/main/kotlin/seg3x02/tempconverterapi/controller/ConverterController.kt` : contrôleur original des conversions, non modifié.
- `src/test/kotlin/seg3x02/tempconverterapi/TemperatureConverterSecurityTests.kt` : tests d'accès autorisés ou refusés.

## Remise

Publier les fichiers sur **son propre dépôt GitHub Classroom attribué pour le Lab 9**, et non sur le dépôt `SEG3502Lab/SEG3502_Lab9_Starter` du professeur.
