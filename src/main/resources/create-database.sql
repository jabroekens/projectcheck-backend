USE master;
GO

DROP DATABASE ProjectCheck;
CREATE DATABASE ProjectCheck;
GO

USE ProjectCheck;
GO

CREATE USER backend FROM LOGIN backend;
ALTER ROLE db_owner ADD MEMBER backend;
GO
