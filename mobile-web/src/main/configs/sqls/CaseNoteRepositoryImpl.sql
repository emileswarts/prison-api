FIND_CASENOTES {
    select OFFENDER_BOOK_ID, 
    CASE_NOTE_TYPE, 
    CASE_NOTE_SUB_TYPE, 
    CASE_NOTE_TEXT, 
    CASE_NOTE_ID, 
    NOTE_SOURCE_CODE, 
    CREATE_DATETIME, 
    CREATE_USER_ID,
    CONTACT_DATE
    from offender_case_notes 
    where offender_Book_Id = :bookingId
}

INSERT_CASE_NOTE {
	INSERT INTO OFFENDER_CASE_NOTES (CASE_NOTE_ID, OFFENDER_BOOK_ID, 
	CONTACT_DATE, CONTACT_TIME, CASE_NOTE_TYPE, CASE_NOTE_SUB_TYPE, STAFF_ID, 
	CASE_NOTE_TEXT, DATE_CREATION, TIME_CREATION, CREATE_USER_ID, NOTE_SOURCE_CODE, MODIFY_DATETIME) 
	VALUES 
    (:caseNoteID, :bookingID, :contactDate, :contactTime, :type, :subType, :staffId, :text, :createDate, :createTime, :createdBy, :sourceCode, null)

}

UPDATE_CASE_NOTE {
	UPDATE OFFENDER_CASE_NOTES SET CASE_NOTE_TEXT = :text,
                              MODIFY_USER_ID = :modifyBy
                              WHERE CASE_NOTE_ID = :caseNoteId
}