package uk.gov.justice.hmpps.prison.repository.sql

enum class CaseNoteRepositorySql(val sql: String) {

  GROUP_BY_TYPES_AND_OFFENDERS(
    """
        SELECT CASE_NOTE_TYPE,
        CASE_NOTE_SUB_TYPE,
        O.OFFENDER_ID_DISPLAY OFFENDER_NO,
        COUNT(*) NUM_CASE_NOTES,
        MAX(OCS.CONTACT_TIME) LATEST_CASE_NOTE
                FROM OFFENDER_CASE_NOTES OCS JOIN OFFENDER_BOOKINGS OB
                ON OB.OFFENDER_BOOK_ID = OCS.OFFENDER_BOOK_ID JOIN OFFENDERS O
        ON O.OFFENDER_ID = OB.OFFENDER_ID
                WHERE OCS.AUDIT_TIMESTAMP between :fromDate and :toDate
        %s
        GROUP BY CASE_NOTE_TYPE, CASE_NOTE_SUB_TYPE, O.OFFENDER_ID_DISPLAY
        ORDER BY O.OFFENDER_ID_DISPLAY
    """,
  ),

  GROUP_BY_TYPES_AND_STAFF(
    """
        SELECT /*+ index(OCS, OFFENDER_CASE_NOTES_X04) */
        STAFF_ID,
        CASE_NOTE_TYPE,
        CASE_NOTE_SUB_TYPE,
        COUNT(*) NUM_CASE_NOTES,
        MAX(OCS.CONTACT_TIME) LATEST_CASE_NOTE
                FROM OFFENDER_CASE_NOTES OCS
        WHERE OCS.AUDIT_TIMESTAMP between :fromDate and :toDate
        AND STAFF_ID IN (:staffIds)
        AND CASE_NOTE_TYPE = COALESCE(:type, CASE_NOTE_TYPE)
        AND CASE_NOTE_SUB_TYPE = COALESCE(:subType, CASE_NOTE_SUB_TYPE)
        GROUP BY STAFF_ID, CASE_NOTE_TYPE, CASE_NOTE_SUB_TYPE
    """,
  ),

  RECENT_CASE_NOTE_EVENTS(
    """
        SELECT O.OFFENDER_ID_DISPLAY       noms_id,
        OC.CASE_NOTE_ID             id,
        OB.AGY_LOC_ID               establishment_code,
        TO_DATE(TO_CHAR(OC.CONTACT_DATE, 'YYYYMMDD') || TO_CHAR(OC.CONTACT_TIME, 'HH24MISS'),
                'YYYYMMDDHH24MISS') contact_timestamp,
        OC.CASE_NOTE_TYPE           main_note_type,
        OC.CASE_NOTE_SUB_TYPE       sub_note_type,
        SM.LAST_NAME,
        SM.FIRST_NAME,
        OC.CASE_NOTE_TEXT           content,
        OC.AUDIT_TIMESTAMP          notification_timestamp
                FROM OFFENDER_CASE_NOTES OC
        JOIN OFFENDER_BOOKINGS OB ON OB.OFFENDER_BOOK_ID = OC.OFFENDER_BOOK_ID
        JOIN OFFENDERS O ON O.OFFENDER_ID = OB.OFFENDER_ID
        JOIN STAFF_MEMBERS SM ON SM.STAFF_ID = OC.STAFF_ID
        WHERE OC.AUDIT_TIMESTAMP >= :fromDate
        AND OC.CASE_NOTE_TYPE in (:types)
        ORDER BY OC.AUDIT_TIMESTAMP
    """,
  ),

  GET_CASE_NOTE_TYPES_WITH_SUB_TYPES_BY_CASELOAD_TYPE(
    """
        SELECT WKS.WORK_TYPE CODE,
        RC1.DOMAIN,
        RC1.DESCRIPTION,
        NULL PARENT_DOMAIN,
        NULL PARENT_CODE,
        'Y' ACTIVE_FLAG,
        WKS.WORK_SUB_TYPE SUB_CODE,
        RC2.DOMAIN SUB_DOMAIN,
        RC2.DESCRIPTION SUB_DESCRIPTION,
        'Y' SUB_ACTIVE_FLAG
                FROM (SELECT W.WORK_TYPE,
                        W.WORK_SUB_TYPE
                                FROM WORKS W
                                WHERE W.WORKFLOW_TYPE = 'CNOTE'
                        AND W.CASELOAD_TYPE IN ((:caseLoadType),'BOTH')
        AND W.MANUAL_SELECT_FLAG = :active_flag
        AND W.ACTIVE_FLAG = :active_flag) WKS
        INNER JOIN REFERENCE_CODES RC1 ON RC1.CODE = WKS.WORK_TYPE
                AND RC1.DOMAIN = 'TASK_TYPE'
        INNER JOIN REFERENCE_CODES RC2 ON RC2.CODE = WKS.WORK_SUB_TYPE
                AND RC2.DOMAIN = 'TASK_SUBTYPE'
        AND COALESCE(RC2.PARENT_DOMAIN, 'TASK_TYPE') = 'TASK_TYPE'
        ORDER BY WKS.WORK_TYPE, WKS.WORK_SUB_TYPE
    """,
  ),

  GET_USED_CASE_NOTE_TYPES_WITH_SUB_TYPES(
    """
        SELECT WKS.WORK_TYPE CODE,
        RC1.DOMAIN,
        RC1.DESCRIPTION,
        NULL PARENT_DOMAIN,
        NULL PARENT_CODE,
        WKS.ACTIVE_FLAG,
        WKS.WORK_SUB_TYPE SUB_CODE,
        RC2.DOMAIN SUB_DOMAIN,
        RC2.DESCRIPTION SUB_DESCRIPTION,
        WKS.ACTIVE_FLAG SUB_ACTIVE_FLAG
                FROM (SELECT W.WORK_TYPE,
                        W.WORK_SUB_TYPE,
                        W.ACTIVE_FLAG
                                FROM WORKS W
                                WHERE W.WORKFLOW_TYPE = 'CNOTE') WKS
                INNER JOIN REFERENCE_CODES RC1 ON RC1.CODE = WKS.WORK_TYPE
                AND RC1.DOMAIN = 'TASK_TYPE'
        INNER JOIN REFERENCE_CODES RC2 ON RC2.CODE = WKS.WORK_SUB_TYPE
                AND RC2.DOMAIN = 'TASK_SUBTYPE'
        AND COALESCE(RC2.PARENT_DOMAIN, 'TASK_TYPE') = 'TASK_TYPE'
    """,
  ),
}
