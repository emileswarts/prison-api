FIND_CASE_LOADS_BY_USERNAME {
	SELECT CL.CASELOAD_ID CASE_LOAD_ID,
			 CL.DESCRIPTION,
			 CL.CASELOAD_TYPE "TYPE",
			 CL.CASELOAD_FUNCTION
	FROM CASELOADS CL
			 INNER JOIN USER_ACCESSIBLE_CASELOADS SC ON CL.CASELOAD_ID = SC.CASELOAD_ID
	WHERE SC.USERNAME = :username
	ORDER BY CL.DESCRIPTION
}

FIND_CASE_LOAD_BY_ID {
	SELECT CL.CASELOAD_ID CASE_LOAD_ID,
	       CL.DESCRIPTION,
				 CL.CASELOAD_TYPE "TYPE"
	  FROM CASELOADS CL
	 WHERE CL.CASELOAD_ID = :caseLoadId
}

FIND_ACTIVE_CASE_LOAD_BY_USERNAME {
	SELECT CL.CASELOAD_ID CASE_LOAD_ID,
				 CL.DESCRIPTION,
				 CL.CASELOAD_TYPE "TYPE"
	FROM CASELOADS CL JOIN STAFF_USER_ACCOUNTS SUA on CL.CASELOAD_ID = SUA.WORKING_CASELOAD_ID
	WHERE SUA.USERNAME = :username
}
