FIND_USER_BY_STAFF_ID {
  SELECT SM.STAFF_ID,
         SM.FIRST_NAME,
         SM.LAST_NAME,
         (SELECT IA.INTERNET_ADDRESS
          FROM INTERNET_ADDRESSES IA
          WHERE IA.OWNER_ID = SM.STAFF_ID
          AND IA.OWNER_CLASS = 'STF'
          AND IA.INTERNET_ADDRESS_CLASS = 'EMAIL') EMAIL,
         (SELECT TI.TAG_IMAGE_ID
          FROM TAG_IMAGES TI
          WHERE TI.IMAGE_OBJECT_ID = SM.STAFF_ID
          AND TI.IMAGE_OBJECT_TYPE = 'STAFF'
          AND TI.ACTIVE_FLAG = 'Y') IMAGE_ID
  FROM STAFF_MEMBERS SM
  WHERE SM.STAFF_ID = :staffId
}

FIND_USER_BY_USERNAME {
  SELECT SM.STAFF_ID,
    AUA.USERNAME AS USER_ID,
    SM.FIRST_NAME,
    SM.LAST_NAME,
    AUA.WORKING_CASELOAD_ID AS ASSIGNED_CASELOAD_ID,
    (   SELECT INTERNET_ADDRESS
        FROM  INTERNET_ADDRESSES
        WHERE OWNER_CLASS = 'STF' AND INTERNET_ADDRESS_CLASS = 'EMAIL' AND OWNER_ID = SM.STAFF_ID
    ) EMAIL,
    (   SELECT TAG_IMAGE_ID
        FROM TAG_IMAGES
        WHERE IMAGE_OBJECT_ID = SM.STAFF_ID
        AND IMAGE_OBJECT_TYPE = 'STAFF'
        AND ACTIVE_FLAG = 'Y'
    ) IMAGE_ID
  FROM STAFF_MEMBERS SM JOIN STAFF_USER_ACCOUNTS AUA ON SM.STAFF_ID = AUA.STAFF_ID
                                                        AND AUA.USERNAME = :username
}


FIND_ROLES_BY_USERNAME {
  select DISTINCT REPLACE(RL.ROLE_CODE, '-', '_') ROLE_CODE
  from USER_CASELOAD_ROLES CLR
    join OMS_ROLES RL on RL.ROLE_ID = CLR.ROLE_ID
  where USERNAME = :username
  ORDER BY 1
}

UPDATE_STAFF_ACTIVE_CASE_LOAD {
  UPDATE STAFF_USER_ACCOUNTS
  SET WORKING_CASELOAD_ID = :caseLoadId
  WHERE STAFF_ID = :staffId
}


GET_CASELOAD_ID {
   SELECT WORKING_CASELOAD_ID FROM STAFF_USER_ACCOUNTS WHERE USERNAME = :username
}
