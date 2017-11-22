DROP USER IF EXISTS "ITAG_USER";
DROP USER IF EXISTS "ELITE2_API_USER";
DROP USER IF EXISTS "HPA_USER";
DROP USER IF EXISTS "API_TEST_USER";

CREATE USER "ITAG_USER" PASSWORD 'password';
CREATE USER "ELITE2_API_USER" PASSWORD 'password';
CREATE USER "HPA_USER" PASSWORD 'password';
CREATE USER "API_TEST_USER" PASSWORD 'password';