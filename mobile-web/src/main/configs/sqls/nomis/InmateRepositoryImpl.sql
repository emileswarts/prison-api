FIND_INMATE_DETAIL {
  SELECT B.OFFENDER_BOOK_ID,
         B.BOOKING_NO,
         O.OFFENDER_ID_DISPLAY,
         O.FIRST_NAME,
         CONCAT(O.middle_name, CASE WHEN middle_name_2 IS NOT NULL THEN concat(' ', O.middle_name_2) ELSE '' END) MIDDLE_NAME,
         O.LAST_NAME,
         B.AGY_LOC_ID,
         B.LIVING_UNIT_ID,
         B.ACTIVE_FLAG,
         pc3.description                RELIGION, -- Deprecated: remove!
         RC1.DESCRIPTION             AS LANGUAGE,
         (SELECT OI.OFFENDER_IMAGE_ID
          FROM OFFENDER_IMAGES OI
          WHERE OI.ACTIVE_FLAG = 'Y'
                AND IMAGE_OBJECT_TYPE = 'OFF_BKG'
                AND OI.OFFENDER_BOOK_ID = B.OFFENDER_BOOK_ID
                AND OI.IMAGE_VIEW_TYPE = 'FACE'
                AND OI.ORIENTATION_TYPE = 'FRONT'
                AND CREATE_DATETIME = (SELECT MAX(CREATE_DATETIME)
                                       FROM OFFENDER_IMAGES
                                       WHERE ACTIVE_FLAG = 'Y'
                                             AND IMAGE_OBJECT_TYPE = 'OFF_BKG'
                                             AND OFFENDER_BOOK_ID = OI.OFFENDER_BOOK_ID
                                             AND IMAGE_VIEW_TYPE = 'FACE'
                                             AND ORIENTATION_TYPE = 'FRONT')) AS FACE_IMAGE_ID,
         O.BIRTH_DATE,
         B.ASSIGNED_STAFF_ID AS ASSIGNED_OFFICER_ID
  FROM OFFENDER_BOOKINGS B
    INNER JOIN OFFENDERS O ON B.OFFENDER_ID = O.OFFENDER_ID
    LEFT JOIN offender_profile_details opd3 ON opd3.offender_book_id = B.offender_book_id AND opd3.profile_type = 'RELF'-- Deprecated: remove!
    LEFT JOIN profile_codes pc3 ON pc3.profile_type = opd3.profile_type AND pc3.profile_code = opd3.profile_code        -- Deprecated: remove!
    LEFT JOIN OFFENDER_LANGUAGES LANG ON LANG.OFFENDER_BOOK_ID = B.OFFENDER_BOOK_ID AND LANG.LANGUAGE_TYPE = 'PREF_SPEAK'
    LEFT JOIN REFERENCE_CODES RC1 ON LANG.LANGUAGE_CODE = RC1.CODE AND RC1.DOMAIN = 'LANG'
  WHERE B.ACTIVE_FLAG = 'Y' AND B.OFFENDER_BOOK_ID = :bookingId
}

FIND_BASIC_INMATE_DETAIL {
  SELECT B.OFFENDER_BOOK_ID,
    B.BOOKING_NO,
    O.OFFENDER_ID_DISPLAY,
    O.FIRST_NAME,
    CONCAT(O.middle_name, CASE WHEN middle_name_2 IS NOT NULL THEN concat(' ', O.middle_name_2) ELSE '' END) MIDDLE_NAME,
    O.LAST_NAME,
    O.BIRTH_DATE,
    B.AGY_LOC_ID,
    B.LIVING_UNIT_ID,
    B.ACTIVE_FLAG
  FROM OFFENDER_BOOKINGS B
    INNER JOIN OFFENDERS O ON B.OFFENDER_ID = O.OFFENDER_ID
  WHERE B.ACTIVE_FLAG = 'Y' AND B.OFFENDER_BOOK_ID = :bookingId
  }

GET_IMAGE_DATA_FOR_BOOKING {
  SELECT
    I.OFFENDER_IMAGE_ID AS IMAGE_ID,
    I.CAPTURE_DATETIME AS CAPTURE_DATE,
    I.IMAGE_VIEW_TYPE,
    I.ORIENTATION_TYPE,
    I.IMAGE_OBJECT_TYPE,
    I.IMAGE_OBJECT_ID
  FROM OFFENDER_IMAGES I
  WHERE I.OFFENDER_IMAGE_ID =
         (SELECT OI.OFFENDER_IMAGE_ID
          FROM OFFENDER_IMAGES OI
          WHERE OI.ACTIVE_FLAG = 'Y'
                AND IMAGE_OBJECT_TYPE = 'OFF_BKG'
                AND OI.OFFENDER_BOOK_ID = :bookingId
                AND OI.IMAGE_VIEW_TYPE = 'FACE'
                AND OI.ORIENTATION_TYPE = 'FRONT'
                AND CREATE_DATETIME = (SELECT MAX(CREATE_DATETIME)
                                       FROM OFFENDER_IMAGES
                                       WHERE ACTIVE_FLAG = 'Y'
                                             AND IMAGE_OBJECT_TYPE = 'OFF_BKG'
                                             AND OFFENDER_BOOK_ID = :bookingId
                                             AND IMAGE_VIEW_TYPE = 'FACE'
                                             AND ORIENTATION_TYPE = 'FRONT'))
}


FIND_ASSIGNED_LIVING_UNIT {
  SELECT B.AGY_LOC_ID,
        B.LIVING_UNIT_ID,
        I.DESCRIPTION LIVING_UNIT_DESCRIPTION,
        AL.DESCRIPTION as AGENCY_NAME
  FROM OFFENDER_BOOKINGS B
    LEFT JOIN AGENCY_INTERNAL_LOCATIONS I ON B.LIVING_UNIT_ID = I.INTERNAL_LOCATION_ID
    LEFT JOIN AGENCY_LOCATIONS AL ON AL.AGY_LOC_ID = B.AGY_LOC_ID
  WHERE B.ACTIVE_FLAG = 'Y' AND B.OFFENDER_BOOK_ID = :bookingId
}

FIND_ALL_INMATES {
      SELECT
        OB.OFFENDER_BOOK_ID,
        OB.BOOKING_NO,
        O.OFFENDER_ID_DISPLAY,
        OB.AGY_LOC_ID,
        O.FIRST_NAME,
        O.MIDDLE_NAME,
        O.LAST_NAME,
        O.BIRTH_DATE,
        NULL AS ALERT_TYPES,
        NULL AS ALIASES,
        OB.LIVING_UNIT_ID,
        AIL.DESCRIPTION as LIVING_UNIT_DESC,
        (
          SELECT OI.OFFENDER_IMAGE_ID
          FROM OFFENDER_IMAGES OI
          WHERE OI.ACTIVE_FLAG = 'Y'
                AND IMAGE_OBJECT_TYPE = 'OFF_BKG'
                AND OI.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID
                AND OI.IMAGE_VIEW_TYPE = 'FACE'
                AND OI.ORIENTATION_TYPE = 'FRONT'
                AND CREATE_DATETIME = (SELECT MAX(CREATE_DATETIME)
                                       FROM OFFENDER_IMAGES
                                       WHERE ACTIVE_FLAG = 'Y'
                                             AND IMAGE_OBJECT_TYPE = 'OFF_BKG'
                                             AND OFFENDER_BOOK_ID = OI.OFFENDER_BOOK_ID
                                             AND IMAGE_VIEW_TYPE = 'FACE'
                                             AND ORIENTATION_TYPE = 'FRONT')
    ) AS FACE_IMAGE_ID,
    NULL AS ASSIGNED_OFFICER_ID
  FROM OFFENDER_BOOKINGS OB
    INNER JOIN OFFENDERS O ON OB.OFFENDER_ID = O.OFFENDER_ID
    LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON OB.LIVING_UNIT_ID = AIL.INTERNAL_LOCATION_ID
  WHERE OB.ACTIVE_FLAG = 'Y' AND OB.BOOKING_SEQ = 1
}

FIND_INMATES_BY_LOCATION {
  SELECT B.OFFENDER_BOOK_ID,
    B.BOOKING_NO,
    O.OFFENDER_ID_DISPLAY,
    B.AGY_LOC_ID,
    O.FIRST_NAME,
    O.MIDDLE_NAME,
    O.LAST_NAME,
    O.BIRTH_DATE,
    NULL AS ALIASES,
    B.LIVING_UNIT_ID,
    (
      SELECT OI.OFFENDER_IMAGE_ID
      FROM OFFENDER_IMAGES OI
      WHERE OI.ACTIVE_FLAG = 'Y'
            AND IMAGE_OBJECT_TYPE = 'OFF_BKG'
            AND OI.OFFENDER_BOOK_ID = B.OFFENDER_BOOK_ID
            AND OI.IMAGE_VIEW_TYPE = 'FACE'
            AND OI.ORIENTATION_TYPE = 'FRONT'
            AND CREATE_DATETIME = (SELECT MAX(CREATE_DATETIME)
                                   FROM OFFENDER_IMAGES
                                   WHERE ACTIVE_FLAG = 'Y'
                                         AND IMAGE_OBJECT_TYPE = 'OFF_BKG'
                                         AND OFFENDER_BOOK_ID = OI.OFFENDER_BOOK_ID
                                         AND IMAGE_VIEW_TYPE = 'FACE'
                                         AND ORIENTATION_TYPE = 'FRONT')
    ) AS FACE_IMAGE_ID
  FROM OFFENDER_BOOKINGS B
    INNER JOIN CASELOAD_AGENCY_LOCATIONS C ON C.CASELOAD_ID = :caseLoadId AND B.AGY_LOC_ID = C.AGY_LOC_ID
    LEFT JOIN OFFENDERS O ON B.OFFENDER_ID = O.OFFENDER_ID
  WHERE B.ACTIVE_FLAG = 'Y' AND B.BOOKING_SEQ = 1
        AND B.LIVING_UNIT_ID IN (
    SELECT INTERNAL_LOCATION_ID
    FROM AGENCY_INTERNAL_LOCATIONS START WITH INTERNAL_LOCATION_ID = :locationId
    CONNECT BY PRIOR INTERNAL_LOCATION_ID = PARENT_INTERNAL_LOCATION_ID
  )

}

FIND_PHYSICAL_MARKS_BY_BOOKING {
SELECT (SELECT DESCRIPTION FROM REFERENCE_CODES WHERE CODE = M.MARK_TYPE AND DOMAIN='MARK_TYPE') AS TYPE,
       (SELECT DESCRIPTION FROM REFERENCE_CODES WHERE CODE = M.SIDE_CODE AND DOMAIN='SIDE') AS SIDE,
       (SELECT DESCRIPTION FROM REFERENCE_CODES WHERE CODE = M.BODY_PART_CODE AND DOMAIN='BODY_PART') AS BODY_PART,
       (SELECT DESCRIPTION FROM REFERENCE_CODES WHERE CODE = M.PART_ORIENTATION_CODE AND DOMAIN='PART_ORIENT') AS ORENTIATION,
  M.COMMENT_TEXT,
       (SELECT I.OFFENDER_IMAGE_ID
        FROM OFFENDER_IMAGES I
        WHERE B.OFFENDER_BOOK_ID = I.OFFENDER_BOOK_ID
              AND I.ACTIVE_FLAG = 'Y'
              AND M.MARK_TYPE = I.IMAGE_VIEW_TYPE
              AND M.BODY_PART_CODE = I.ORIENTATION_TYPE
              AND M.ID_MARK_SEQ = I.IMAGE_OBJECT_ID
       ) AS IMAGE_ID
FROM OFFENDER_IDENTIFYING_MARKS M
  JOIN OFFENDER_BOOKINGS B ON B.OFFENDER_BOOK_ID = M.OFFENDER_BOOK_ID
WHERE B.OFFENDER_BOOK_ID = :bookingId
      AND B.ACTIVE_FLAG = 'Y'
      AND M.BODY_PART_CODE != 'CONV'
}

FIND_OFFENDERS {
  SELECT
    O.OFFENDER_ID_DISPLAY             OFFENDER_NO,
    O.TITLE                           TITLE,
    O.SUFFIX                          SUFFIX,
    O.FIRST_NAME                      FIRST_NAME,
    CONCAT(O.MIDDLE_NAME,
      CASE WHEN MIDDLE_NAME_2 IS NOT NULL
        THEN CONCAT(' ', O.MIDDLE_NAME_2)
      ELSE '' END)                    MIDDLE_NAMES,
    O.LAST_NAME                       LAST_NAME,
    O.BIRTH_DATE                      DATE_OF_BIRTH,
    RCE.DESCRIPTION                   ETHNICITY,
    RCS.DESCRIPTION                   GENDER,
    RCC.DESCRIPTION                   BIRTH_COUNTRY,
    OB.OFFENDER_BOOK_ID               LATEST_BOOKING_ID,
    OB.BOOKING_BEGIN_DATE             RECEPTION_DATE,
    OB.ACTIVE_FLAG                    CURRENTLY_IN_PRISON,
    OB.AGY_LOC_ID                     LATEST_LOCATION_ID,
    AL.DESCRIPTION                    LATEST_LOCATION,
    AIL.DESCRIPTION                   INTERNAL_LOCATION,
    CASE WHEN CAST(IST.BAND_CODE AS int) <= 8
      THEN 'Convicted'
    WHEN CAST(IST.BAND_CODE AS int) > 8
      THEN 'Remand'
    ELSE NULL END                     CONVICTED_STATUS,
    CASE WHEN OPD2.PROFILE_CODE IS NOT NULL
      THEN OPD2.PROFILE_CODE
    ELSE PC.DESCRIPTION END           NATIONALITIES,
    PC3.DESCRIPTION                   RELIGION,
    PC2.DESCRIPTION                   MARITAL_STATUS,
    OIS.IMPRISONMENT_STATUS,
    (SELECT OI1.IDENTIFIER
     FROM OFFENDER_IDENTIFIERS OI1
     WHERE OI1.OFFENDER_ID = OB.OFFENDER_ID
       AND OI1.IDENTIFIER_TYPE = 'PNC'
       AND OI1.OFFENDER_ID_SEQ = (SELECT MAX(OFFENDER_ID_SEQ)
                                  FROM OFFENDER_IDENTIFIERS OI11
                                  WHERE OI11.OFFENDER_ID = OI1.OFFENDER_ID
                                    AND OI11.IDENTIFIER_TYPE = OI1.IDENTIFIER_TYPE )) PNC_NUMBER,
    (SELECT OI2.IDENTIFIER
     FROM OFFENDER_IDENTIFIERS OI2
     WHERE OI2.OFFENDER_ID = OB.OFFENDER_ID
       AND OI2.IDENTIFIER_TYPE = 'CRO'
       AND OI2.OFFENDER_ID_SEQ = (SELECT MAX(OFFENDER_ID_SEQ)
                                  FROM OFFENDER_IDENTIFIERS OI21
                                  WHERE OI21.OFFENDER_ID = OI2.OFFENDER_ID
                                    AND OI21.IDENTIFIER_TYPE = OI2.IDENTIFIER_TYPE )) CRO_NUMBER
  FROM OFFENDERS O
    INNER JOIN OFFENDER_BOOKINGS OB ON OB.OFFENDER_ID = O.OFFENDER_ID AND OB.BOOKING_SEQ = 1
    INNER JOIN AGENCY_LOCATIONS AL ON AL.AGY_LOC_ID = OB.AGY_LOC_ID
    LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON OB.LIVING_UNIT_ID = AIL.INTERNAL_LOCATION_ID
    LEFT JOIN OFFENDER_IMPRISON_STATUSES OIS ON OIS.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID AND OIS.LATEST_STATUS = 'Y'
    LEFT JOIN IMPRISONMENT_STATUSES IST ON IST.IMPRISONMENT_STATUS = OIS.IMPRISONMENT_STATUS
    LEFT JOIN REFERENCE_CODES RCE ON O.RACE_CODE = RCE.CODE AND RCE.DOMAIN = 'ETHNICITY'
    LEFT JOIN REFERENCE_CODES RCS ON O.SEX_CODE = RCS.CODE AND RCS.DOMAIN = 'SEX'
    LEFT JOIN REFERENCE_CODES RCC ON O.BIRTH_COUNTRY_CODE = RCC.CODE AND RCC.DOMAIN = 'COUNTRY'
    LEFT JOIN OFFENDER_PROFILE_DETAILS OPD1 ON OPD1.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID AND OPD1.PROFILE_TYPE = 'NAT'
    LEFT JOIN OFFENDER_PROFILE_DETAILS OPD2 ON OPD2.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID AND OPD2.PROFILE_TYPE = 'NATIO'
    LEFT JOIN OFFENDER_PROFILE_DETAILS OPD3 ON OPD3.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID AND OPD3.PROFILE_TYPE = 'RELF'
    LEFT JOIN OFFENDER_PROFILE_DETAILS OPD4 ON OPD4.OFFENDER_BOOK_ID = OB.OFFENDER_BOOK_ID AND OPD4.PROFILE_TYPE = 'MARITAL'
    LEFT JOIN PROFILE_CODES PC ON PC.PROFILE_TYPE = OPD1.PROFILE_TYPE AND PC.PROFILE_CODE = OPD1.PROFILE_CODE
    LEFT JOIN PROFILE_CODES PC2 ON PC2.PROFILE_TYPE = OPD4.PROFILE_TYPE AND PC2.PROFILE_CODE = OPD4.PROFILE_CODE
    LEFT JOIN PROFILE_CODES PC3 ON PC3.PROFILE_TYPE = OPD3.PROFILE_TYPE AND PC3.PROFILE_CODE = OPD3.PROFILE_CODE
}

FIND_OFFENDERS_WITH_ALIASES {
SELECT
       O.OFFENDER_ID                     OFFENDER_ID,
       O.OFFENDER_ID_DISPLAY             OFFENDER_NO,
       O.TITLE                           TITLE,
       O.SUFFIX                          SUFFIX,
       O.FIRST_NAME                      FIRST_NAME,
       CONCAT(O.MIDDLE_NAME,
              CASE WHEN MIDDLE_NAME_2 IS NOT NULL
                        THEN CONCAT(' ', O.MIDDLE_NAME_2)
                   ELSE '' END)                    MIDDLE_NAMES,
       O.LAST_NAME                       LAST_NAME,
       O.BIRTH_DATE                      DATE_OF_BIRTH,
       RCE.DESCRIPTION                   ETHNICITY,
       RCS.DESCRIPTION                   GENDER,
       RCC.DESCRIPTION                   BIRTH_COUNTRY,
       WB.OFFENDER_BOOK_ID               LATEST_BOOKING_ID,
       WB.BOOKING_BEGIN_DATE             RECEPTION_DATE,
       WB.ACTIVE_FLAG                    CURRENTLY_IN_PRISON,
       WB.AGY_LOC_ID                     LATEST_LOCATION_ID,
       AL.DESCRIPTION                    LATEST_LOCATION,
       AIL.DESCRIPTION                   INTERNAL_LOCATION,
       CASE WHEN CAST(IST.BAND_CODE AS int) <= 8
                 THEN 'Convicted'
            WHEN CAST(IST.BAND_CODE AS int) > 8
                 THEN 'Remand'
            ELSE NULL END                     CONVICTED_STATUS,
       CASE WHEN OPD2.PROFILE_CODE IS NOT NULL
                 THEN OPD2.PROFILE_CODE
            ELSE PC.DESCRIPTION END           NATIONALITIES,
       PC3.DESCRIPTION                   RELIGION,
       PC2.DESCRIPTION                   MARITAL_STATUS,
       OIS.IMPRISONMENT_STATUS,
       (SELECT OI1.IDENTIFIER
        FROM OFFENDER_IDENTIFIERS OI1
        WHERE OI1.OFFENDER_ID = WB.OFFENDER_ID
          AND OI1.IDENTIFIER_TYPE = 'PNC'
          AND OI1.OFFENDER_ID_SEQ = (SELECT MAX(OFFENDER_ID_SEQ)
                                     FROM OFFENDER_IDENTIFIERS OI11
                                     WHERE OI11.OFFENDER_ID = OI1.OFFENDER_ID
                                       AND OI11.IDENTIFIER_TYPE = OI1.IDENTIFIER_TYPE )) PNC_NUMBER,
       (SELECT OI2.IDENTIFIER
        FROM OFFENDER_IDENTIFIERS OI2
        WHERE OI2.OFFENDER_ID = WB.OFFENDER_ID
          AND OI2.IDENTIFIER_TYPE = 'CRO'
          AND OI2.OFFENDER_ID_SEQ = (SELECT MAX(OFFENDER_ID_SEQ)
                                     FROM OFFENDER_IDENTIFIERS OI21
                                     WHERE OI21.OFFENDER_ID = OI2.OFFENDER_ID
                                       AND OI21.IDENTIFIER_TYPE = OI2.IDENTIFIER_TYPE )) CRO_NUMBER,
       WO.FIRST_NAME CURRENT_WORKING_FIRST_NAME,
       WO.LAST_NAME  CURRENT_WORKING_LAST_NAME,
       WO.BIRTH_DATE CURRENT_WORKING_BIRTH_DATE
FROM OFFENDERS O
       INNER JOIN OFFENDER_BOOKINGS WB ON (
          WB.ROOT_OFFENDER_ID = O.ROOT_OFFENDER_ID AND
          WB.BOOKING_SEQ = 1
        )
       INNER JOIN OFFENDERS WO on WO.OFFENDER_ID = WB.OFFENDER_ID
       INNER JOIN AGENCY_LOCATIONS AL ON AL.AGY_LOC_ID = WB.AGY_LOC_ID
       LEFT JOIN AGENCY_INTERNAL_LOCATIONS AIL ON WB.LIVING_UNIT_ID = AIL.INTERNAL_LOCATION_ID
       LEFT JOIN OFFENDER_IMPRISON_STATUSES OIS ON OIS.OFFENDER_BOOK_ID = WB.OFFENDER_BOOK_ID AND OIS.LATEST_STATUS = 'Y'
       LEFT JOIN IMPRISONMENT_STATUSES IST ON IST.IMPRISONMENT_STATUS = OIS.IMPRISONMENT_STATUS
       LEFT JOIN REFERENCE_CODES RCE ON O.RACE_CODE = RCE.CODE AND RCE.DOMAIN = 'ETHNICITY'
       LEFT JOIN REFERENCE_CODES RCS ON O.SEX_CODE = RCS.CODE AND RCS.DOMAIN = 'SEX'
       LEFT JOIN REFERENCE_CODES RCC ON O.BIRTH_COUNTRY_CODE = RCC.CODE AND RCC.DOMAIN = 'COUNTRY'
       LEFT JOIN OFFENDER_PROFILE_DETAILS OPD1 ON OPD1.OFFENDER_BOOK_ID = WB.OFFENDER_BOOK_ID AND OPD1.PROFILE_TYPE = 'NAT'
       LEFT JOIN OFFENDER_PROFILE_DETAILS OPD2 ON OPD2.OFFENDER_BOOK_ID = WB.OFFENDER_BOOK_ID AND OPD2.PROFILE_TYPE = 'NATIO'
       LEFT JOIN OFFENDER_PROFILE_DETAILS OPD3 ON OPD3.OFFENDER_BOOK_ID = WB.OFFENDER_BOOK_ID AND OPD3.PROFILE_TYPE = 'RELF'
       LEFT JOIN OFFENDER_PROFILE_DETAILS OPD4 ON OPD4.OFFENDER_BOOK_ID = WB.OFFENDER_BOOK_ID AND OPD4.PROFILE_TYPE = 'MARITAL'
       LEFT JOIN PROFILE_CODES PC ON PC.PROFILE_TYPE = OPD1.PROFILE_TYPE AND PC.PROFILE_CODE = OPD1.PROFILE_CODE
       LEFT JOIN PROFILE_CODES PC2 ON PC2.PROFILE_TYPE = OPD4.PROFILE_TYPE AND PC2.PROFILE_CODE = OPD4.PROFILE_CODE
       LEFT JOIN PROFILE_CODES PC3 ON PC3.PROFILE_TYPE = OPD3.PROFILE_TYPE AND PC3.PROFILE_CODE = OPD3.PROFILE_CODE
 }
