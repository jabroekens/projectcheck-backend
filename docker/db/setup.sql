USE master;
CREATE LOGIN projectcheck_backend WITH PASSWORD = '$(password)';
CREATE DATABASE ProjectCheck;
GO

USE ProjectCheck;
CREATE USER projectcheck_backend FROM LOGIN projectcheck_backend;
ALTER ROLE db_owner ADD MEMBER projectcheck_backend;
GO
