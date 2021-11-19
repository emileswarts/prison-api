CREATE TABLE AGENCY_VISIT_SLOTS
(
    AGENCY_VISIT_SLOT_ID NUMBER(10, 0) NOT NULL
    , AGY_LOC_ID VARCHAR2(6 CHAR) NOT NULL
    , WEEK_DAY VARCHAR2(3 CHAR) NOT NULL
    , TIME_SLOT_SEQ NUMBER(6, 0) NOT NULL
    , INTERNAL_LOCATION_ID NUMBER(10, 0) NOT NULL
    , MAX_GROUPS NUMBER(6, 0)
    , MAX_ADULTS NUMBER(6, 0)
    , CREATE_DATETIME TIMESTAMP(9) DEFAULT systimestamp NOT NULL
    , CREATE_USER_ID VARCHAR2(32 CHAR) DEFAULT USER NOT NULL
    , MODIFY_DATETIME TIMESTAMP(9)
    , MODIFY_USER_ID VARCHAR2(32 CHAR)
    , AUDIT_TIMESTAMP TIMESTAMP(9)
    , AUDIT_USER_ID VARCHAR2(32 CHAR)
    , AUDIT_MODULE_NAME VARCHAR2(65 CHAR)
    , AUDIT_CLIENT_USER_ID VARCHAR2(64 CHAR)
    , AUDIT_CLIENT_IP_ADDRESS VARCHAR2(39 CHAR)
    , AUDIT_CLIENT_WORKSTATION_NAME VARCHAR2(64 CHAR)
    , AUDIT_ADDITIONAL_INFO VARCHAR2(256 CHAR)
    , CONSTRAINT AGENCY_VISIT_SLOTS_PK PRIMARY KEY (AGENCY_VISIT_SLOT_ID),
    CONSTRAINT AGY_VIS_SLOT_AGY_INT_LOC_FK FOREIGN KEY (INTERNAL_LOCATION_ID) REFERENCES AGENCY_INTERNAL_LOCATIONS(INTERNAL_LOCATION_ID),
    CONSTRAINT AGY_VIS_SLOT_AGY_VIS_DT_FK FOREIGN KEY(AGY_LOC_ID, WEEK_DAY, TIME_SLOT_SEQ)REFERENCES AGENCY_VISIT_TIMES(AGY_LOC_ID, WEEK_DAY, TIME_SLOT_SEQ),
    CONSTRAINT AGENCY_VISIT_SLOTS_UK UNIQUE(WEEK_DAY, TIME_SLOT_SEQ, INTERNAL_LOCATION_ID)
);
CREATE INDEX AGENCY_VISIT_SLOTS_NI1 ON AGENCY_VISIT_SLOTS (INTERNAL_LOCATION_ID ASC);
CREATE UNIQUE INDEX AGENCY_VISIT_SLOTS_PK ON AGENCY_VISIT_SLOTS (AGENCY_VISIT_SLOT_ID ASC);
CREATE UNIQUE INDEX AGENCY_VISIT_SLOTS_UK ON AGENCY_VISIT_SLOTS (WEEK_DAY ASC, TIME_SLOT_SEQ ASC, INTERNAL_LOCATION_ID ASC);
CREATE INDEX AGY_VIS_SLOT_AGY_VIS_DT_FK ON AGENCY_VISIT_SLOTS (AGY_LOC_ID ASC, WEEK_DAY ASC, TIME_SLOT_SEQ ASC);

