create table DB_PATCHES
(
  PROFILE_CODE     VARCHAR2(12)                       NOT NULL,
  PROFILE_VALUE    VARCHAR2(40)                       NOT NULL,
  CREATE_DATETIME  TIMESTAMP(9) default SYSTIMESTAMP  NOT NULL,
  CREATE_USER_ID   VARCHAR2(32)                       NOT NULL,
  MODIFY_DATETIME  TIMESTAMP(9) default SYSTIMESTAMP,
  MODIFY_USER_ID   VARCHAR2(32),
  SEAL_FLAG        VARCHAR2(1),
  CONSTRAINT DB_PATCHES_PK
    PRIMARY KEY (PROFILE_CODE, PROFILE_VALUE)
);

COMMENT ON COLUMN DB_PATCHES.CREATE_DATETIME IS 'The timestamp when the record is created';
COMMENT ON COLUMN DB_PATCHES.CREATE_USER_ID  IS 'The user who creates the record';
COMMENT ON COLUMN DB_PATCHES.MODIFY_DATETIME IS 'The timestamp when the record is modified ';
COMMENT ON COLUMN DB_PATCHES.MODIFY_USER_ID  IS 'The user who modifies the record';

