-------------------------------
-- OFFENDER_KEY_DATE_ADJUSTS --
-------------------------------

-- Single, active 'ADA' record - additional days added should be value of ADJUST_DAYS from this record
INSERT INTO OFFENDER_KEY_DATE_ADJUSTS (OFFENDER_KEY_DATE_ADJUST_ID, SENTENCE_ADJUST_CODE, ADJUST_DATE, ADJUST_DAYS, ADJUST_STATUS, OFFENDER_BOOK_ID, ACTIVE_FLAG)
  VALUES (-1, 'ADA', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 12, null, -1, 'Y');

-- Single, inactive 'ADA' record - additional days added should be zero because record is not active
INSERT INTO OFFENDER_KEY_DATE_ADJUSTS (OFFENDER_KEY_DATE_ADJUST_ID, SENTENCE_ADJUST_CODE, ADJUST_DATE, ADJUST_DAYS, ADJUST_STATUS, OFFENDER_BOOK_ID, ACTIVE_FLAG)
  VALUES (-2, 'ADA', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 15, null, -2, 'N');

-- Single, active 'UAL' record - additional days added should be zero because record is not an 'ADA' record
INSERT INTO OFFENDER_KEY_DATE_ADJUSTS (OFFENDER_KEY_DATE_ADJUST_ID, SENTENCE_ADJUST_CODE, ADJUST_DATE, ADJUST_DAYS, ADJUST_STATUS, OFFENDER_BOOK_ID, ACTIVE_FLAG)
  VALUES (-3, 'UAL', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 3, null, -3, 'Y');

-- Multiple, active records, one 'ADA' and other 'UAL' - additional days added should be value of ADJUST_DAYS from 'ADA' record only
INSERT INTO OFFENDER_KEY_DATE_ADJUSTS (OFFENDER_KEY_DATE_ADJUST_ID, SENTENCE_ADJUST_CODE, ADJUST_DATE, ADJUST_DAYS, ADJUST_STATUS, OFFENDER_BOOK_ID, ACTIVE_FLAG)
  VALUES (-4, 'ADA', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 5, null, -4, 'Y'),
         (-5, 'UAL', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 13, null, -4, 'Y');

-- Multiple, active 'ADA' records - additional days added should be sum of ADJUST_DAYS from all active 'ADA' records
INSERT INTO OFFENDER_KEY_DATE_ADJUSTS (OFFENDER_KEY_DATE_ADJUST_ID, SENTENCE_ADJUST_CODE, ADJUST_DATE, ADJUST_DAYS, ADJUST_STATUS, OFFENDER_BOOK_ID, ACTIVE_FLAG)
  VALUES (-6, 'ADA', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 6, null, -5, 'Y'),
         (-7, 'ADA', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 8, null, -5, 'Y');

-- Multiple records, some active and some inactive, some 'ADA' and some not - additional days added should be sum of ADJUST_DAYS from all active 'ADA' records
INSERT INTO OFFENDER_KEY_DATE_ADJUSTS (OFFENDER_KEY_DATE_ADJUST_ID, SENTENCE_ADJUST_CODE, ADJUST_DATE, ADJUST_DAYS, ADJUST_STATUS, OFFENDER_BOOK_ID, ACTIVE_FLAG)
  VALUES (-8, 'ADA', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 4, null, -6, 'Y'),
         (-9, 'ADA', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 9, null, -6, 'N'),
         (-10, 'ADA', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 13, null, -6, 'Y'),
         (-11, 'UAL', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 1, null, -6, 'N'),
         (-12, 'RX', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 2, null, -6, 'Y'),
         (-13, 'UAL', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 7, null, -6, 'Y');
