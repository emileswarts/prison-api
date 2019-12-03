
  CREATE TABLE "OFFENDER_MINIMUM_BALANCES"
   (    "OFFENDER_ID" NUMBER(10,0) NOT NULL,
    "MINIMUM_BALANCE" NUMBER(11,2) NOT NULL,
    "CREATE_DATETIME" TIMESTAMP (9) DEFAULT systimestamp NOT NULL,
    "CREATE_USER_ID" VARCHAR2(32 CHAR) DEFAULT USER NOT NULL,
    "MODIFY_DATETIME" TIMESTAMP (9),
    "MODIFY_USER_ID" VARCHAR2(32 CHAR),
    "AUDIT_TIMESTAMP" TIMESTAMP (9),
    "AUDIT_USER_ID" VARCHAR2(32 CHAR),
    "AUDIT_MODULE_NAME" VARCHAR2(65 CHAR),
    "AUDIT_CLIENT_USER_ID" VARCHAR2(64 CHAR),
    "AUDIT_CLIENT_IP_ADDRESS" VARCHAR2(39 CHAR),
    "AUDIT_CLIENT_WORKSTATION_NAME" VARCHAR2(64 CHAR),
    "AUDIT_ADDITIONAL_INFO" VARCHAR2(256 CHAR),
     CONSTRAINT "OFFENDER_MINIMUM_BALANCES_PK" PRIMARY KEY ("OFFENDER_ID"),
  );

  CREATE UNIQUE INDEX "OFFENDER_MINIMUM_BALANCES_PK" ON "OFFENDER_MINIMUM_BALANCES" ("OFFENDER_ID");