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
1. Download en installeer Java 12 of hoger, met uitzondering van Java 16 of hoger: <https://adoptopenjdk.net/installation.html>
    * Zorg dat de omgevingsvariabel `JAVA_HOME` is ingesteld en dat `%JAVA_HOME%\bin` (Windows) of `$JAVA_HOME/bin` (Linux/macOS) toegevoegd zijn aan het systeempad
2. Download en installeer [Apache TomEE 9.0.0-M3 Plus](https://www.apache.org/dyn/closer.cgi/tomee/tomee-9.0.0-M3/apache-tomee-9.0.0-M3-plus.zip)
    * Zie: <https://tomee.apache.org/tomee-9.0/docs/installing-tomee.html>
    * Voeg `CATALINA_HOME` toe aan de omgevingsvariabelen, met als waarde de locatie van de Apache TomEE-installatiefolder
    * Voeg `$CATALINA_HOME/bin` (Linux/macOS) of `%CATALINA_HOME%\bin` (Windows) toe aan het systeempad
1. Download en installeer [SQL Server 2019 Express](https://www.microsoft.com/en-us/Download/details.aspx?id=101064)
3. Download [Microsoft JDBC Driver 9.2 for SQL Server](https://docs.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server?view=sql-server-ver15)
    1. Pak het ZIP-bestand uit
    2. Navigeer naar `sqljdbc_9.2/enu/`
    3. Kopieer `mssql-jdbc-9.2.1.jre11.jar` naar `%CATALINA_HOME%\lib\` (Windows) of `$CATALINA_HOME/lib/` (Linux/macOS)
4. Voer de onderstaande commando's uit:
    * Linux/macOS:
        ```
        keytool -genkeypair -alias localhost-rsa -keyalg RSA -dname CN=localhost -ext SAN=DNS:localhost,IP:127.0.0.1 -keystore "$CATALINA_HOME/conf/localhost-rsa.pfx" -storepass changeit -storetype PKCS12
        keytool -exportcert -rfc -alias localhost-rsa -file "$CATALINA_HOME/conf/localhost-rsa.cert" -keystore "$CATALINA_HOME/conf/localhost-rsa.pfx" -storepass changeit
        ```
    * Windows:
        ```
        keytool -genkeypair -alias localhost-rsa -keyalg RSA -dname CN=localhost -ext SAN=DNS:localhost,IP:127.0.0.1 -keystore "%CATALINA_HOME%\conf\localhost-rsa.pfx" -storepass changeit -storetype PKCS12
        keytool -exportcert -rfc -alias localhost-rsa -file "%CATALINA_HOME%\conf\localhost-rsa.cert" -keystore "%CATALINA_HOME%\conf\localhost-rsa.pfx" -storepass changeit
        ```
    * Dit maakt een sleutel aan en een [self-signed certificate](https://en.wikipedia.org/wiki/Self-signed_certificate) genaamd `localhost-rsa.cert` in `%CATALINA_HOME%\conf\` (Windows) of `$CATALINA_HOME/conf/` (Linux/macOS)
5. Open het bestand `%CATALINA_HOME%\conf\server.xml` (Windows) of `$CATALINA_HOME/conf/server.xml` (Linux/macOS)
    1. Vind de onderstaande tekst
        ```
        <!--
        <Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
                   maxThreads="150" SSLEnabled="true">
            <SSLHostConfig>
                <Certificate certificateKeystoreFile="conf/localhost-rsa.jks"
                             type="RSA" xpoweredBy="false" server="Apache TomEE" />
            </SSLHostConfig>
        </Connector>
        -->
        ```
    2. Verwijder de regels met `<!--` en `-->`
    3. Verander `conf/localhost-rsa.jks` in `certificateKeystoreFile="conf/localhost-rsa.jks"` naar `conf/localhost-rsa.pfx`
    2. Sla het geopende bestand op
6. Open het bestand `%CATALINA_HOME%\conf\tomee.xml` (Windows) of `$CATALINA_HOME/conf/tomee.xml` (Linux/macOS)
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
7. Voer de volgende SQL-code voor SQL Server uit:
    ```
    USE master;
    CREATE LOGIN backend WITH PASSWORD = '3jsdolD$aev9%xzAbRnA4FuBb';
    CREATE DATABASE ProjectCheck;
    GO

    USE ProjectCheck;
    CREATE USER backend FROM LOGIN backend;
    ALTER ROLE db_owner ADD MEMBER backend;
    GO
    ```
8. Stel de Server Authentication Mode voor SQL Server in op `SQL Server and Windows Authentication mode` door de stappen te volgen zoals hier beschreven:
   <https://docs.microsoft.com/en-us/sql/database-engine/configure-windows/change-server-authentication-mode?view=sql-server-ver15#change-authentication-mode-with-ssms>

### Opmerkingen
De applicatie is ontwikkelt om met iedere JDBC-Compliant Database te kunnen communiceren.
Het is dus mogelijk om bijvoorbeeld gebruik te maken van MySQL, indien dat gewenst is.

**In een productie-omgeving dient een certificaat van een Certificate Authority (CA) aangevraagd te worden.
Stap 4 en 5 zullen verschillen afhankelijk van het certificaat dat gebruikt wordt.**
[Let's Encrypt](https://letsencrypt.org/getting-started/) wordt aangeraden.
Meer informatie hierover is te vinden op: <https://tomcat.apache.org/tomcat-9.0-doc/ssl-howto.html>.

Ook is het mogelijk, en zeer aangeraden, om in een productie-omgeving het wachtwoord (en wellicht ook de gebruiker) van
de database te veranderen. Het is mogelijk om achteraf een nieuwe gebruiker toe te voegen, of de in de bovenstaande
instructies aangegeven gebruikersgegevens te wijzigen (ervanuitgaande dat SQL Server 2019 gebruikt wordt).
Raadpleeg hiervoor de relevante documentatie van de gebruikte database.

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
