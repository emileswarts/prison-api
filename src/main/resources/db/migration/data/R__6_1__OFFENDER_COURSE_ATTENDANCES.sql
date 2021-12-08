INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID) VALUES (-1,  -3, -2, -6, 'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-11', 'YYYY-MM-DD'), 'COMP', -2);
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID) VALUES (-2,  -3, -2, -7, 'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-12', 'YYYY-MM-DD'), 'EXP',  -2);
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID) VALUES (-3,  -3, -2, -8, 'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-13', 'YYYY-MM-DD'), 'CANC', -2);
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID) VALUES (-4,  -3, -2, -9, 'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-14', 'YYYY-MM-DD'), 'SCH',  -2);
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID) VALUES (-5,  -3, -2, -10,'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-15', 'YYYY-MM-DD'), 'SCH',  -2);
-- clashes with -2 and each other:
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID) VALUES (-6,  -3, -2, -11,'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-12', 'YYYY-MM-DD'), 'SCH',  -3);
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID) VALUES (-7,  -3, -2, -32,'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-12', 'YYYY-MM-DD'), 'SCH',  -3);

INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID)                VALUES (-11, -2, -2, -26,'INT_MOV', 'PRISON_ACT', 'EDUC', trunc(sysdate),                      'COMP', -5);
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID)                VALUES (-12, -2, -3, -27,'INT_MOV', 'PRISON_ACT', 'EDUC', trunc(sysdate),                      'EXP',  -5);
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID, EVENT_OUTCOME) VALUES (-13, -2, -1, -8, 'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-13', 'YYYY-MM-DD'), 'CANC', -5, 'UNACAB');
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID, EVENT_OUTCOME) VALUES (-14, -2, -2, -9, 'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-14', 'YYYY-MM-DD'), 'SCH',  -5, 'ACCABS');
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID)                VALUES (-15, -2, -2, -10,'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-15', 'YYYY-MM-DD'), 'SCH',  -5);
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID)                VALUES (-16, -2, -2, -6, 'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-11', 'YYYY-MM-DD'), 'SCH',  -5);
INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID)                VALUES (-17, -2, -2, -7, 'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-12', 'YYYY-MM-DD'), 'SCH',  -5);

INSERT INTO OFFENDER_COURSE_ATTENDANCES (EVENT_ID, OFFENDER_BOOK_ID, CRS_ACTY_ID, CRS_SCH_ID, EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_DATE, EVENT_STATUS, OFF_PRGREF_ID, COMMENT_TEXT)  VALUES (-18,  -1, -2, -6, 'INT_MOV', 'PRISON_ACT', 'EDUC', TO_DATE('2017-09-11', 'YYYY-MM-DD'), 'COMP', -2, 'Some Comment Text');
