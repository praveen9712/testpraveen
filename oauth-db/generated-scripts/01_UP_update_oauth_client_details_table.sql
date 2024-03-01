-- Mybatis CHANGELOG and versioning was not correctly used before this update script.  This will mock
-- the versioning of the already applied changes.

IF object_id('CHANGELOG') IS NULL
BEGIN

CREATE TABLE CHANGELOG (
ID NUMERIC(20,0) NOT NULL,
APPLIED_AT VARCHAR(25) NOT NULL,
DESCRIPTION VARCHAR(255) NOT NULL
);

ALTER TABLE CHANGELOG
ADD CONSTRAINT PK_CHANGELOG
PRIMARY KEY (id);

INSERT INTO CHANGELOG (ID, APPLIED_AT, DESCRIPTION) VALUES (20160616185939, '2018-04-17 11:36:44', 'create changelog');
INSERT INTO CHANGELOG (ID, APPLIED_AT, DESCRIPTION) VALUES (20160616185940, '2018-04-17 11:36:44', 'first migration');

END

-- 20180207224947_update_oauth_client_details_table.sql

--  update oauth client details table
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

INSERT INTO CHANGELOG (ID, APPLIED_AT, DESCRIPTION) VALUES (20180207224947, '2018-04-17 11:36:44', 'update oauth client details table');