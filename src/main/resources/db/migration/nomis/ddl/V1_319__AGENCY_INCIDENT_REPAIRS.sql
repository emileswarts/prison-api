CREATE TABLE AGENCY_INCIDENT_REPAIRS (
    AGENCY_INCIDENT_ID NUMBER(10,0) NOT NULL,
    REPAIR_SEQ NUMBER(6,0) DEFAULT 1 NOT NULL,
    REPAIR_TYPE VARCHAR2(12) NOT NULL,
    COMMENT_TEXT VARCHAR2(4000),
    MODIFY_USER_ID VARCHAR2(32),
    MODIFY_DATETIME TIMESTAMP,
    REPAIR_COST NUMBER(12,2),
    CREATE_DATETIME TIMESTAMP DEFAULT systimestamp NOT NULL,
    CREATE_USER_ID VARCHAR2(32) DEFAULT USER NOT NULL,
    AUDIT_TIMESTAMP TIMESTAMP,
    AUDIT_USER_ID VARCHAR2(32),
    AUDIT_MODULE_NAME VARCHAR2(65),
    AUDIT_CLIENT_USER_ID VARCHAR2(64),
    AUDIT_CLIENT_IP_ADDRESS VARCHAR2(39),
    AUDIT_CLIENT_WORKSTATION_NAME VARCHAR2(64),
    AUDIT_ADDITIONAL_INFO VARCHAR2(256),
    CONSTRAINT AGENCY_INCIDENT_REPAIRS_PK PRIMARY KEY (AGENCY_INCIDENT_ID,REPAIR_SEQ),
    CONSTRAINT AGY_INC_RPR_AGY_INC_FK FOREIGN KEY (AGENCY_INCIDENT_ID) REFERENCES AGENCY_INCIDENTS(AGENCY_INCIDENT_ID)
);
CREATE UNIQUE INDEX AGENCY_INCIDENT_REPAIRS_PK ON AGENCY_INCIDENT_REPAIRS (AGENCY_INCIDENT_ID,REPAIR_SEQ);