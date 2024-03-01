-- // widen oauth columns to max size
-- Migration SQL that makes the change goes here.

ALTER TABLE     oauth_client_details
ALTER COLUMN    resource_ids NVARCHAR(MAX);

ALTER TABLE     oauth_client_details
ALTER COLUMN    scope NVARCHAR(MAX);

ALTER TABLE     oauth_client_details
ALTER COLUMN    authorized_grant_types NVARCHAR(MAX);

ALTER TABLE     oauth_client_details
ALTER COLUMN    web_server_redirect_uri NVARCHAR(MAX);

ALTER TABLE     oauth_client_details
ALTER COLUMN    authorities NVARCHAR(MAX);

-- //@UNDO
-- SQL to undo the change goes here.

-- clear to prevent truncation
DELETE FROM oauth_client_details;

ALTER TABLE     oauth_client_details
ALTER COLUMN    resource_ids NVARCHAR(1000);

ALTER TABLE     oauth_client_details
ALTER COLUMN    scope NVARCHAR(1000);

ALTER TABLE     oauth_client_details
ALTER COLUMN    authorized_grant_types NVARCHAR(1000);

ALTER TABLE     oauth_client_details
ALTER COLUMN    web_server_redirect_uri NVARCHAR(1000);

ALTER TABLE     oauth_client_details
ALTER COLUMN    authorities NVARCHAR(1000);
