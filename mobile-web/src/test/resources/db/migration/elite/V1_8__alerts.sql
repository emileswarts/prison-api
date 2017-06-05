CREATE TABLE OFFENDER_ALERTS
(
  "ALERT_DATE"                    DATE              DEFAULT SYSDATE,
  "OFFENDER_BOOK_ID"              NUMBER(10, 0),
  "ROOT_OFFENDER_ID"              NUMBER(10, 0),
  "ALERT_SEQ"                     NUMBER(6, 0),
  "ALERT_TYPE"                    VARCHAR2(12 CHAR),
  "ALERT_CODE"                    VARCHAR2(12 CHAR),
  "AUTHORIZE_PERSON_TEXT"         VARCHAR2(40 CHAR),
  "CREATE_DATE"                   DATE,
  "ALERT_STATUS"                  VARCHAR2(12 CHAR),
  "VERIFIED_FLAG"                 VARCHAR2(1 CHAR)  DEFAULT 'N',
  "EXPIRY_DATE"                   DATE,
  "COMMENT_TEXT"                  VARCHAR2(1000 CHAR),
  "CASELOAD_ID"                   VARCHAR2(6 CHAR),
  "MODIFY_USER_ID"                VARCHAR2(32 CHAR),
  "MODIFY_DATETIME"               TIMESTAMP(9),
  "CASELOAD_TYPE"                 VARCHAR2(12 CHAR),
  "CREATE_DATETIME"               TIMESTAMP(9)      DEFAULT systimestamp,
  "CREATE_USER_ID"                VARCHAR2(32 CHAR) DEFAULT USER,
  "SEAL_FLAG"                     VARCHAR2(32 CHAR)
);
