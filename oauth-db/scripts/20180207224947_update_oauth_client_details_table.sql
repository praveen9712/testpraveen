
-- // update oauth client details table
-- Migration SQL that makes the change goes here.
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

ALTER TABLE     oauth_client_details 
ALTER COLUMN    autoapprove NVARCHAR(1000);


-- //@UNDO
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
