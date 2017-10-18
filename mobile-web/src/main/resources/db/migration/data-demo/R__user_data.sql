CREATE USER "officer1" PASSWORD 'password';
CREATE USER "officer2" PASSWORD 'password';
CREATE USER "adminstaff" PASSWORD 'password';

INSERT INTO OMS_ROLES (ROLE_ID, ROLE_NAME, ROLE_SEQ, ROLE_CODE, PARENT_ROLE_CODE) VALUES ( -1, 'Admin', 1, 'CENTRAL_ADMIN', null);
INSERT INTO OMS_ROLES (ROLE_ID, ROLE_NAME, ROLE_SEQ, ROLE_CODE, PARENT_ROLE_CODE) VALUES ( -2, 'General', 1, 'GEN', null);

INSERT INTO STAFF_USER_ACCOUNTS (USERNAME, STAFF_ID, STAFF_USER_TYPE, ID_SOURCE, WORKING_CASELOAD_ID) VALUES ('officer1', -1, 'GENERAL', 'USER', 'AG1');
INSERT INTO STAFF_USER_ACCOUNTS (USERNAME, STAFF_ID, STAFF_USER_TYPE, ID_SOURCE, WORKING_CASELOAD_ID) VALUES ('officer2', -2, 'GENERAL', 'USER', 'AG5');
INSERT INTO STAFF_USER_ACCOUNTS (USERNAME, STAFF_ID, STAFF_USER_TYPE, ID_SOURCE, WORKING_CASELOAD_ID) VALUES ('adminstaff',    -3, 'GENERAL', 'USER', 'AG2');

INSERT INTO STAFF_MEMBERS (STAFF_ID, ASSIGNED_CASELOAD_ID, WORKING_CASELOAD_ID, LAST_NAME, FIRST_NAME, MIDDLE_NAME) VALUES (-1, 'AG1', 'AG1', 'Roderick', 'Royce', 'Merle');
INSERT INTO STAFF_MEMBERS (STAFF_ID, ASSIGNED_CASELOAD_ID, WORKING_CASELOAD_ID, LAST_NAME, FIRST_NAME, MIDDLE_NAME) VALUES (-2, 'AG5', 'AG5', 'Asher', 'Woody', 'Rowland');
INSERT INTO STAFF_MEMBERS (STAFF_ID, ASSIGNED_CASELOAD_ID, WORKING_CASELOAD_ID, LAST_NAME, FIRST_NAME, MIDDLE_NAME) VALUES (-3, 'AG2', 'AG2', 'Eliott', 'Daniel', null);

INSERT INTO STAFF_MEMBER_ROLES (ROLE_ID, STAFF_ID, ROLE_CODE) VALUES (-2, -1, 'GEN');
INSERT INTO STAFF_MEMBER_ROLES (ROLE_ID, STAFF_ID, ROLE_CODE) VALUES (-2, -2, 'GEN');
INSERT INTO STAFF_MEMBER_ROLES (ROLE_ID, STAFF_ID, ROLE_CODE) VALUES (-2, -3, 'GEN');
INSERT INTO STAFF_MEMBER_ROLES (ROLE_ID, STAFF_ID, ROLE_CODE) VALUES (-1, -3, 'CENTRAL_ADMIN');

INSERT INTO INTERNET_ADDRESSES (INTERNET_ADDRESS_ID, OWNER_ID, OWNER_CLASS, INTERNET_ADDRESS_CLASS, INTERNET_ADDRESS) VALUES (-1, -1, 'STF', 'EMAIL', 'RRoderick@syscon.net');
INSERT INTO INTERNET_ADDRESSES (INTERNET_ADDRESS_ID, OWNER_ID, OWNER_CLASS, INTERNET_ADDRESS_CLASS, INTERNET_ADDRESS) VALUES (-2, -2, 'STF', 'EMAIL', 'WAsher@syscon.net');
INSERT INTO INTERNET_ADDRESSES (INTERNET_ADDRESS_ID, OWNER_ID, OWNER_CLASS, INTERNET_ADDRESS_CLASS, INTERNET_ADDRESS) VALUES (-3, -3, 'STF', 'EMAIL', 'DEliott@syscon.net');

INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-2, 'officer1', 'AG1');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-2, 'officer2', 'AG5');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-2, 'adminstaff', 'AG1');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-1, 'adminstaff', 'AG1');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-2, 'adminstaff', 'AG2');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-1, 'adminstaff', 'AG2');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-2, 'adminstaff', 'AG3');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-1, 'adminstaff', 'AG3');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-2, 'adminstaff', 'AG4');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-1, 'adminstaff', 'AG4');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-2, 'adminstaff', 'AG5');
INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, USERNAME, CASELOAD_ID) VALUES (-1, 'adminstaff', 'AG5');

