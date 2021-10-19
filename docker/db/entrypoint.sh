#!/bin/sh
/opt/mssql/bin/sqlservr &
p=$!

while sleep 5; do
    if /opt/mssql-tools/bin/sqlcmd -l 30 -S localhost -U sa -P "$(cat "$MSSQL_SA_PASSWORD_FILE")" -Q ''; then
        break
    fi
done

if ! test -e /var/opt/mssql/sqlsetup_complete; then
    if /opt/mssql-tools/bin/sqlcmd -l 30 -S localhost -U sa -P "$(cat "$MSSQL_SA_PASSWORD_FILE")" -v password="$(cat /run/secrets/projectcheck_backend_password)" -i setup.sql; then
        touch /var/opt/mssql/sqlsetup_complete
    fi
fi

trap "kill -15 $p" TERM
wait $p
exit 0
