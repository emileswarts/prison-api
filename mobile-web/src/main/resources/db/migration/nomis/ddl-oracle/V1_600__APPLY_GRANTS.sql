GRANT SELECT ON ACCOUNT_CODES TO TAG_USER;
GRANT SELECT ON ADDRESSES TO TAG_USER;
GRANT SELECT ON AGENCY_INTERNAL_LOCATIONS TO TAG_USER;
GRANT SELECT ON AGENCY_LOCATIONS TO TAG_USER;
GRANT SELECT ON AREAS TO TAG_USER;
GRANT SELECT ON ASSESSMENTS TO TAG_USER;
GRANT SELECT ON CASELOAD_AGENCY_LOCATIONS TO TAG_USER;
GRANT SELECT ON CASELOADS TO TAG_USER;
GRANT SELECT ON CONTACT_PERSON_TYPES TO TAG_USER;
GRANT SELECT ON CORPORATES TO TAG_USER;
GRANT SELECT ON COURSE_ACTIVITIES TO TAG_USER;
GRANT SELECT ON COURSE_SCHEDULES TO TAG_USER;
GRANT SELECT ON DB_PATCHES TO TAG_USER;
GRANT SELECT ON HO_CODES TO TAG_USER;
GRANT SELECT ON IEP_LEVELS TO TAG_USER;
GRANT SELECT ON IMPRISONMENT_STATUSES TO TAG_USER;
GRANT SELECT ON INT_LOC_USAGE_LOCATIONS TO TAG_USER;
GRANT SELECT ON INTERNAL_LOCATION_USAGES TO TAG_USER;
GRANT SELECT ON INTERNAL_SCHEDULE_REASONS TO TAG_USER;
GRANT SELECT ON INTERNET_ADDRESSES TO TAG_USER;
GRANT SELECT ON MOVEMENT_REASONS TO TAG_USER;
GRANT SELECT ON OFFENCE_RESULT_CODES TO TAG_USER;
GRANT SELECT ON OFFENCES TO TAG_USER;
GRANT SELECT ON OFFENDER_ALERTS TO TAG_USER;
GRANT SELECT ON OFFENDER_ASSESSMENTS TO TAG_USER;
GRANT SELECT ON OFFENDER_BOOKINGS TO TAG_USER;
GRANT SELECT, UPDATE, INSERT ON OFFENDER_CASE_NOTES TO TAG_USER;
GRANT SELECT ON OFFENDER_CASES TO TAG_USER;
GRANT SELECT ON OFFENDER_CHARGES TO TAG_USER;
GRANT SELECT ON OFFENDER_CONTACT_PERSONS TO TAG_USER;
GRANT SELECT ON OFFENDER_COURSE_ATTENDANCES TO TAG_USER;
GRANT SELECT ON OFFENDER_EXCLUDE_ACTS_SCHDS TO TAG_USER;
GRANT SELECT ON OFFENDER_EXTERNAL_MOVEMENTS TO TAG_USER;
GRANT SELECT ON OFFENDER_IDENTIFIERS TO TAG_USER;
GRANT SELECT ON OFFENDER_IDENTIFYING_MARKS TO TAG_USER;
GRANT SELECT ON OFFENDER_IEP_LEVELS TO TAG_USER;
GRANT SELECT ON OFFENDER_IMPRISON_STATUSES TO TAG_USER;
GRANT SELECT ON OFFENDER_IMAGES TO TAG_USER;
GRANT SELECT ON OFFENDER_IND_SCHEDULES TO TAG_USER;
GRANT SELECT ON OFFENDER_KEY_DATE_ADJUSTS TO TAG_USER;
GRANT SELECT ON OFFENDER_KEY_WORKERS TO TAG_USER;
GRANT SELECT ON OFFENDER_MOVEMENT_APPS TO TAG_USER;
GRANT SELECT ON OFFENDER_OIC_SANCTIONS TO TAG_USER;
GRANT SELECT ON OFFENDER_PHYSICAL_ATTRIBUTES TO TAG_USER;
GRANT SELECT ON OFFENDER_PRG_OBLIGATIONS TO TAG_USER;
GRANT SELECT ON OFFENDER_PROFILE_DETAILS TO TAG_USER;
GRANT SELECT ON OFFENDER_PROGRAM_PROFILES TO TAG_USER;
GRANT SELECT ON OFFENDER_RELEASE_DETAILS TO TAG_USER;
GRANT SELECT ON OFFENDER_SENT_CALCULATIONS TO TAG_USER;
GRANT SELECT ON OFFENDER_SENTENCE_ADJUSTS TO TAG_USER;
GRANT SELECT ON OFFENDER_SENTENCE_TERMS TO TAG_USER;
GRANT SELECT ON OFFENDER_SENTENCES TO TAG_USER;
GRANT SELECT ON OFFENDER_SUB_ACCOUNTS TO TAG_USER;
GRANT SELECT ON OFFENDER_TRANSACTIONS TO TAG_USER;
GRANT SELECT ON OFFENDER_TRUST_ACCOUNTS TO TAG_USER;
GRANT SELECT ON OFFENDER_VISITS TO TAG_USER;
GRANT SELECT ON OFFENDERS TO TAG_USER;
GRANT SELECT ON OIC_HEARING_RESULTS TO TAG_USER;
GRANT SELECT ON OMS_DELETED_ROWS TO TAG_USER;
GRANT SELECT ON OMS_ROLES TO TAG_USER;
GRANT SELECT ON ORDERS TO TAG_USER;
GRANT SELECT ON PERSONS TO TAG_USER;
GRANT SELECT ON PHONES TO TAG_USER;
GRANT SELECT ON PROFILE_CODES TO TAG_USER;
GRANT SELECT ON PROFILE_TYPES TO TAG_USER;
GRANT SELECT ON PROGRAM_SERVICES TO TAG_USER;
GRANT SELECT ON REFERENCE_CODES TO TAG_USER;
GRANT SELECT ON REFERENCE_DOMAINS TO TAG_USER;
GRANT SELECT ON SENTENCE_CALC_TYPES TO TAG_USER;
GRANT SELECT ON STAFF_MEMBER_ROLES TO TAG_USER;
GRANT SELECT, UPDATE ON STAFF_MEMBERS TO TAG_USER;
GRANT SELECT, UPDATE ON STAFF_USER_ACCOUNTS TO TAG_USER;
GRANT SELECT ON STATUTES TO TAG_USER;
GRANT SELECT ON SYSTEM_PROFILES TO TAG_USER;
GRANT SELECT ON TAG_ERROR_LOGS TO TAG_USER;
GRANT SELECT ON TAG_IMAGES TO TAG_USER;
GRANT SELECT ON TRUST_AUDITS_TMP TO TAG_USER;
GRANT SELECT ON USER_ACCESSIBLE_CASELOADS TO TAG_USER;
GRANT SELECT ON USER_CASELOAD_ROLES TO TAG_USER;
GRANT SELECT ON WORKS TO TAG_USER;
GRANT SELECT ON CASE_NOTE_ID TO TAG_USER;
GRANT SELECT ON agy_loc_amend_id TO TAG_USER;
GRANT SELECT ON PERSON_ID TO TAG_USER;
GRANT SELECT ON OFFENDER_CONTACT_PERSON_ID TO TAG_USER;

GRANT SELECT ON SYSTEM_PROFILES TO API_PROXY_USER;
GRANT EXECUTE ON DECRYPTION TO API_PROXY_USER;

