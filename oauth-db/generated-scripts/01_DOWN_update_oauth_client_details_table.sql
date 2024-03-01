------------------------------------------------------------------------
-- MyBatis Migrations - script
------------------------------------------------------------------------
-- 20180207224947_update_oauth_client_details_table.sql
-- @UNDO
-- SQL to undo the change goes here.
ALTER TABLE     oauth_client_details
ALTER COLUMN    resource_ids NVARCHAR(256);

ALTER TABLE     oauth_client_details
ALTER COLUMN    scope NVARCHAR(256);

ALTER TABLE     oauth_client_details
ALTER COLUMN    authorized_grant_types NVARCHAR(256);

ALTER TABLE     oauth_client_details
ALTER COLUMN    web_server_redirect_uri NVARCHAR(256);

ALTER TABLE     oauth_client_details
ALTER COLUMN    authorities NVARCHAR(256);

ALTER TABLE     oauth_client_details
ALTER COLUMN    autoapprove NVARCHAR(256);

DELETE FROM CHANGELOG WHERE ID = 20180207224947;

------------------------------------------------------------------------
-- MyBatis Migrations SUCCESS
-- Total time: 0s
-- Finished at: Tue Apr 17 12:07:34 PDT 2018
-- Final Memory: 5M/479M
------------------------------------------------------------------------