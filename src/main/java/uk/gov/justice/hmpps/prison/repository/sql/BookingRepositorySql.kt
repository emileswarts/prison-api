package uk.gov.justice.hmpps.prison.repository.sql

enum class BookingRepositorySql(val sql: String) {
    GET_BOOKING_SENTENCE_DETAIL("""
        SELECT OB.OFFENDER_BOOK_ID,
        (SELECT MIN(OST.START_DATE)
        FROM OFFENDER_SENTENCE_TERMS OST
        JOIN OFFENDER_SENTENCES OS ON OST.OFFENDER_BOOK_ID = OS.OFFENDER_BOOK_ID
        AND OST.SENTENCE_SEQ = OS.SENTENCE_SEQ
        WHERE OST.SENTENCE_TERM_CODE = 'IMP'
        AND OST.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
        AND OS.SENTENCE_STATUS = 'A'
        GROUP BY OST.OFFENDER_BOOK_ID) SENTENCE_START_DATE,
        SED SENTENCE_EXPIRY_DATE,
        LED LICENCE_EXPIRY_DATE,
        PED PAROLE_ELIGIBILITY_DATE,
        HDCED HOME_DET_CURF_ELIGIBILITY_DATE,
        HDCAD_OVERRIDED_DATE HOME_DET_CURF_ACTUAL_DATE,
        APD_OVERRIDED_DATE ACTUAL_PAROLE_DATE,
        ETD EARLY_TERM_DATE,
        MTD MID_TERM_DATE,
        LTD LATE_TERM_DATE,
        TARIFF_DATE,
        ARD_OVERRIDED_DATE,
        ARD_CALCULATED_DATE,
        CRD_OVERRIDED_DATE,
        CRD_CALCULATED_DATE,
        NPD_OVERRIDED_DATE,
        NPD_CALCULATED_DATE,
        PRRD_OVERRIDED_DATE,
        PRRD_CALCULATED_DATE,
        ROTL RELEASE_ON_TEMP_LICENCE_DATE,
        ERSED EARLY_REMOVAL_SCHEME_ELIG_DATE,
        TUSED TOPUP_SUPERVISION_EXPIRY_DATE,
        EFFECTIVE_SENTENCE_END_DATE,
        DPRRD_CALCULATED_DATE,
        DPRRD_OVERRIDED_DATE,
        TERSED TARIFF_ERS_SCHEME_ELIG_DATE,
        (SELECT SUM(ADJUST_DAYS)
        FROM OFFENDER_KEY_DATE_ADJUSTS OKDA
        WHERE OKDA.SENTENCE_ADJUST_CODE = 'ADA'
        AND OKDA.ACTIVE_FLAG = 'Y'
        AND OKDA.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
        GROUP BY OKDA.OFFENDER_BOOK_ID) ADDITIONAL_DAYS_AWARDED
        FROM
        (SELECT OSC.OFFENDER_BOOK_ID,
        CALCULATION_DATE,
        COALESCE(SED_OVERRIDED_DATE, SED_CALCULATED_DATE) SED,
        COALESCE(LED_OVERRIDED_DATE, LED_CALCULATED_DATE) LED,
        COALESCE(PED_OVERRIDED_DATE, PED_CALCULATED_DATE) PED,
        COALESCE(HDCED_OVERRIDED_DATE, HDCED_CALCULATED_DATE) HDCED,
        COALESCE(TUSED_OVERRIDED_DATE, TUSED_CALCULATED_DATE) TUSED,
        COALESCE(ETD_OVERRIDED_DATE, ETD_CALCULATED_DATE) ETD,
        COALESCE(MTD_OVERRIDED_DATE, MTD_CALCULATED_DATE) MTD,
        COALESCE(LTD_OVERRIDED_DATE, LTD_CALCULATED_DATE) LTD,
        COALESCE(TARIFF_OVERRIDED_DATE, TARIFF_CALCULATED_DATE) TARIFF_DATE,
        HDCAD_OVERRIDED_DATE,
        APD_OVERRIDED_DATE,
        ARD_OVERRIDED_DATE,
        ARD_CALCULATED_DATE,
        CRD_OVERRIDED_DATE,
        CRD_CALCULATED_DATE,
        NPD_OVERRIDED_DATE,
        NPD_CALCULATED_DATE,
        PRRD_OVERRIDED_DATE,
        PRRD_CALCULATED_DATE,
        ROTL_OVERRIDED_DATE ROTL,
        ERSED_OVERRIDED_DATE ERSED,
        EFFECTIVE_SENTENCE_END_DATE,
        DPRRD_CALCULATED_DATE,
        DPRRD_OVERRIDED_DATE,
        TERSED_OVERRIDED_DATE TERSED
        FROM OFFENDER_SENT_CALCULATIONS OSC
        INNER JOIN (SELECT OFFENDER_BOOK_ID, MAX(OFFENDER_SENT_CALCULATION_ID) MAX_OSC_ID
        FROM OFFENDER_SENT_CALCULATIONS
        GROUP BY OFFENDER_BOOK_ID) LATEST_OSC
        ON OSC.OFFENDER_BOOK_ID = LATEST_OSC.OFFENDER_BOOK_ID
        AND OSC.OFFENDER_SENT_CALCULATION_ID = LATEST_OSC.MAX_OSC_ID) CALC_DATES
        RIGHT JOIN OFFENDER_BOOKINGS OB ON CALC_DATES.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
        WHERE OB.OFFENDER_BOOK_ID = :bookingId
    """),

    GET_OFFENDER_SENTENCE_DETAIL("""
        SELECT OB.OFFENDER_BOOK_ID,
        O.OFFENDER_ID_DISPLAY                          OFFENDER_NO,
        O.FIRST_NAME,
        O.LAST_NAME,
        O.BIRTH_DATE                                   DATE_OF_BIRTH,
        OB.agy_loc_id                                  agency_location_id,
        AL.description                                 agency_location_desc,
        AIL.DESCRIPTION                                internal_location_desc,
        (
                SELECT MAX(OI.OFFENDER_IMAGE_ID)
                        FROM OFFENDER_IMAGES OI
                        WHERE OI.ACTIVE_FLAG = 'Y'
        AND IMAGE_OBJECT_TYPE = 'OFF_BKG'
        AND OI.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
                AND OI.IMAGE_VIEW_TYPE = 'FACE'
        AND OI.ORIENTATION_TYPE = 'FRONT'
        )                                                 AS FACIAL_IMAGE_ID,
        COALESCE(ord.release_date, ord.auto_release_date) AS CONFIRMED_RELEASE_DATE,
        (  SELECT MIN(OST.START_DATE)
                FROM OFFENDER_SENTENCE_TERMS OST
                JOIN OFFENDER_SENTENCES OS ON OST.OFFENDER_BOOK_ID = OS.OFFENDER_BOOK_ID
                AND OST.SENTENCE_SEQ = OS.SENTENCE_SEQ
                WHERE OST.SENTENCE_TERM_CODE = 'IMP'
                AND OST.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
                AND OS.SENTENCE_STATUS = 'A'
                GROUP BY OST.OFFENDER_BOOK_ID)                 AS SENTENCE_START_DATE,
        SED SENTENCE_EXPIRY_DATE,
        LED LICENCE_EXPIRY_DATE,
        PED PAROLE_ELIGIBILITY_DATE,
        HDCED HOME_DET_CURF_ELIGIBILITY_DATE,
        HDCAD_OVERRIDED_DATE HOME_DET_CURF_ACTUAL_DATE,
        APD_OVERRIDED_DATE ACTUAL_PAROLE_DATE,
        ETD EARLY_TERM_DATE,
        MTD MID_TERM_DATE,
        LTD LATE_TERM_DATE,
        TARIFF_DATE,
        ARD_OVERRIDED_DATE,
        ARD_CALCULATED_DATE,
        CRD_OVERRIDED_DATE,
        CRD_CALCULATED_DATE,
        NPD_OVERRIDED_DATE,
        NPD_CALCULATED_DATE,
        PRRD_OVERRIDED_DATE,
        PRRD_CALCULATED_DATE,
        ROTL RELEASE_ON_TEMP_LICENCE_DATE,
        ERSED EARLY_REMOVAL_SCHEME_ELIG_DATE,
        TUSED TOPUP_SUPERVISION_EXPIRY_DATE,
        EFFECTIVE_SENTENCE_END_DATE,
        DPRRD_CALCULATED_DATE,
        DPRRD_OVERRIDED_DATE,
        TERSED TARIFF_ERS_SCHEME_ELIG_DATE,
        (  SELECT SUM(ADJUST_DAYS)
                FROM OFFENDER_KEY_DATE_ADJUSTS OKDA
                WHERE OKDA.SENTENCE_ADJUST_CODE = 'ADA'
        AND OKDA.ACTIVE_FLAG = 'Y'
        AND OKDA.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
                GROUP BY OKDA.OFFENDER_BOOK_ID)                AS ADDITIONAL_DAYS_AWARDED
        FROM
        (SELECT OSC.OFFENDER_BOOK_ID,
        CALCULATION_DATE,
        COALESCE(SED_OVERRIDED_DATE, SED_CALCULATED_DATE) SED,
        COALESCE(LED_OVERRIDED_DATE, LED_CALCULATED_DATE) LED,
        COALESCE(PED_OVERRIDED_DATE, PED_CALCULATED_DATE) PED,
        COALESCE(HDCED_OVERRIDED_DATE, HDCED_CALCULATED_DATE) HDCED,
        COALESCE(TUSED_OVERRIDED_DATE, TUSED_CALCULATED_DATE) TUSED,
        COALESCE(ETD_OVERRIDED_DATE, ETD_CALCULATED_DATE) ETD,
        COALESCE(MTD_OVERRIDED_DATE, MTD_CALCULATED_DATE) MTD,
        COALESCE(LTD_OVERRIDED_DATE, LTD_CALCULATED_DATE) LTD,
        COALESCE(TARIFF_OVERRIDED_DATE, TARIFF_CALCULATED_DATE) TARIFF_DATE,
        HDCAD_OVERRIDED_DATE,
        APD_OVERRIDED_DATE,
        ARD_OVERRIDED_DATE,
        ARD_CALCULATED_DATE,
        CRD_OVERRIDED_DATE,
        CRD_CALCULATED_DATE,
        NPD_OVERRIDED_DATE,
        NPD_CALCULATED_DATE,
        PRRD_OVERRIDED_DATE,
        PRRD_CALCULATED_DATE,
        ROTL_OVERRIDED_DATE ROTL,
        ERSED_OVERRIDED_DATE ERSED,
        EFFECTIVE_SENTENCE_END_DATE,
        DPRRD_CALCULATED_DATE,
        DPRRD_OVERRIDED_DATE,
        TERSED_OVERRIDED_DATE TERSED
                FROM OFFENDER_SENT_CALCULATIONS OSC
        INNER JOIN (
                SELECT OFFENDER_BOOK_ID, MAX(OFFENDER_SENT_CALCULATION_ID) MAX_OSC_ID
        FROM OFFENDER_SENT_CALCULATIONS
                GROUP BY OFFENDER_BOOK_ID
        ) LATEST_OSC ON OSC.OFFENDER_BOOK_ID = LATEST_OSC.OFFENDER_BOOK_ID
        AND OSC.OFFENDER_SENT_CALCULATION_ID = LATEST_OSC.MAX_OSC_ID
        ) CALC_DATES
        RIGHT JOIN OFFENDER_BOOKINGS OB ON CALC_DATES.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
                INNER JOIN OFFENDERS O ON OB.OFFENDER_ID = O.OFFENDER_ID
                LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON OB.LIVING_UNIT_ID = AIL.INTERNAL_LOCATION_ID
                LEFT JOIN AGENCY_LOCATIONS AL ON AL.AGY_LOC_ID = OB.AGY_LOC_ID
                LEFT JOIN OFFENDER_RELEASE_DETAILS ORD ON ORD.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
    """),

    GET_OFFENDER_SENT_CALCULATIONS("""
        SELECT OB.OFFENDER_BOOK_ID                                     BOOKING_ID,
        O.OFFENDER_ID_DISPLAY                                   OFFENDER_NO,
        O.FIRST_NAME,
        O.LAST_NAME,
        OB.AGY_LOC_ID                                           agency_location_id,
        OFFENDER_SENT_CALCULATION_ID,
        CALCULATION_DATE,
        COALESCE(SED_OVERRIDED_DATE, SED_CALCULATED_DATE)       SENTENCE_EXPIRY_DATE,
        COALESCE(LED_OVERRIDED_DATE, LED_CALCULATED_DATE)       LICENCE_EXPIRY_DATE,
        COALESCE(PED_OVERRIDED_DATE, PED_CALCULATED_DATE)       PAROLE_ELIGIBILITY_DATE,
        COALESCE(HDCED_OVERRIDED_DATE, HDCED_CALCULATED_DATE)   HOME_DET_CURF_ELIGIBILITY_DATE,
        COALESCE(HDCAD_OVERRIDED_DATE, HDCAD_CALCULATED_DATE)   HOME_DET_CURF_ACTUAL_DATE,
        COALESCE(ARD_OVERRIDED_DATE, ARD_CALCULATED_DATE)       AUTOMATIC_RELEASE_DATE,
        COALESCE(CRD_OVERRIDED_DATE, CRD_CALCULATED_DATE)       CONDITIONAL_RELEASE_DATE,
        COALESCE(NPD_OVERRIDED_DATE, NPD_CALCULATED_DATE)       NON_PAROLE_DATE,
        COALESCE(PRRD_OVERRIDED_DATE, PRRD_CALCULATED_DATE)     POST_RECALL_RELEASE_DATE,
        COALESCE(APD_OVERRIDED_DATE, APD_CALCULATED_DATE)       ACTUAL_PAROLE_DATE,
        COALESCE(TUSED_OVERRIDED_DATE, TUSED_CALCULATED_DATE)   TOPUP_SUPERVISION_EXPIRY_DATE,
        COALESCE(ETD_OVERRIDED_DATE, ETD_CALCULATED_DATE)       EARLY_TERM_DATE,
        COALESCE(MTD_OVERRIDED_DATE, MTD_CALCULATED_DATE)       MID_TERM_DATE,
        COALESCE(LTD_OVERRIDED_DATE, LTD_CALCULATED_DATE)       LATE_TERM_DATE,
        COALESCE(TARIFF_OVERRIDED_DATE, TARIFF_CALCULATED_DATE) TARIFF_DATE,
        ROTL_OVERRIDED_DATE                                     ROTL,
        ERSED_OVERRIDED_DATE                                    ERSED
                FROM OFFENDER_BOOKINGS OB
        INNER JOIN OFFENDER_SENT_CALCULATIONS OSC ON OSC.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
                INNER JOIN OFFENDERS O ON OB.OFFENDER_ID = O.OFFENDER_ID
                WHERE OB.AGY_LOC_ID IN (:agencyIds)
        AND OB.ACTIVE_FLAG = :activeFlag
        AND OB.BOOKING_SEQ = :bookingSeq
    """),

    GET_OFFENDER_SENTENCE_TERMS("""
        SELECT OS.OFFENDER_BOOK_ID       AS BOOKING_ID,
        OS.SENTENCE_SEQ           AS SENTENCE_SEQUENCE,
        OST.TERM_SEQ              AS TERM_SEQUENCE,
        OS.CONSEC_TO_SENTENCE_SEQ AS CONSECUTIVE_TO,
        OS.SENTENCE_CALC_TYPE     AS SENTENCE_TYPE,
        SCT.DESCRIPTION           AS SENTENCE_TYPE_DESCRIPTION,
        OST.START_DATE,
        OS.START_DATE             AS SENTENCE_START_DATE,
        OST.YEARS,
        OST.MONTHS,
        OST.WEEKS,
        OST.DAYS,
        OS.CASE_ID,
        OS.FINE_AMOUNT,
        OST.SENTENCE_TERM_CODE,
        OST.LIFE_SENTENCE_FLAG    AS LIFE_SENTENCE,
        OS.LINE_SEQ
        FROM OFFENDER_SENTENCE_TERMS OST
        JOIN OFFENDER_SENTENCES OS ON OST.OFFENDER_BOOK_ID = OS.OFFENDER_BOOK_ID
        AND OST.SENTENCE_SEQ = OS.SENTENCE_SEQ
                LEFT JOIN SENTENCE_CALC_TYPES SCT ON SCT.SENTENCE_CALC_TYPE = OS.SENTENCE_CALC_TYPE
                AND SCT.SENTENCE_CATEGORY = OS.SENTENCE_CATEGORY
                WHERE OST.SENTENCE_TERM_CODE in (:sentenceTermCodes)
        AND OST.OFFENDER_BOOK_ID = :bookingId
        AND OS.SENTENCE_STATUS = 'A'
    """),

    GET_BOOKING_ACTIVITIES("""
        SELECT OPP.OFFENDER_BOOK_ID BOOKING_ID,
        OCA.EVENT_ID,
        OCA.EVENT_OUTCOME,
        OCA.PERFORMANCE_CODE AS PERFORMANCE,
        OCA.COMMENT_TEXT AS OUTCOME_COMMENT,
        OCA.PAY_FLAG AS PAID,
        AIL.INTERNAL_LOCATION_CODE AS LOCATION_CODE,
        'INT_MOV' EVENT_CLASS,
        COALESCE(OCA.EVENT_STATUS, 'SCH') EVENT_STATUS,
        'PRISON_ACT' EVENT_TYPE,
        RD1.DESCRIPTION EVENT_TYPE_DESC,
        CA.COURSE_ACTIVITY_TYPE EVENT_SUB_TYPE,
        RD2.DESCRIPTION EVENT_SUB_TYPE_DESC,
        CS.SCHEDULE_DATE EVENT_DATE,
        CS.START_TIME,
        CS.END_TIME,
        COALESCE(AIL.USER_DESC, AIL.DESCRIPTION, AGY.DESCRIPTION, ADDR.PREMISE) EVENT_LOCATION,
        CASE WHEN COALESCE(AIL.USER_DESC, AIL.DESCRIPTION) IS NOT NULL THEN AIL.INTERNAL_LOCATION_ID ELSE NULL END AS EVENT_LOCATION_ID,
        'PA' EVENT_SOURCE,
        CA.CODE EVENT_SOURCE_CODE,
        CA.DESCRIPTION EVENT_SOURCE_DESC,
        (SELECT CASE WHEN CA.PAY_PER_SESSION = 'H' THEN CAPR.HALF_DAY_RATE ELSE CAPR.HALF_DAY_RATE * 2 END
        FROM OFFENDER_PRG_PRF_PAY_BANDS OPPPB,
        COURSE_ACTIVITY_PAY_RATES CAPR
                WHERE OCA.CRS_ACTY_ID = CAPR.CRS_ACTY_ID
                AND OPPPB.OFF_PRGREF_ID = OCA.OFF_PRGREF_ID
                AND CAPR.PAY_BAND_CODE = OPPPB.PAY_BAND_CODE
                AND (SELECT OIL.IEP_LEVEL
                        FROM OFFENDER_IEP_LEVELS OIL
                        WHERE OIL.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
                        AND OIL.IEP_LEVEL_SEQ = (SELECT MAX (IEP_LEVEL_SEQ)
                        FROM OFFENDER_IEP_LEVELS OIL2
                        WHERE OIL2.OFFENDER_BOOK_ID = OIL.OFFENDER_BOOK_ID
                        AND OIL2.IEP_DATE <= OCA.EVENT_DATE)) = CAPR.IEP_LEVEL
        AND OCA.EVENT_DATE BETWEEN CAPR.START_DATE  AND COALESCE (CAPR.END_DATE,  TO_DATE('31-12-2382','DD-MM-YYYY'))
        AND OCA.EVENT_DATE BETWEEN OPPPB.START_DATE AND COALESCE (OPPPB.END_DATE, TO_DATE('31-12-2382','DD-MM-YYYY'))
        AND ROWNUM = 1
        ) AS PAY_RATE

                FROM OFFENDER_PROGRAM_PROFILES OPP
        INNER JOIN OFFENDER_BOOKINGS OB ON OB.OFFENDER_BOOK_ID = OPP.OFFENDER_BOOK_ID AND OB.ACTIVE_FLAG = 'Y' AND OB.BOOKING_SEQ = 1
        INNER JOIN COURSE_ACTIVITIES CA ON CA.CRS_ACTY_ID = OPP.CRS_ACTY_ID
                INNER JOIN COURSE_SCHEDULES CS ON CA.CRS_ACTY_ID = CS.CRS_ACTY_ID
                AND CS.SCHEDULE_DATE >= TRUNC(OPP.OFFENDER_START_DATE)
        AND TRUNC(CS.SCHEDULE_DATE) <= COALESCE(OPP.OFFENDER_END_DATE, CA.SCHEDULE_END_DATE, CS.SCHEDULE_DATE)
        AND CS.SCHEDULE_DATE >= TRUNC(COALESCE(:fromDate, CS.SCHEDULE_DATE))
        AND TRUNC(CS.SCHEDULE_DATE) <= COALESCE(:toDate, CS.SCHEDULE_DATE)
        LEFT JOIN OFFENDER_COURSE_ATTENDANCES OCA ON OCA.OFFENDER_BOOK_ID = OPP.OFFENDER_BOOK_ID
                AND TRUNC(OCA.EVENT_DATE) = TRUNC(CS.SCHEDULE_DATE)
        AND OCA.CRS_SCH_ID = CS.CRS_SCH_ID
                LEFT JOIN REFERENCE_CODES RD1 ON RD1.CODE = 'PRISON_ACT' AND RD1.DOMAIN = 'INT_SCH_TYPE'
        LEFT JOIN REFERENCE_CODES RD2 ON RD2.CODE = CA.COURSE_ACTIVITY_TYPE AND RD2.DOMAIN = 'INT_SCH_RSN'
        LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON CA.INTERNAL_LOCATION_ID = AIL.INTERNAL_LOCATION_ID
                LEFT JOIN AGENCY_LOCATIONS AGY ON CA.AGY_LOC_ID = AGY.AGY_LOC_ID
                LEFT JOIN ADDRESSES ADDR ON CA.SERVICES_ADDRESS_ID = ADDR.ADDRESS_ID
                WHERE OPP.OFFENDER_PROGRAM_STATUS = 'ALLOC'
        AND COALESCE(OPP.SUSPENDED_FLAG, 'N') = 'N'
        AND CA.ACTIVE_FLAG = 'Y'
        AND CA.COURSE_ACTIVITY_TYPE IS NOT NULL
        AND CS.CATCH_UP_CRS_SCH_ID IS NULL
                AND (UPPER(TO_CHAR(CS.SCHEDULE_DATE, 'DY')), CS.SLOT_CATEGORY_CODE) NOT IN
        (SELECT OE.EXCLUDE_DAY, COALESCE(OE.SLOT_CATEGORY_CODE, CS.SLOT_CATEGORY_CODE)
        FROM OFFENDER_EXCLUDE_ACTS_SCHDS OE
        WHERE OE.OFF_PRGREF_ID = OPP.OFF_PRGREF_ID)
    """),

    GET_BOOKING_APPOINTMENTS("""
        SELECT OIS.OFFENDER_BOOK_ID BOOKING_ID,
        OIS.EVENT_CLASS,
        OIS.EVENT_STATUS,
        OIS.EVENT_TYPE,
        RC1.DESCRIPTION EVENT_TYPE_DESC,
        OIS.EVENT_SUB_TYPE,
        RC2.DESCRIPTION EVENT_SUB_TYPE_DESC,
        OIS.EVENT_DATE,
        OIS.START_TIME,
        OIS.END_TIME,
        COALESCE(AIL.USER_DESC, AIL.DESCRIPTION, AGY.DESCRIPTION, ADDR.PREMISE, RC3.DESCRIPTION) EVENT_LOCATION,
        'APP' EVENT_SOURCE,
        'APP' EVENT_SOURCE_CODE,
        OIS.COMMENT_TEXT EVENT_SOURCE_DESC
                FROM OFFENDER_IND_SCHEDULES OIS
        INNER JOIN OFFENDER_BOOKINGS OB ON OB.OFFENDER_BOOK_ID = OIS.OFFENDER_BOOK_ID AND OB.ACTIVE_FLAG = 'Y'
        LEFT JOIN REFERENCE_CODES RC1 ON RC1.CODE = OIS.EVENT_TYPE AND RC1.DOMAIN = 'INT_SCH_TYPE'
        LEFT JOIN REFERENCE_CODES RC2 ON RC2.CODE = OIS.EVENT_SUB_TYPE AND RC2.DOMAIN = 'INT_SCH_RSN'
        LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON OIS.TO_INTERNAL_LOCATION_ID = AIL.INTERNAL_LOCATION_ID
                LEFT JOIN AGENCY_LOCATIONS AGY ON OIS.TO_AGY_LOC_ID = AGY.AGY_LOC_ID
                LEFT JOIN ADDRESSES ADDR ON OIS.TO_ADDRESS_ID = ADDR.ADDRESS_ID
                LEFT JOIN REFERENCE_CODES RC3 ON RC3.CODE = OIS.TO_CITY_CODE AND RC3.DOMAIN = 'CITY'
        WHERE OIS.EVENT_TYPE = 'APP'
        AND OIS.EVENT_STATUS = 'SCH'
        AND OIS.EVENT_DATE >= TRUNC(COALESCE(:fromDate, OIS.EVENT_DATE))
        AND TRUNC(OIS.EVENT_DATE) <= COALESCE(:toDate, OIS.EVENT_DATE)
    """),


    UPDATE_ATTENDANCE("""
        UPDATE OFFENDER_COURSE_ATTENDANCES SET
        EVENT_OUTCOME = :eventOutcome,
        PERFORMANCE_CODE = :performanceCode,
        COMMENT_TEXT = :commentText,
        PAY_FLAG = :paid,
        AUTHORISED_ABSENCE_FLAG = :authorisedAbsence
                WHERE EVENT_ID = :eventId
        AND OFFENDER_BOOK_ID = :bookingId
    """),

    GET_ATTENDANCE_DATE("""
        SELECT EVENT_DATE
                FROM OFFENDER_COURSE_ATTENDANCES
                WHERE EVENT_ID = :eventId
    """),

    GET_PAYABLE_ATTENDANCE_OUTCOMES("""
        SELECT PAYABLE_ATTENDANCE_OUTCOMES_ID,
        EVENT_TYPE,
        OUTCOME_CODE,
        PAY_FLAG,
        AUTHORISED_ABSENCE_FLAG
        FROM PAYABLE_ATTENDANCE_OUTCOMES
                WHERE EVENT_TYPE=:eventType
        AND OUTCOME_CODE=:outcomeCode
        AND END_DATE IS NULL
                AND START_DATE <= SYSDATE
    """),

    GET_BOOKING_IEP_DETAILS_BY_IDS("""
        SELECT OIL.OFFENDER_BOOK_ID AS BOOKING_ID,
        IEP_DATE,
        IEP_TIME,
        OIL.AGY_LOC_ID AS AGENCY_ID,
        COALESCE(RC.DESCRIPTION, OIL.IEP_LEVEL) AS IEP_LEVEL,
        COMMENT_TEXT AS "COMMENTS",
        USER_ID
        FROM OFFENDER_IEP_LEVELS OIL
        INNER JOIN OFFENDER_BOOKINGS OB ON OIL.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
                LEFT JOIN REFERENCE_CODES RC ON RC.CODE = OIL.IEP_LEVEL AND RC.DOMAIN = 'IEP_LEVEL'
        WHERE OB.OFFENDER_BOOK_ID IN (:bookingIds)
        ORDER BY OB.OFFENDER_BOOK_ID, OIL.IEP_DATE DESC, OIL.IEP_LEVEL_SEQ DESC
    """),

    ADD_IEP_LEVEL("""
        INSERT INTO
                OFFENDER_IEP_LEVELS(
                        OFFENDER_BOOK_ID,
                        AGY_LOC_ID,
                        IEP_LEVEL_SEQ,
                        IEP_LEVEL,
                        COMMENT_TEXT,
                        IEP_DATE,
                        IEP_TIME,
                        USER_ID)
        VALUES (
                :bookingId,
                (select AGY_LOC_ID from OFFENDER_BOOKINGS WHERE OFFENDER_BOOK_ID = :bookingId),
        (select NVL( MAX(IEP_LEVEL_SEQ), 0) + 1 from OFFENDER_IEP_LEVELS where OFFENDER_BOOK_ID = :bookingId),
        :iepLevel,
        :comment,
        :date,
        :time,
        :userId
        )
    """),

    IEP_LEVELS_FOR_AGENCY_SELECTED_BY_BOOKING("""
        SELECT IEP_LEVEL
                FROM IEP_LEVELS IL
        JOIN OFFENDER_BOOKINGS OB ON OB.AGY_LOC_ID = IL.AGY_LOC_ID
        WHERE OB.OFFENDER_BOOK_ID = :bookingId
        AND IL.ACTIVE_FLAG = 'Y'
    """),

    CHECK_BOOKING_AGENCIES("""
        SELECT OFFENDER_BOOK_ID
                FROM OFFENDER_BOOKINGS
                WHERE OFFENDER_BOOK_ID = :bookingId
        AND AGY_LOC_ID IN (:agencyIds)
    """),

    GET_OFFENDER_BOOKING_AGENCY("""
        SELECT AGY_LOC_ID
                FROM OFFENDER_BOOKINGS
                WHERE OFFENDER_BOOK_ID = :bookingId
    """),

    GET_BOOKING_VISITS("""
        SELECT VIS.OFFENDER_BOOK_ID BOOKING_ID,
        'INT_MOV' EVENT_CLASS,
        vis.VISIT_STATUS EVENT_STATUS,
        'VISIT' EVENT_TYPE,
        RC1.DESCRIPTION EVENT_TYPE_DESC,
        'VISIT' EVENT_SUB_TYPE,
        RC2.DESCRIPTION EVENT_SUB_TYPE_DESC,
        VIS.VISIT_DATE EVENT_DATE,
        VIS.START_TIME,
        VIS.END_TIME,
        COALESCE(AIL.USER_DESC, AIL.DESCRIPTION, AGY.DESCRIPTION) EVENT_LOCATION,
        'VIS' EVENT_SOURCE,
        VIS.VISIT_TYPE EVENT_SOURCE_CODE,
        RC3.DESCRIPTION EVENT_SOURCE_DESC
                FROM OFFENDER_VISITS VIS
        INNER JOIN OFFENDER_BOOKINGS OB ON OB.OFFENDER_BOOK_ID = VIS.OFFENDER_BOOK_ID AND OB.ACTIVE_FLAG = 'Y'
        LEFT JOIN REFERENCE_CODES RC1 ON RC1.CODE = 'VISIT' AND RC1.DOMAIN = 'INT_SCH_TYPE'
        LEFT JOIN REFERENCE_CODES RC2 ON RC2.CODE = 'VISIT' AND RC2.DOMAIN = 'INT_SCH_RSN'
        LEFT JOIN REFERENCE_CODES RC3 ON RC3.CODE = VIS.VISIT_TYPE AND RC3.DOMAIN = 'VISIT_TYPE'
        LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON VIS.VISIT_INTERNAL_LOCATION_ID = AIL.INTERNAL_LOCATION_ID
                LEFT JOIN AGENCY_LOCATIONS AGY ON VIS.AGY_LOC_ID = AGY.AGY_LOC_ID
                WHERE VIS.VISIT_DATE >= TRUNC(COALESCE(:fromDate, VIS.VISIT_DATE))
        AND TRUNC(VIS.VISIT_DATE) <= COALESCE(:toDate, VIS.VISIT_DATE)
    """),

    GET_LAST_BOOKING_VISIT("""
        SELECT * FROM (SELECT
                VISITOR.OUTCOME_REASON_CODE CANCELLATION_REASON,
                RC5.DESCRIPTION CANCEL_REASON_DESCRIPTION,
                VISITOR.EVENT_STATUS,
                RC2.DESCRIPTION EVENT_STATUS_DESCRIPTION,
                NVL(VISITOR.EVENT_OUTCOME,'ATT') EVENT_OUTCOME,
                RC4.DESCRIPTION EVENT_OUTCOME_DESCRIPTION,
                VISIT.START_TIME,
                VISIT.END_TIME,
                COALESCE(AIL.USER_DESC, AIL.DESCRIPTION, AGY.DESCRIPTION) LOCATION,
                VISIT.VISIT_TYPE,
                RC3.DESCRIPTION VISIT_TYPE_DESCRIPTION,
                P.FIRST_NAME || ' ' || P.LAST_NAME LEAD_VISITOR,
                OCP.RELATIONSHIP_TYPE RELATIONSHIP,
                RC1.DESCRIPTION RELATIONSHIP_DESCRIPTION
                        FROM OFFENDER_VISITS VISIT
                        INNER JOIN OFFENDER_BOOKINGS BOOKING ON BOOKING.OFFENDER_BOOK_ID = VISIT.OFFENDER_BOOK_ID AND BOOKING.ACTIVE_FLAG = 'Y'
                        LEFT JOIN OFFENDER_VISIT_VISITORS VISITOR ON VISIT.OFFENDER_VISIT_ID = VISITOR.OFFENDER_VISIT_ID AND VISITOR.GROUP_LEADER_FLAG = 'Y' AND VISITOR.PERSON_ID IS NOT NULL
                        LEFT JOIN OFFENDER_CONTACT_PERSONS OCP ON VISITOR.PERSON_ID = OCP.PERSON_ID AND VISIT.OFFENDER_BOOK_ID = OCP.OFFENDER_BOOK_ID
                        LEFT JOIN REFERENCE_CODES RC1 ON RC1.DOMAIN = 'RELATIONSHIP' AND RC1.CODE = OCP.RELATIONSHIP_TYPE
                LEFT JOIN REFERENCE_CODES RC2 ON RC2.DOMAIN = 'EVENT_STS' AND RC2.CODE = VISITOR.EVENT_STATUS
                LEFT JOIN REFERENCE_CODES RC3 ON RC3.DOMAIN = 'VISIT_TYPE' AND RC3.CODE = VISIT.VISIT_TYPE
                LEFT JOIN REFERENCE_CODES RC4 ON RC4.DOMAIN = 'OUTCOMES' AND RC4.CODE = NVL(VISITOR.EVENT_OUTCOME,'ATT')
                LEFT JOIN REFERENCE_CODES RC5 ON RC5.DOMAIN = 'MOVE_CANC_RS' AND RC5.CODE = VISITOR.OUTCOME_REASON_CODE
                LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON VISIT.VISIT_INTERNAL_LOCATION_ID = AIL.INTERNAL_LOCATION_ID
                LEFT JOIN AGENCY_LOCATIONS AGY ON VISIT.AGY_LOC_ID = AGY.AGY_LOC_ID
                LEFT JOIN PERSONS P ON P.PERSON_ID = VISITOR.PERSON_ID
                WHERE VISIT.OFFENDER_BOOK_ID = :bookingId
                AND VISIT.START_TIME < :cutoffDate
                ORDER BY VISIT.START_TIME DESC, VISIT.OFFENDER_VISIT_ID DESC)
        WHERE ROWNUM = 1
    """),

    GET_NEXT_BOOKING_VISIT("""
        SELECT * FROM (SELECT
                VISITOR.EVENT_STATUS,
                RC2.DESCRIPTION EVENT_STATUS_DESCRIPTION,
                VISIT.START_TIME,
                VISIT.END_TIME,
                COALESCE(AIL.USER_DESC, AIL.DESCRIPTION, AGY.DESCRIPTION) LOCATION,
                VISIT.VISIT_TYPE,
                RC3.DESCRIPTION VISIT_TYPE_DESCRIPTION,
                P.FIRST_NAME || ' ' || P.LAST_NAME LEAD_VISITOR,
                OCP.RELATIONSHIP_TYPE RELATIONSHIP,
                RC1.DESCRIPTION RELATIONSHIP_DESCRIPTION
                        FROM OFFENDER_VISITS VISIT
                        INNER JOIN OFFENDER_BOOKINGS BOOKING ON BOOKING.OFFENDER_BOOK_ID = VISIT.OFFENDER_BOOK_ID AND BOOKING.ACTIVE_FLAG = 'Y'
                        LEFT JOIN OFFENDER_VISIT_VISITORS VISITOR ON VISIT.OFFENDER_VISIT_ID = VISITOR.OFFENDER_VISIT_ID AND VISITOR.GROUP_LEADER_FLAG = 'Y' AND VISITOR.PERSON_ID IS NOT NULL
                        LEFT JOIN OFFENDER_CONTACT_PERSONS OCP ON VISITOR.PERSON_ID = OCP.PERSON_ID AND VISIT.OFFENDER_BOOK_ID = OCP.OFFENDER_BOOK_ID
                        LEFT JOIN REFERENCE_CODES RC1 ON RC1.DOMAIN = 'RELATIONSHIP' AND RC1.CODE = OCP.RELATIONSHIP_TYPE
                LEFT JOIN REFERENCE_CODES RC2 ON RC2.DOMAIN = 'EVENT_STS' AND RC2.CODE = VISITOR.EVENT_STATUS
                LEFT JOIN REFERENCE_CODES RC3 ON RC3.DOMAIN = 'VISIT_TYPE' AND RC3.CODE = VISIT.VISIT_TYPE
                LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON VISIT.VISIT_INTERNAL_LOCATION_ID = AIL.INTERNAL_LOCATION_ID
                LEFT JOIN AGENCY_LOCATIONS AGY ON VISIT.AGY_LOC_ID = AGY.AGY_LOC_ID
                LEFT JOIN PERSONS P ON P.PERSON_ID = VISITOR.PERSON_ID
                WHERE VISIT.OFFENDER_BOOK_ID = :bookingId
                AND VISIT.START_TIME > :fromDate AND visit.VISIT_STATUS = 'SCH'
                ORDER BY VISIT.START_TIME ASC, VISIT.OFFENDER_VISIT_ID ASC)
        WHERE ROWNUM = 1
    """),

    GET_BOOKING_APPOINTMENT("""
        SELECT OIS.OFFENDER_BOOK_ID BOOKING_ID,
        OIS.EVENT_ID,
        OIS.EVENT_CLASS,
        OIS.EVENT_STATUS,
        OIS.EVENT_TYPE,
        RC1.DESCRIPTION EVENT_TYPE_DESC,
        OIS.EVENT_SUB_TYPE,
        RC2.DESCRIPTION EVENT_SUB_TYPE_DESC,
        OIS.EVENT_DATE,
        OIS.START_TIME,
        OIS.END_TIME,
        COALESCE(AIL.USER_DESC, AIL.DESCRIPTION, AGY.DESCRIPTION, ADDR.STREET, RC3.DESCRIPTION) EVENT_LOCATION,
        'APP' EVENT_SOURCE,
        'APP' EVENT_SOURCE_CODE,
        OIS.COMMENT_TEXT EVENT_SOURCE_DESC
                FROM OFFENDER_IND_SCHEDULES OIS
        LEFT JOIN REFERENCE_CODES RC1 ON RC1.CODE = OIS.EVENT_TYPE AND RC1.DOMAIN = 'INT_SCH_TYPE'
        LEFT JOIN REFERENCE_CODES RC2 ON RC2.CODE = OIS.EVENT_SUB_TYPE AND RC2.DOMAIN = 'INT_SCH_RSN'
        LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON OIS.TO_INTERNAL_LOCATION_ID = AIL.INTERNAL_LOCATION_ID
                LEFT JOIN AGENCY_LOCATIONS AGY ON OIS.TO_AGY_LOC_ID = AGY.AGY_LOC_ID
                LEFT JOIN ADDRESSES ADDR ON OIS.TO_ADDRESS_ID = ADDR.ADDRESS_ID
                LEFT JOIN REFERENCE_CODES RC3 ON RC3.CODE = OIS.TO_CITY_CODE AND RC3.DOMAIN = 'CITY'
        WHERE OIS.OFFENDER_BOOK_ID = :bookingId
        AND OIS.EVENT_ID = :eventId
        AND OIS.EVENT_TYPE = 'APP'
        AND OIS.EVENT_STATUS = 'SCH'
    """),

    GET_BOOKING_APPOINTMENT_BY_EVENT_ID("""
        SELECT OIS.OFFENDER_BOOK_ID BOOKING_ID,
               OIS.EVENT_ID,
               OIS.EVENT_CLASS,
               OIS.EVENT_STATUS,
               OIS.EVENT_TYPE,
               RC1.DESCRIPTION EVENT_TYPE_DESC,
               OIS.EVENT_SUB_TYPE,
               RC2.DESCRIPTION EVENT_SUB_TYPE_DESC,
               OIS.EVENT_DATE,
               OIS.START_TIME,
               OIS.END_TIME,
               OIS.TO_INTERNAL_LOCATION_ID EVENT_LOCATION_ID,
               COALESCE(AIL.USER_DESC, AIL.DESCRIPTION, AGY.DESCRIPTION) EVENT_LOCATION,
               'APP' EVENT_SOURCE,
               'APP' EVENT_SOURCE_CODE,
               OIS.COMMENT_TEXT EVENT_SOURCE_DESC
          FROM OFFENDER_IND_SCHEDULES OIS
               LEFT JOIN REFERENCE_CODES RC1 ON RC1.CODE = OIS.EVENT_TYPE AND RC1.DOMAIN = 'INT_SCH_TYPE'
               LEFT JOIN REFERENCE_CODES RC2 ON RC2.CODE = OIS.EVENT_SUB_TYPE AND RC2.DOMAIN = 'INT_SCH_RSN'
               LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON OIS.TO_INTERNAL_LOCATION_ID = AIL.INTERNAL_LOCATION_ID
               LEFT JOIN AGENCY_LOCATIONS AGY ON OIS.TO_AGY_LOC_ID = AGY.AGY_LOC_ID
         WHERE OIS.EVENT_ID = :eventId
    """),

    INSERT_APPOINTMENT("""
        INSERT INTO OFFENDER_IND_SCHEDULES (EVENT_ID, OFFENDER_BOOK_ID, EVENT_DATE, START_TIME, END_TIME, COMMENT_TEXT,
                EVENT_CLASS, EVENT_TYPE, EVENT_SUB_TYPE, EVENT_STATUS, AGY_LOC_ID, TO_INTERNAL_LOCATION_ID)
        VALUES (EVENT_ID.NEXTVAL, :bookingId, :eventDate, :startTime, :endTime, :comment,
                'INT_MOV', 'APP', :eventSubType, 'SCH', :agencyId, :locationId)
    """),

    DELETE_APPOINTMENT("""
        DELETE FROM OFFENDER_IND_SCHEDULES
        WHERE EVENT_ID = :eventId
    """),

    FIND_BOOKING_IDS_BY_OFFENDER_NO("""
        SELECT O.OFFENDER_ID_DISPLAY OFFENDER_NO, B.OFFENDER_BOOK_ID BOOKING_ID, B.BOOKING_SEQ
        FROM OFFENDERS O
        LEFT JOIN OFFENDER_BOOKINGS B ON B.OFFENDER_ID = O.OFFENDER_ID
                WHERE O.OFFENDER_ID_DISPLAY = :offenderNo
        ORDER BY B.BOOKING_SEQ
    """),

    GET_LATEST_BOOKING_BY_BOOKING_ID("""
        SELECT
        O.OFFENDER_ID_DISPLAY             OFFENDER_NO,
        UPPER(O.TITLE)                    TITLE,
        UPPER(O.SUFFIX)                   SUFFIX,
        UPPER(O.FIRST_NAME)               FIRST_NAME,
        UPPER(CONCAT(O.MIDDLE_NAME,
                CASE WHEN MIDDLE_NAME_2 IS NOT NULL
                        THEN CONCAT(' ', O.MIDDLE_NAME_2)
                ELSE '' END))                   MIDDLE_NAMES,
        UPPER(O.LAST_NAME)                LAST_NAME,
        O.BIRTH_DATE                      DATE_OF_BIRTH,
        OB.OFFENDER_BOOK_ID               BOOKING_ID,
        OB.ACTIVE_FLAG                    CURRENTLY_IN_PRISON,
        OB.AGY_LOC_ID                     AGENCY_LOCATION_ID,
        AL.DESCRIPTION                    AGENCY_LOCATION_DESC,
        OB.LIVING_UNIT_ID                 INTERNAL_LOCATION_ID,
        AIL.DESCRIPTION                   INTERNAL_LOCATION_DESC
                FROM OFFENDERS O
        INNER JOIN OFFENDER_BOOKINGS OB ON OB.OFFENDER_ID = O.OFFENDER_ID AND OB.BOOKING_SEQ = 1
        INNER JOIN AGENCY_LOCATIONS AL ON AL.AGY_LOC_ID = OB.AGY_LOC_ID
                LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON AIL.INTERNAL_LOCATION_ID = OB.LIVING_UNIT_ID
                WHERE O.OFFENDER_ID_DISPLAY = (SELECT O2.OFFENDER_ID_DISPLAY
                FROM OFFENDERS O2
                INNER JOIN OFFENDER_BOOKINGS OB2 ON O2.OFFENDER_ID = OB2.OFFENDER_ID
                WHERE OB2.OFFENDER_BOOK_ID = :bookingId)
    """),

    GET_LATEST_BOOKING_BY_OFFENDER_NO("""
        SELECT
        O.OFFENDER_ID_DISPLAY             OFFENDER_NO,
        UPPER(O.TITLE)                    TITLE,
        UPPER(O.SUFFIX)                   SUFFIX,
        UPPER(O.FIRST_NAME)               FIRST_NAME,
        UPPER(CONCAT(O.MIDDLE_NAME,
                CASE WHEN MIDDLE_NAME_2 IS NOT NULL
                        THEN CONCAT(' ', O.MIDDLE_NAME_2)
                ELSE '' END))                   MIDDLE_NAMES,
        UPPER(O.LAST_NAME)                LAST_NAME,
        O.BIRTH_DATE                      DATE_OF_BIRTH,
        OB.OFFENDER_BOOK_ID               BOOKING_ID,
        OB.ACTIVE_FLAG                    CURRENTLY_IN_PRISON,
        OB.AGY_LOC_ID                     AGENCY_LOCATION_ID,
        AL.DESCRIPTION                    AGENCY_LOCATION_DESC,
        OB.LIVING_UNIT_ID                 INTERNAL_LOCATION_ID,
        AIL.DESCRIPTION                   INTERNAL_LOCATION_DESC
                FROM OFFENDERS O
        INNER JOIN OFFENDER_BOOKINGS OB ON OB.OFFENDER_ID = O.OFFENDER_ID AND OB.BOOKING_SEQ = 1
        INNER JOIN AGENCY_LOCATIONS AL ON AL.AGY_LOC_ID = OB.AGY_LOC_ID
                LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON AIL.INTERNAL_LOCATION_ID = OB.LIVING_UNIT_ID
                WHERE O.OFFENDER_ID_DISPLAY = :offenderNo
    """),

    FIND_BOOKINGS_BY_PERSON_CONTACT("""
        SELECT  OB.OFFENDER_BOOK_ID                            booking_id,
        O.OFFENDER_ID_DISPLAY                          offender_no,
        O.TITLE,
        O.SUFFIX,
        O.FIRST_NAME,
        CONCAT(O.middle_name, CASE WHEN middle_name_2 IS NOT NULL
                THEN concat(' ', O.middle_name_2)
        ELSE '' END)                MIDDLE_NAMES,
        O.LAST_NAME,
        OB.ACTIVE_FLAG                                 currently_in_prison,
        OB.agy_loc_id                                  agency_location_id,
        AIL.description                                agency_location_desc,
        OB.LIVING_UNIT_ID                              internal_location_id,
        AIL.DESCRIPTION                                internal_location_desc
                FROM OFFENDER_BOOKINGS OB
        JOIN OFFENDERS O ON OB.OFFENDER_ID = O.OFFENDER_ID
        JOIN OFFENDER_CONTACT_PERSONS OCP ON OCP.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
        JOIN PERSONS P ON P.PERSON_ID = OCP.PERSON_ID
        JOIN PERSON_IDENTIFIERS PI ON PI.PERSON_ID = P.PERSON_ID AND PI.IDENTIFIER_TYPE = :identifierType
                AND PI.ID_SEQ = (SELECT MAX(ID_SEQ) FROM PERSON_IDENTIFIERS pi1 where pi1.PERSON_ID = PI.PERSON_ID AND pi1.IDENTIFIER_TYPE = PI.IDENTIFIER_TYPE )
        LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON OB.LIVING_UNIT_ID = AIL.INTERNAL_LOCATION_ID
                WHERE PI.IDENTIFIER = :identifier
        AND OCP.RELATIONSHIP_TYPE = COALESCE(:relationshipType, OCP.RELATIONSHIP_TYPE)
        AND OB.ACTIVE_FLAG = 'Y'
    """),

    FIND_BOOKINGS_BY_PERSON_ID_CONTACT("""
        SELECT  OB.OFFENDER_BOOK_ID                            booking_id,
        O.OFFENDER_ID_DISPLAY                          offender_no,
        O.TITLE,
        O.SUFFIX,
        O.FIRST_NAME,
        CONCAT(O.middle_name, CASE WHEN middle_name_2 IS NOT NULL
                THEN concat(' ', O.middle_name_2)
        ELSE '' END)                MIDDLE_NAMES,
        O.LAST_NAME,
        OB.ACTIVE_FLAG                                 currently_in_prison,
        OB.agy_loc_id                                  agency_location_id,
        AIL.description                                agency_location_desc,
        OB.LIVING_UNIT_ID                              internal_location_id,
        AIL.DESCRIPTION                                internal_location_desc
                FROM OFFENDER_BOOKINGS OB
        JOIN OFFENDERS O ON OB.OFFENDER_ID = O.OFFENDER_ID
        JOIN OFFENDER_CONTACT_PERSONS OCP ON OCP.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID AND OCP.PERSON_ID = :personId
                LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON OB.LIVING_UNIT_ID = AIL.INTERNAL_LOCATION_ID
                WHERE OCP.RELATIONSHIP_TYPE = COALESCE(:relationshipType, OCP.RELATIONSHIP_TYPE)
        AND OB.ACTIVE_FLAG = 'Y'
    """),

    FIND_BOOKING_IDS_IN_AGENCY("""
        SELECT OB.OFFENDER_BOOK_ID booking_id
        FROM OFFENDER_BOOKINGS OB
        WHERE OB.OFFENDER_BOOK_ID IN (:bookingIds)
        AND OB.AGY_LOC_ID = :agencyId
    """),

    FIND_REMAINING_VO_PVO("""
        SELECT ovb.remaining_vo, ovb.remaining_pvo
        FROM offender_visit_balances ovb
        WHERE ovb.offender_book_id = :bookingId
    """),

    ACTIVITIES_BOOKING_ID_CLAUSE(" AND OPP.OFFENDER_BOOK_ID = :bookingId"),
    ACTIVITIES_BOOKING_ID_IN_CLAUSE(" AND OPP.OFFENDER_BOOK_ID IN (:bookingIds)"),
    VISITS_BOOKING_ID_CLAUSE(" AND VIS.OFFENDER_BOOK_ID = :bookingId"),
    VISITS_BOOKING_ID_IN_CLAUSE(" AND VIS.OFFENDER_BOOK_ID IN (:bookingIds)"),
    APPOINTMENTS_BOOKING_ID_CLAUSE(" AND OIS.OFFENDER_BOOK_ID = :bookingId"),
    APPOINTMENTS_BOOKING_ID_IN_CLAUSE(" AND OIS.OFFENDER_BOOK_ID IN (:bookingIds)")
}
