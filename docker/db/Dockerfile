FROM mcr.microsoft.com/mssql/server:2019-latest
COPY entrypoint.sh setup.sql ./
ARG MSSQL_PID
ENV ACCEPT_EULA=Y MSSQL_PID=Express MSSQL_SA_PASSWORD_FILE=/run/secrets/sa_password
CMD ["./entrypoint.sh"]
