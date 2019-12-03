package net.syscon.elite.repository.impl;

import lombok.extern.slf4j.Slf4j;
import net.syscon.elite.repository.OffenderDeletionRepository;
import net.syscon.elite.service.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class OffenderDeletionRepositoryImpl extends RepositoryBase implements OffenderDeletionRepository {

    @Value("${offender.deletion.db.enable.parallel.hints:false}")
    private boolean enableParallelHints;

    @Override
    public void deleteOffender(final String offenderNumber) {

        log.info("Deleting all data for offender: '{}'", offenderNumber);

        final List<String> offenderIds = offenderIdsFor(offenderNumber);

        if (offenderIds.isEmpty()) {
            throw EntityNotFoundException.withId(offenderNumber);
        }

        if (enableParallelHints) {
            getJdbcTemplateBase().execute(getQuery("OD_ENABLE_PARALLEL_HINTS"));
        }

        deleteOffenderBookings(offenderIds);
        deleteOffenderData(offenderIds);

        log.info("Deleted {} offender records with offenderNumber: '{}'", offenderIds.size(), offenderNumber);
    }

    private List<String> offenderIdsFor(final String offenderNumber) {
        return jdbcTemplate.queryForList(
                getQuery("OD_OFFENDER_IDS"),
                createParams("offenderNo", offenderNumber),
                String.class);
    }

    private void deleteOffenderBookings(final List<String> offenderIds) {

        log.debug("Deleting all offender booking data for offender ID: '{}'", offenderIds);

        deleteOffenderBooking(jdbcTemplate.queryForList(
                getQuery("OD_OFFENDER_BOOKING_IDS"),
                createParams("offenderIds", offenderIds),
                String.class));
    }

    private void deleteOffenderBooking(final List<String> bookIds) {

        log.debug("Deleting all offender booking data for book ID: '{}'", bookIds);

        deleteAgencyIncidents(bookIds);
        deleteOffenderCases(bookIds);
        deleteOffenderContactPersons(bookIds);
        deleteOffenderCSIPReports(bookIds);
        deleteOffenderCurfews(bookIds);
        deleteOffenderGangAffiliations(bookIds);
        deleteOffenderHealthProblems(bookIds);
        deleteOffenderLIDSKeyDates(bookIds);
        deleteOffenderNonAssociations(bookIds);
        deleteOffenderRehabDecisions(bookIds);
        deleteOffenderSentCalculations(bookIds);
        deleteOffenderSubstanceUses(bookIds);
        deleteOffenderVisits(bookIds);
        deleteOffenderVisitBalances(bookIds);
        deleteOffenderVisitOrders(bookIds);
        deleteOffenderVSCSentences(bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_BED_ASSIGNMENT_HISTORIES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_CASE_ASSOCIATED_PERSONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_INCIDENT_CASE_PARTIES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_IWP_DOCUMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_ALERTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_ASSESSMENT_ITEMS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_ASSESSMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_BOOKING_AGY_LOCS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_BOOKING_DETAILS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_BOOKING_EVENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CASE_ASSOCIATIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CASE_OFFICERS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_DATA_CORRECTIONS_HTY", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_DISCHARGE_BALANCES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_EDUCATIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_EMPLOYMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_EXTERNAL_MOVEMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_FINE_PAYMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_FIXED_TERM_RECALLS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_IDENTIFYING_MARKS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_IEP_LEVELS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_IMAGES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_IMPRISON_STATUSES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_INTER_MVMT_LOCATIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_KEY_WORKERS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_LANGUAGES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_MILITARY_RECORDS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_NO_PAY_PERIODS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_OGRS3_RISK_PREDICTORS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PAY_STATUSES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PHYSICAL_ATTRIBUTES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PPTY_CONTAINERS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PROFILES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PROFILE_DETAILS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_RELEASE_DETAILS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_RELEASE_DETAILS_HTY", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_RESTRICTIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_RISK_PREDICTORS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SUPERVISING_COURTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_TEAM_ASSIGNMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_TEAM_ASSIGN_HTY", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_TEST_SELECTIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_TMP_REL_SCHEDULES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_TRUST_ACCOUNTS_TEMP", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VSC_SENT_CALCULATIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_TASK_ASSIGNMENT_HTY", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_WORKFLOW_HISTORY", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VSC_ERROR_LOGS", bookIds);
    }

    private void deleteOffenderCases(final List<String> bookIds) {
        deleteOrders(bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CASE_IDENTIFIERS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CASE_STATUSES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CASES", bookIds);
    }

    private void deleteOrders(final List<String> bookIds) {
        deleteOffenderSentences(bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_REORDER_SENTENCES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_ORDER_PURPOSES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_ORDERS", bookIds);
        deleteCourtEventsAndOffenderCharges(bookIds);
    }

    private void deleteOffenderSentences(final List<String> bookIds) {
        deleteOffenderCaseNotes(bookIds);
        deleteOffenderSentConditions(bookIds);
        deleteOffenderSentenceAdjusts(bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_LICENCE_CONDITIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_LICENCE_RECALLS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_LICENCE_SENTENCES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SENTENCE_CHARGES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SENTENCE_STATUSES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SENTENCE_TERMS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SENTENCE_UA_EVENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SENTENCES", bookIds);
    }

    private void deleteOffenderSentConditions(final List<String> bookIds) {
        deleteOffenderPrgObligations(bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SENT_COND_STATUSES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SENT_CONDITIONS", bookIds);
    }

    private void deleteOffenderPrgObligations(final List<String> bookIds) {
        deleteOffenderMovementApps(bookIds);
        deleteOffenderProgramProfiles(bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PRG_OBLIGATION_HTY", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PRG_OBLIGATIONS", bookIds);
    }

    private void deleteOffenderMovementApps(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_IND_SCH_SENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_IND_SCHEDULES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_MOVEMENT_APPS", bookIds);
    }

    private void deleteOffenderSentenceAdjusts(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SENTENCE_ADJUSTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_KEY_DATE_ADJUSTS", bookIds);
    }

    private void deleteOffenderProgramProfiles(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_COURSE_ATTENDANCES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_EXCLUDE_ACTS_SCHDS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PRG_PRF_PAY_BANDS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PROGRAM_PROFILES", bookIds);
    }

    private void deleteOffenderCaseNotes(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CASE_NOTE_SENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFF_CASE_NOTE_RECIPIENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CASE_NOTES", bookIds);
    }

    private void deleteCourtEventsAndOffenderCharges(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_LINK_CASE_TXNS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_COURT_EVENT_CHARGES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_COURT_EVENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CHARGES", bookIds);
    }

    private void deleteAgencyIncidents(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_OIC_SANCTIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OIC_HEARING_RESULTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OIC_HEARING_COMMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OIC_HEARING_NOTICES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OIC_HEARINGS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_AGY_INC_INV_STATEMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_AGY_INC_INVESTIGATIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_AGENCY_INCIDENT_CHARGES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_AGENCY_INCIDENT_PARTIES", bookIds);
    }

    private void deleteOffenderContactPersons(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_PERSON_RESTRICTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CONTACT_PERSONS", bookIds);
    }

    private void deleteOffenderCSIPReports(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CSIP_FACTORS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CSIP_INTVW", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CSIP_PLANS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CSIP_ATTENDEES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CSIP_REVIEWS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CSIP_REPORTS", bookIds);
    }

    private void deleteCurfewAddresses(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_CURFEW_ADDRESS_OCCUPANTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_CURFEW_ADDRESSES", bookIds);
    }

    private void deleteHDCRequestReferrals(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_HDC_PROB_STAFF_RESPONSES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_HDC_PROB_STAFF_COMMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_HDC_BOARD_DECISIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_HDC_GOVERNOR_DECISIONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_HDC_REQUEST_REFERRALS", bookIds);
    }

    private void deleteHDCStatusTrackings(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_HDC_STATUS_REASONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_HDC_STATUS_TRACKINGS", bookIds);
    }

    private void deleteOffenderCurfews(final List<String> bookIds) {
        deleteCurfewAddresses(bookIds);
        deleteHDCRequestReferrals(bookIds);
        deleteHDCStatusTrackings(bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_HDC_PRISON_STAFF_COMMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_CURFEWS", bookIds);
    }

    private void deleteOffenderGangAffiliations(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_GANG_INVESTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_GANG_EVIDENCES", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_GANG_AFFILIATIONS", bookIds);
    }

    private void deleteOffenderHealthProblems(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_MEDICAL_TREATMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_HEALTH_PROBLEMS", bookIds);
    }

    private void deleteOffenderLIDSKeyDates(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_LIDS_REMAND_DAYS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_LIDS_KEY_DATES", bookIds);
    }

    private void deleteOffenderNonAssociations(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_NA_DETAILS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_NON_ASSOCIATIONS", bookIds);
    }

    private void deleteOffenderRehabDecisions(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_REHAB_PROVIDERS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_REHAB_DECISIONS", bookIds);
    }

    private void deleteOffenderSentCalculations(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_HDC_CALC_EXCLUSION_REASONS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SENT_CALCULATIONS", bookIds);
    }

    private void deleteOffenderSubstanceUses(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SUBSTANCE_DETAILS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SUBSTANCE_TREATMENTS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_SUBSTANCE_USES", bookIds);
    }

    private void deleteOffenderVisits(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VISIT_VISITORS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VISITS", bookIds);
    }

    private void deleteOffenderVisitBalances(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VISIT_BALANCE_ADJS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VISIT_BALANCES", bookIds);
    }

    private void deleteOffenderVisitOrders(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VO_VISITORS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VISIT_ORDERS", bookIds);
    }

    private void deleteOffenderVSCSentences(final List<String> bookIds) {
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VSC_SENTENCE_TERMS", bookIds);
        executeNamedSqlWithBookingIds("OD_DELETE_OFFENDER_VSC_SENTENCES", bookIds);
    }

    private void deleteOffenderBeneficiaries(final List<String> offenderIds) {
        executeNamedSqlWithOffenderIds("OD_DELETE_BENEFICIARY_TRANSACTIONS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_BENEFICIARIES", offenderIds);
    }

    private void deleteOffenderDeductions(final List<String> offenderIds) {
        deleteOffenderBeneficiaries(offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_ADJUSTMENT_TXNS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_DEDUCTION_RECEIPTS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_DEDUCTIONS", offenderIds);
    }

    private void deleteOffenderTransactions(final List<String> offenderIds) {
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_TRANSACTION_DETAILS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_TRANSACTIONS", offenderIds);
    }

    private void deleteOffenderData(final List<String> offenderIds) {

        log.debug("Deleting all (non-booking) offender data for offender ID: '{}'", offenderIds);

        deleteAddresses(offenderIds);
        deleteOffenderFinances(offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_BANK_CHEQUE_BENEFICIARIES", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_GL_TRANSACTIONS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_DAMAGE_OBLIGATIONS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_FREEZE_DISBURSEMENTS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_IDENTIFIERS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_MINIMUM_BALANCES", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_SYSTEM_REPORT_REQUESTS", offenderIds);

        executeNamedSqlWithOffenderIds("OD_DELETE_MERGE_TRANSACTION_LOGS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_MERGE_TRANSACTIONS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_LOCKED_MODULES", offenderIds);

        var bookingRowsDeleted = executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_BOOKINGS", offenderIds);

        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER", offenderIds);

        log.info("Deleted {} bookings for offender ID: {}", bookingRowsDeleted, offenderIds);
    }

    private void deleteAddresses(final List<String> offenderIds) {
        executeNamedSqlWithOffenderIds("OD_DELETE_ADDRESS_PHONES", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_ADDRESS_USAGES", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_ADDRESSES", offenderIds);
    }

    private void deleteOffenderFinances(final List<String> offenderIds) {
        deleteOffenderTransactions(offenderIds);
        deleteOffenderDeductions(offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_SUB_ACCOUNTS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_TRUST_ACCOUNTS", offenderIds);
        executeNamedSqlWithOffenderIds("OD_DELETE_OFFENDER_PAYMENT_PROFILES", offenderIds);
    }

    private int executeNamedSqlWithOffenderIds(final String sql, final List<String> ids) {
        return jdbcTemplate.update(getQuery(sql), createParams("offenderIds", ids));
    }

    private void executeNamedSqlWithBookingIds(final String sql, final List<String> ids) {
        jdbcTemplate.update(getQuery(sql), createParams("bookIds", ids));
    }
}