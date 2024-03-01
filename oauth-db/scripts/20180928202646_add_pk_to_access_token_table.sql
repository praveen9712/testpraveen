
-- Add primary key to oauth_access_token if there is not one

IF NOT EXISTS(SELECT 1 FROM sys.objects WHERE type = 'PK' AND  parent_object_id = OBJECT_ID ('oauth_access_token'))
BEGIN
    ALTER TABLE oauth_access_token ADD CONSTRAINT PK_oauth_access_token PRIMARY KEY (authentication_id)
END


-- //@UNDO
-- SQL to undo the change goes here.

IF EXISTS(SELECT 1 FROM sys.objects WHERE type = 'PK' AND  parent_object_id = OBJECT_ID ('oauth_access_token'))
BEGIN
    ALTER TABLE oauth_access_token DROP CONSTRAINT PK_oauth_access_token
END

