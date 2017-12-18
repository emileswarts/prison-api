CREATE TABLE OFFENDER_IDENTIFIERS
(
  OFFENDER_ID           NUMBER(10)                        NOT NULL,
  OFFENDER_ID_SEQ       VARCHAR2(12)                      NOT NULL,
  IDENTIFIER_TYPE       VARCHAR2(12)                      NOT NULL,
  IDENTIFIER            VARCHAR2(20)                      NOT NULL,
  ISSUED_AUTHORITY_TEXT VARCHAR2(240),
  ISSUED_DATE           DATE,
  ROOT_OFFENDER_ID      NUMBER(10),
  CASELOAD_TYPE         VARCHAR2(12),
  MODIFY_USER_ID        VARCHAR2(32),
  MODIFY_DATETIME       TIMESTAMP(9) DEFAULT SYSTIMESTAMP,
  VERIFIED_FLAG         VARCHAR2(1)  DEFAULT 'Y'
    CONSTRAINT OFFENDER_IDENTIFIERS_C1
    CHECK ( verified_flag IN ('Y', 'N')),
  CREATE_DATETIME       TIMESTAMP(9) DEFAULT SYSTIMESTAMP NOT NULL,
  CREATE_USER_ID        VARCHAR2(32) DEFAULT user         NOT NULL,
  SEAL_FLAG             VARCHAR2(1),
  CONSTRAINT OFFENDER_NAME_IDENTIFIERS_PK
  PRIMARY KEY (OFFENDER_ID, OFFENDER_ID_SEQ),
  CONSTRAINT OFFENDER_IDENTIFIERS_UK1
  UNIQUE (ROOT_OFFENDER_ID, IDENTIFIER_TYPE, IDENTIFIER)
);

CREATE INDEX OFFENDER_IDENTIFIERS_NI1
  ON OFFENDER_IDENTIFIERS (IDENTIFIER, IDENTIFIER_TYPE);

CREATE INDEX OFFENDER_IDENTIFIERS_IN2
  ON OFFENDER_IDENTIFIERS (ROOT_OFFENDER_ID);