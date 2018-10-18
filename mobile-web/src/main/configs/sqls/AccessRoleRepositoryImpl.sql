INSERT_ACCESS_ROLE {
  INSERT INTO OMS_ROLES (
        ROLE_ID,
        ROLE_NAME,
        ROLE_CODE,
        ROLE_SEQ,
        PARENT_ROLE_CODE,
        ROLE_TYPE,
        ROLE_FUNCTION,
	      SYSTEM_DATA_FLAG)
	VALUES (
	      ROLE_ID.NEXTVAL,
	      :roleName,
	      :roleCode,
	      1,
	      :parentRoleCode,
	      'APP',
	      :roleFunction,
	      'Y')
}

UPDATE_ACCESS_ROLE {
  UPDATE OMS_ROLES SET
    ROLE_NAME = :roleName
  WHERE ROLE_CODE = :roleCode
}

UPDATE_ACCESS_ROLE_WITH_FUNCTION {
  UPDATE OMS_ROLES SET
	  ROLE_NAME = :roleName,
	  ROLE_FUNCTION = :roleFunction
  WHERE ROLE_CODE = :roleCode
}

GET_ACCESS_ROLE {
  SELECT *
  FROM OMS_ROLES
  WHERE ROLE_CODE = :roleCode
}

GET_ACCESS_ROLES {
  SELECT *
  FROM OMS_ROLES
  WHERE ROLE_TYPE = 'APP'
}
