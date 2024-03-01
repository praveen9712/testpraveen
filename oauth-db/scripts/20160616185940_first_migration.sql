-- // First migration.
-- Migration SQL that makes the change goes here.
CREATE TABLE oauth_client_details (
	client_id NVARCHAR(256) NOT NULL
	,resource_ids NVARCHAR(256)
	,client_secret NVARCHAR(256)
	,scope NVARCHAR(256)
	,authorized_grant_types NVARCHAR(256)
	,web_server_redirect_uri NVARCHAR(256)
	,authorities NVARCHAR(256)
	,access_token_validity INTEGER
	,refresh_token_validity INTEGER
	,additional_information NVARCHAR(4000)
	,autoapprove NVARCHAR(256)
	,display_name NVARCHAR(256)
	,access_token_validity_override INTEGER
	,refresh_token_validity_override INTEGER
);

CREATE TABLE oauth_client_token (
	token_id NVARCHAR(256)
	,token VARBINARY(max)
	,authentication_id NVARCHAR(256) NOT NULL
	,user_name NVARCHAR(256)
	,client_id NVARCHAR(256)
);

CREATE TABLE oauth_access_token (
	token_id NVARCHAR(256)
	,token VARBINARY(max)
	,authentication_id NVARCHAR(256) NOT NULL
	,user_name NVARCHAR(256)
	,client_id NVARCHAR(256)
	,authentication VARBINARY(max)
	,refresh_token NVARCHAR(256)
);

CREATE TABLE oauth_refresh_token (
	token_id NVARCHAR(256)
	,token VARBINARY(max)
	,authentication VARBINARY(max)
);

CREATE TABLE oauth_code (
	code VARCHAR(256)
	,authentication VARBINARY(max)
);

CREATE TABLE oauth_approvals (
	userId NVARCHAR(256)
	,clientId NVARCHAR(256)
	,scope NVARCHAR(256)
	,STATUS NVARCHAR(10)
	,expiresAt DATETIME
	,lastModifiedAt DATETIME
);

CREATE TABLE oauth_scopes (
	scope_id NVARCHAR(256) NOT NULL
	,scope_name NVARCHAR(256)
	,scope_summary NVARCHAR(256)
);

-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE oauth_scopes;
DROP TABLE oauth_approvals;
DROP TABLE oauth_code;
DROP TABLE oauth_refresh_token;
DROP TABLE oauth_access_token;
DROP TABLE oauth_client_token;
DROP TABLE oauth_client_details;
