# De ProjectCheck — Back-end
De ProjectCheck is een online instrument waarmee projecten kunnen worden geëvalueerd. Het instrument heeft de vorm van een kaartspel.
Op www.deprojectcheck.nl is te zien hoe het eruit ziet, en hoe het in zijn werk gaat. In deze tijden waarin alles online moet gebeuren,
moet niet noodzakelijk zijn dat de deelnemers fysiek bijeenkomen. Met De ProjectCheck kunnen evaluaties worden voorbereid, waarna de
deelnemers via bijvoorbeeld Zoom of Microsoft Teams bijeenkomen en hun bevindingen via het scherm delen.

[![Build Status](https://jenkins.aimsites.nl/buildStatus/icon?job=%28OOSE+2020-2021-s2+Buizerd%29+De+ProjectCheck+-+Back-end)](https://jenkins.aimsites.nl/job/(OOSE%202020-2021-s2%20Buizerd)%20De%20ProjectCheck%20-%20Back-end/)
[![Quality Gate Status](https://sonarqube.aimsites.nl/api/project_badges/measure?project=nl.han.oose.buizerd%3Aprojectcheck-backend&metric=alert_status)](https://sonarqube.aimsites.nl/dashboard?id=nl.han.oose.buizerd%3Aprojectcheck-backend)
[![Coverage](https://sonarqube.aimsites.nl/api/project_badges/measure?project=nl.han.oose.buizerd%3Aprojectcheck-backend&metric=coverage)](https://sonarqube.aimsites.nl/dashboard?id=nl.han.oose.buizerd%3Aprojectcheck-backend)
[![Code Smells](https://sonarqube.aimsites.nl/api/project_badges/measure?project=nl.han.oose.buizerd%3Aprojectcheck-backend&metric=code_smells)](https://sonarqube.aimsites.nl/dashboard?id=nl.han.oose.buizerd%3Aprojectcheck-backend)
[![Bugs](https://sonarqube.aimsites.nl/api/project_badges/measure?project=nl.han.oose.buizerd%3Aprojectcheck-backend&metric=bugs)](https://sonarqube.aimsites.nl/dashboard?id=nl.han.oose.buizerd%3Aprojectcheck-backend)

## Setup
1. Download en installeer Docker: <https://www.docker.com/get-started>
2. Zorg dat de Docker service draaidend is
3. Navigeer naar de `docker` folder en maak de folder `secrets` aan
4. Maak in de `secrets` folder de bestanden `projectcheck_backend_password` en `sa_password` aan, met daarin unieke (sterke) wachtwoorden
5. Voer de onderstaande commando's uit in de `docker` folder:
    * Linux/macOS:
        ```
        keytool -genkeypair -alias localhost-rsa -keyalg RSA -dname CN=localhost -ext SAN=DNS:localhost,IP:127.0.0.1 -keystore backend/localhost-rsa.pfx -storepass changeit -storetype PKCS12
        keytool -exportcert -rfc -alias localhost-rsa -file backend/localhost-rsa.cert -keystore backend/localhost-rsa.pfx -storepass changeit
        ```

        **Let op:** als je gebruikt maakt van WSL2, voer dan de volgende commando's uit:
        ```
        keytool -genkeypair -alias localhost-rsa -keyalg RSA -dname CN="$(hostname).local" -ext SAN=DNS:"$(hostname).local,IP:$(grep -oP '(?<=nameserver )[\d.]+' /etc/resolv.conf)" -keystore backend/localhost-rsa.pfx -storepass changeit -storetype PKCS12
        keytool -exportcert -rfc -alias localhost-rsa -file backend/localhost-rsa.cert -keystore backend/localhost-rsa.pfx -storepass changeit
        ```
    * Windows:
        ```
        keytool -genkeypair -alias localhost-rsa -keyalg RSA -dname CN=localhost -ext SAN=DNS:localhost,IP:127.0.0.1 -keystore backend\localhost-rsa.pfx -storepass changeit -storetype PKCS12
        keytool -exportcert -rfc -alias localhost-rsa -file backend\localhost-rsa.cert -keystore backend/localhost-rsa.pfx -storepass changeit
        ```
    * Dit maakt een sleutel aan en een [self-signed certificate](https://en.wikipedia.org/wiki/Self-signed_certificate) genaamd `localhost-rsa.cert` in de `backend` folder
5. Voer uit in de `docker` folder: `docker-compose up -d`

### Opmerkingen
De applicatie is ontwikkelt om met iedere JDBC-Compliant Database te kunnen communiceren.
Het is dus mogelijk om bijvoorbeeld gebruik te maken van MySQL, indien dat gewenst is.

**In een productie-omgeving dient een certificaat van een Certificate Authority (CA) aangevraagd te worden.
Raadpleeg hiervoor de documentatie: <https://tomcat.apache.org/tomcat-10.0-doc/ssl-howto.html>.**
[Let's Encrypt](https://letsencrypt.org/getting-started/) wordt aangeraden.
Meer informatie hierover is te vinden op: <https://tomcat.apache.org/tomcat-9.0-doc/ssl-howto.html>.

Ook is het mogelijk, en zeer aangeraden, om in een productie-omgeving het wachtwoord (en wellicht ook de gebruiker) van
de database te veranderen. Raadpleeg hiervoor de relevante documentatie van de gebruikte database en pas zonodig de
`Dockerfile`'s in de `docker/` folder aan.

## Opdrachtgevers
- Antonie Reichling (<Antonie@reichling.nl>)
- Tineke Jacobs (<Tineke.Jacobs@han.nl>)

## Scrum team
- Daan van Vugt (<D.vanVugt@student.han.nl>)
- Daniël Vervloed (<DJ.Vervloed@student.han.nl>)
- Hugo Verweij (<HM.Verweij@student.han.nl>)
- Jens Broekens (<JA.Broekens@student.han.nl>)
- Luka van Ringelstein (<L.vanRingelestein@student.han.nl>)
- Nathan Wijnberg (<NAA.Wijnberg@student.han.nl>)

## Docenten
- Mark Achten (<Mark.Achten@han.nl>)
- Miranda Kampers (<Miranda.Kampers@han.nl>)
