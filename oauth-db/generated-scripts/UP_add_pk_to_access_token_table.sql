------------------------------------------------------------------------
-- MyBatis Migrations - script
------------------------------------------------------------------------
-- 20180928202646_add_pk_to_access_token_table.sql

-- Add primary key to oauth_access_token if there is not one

IF NOT EXISTS(SELECT 1 FROM sys.objects WHERE type = 'PK' AND  parent_object_id = OBJECT_ID ('oauth_access_token'))
BEGIN
    ALTER TABLE oauth_access_token ADD CONSTRAINT PK_oauth_access_token PRIMARY KEY (authentication_id)
END



INSERT INTO CHANGELOG (ID, APPLIED_AT, DESCRIPTION) VALUES (20180928202646, '2018-10-18 16:13:38', 'add pk to access token table');

------------------------------------------------------------------------
-- MyBatis Migrations SUCCESS
-- Total time: 0s
-- Finished at: Thu Oct 18 16:13:38 PDT 2018
-- Final Memory: 5M/479M
------------------------------------------------------------------------
