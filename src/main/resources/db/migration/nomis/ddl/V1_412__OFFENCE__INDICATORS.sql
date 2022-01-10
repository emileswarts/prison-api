
CREATE TABLE "OFFENCE_INDICATORS" (
	"OFFENCE_INDICATOR_ID"          NUMBER(10,0)                NOT NULL,
	"OFFENCE_CODE"                  VARCHAR2(25)                NOT NULL,
	"STATUTE_CODE"                  VARCHAR2(12)                NOT NULL,
	"INDICATOR_CODE"                VARCHAR2(12),
	CONSTRAINT "OFFENCE_INDICATORS_PK" PRIMARY KEY ("OFFENCE_INDICATOR_ID"),
	CONSTRAINT OFF_IND_OFFENCE_FK1 FOREIGN KEY ("OFFENCE_CODE","STATUTE_CODE") REFERENCES OFFENCES("OFFENCE_CODE","STATUTE_CODE")
);
CREATE UNIQUE INDEX OFFENCE_INDICATORS_PK ON OFFENCE_INDICATORS (OFFENCE_INDICATOR_ID);
CREATE INDEX OFFENCE_INDICATOR_NI1 ON OFFENCE_INDICATORS (OFFENCE_CODE,STATUTE_CODE);