CREATE TABLE "COURSE_SCHEDULES"
(
  "CRS_SCH_ID"                    NUMBER(10, 0)                     NOT NULL ,
  "CRS_ACTY_ID"                   NUMBER(10, 0)                     NOT NULL ,
  "WEEKDAY"                       VARCHAR2(12 CHAR),
  "SCHEDULE_DATE"                 DATE                              NOT NULL ,
  "START_TIME"                    DATE                              NOT NULL ,
  "END_TIME"                      DATE,
  "SESSION_NO"                    NUMBER(6, 0),
  "DETAILS"                       VARCHAR2(40 CHAR),
  "CREATE_DATETIME"               TIMESTAMP(9) DEFAULT systimestamp NOT NULL ,
  "CREATE_USER_ID"                VARCHAR2(32 CHAR) DEFAULT USER    NOT NULL ,
  "MODIFY_DATETIME"               TIMESTAMP(9),
  "MODIFY_USER_ID"                VARCHAR2(32 CHAR),
  "AUDIT_TIMESTAMP"               TIMESTAMP(9),
  "AUDIT_USER_ID"                 VARCHAR2(32 CHAR),
  "AUDIT_MODULE_NAME"             VARCHAR2(65 CHAR),
  "AUDIT_CLIENT_USER_ID"          VARCHAR2(64 CHAR),
  "AUDIT_CLIENT_IP_ADDRESS"       VARCHAR2(39 CHAR),
  "AUDIT_CLIENT_WORKSTATION_NAME" VARCHAR2(64 CHAR),
  "AUDIT_ADDITIONAL_INFO"         VARCHAR2(256 CHAR),
  "SCHEDULE_STATUS"               VARCHAR2(12 CHAR) DEFAULT 'SCH',
  "CATCH_UP_CRS_SCH_ID"           NUMBER(10, 0),
  "VIDEO_REFERENCE_ID"            VARCHAR2(20 CHAR),
  "SLOT_CATEGORY_CODE"            VARCHAR2(12 CHAR),
  CONSTRAINT "COURSE_SCHEDULES_PK" PRIMARY KEY ("CRS_SCH_ID"),
  CONSTRAINT "CRS_SCH_CRS_SCH_FK"  FOREIGN KEY ("CATCH_UP_CRS_SCH_ID") REFERENCES "COURSE_SCHEDULES" ("CRS_SCH_ID") ON DELETE CASCADE ,
  CONSTRAINT "CSR_SCH_CRS_ACTY_FK" FOREIGN KEY ("CRS_ACTY_ID")         REFERENCES "COURSE_ACTIVITIES" ("CRS_ACTY_ID")
);


CREATE INDEX "COURSE_SCHEDULES_NI1"
  ON "COURSE_SCHEDULES" ("SCHEDULE_DATE");


CREATE INDEX "COURSE_SCHEDULES_NI2"
  ON "COURSE_SCHEDULES" ("CRS_ACTY_ID");


CREATE INDEX "COURSE_SCHEDULES_X01"
  ON "COURSE_SCHEDULES" ("CRS_ACTY_ID", "SCHEDULE_DATE");


CREATE INDEX "CRS_SCH_CRS_SCH_FK"
  ON "COURSE_SCHEDULES" ("CATCH_UP_CRS_SCH_ID");

