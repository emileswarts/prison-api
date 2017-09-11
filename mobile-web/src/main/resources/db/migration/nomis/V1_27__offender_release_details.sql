CREATE TABLE OFFENDER_RELEASE_DETAILS
(
  OFFENDER_BOOK_ID              DECIMAL(10, 0)                 NOT NULL  PRIMARY KEY,
  RELEASE_DATE                  TIMESTAMP(7),
  COMMENT_TEXT                  VARCHAR(240),
  MOVEMENT_TYPE                 VARCHAR(12)                    NOT NULL,
  MOVEMENT_REASON_CODE          VARCHAR(12),
  EVENT_ID                      DECIMAL(10, 0),
  EVENT_STATUS                  VARCHAR(12) DEFAULT 'SCH'      NOT NULL,
  APPROVED_RELEASE_DATE         TIMESTAMP(7),
  AUTO_RELEASE_DATE             TIMESTAMP(7),
  DTO_APPROVED_DATE             TIMESTAMP(7),
  DTO_MID_TERM_DATE             TIMESTAMP(7),
  VERIFIED_FLAG                 VARCHAR(1) DEFAULT 'N',
  CREATE_DATETIME               TIMESTAMP(6) DEFAULT now()     NOT NULL,
  CREATE_USER_ID                VARCHAR(32) DEFAULT USER       NOT NULL,
  MODIFY_DATETIME               TIMESTAMP(6),
  MODIFY_USER_ID                VARCHAR(32),
  AUDIT_TIMESTAMP               TIMESTAMP(6),
  AUDIT_USER_ID                 VARCHAR(32),
  AUDIT_MODULE_NAME             VARCHAR(65),
  AUDIT_CLIENT_USER_ID          VARCHAR(64),
  AUDIT_CLIENT_IP_ADDRESS       VARCHAR(39),
  AUDIT_CLIENT_WORKSTATION_NAME VARCHAR(64),
  AUDIT_ADDITIONAL_INFO         VARCHAR(256)
);

CREATE UNIQUE INDEX OFFENDER_RELEASE_DETAILS_X01
  ON OFFENDER_RELEASE_DETAILS (OFFENDER_BOOK_ID, RELEASE_DATE, AUTO_RELEASE_DATE);

CREATE UNIQUE INDEX OFFENDER_RELEASE_DETAILS_NI1
  ON OFFENDER_RELEASE_DETAILS (RELEASE_DATE);

CREATE UNIQUE INDEX OFF_RELDE_MOV_RSN_FK1
  ON OFFENDER_RELEASE_DETAILS (MOVEMENT_TYPE, MOVEMENT_REASON_CODE, RELEASE_DATE);

CREATE UNIQUE INDEX OFFENDER_RELEASE_DETAILS_UK
  ON OFFENDER_RELEASE_DETAILS (EVENT_ID);

CREATE UNIQUE INDEX OFFENDER_RELEASE_DETAILS_NI2
  ON OFFENDER_RELEASE_DETAILS (APPROVED_RELEASE_DATE);

CREATE UNIQUE INDEX OFFENDER_RELEASE_DETAILS_NI3
  ON OFFENDER_RELEASE_DETAILS (AUTO_RELEASE_DATE);

CREATE UNIQUE INDEX OFFENDER_RELEASE_DETAILS_NI4
  ON OFFENDER_RELEASE_DETAILS (DTO_APPROVED_DATE);

CREATE UNIQUE INDEX OFFENDER_RELEASE_DETAILS_NI5
  ON OFFENDER_RELEASE_DETAILS (DTO_MID_TERM_DATE);

