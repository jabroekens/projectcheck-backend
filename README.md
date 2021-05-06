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
1. Download en installeer [SQL Server 2019 Express](https://go.microsoft.com/fwlink/?linkid=866658)
2. Download en installeer [Apache TomEE 8.0.6 Plus](https://www.apache.org/dyn/closer.cgi/tomee/tomee-8.0.6/apache-tomee-8.0.6-plus.zip)
3. Download [Microsoft JDBC Driver 9.2 for SQL Server](https://go.microsoft.com/fwlink/?linkid=2155948)
    1. Pak het ZIP-bestand uit
    2. Navigeer naar `sqljdbc_9.2\enu`
    3. Kopieer `mssql-jdbc-9.2.1.jre11.jar` naar de `lib` folder van de TomEE-installatie
4. Open het bestand `tomee.xml` in de `conf` folder van de TomEE-installatie
    1. Zorgt dat de volgende tekst tussen `<tomee>` en `</tomee>` staat:
        ```
        <Resource id="ProjectCheckDb" type="DataSource">
            JdbcDriver = com.microsoft.sqlserver.jdbc.SQLServerDriver
            JdbcUrl = jdbc:sqlserver://localhost:1433;databaseName=ProjectCheck
            UserName = backend
            Password = 3jsdolD$aev9%xzAbRnA4FuBb
        </Resource>
        <Resource id="ProjectCheckDbUnmanaged" type="DataSource">
            JdbcDriver = com.microsoft.sqlserver.jdbc.SQLServerDriver
            JdbcUrl = jdbc:sqlserver://localhost:1433;databaseName=ProjectCheck
            UserName = backend
            Password = 3jsdolD$aev9%xzAbRnA4FuBb
            JtaManaged = false
        </Resource>
        ```
    2. Sla het geopende bestand op
5. Voer de volgende SQL-code voor SQL Server uit:
    ```
    USE master;
    GO

    CREATE LOGIN backend WITH PASSWORD = '3jsdolD$aev9%xzAbRnA4FuBb';
    CREATE DATABASE ProjectCheck;
    GO

    USE ProjectCheck;
    GO

    CREATE USER backend FROM LOGIN backend;
    ALTER ROLE db_owner ADD MEMBER backend;
    GO
    ```

### Opmerkingen
De applicatie is ontwikkelt om met iedere JDBC-Compliant Database te kunnen communiceren.
Het is dus mogelijk om bijvoorbeeld gebruik te maken van MySQL, indien dat gewenst is.

Ook is het mogelijk, en zeer aangeraden, om in een productie-omgeving het wachtwoord (en wellicht
ook de gebruiker) te veranderen. Het is mogelijk om achteraf een nieuwe gebruiker toe te voegen,
of de in de bovenstaande instructies aangegeven gebruikersgegevens te wijzigen. Raadpleeg
hiervoor de relevante documentatie van de gebruikte database.

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
