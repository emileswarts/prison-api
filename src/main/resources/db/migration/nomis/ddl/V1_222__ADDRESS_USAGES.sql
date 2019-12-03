
  CREATE TABLE "ADDRESS_USAGES"
   (    "ADDRESS_ID" NUMBER(10,0) NOT NULL,
    "ADDRESS_USAGE" VARCHAR2(12 CHAR) NOT NULL,
    "ACTIVE_FLAG" VARCHAR2(1 CHAR) DEFAULT N NOT NULL,
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
     CONSTRAINT "ADDRESS_USAGES_PK" PRIMARY KEY ("ADDRESS_ID", "ADDRESS_USAGE"),
  );

  CREATE UNIQUE INDEX "ADDRESS_USAGES_PK" ON "ADDRESS_USAGES" ("ADDRESS_ID", "ADDRESS_USAGE");