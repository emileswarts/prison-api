FIND_ROLE_PASSWORD {
  SELECT PROFILE_VALUE ROLE_PWD
  FROM %sSYSTEM_PROFILES
  WHERE PROFILE_TYPE = 'SYS' AND PROFILE_CODE = 'ROLE_PSWD'
}

FIND_USER_BY_USERNAME {
  SELECT SM.STAFF_ID,
         AUA.USERNAME,
         SM.FIRST_NAME,
         SM.LAST_NAME,
         AUA.WORKING_CASELOAD_ID AS ACTIVE_CASE_LOAD_ID,
         SM.STATUS ACCOUNT_STATUS,
         (   SELECT TAG_IMAGE_ID
             FROM TAG_IMAGES
             WHERE IMAGE_OBJECT_ID = SM.STAFF_ID
               AND IMAGE_OBJECT_TYPE = 'STAFF'
               AND ACTIVE_FLAG = 'Y'
         ) THUMBNAIL_ID
  FROM STAFF_MEMBERS SM JOIN STAFF_USER_ACCOUNTS AUA ON SM.STAFF_ID = AUA.STAFF_ID
    AND AUA.USERNAME = :username
}

FIND_USERS_BY_USERNAMES {
SELECT SM.STAFF_ID,
       AUA.USERNAME,
       SM.FIRST_NAME,
       SM.LAST_NAME,
       AUA.WORKING_CASELOAD_ID AS ACTIVE_CASE_LOAD_ID,
       SM.STATUS ACCOUNT_STATUS,
       (   SELECT TAG_IMAGE_ID
           FROM TAG_IMAGES
           WHERE IMAGE_OBJECT_ID = SM.STAFF_ID
             AND IMAGE_OBJECT_TYPE = 'STAFF'
             AND ACTIVE_FLAG = 'Y'
       ) THUMBNAIL_ID
FROM STAFF_MEMBERS SM JOIN STAFF_USER_ACCOUNTS AUA ON SM.STAFF_ID = AUA.STAFF_ID
  AND AUA.USERNAME IN (:usernames)
}

       FIND_BASIC_INMATE_DETAIL_BY_BOOKING_IDS {
SELECT OB.OFFENDER_BOOK_ID  as BOOKING_ID,
       OB.BOOKING_NO,
       OB.AGY_LOC_ID        as AGENCY_ID,
       OB.LIVING_UNIT_ID    as ASSIGNED_LIVING_UNIT_ID,
       O.OFFENDER_ID_DISPLAY as OFFENDER_NO,
       O.FIRST_NAME,
       CONCAT(O.middle_name, CASE WHEN middle_name_2 IS NOT NULL THEN concat(' ', O.middle_name_2) ELSE '' END) MIDDLE_NAME,
       O.LAST_NAME,
       O.BIRTH_DATE as DATE_OF_BIRTH
FROM OFFENDER_BOOKINGS OB
       INNER JOIN OFFENDERS O ON OB.OFFENDER_ID = O.OFFENDER_ID
WHERE OB.OFFENDER_BOOK_ID IN (:bookingIds)
  AND OB.AGY_LOC_ID = :caseloadId
  }

FIND_ROLES_BY_USERNAME {
  SELECT RL.ROLE_ID,
         CONCAT(CLR.CASELOAD_ID, CONCAT('_', REPLACE(RL.ROLE_CODE, '-', '_'))) ROLE_CODE,
         ROLE_NAME,
         PARENT_ROLE_CODE,
         CLR.CASELOAD_ID
  FROM USER_CASELOAD_ROLES CLR
         INNER JOIN OMS_ROLES RL ON RL.ROLE_ID = CLR.ROLE_ID
  WHERE USERNAME = :username
}

FIND_ACCESS_ROLES_BY_USERNAME_AND_CASELOAD {
  SELECT RL.ROLE_ID,
         RL.ROLE_CODE,
         ROLE_NAME,
         PARENT_ROLE_CODE,
         RL.ROLE_FUNCTION
  FROM USER_CASELOAD_ROLES CLR
         INNER JOIN OMS_ROLES RL ON RL.ROLE_ID = CLR.ROLE_ID
  WHERE USERNAME = :username and CASELOAD_ID = :caseloadId
}

UPDATE_STAFF_ACTIVE_CASE_LOAD {
  UPDATE STAFF_USER_ACCOUNTS
  SET WORKING_CASELOAD_ID = :caseLoadId
  WHERE USERNAME = :username
}

FIND_USER_BY_STAFF_ID_STAFF_USER_TYPE {
  SELECT SM.STAFF_ID,
         AUA.USERNAME,
         SM.FIRST_NAME,
         SM.LAST_NAME,
         AUA.WORKING_CASELOAD_ID AS ACTIVE_CASE_LOAD_ID,
         SM.STATUS ACCOUNT_STATUS,
         (SELECT TAG_IMAGE_ID
          FROM TAG_IMAGES
          WHERE IMAGE_OBJECT_ID = SM.STAFF_ID
            AND IMAGE_OBJECT_TYPE = 'STAFF'
            AND ACTIVE_FLAG = 'Y') THUMBNAIL_ID
  FROM STAFF_MEMBERS SM
         INNER JOIN STAFF_USER_ACCOUNTS AUA ON SM.STAFF_ID = AUA.STAFF_ID
      AND AUA.STAFF_ID = :staffId
      AND AUA.STAFF_USER_TYPE = :staffUserType
}

ROLE_ASSIGNED_COUNT {
  SELECT COUNT(*)
  FROM USER_CASELOAD_ROLES
  WHERE CASELOAD_ID = :caseloadId AND
      USERNAME = :username AND
      ROLE_ID = :roleId
}

USER_ACCESSIBLE_CASELOAD_COUNT {
  SELECT COUNT(*) FROM USER_ACCESSIBLE_CASELOADS WHERE CASELOAD_ID = :caseloadId AND USERNAME = :username
}

FIND_ACTIVE_STAFF_USERS_WITH_ACCESSIBLE_CASELOAD {
  SELECT SM.STAFF_ID,
         SUA.USERNAME,
         SM.FIRST_NAME,
         SM.LAST_NAME,
         SUA.WORKING_CASELOAD_ID AS ACTIVE_CASE_LOAD_ID,
         SM.STATUS ACCOUNT_STATUS
  FROM USER_ACCESSIBLE_CASELOADS UAC
         INNER JOIN STAFF_USER_ACCOUNTS SUA ON SUA.USERNAME = UAC.USERNAME
         INNER JOIN STAFF_MEMBERS SM ON SUA.STAFF_ID = SM.STAFF_ID
  WHERE UAC.CASELOAD_ID = :caseloadId
    AND SM.STATUS = 'ACTIVE'
    AND NOT EXISTS (SELECT 1 FROM USER_ACCESSIBLE_CASELOADS UAC2
                      WHERE UAC2.USERNAME = SUA.USERNAME
                      AND UAC2.CASELOAD_ID = :missingCaseloadId)
}

USER_ACCESSIBLE_CASELOAD_INSERT {
  INSERT INTO USER_ACCESSIBLE_CASELOADS (CASELOAD_ID, USERNAME, START_DATE) VALUES (:caseloadId, :username, :startDate)
}

FIND_ROLES_BY_CASELOAD_AND_ROLE {
  SELECT RL.ROLE_ID,
         REPLACE(RL.ROLE_CODE, '-', '_') ROLE_CODE,
         ROLE_NAME,
         REPLACE(RL.PARENT_ROLE_CODE, '-', '_') PARENT_ROLE_CODE,
         CLR.CASELOAD_ID,
         CLR.USERNAME,
         AUA.STAFF_ID
  FROM USER_CASELOAD_ROLES CLR
         INNER JOIN OMS_ROLES RL ON RL.ROLE_ID = CLR.ROLE_ID
         INNER JOIN STAFF_USER_ACCOUNTS AUA ON AUA.USERNAME = CLR.USERNAME
  WHERE RL.ROLE_CODE = :roleCode
    AND   CLR.CASELOAD_ID = :caseloadId
}

GET_ROLE_ID_FOR_ROLE_CODE {
  select ROLE_ID from OMS_ROLES WHERE ROLE_CODE = :roleCode
}

GET_ROLE_BY_ROLE_CODE {
  SELECT ROLE_ID,
         ROLE_CODE,
         ROLE_NAME,
         PARENT_ROLE_CODE,
         ROLE_FUNCTION
  FROM OMS_ROLES
  WHERE ROLE_CODE = :roleCode
}

INSERT_USER_ROLE {
  INSERT INTO USER_CASELOAD_ROLES (ROLE_ID, CASELOAD_ID, USERNAME)
  VALUES (
           :roleId, :caseloadId, :username)
}

DELETE_USER_ROLE {
  DELETE FROM USER_CASELOAD_ROLES WHERE CASELOAD_ID = :caseloadId AND USERNAME = :username AND ROLE_ID = :roleId
}

FIND_USERS_BY_CASELOAD {
  SELECT SM.STAFF_ID,
       SUA.USERNAME,
       SM.FIRST_NAME,
       SM.LAST_NAME,
       SM.STATUS ACCOUNT_STATUS,
       SUA.WORKING_CASELOAD_ID ACTIVE_CASE_LOAD_ID
  FROM STAFF_USER_ACCOUNTS SUA
       INNER JOIN STAFF_MEMBERS SM ON SUA.STAFF_ID = SM.STAFF_ID
       INNER JOIN USER_ACCESSIBLE_CASELOADS UAC ON SUA.USERNAME = UAC.USERNAME

  WHERE UAC.CASELOAD_ID = :caseloadId
}

FIND_USERS {
  SELECT SM.STAFF_ID,
       SUA.USERNAME,
       SM.FIRST_NAME,
       SM.LAST_NAME,
       SM.STATUS ACCOUNT_STATUS,
       SUA.WORKING_CASELOAD_ID ACTIVE_CASE_LOAD_ID
  FROM STAFF_USER_ACCOUNTS SUA
       INNER JOIN STAFF_MEMBERS SM ON SUA.STAFF_ID = SM.STAFF_ID
     }

FIND_USERS_AVAILABLE_TO_LAA_USER {
  SELECT SM.STAFF_ID,
       SUA.USERNAME,
       SM.FIRST_NAME,
       SM.LAST_NAME,
       SM.STATUS ACCOUNT_STATUS,
       SUA.WORKING_CASELOAD_ID ACTIVE_CASE_LOAD_ID
  FROM STAFF_USER_ACCOUNTS SUA
       INNER JOIN STAFF_MEMBERS SM ON SUA.STAFF_ID = SM.STAFF_ID
       INNER JOIN LAA_GENERAL_USERS LAAG ON SUA.USERNAME = LAAG.USERNAME AND ACTIVE_FLAG = :activeFlag
       INNER JOIN LAA_ADMINISTRATORS LAAA ON LAAA.USERNAME = :laaUsername AND LAAA.ACTIVE_FLAG = :activeFlag and LAAG.LOCAL_AUTHORITY_CODE = LAAA.LOCAL_AUTHORITY_CODE
}