
-- // Add Database snapshot isolation

-- Change the entire database to use READ_COMMITTED_SNAPSHOT isolation level to improve speed and reduce potential for deadlocks.
-- REQUIRED: set ${OAUTH_DATABASE_NAME} to the oauth database name.


ALTER DATABASE ${OAUTH_DATABASE_NAME} SET ALLOW_SNAPSHOT_ISOLATION ON;
ALTER DATABASE ${OAUTH_DATABASE_NAME} SET READ_COMMITTED_SNAPSHOT ON;


-- //@UNDO
ALTER DATABASE ${OAUTH_DATABASE_NAME} SET ALLOW_SNAPSHOT_ISOLATION OFF;
ALTER DATABASE ${OAUTH_DATABASE_NAME} SET READ_COMMITTED_SNAPSHOT OFF;
