------------------------------------------------------------------------
-- MyBatis Migrations - script
------------------------------------------------------------------------
-- 20180928202646_add_pk_to_access_token_table.sql
-- @UNDO
-- SQL to undo the change goes here.

IF EXISTS(SELECT 1 FROM sys.objects WHERE type = 'PK' AND  parent_object_id = OBJECT_ID ('oauth_access_token'))
BEGIN
    ALTER TABLE oauth_access_token DROP CONSTRAINT PK_oauth_access_token
END


DELETE FROM CHANGELOG WHERE ID = 20180928202646;

------------------------------------------------------------------------
-- MyBatis Migrations SUCCESS
-- Total time: 0s
-- Finished at: Thu Oct 18 16:24:18 PDT 2018
-- Final Memory: 5M/479M
------------------------------------------------------------------------
