FROM maven:3.8.3-openjdk-11 AS build
COPY pom.xml ./
COPY src src/
RUN mvn -DskipTests clean package

FROM tomee:11-jre-9.0.0-M7-plus as tomee
COPY docker/backend/server.xml \
     docker/backend/tomee.xml \
     docker/backend/localhost-rsa.pfx \
     docker/backend/localhost-rsa.cert \
     /usr/local/tomee/conf/
RUN wget --content-disposition 'https://go.microsoft.com/fwlink/?linkid=2168494' \
    && tar -zxf sqljdbc_9.4.0.0_enu.tar.gz sqljdbc_9.4/enu/mssql-jdbc-9.4.0.jre11.jar \
    && mv sqljdbc_9.4/enu/mssql-jdbc-9.4.0.jre11.jar lib/ \
    && rm -rf sqljdbc_9.4 sqljdbc_9.4.0.0_enu.tar.gz
RUN rm -rf /usr/local/tomee/webapps/*
COPY --from=build target/projectcheck-backend-0.0.1.war /usr/local/tomee/webapps/ROOT.war
