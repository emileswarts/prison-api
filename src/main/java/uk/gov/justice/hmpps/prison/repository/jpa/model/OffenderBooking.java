package uk.gov.justice.hmpps.prison.repository.jpa.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ListIndexBase;
import uk.gov.justice.hmpps.prison.api.model.LegalStatus;
import uk.gov.justice.hmpps.prison.api.model.SentenceCalcDates;
import uk.gov.justice.hmpps.prison.repository.jpa.model.OffenderMilitaryRecord.BookingAndSequence;
import uk.gov.justice.hmpps.prison.repository.jpa.model.OffenderProfileDetail.PK;
import uk.gov.justice.hmpps.prison.repository.jpa.model.SentenceCalculation.KeyDateValues;
import uk.gov.justice.hmpps.prison.service.support.NonDtoReleaseDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@EqualsAndHashCode(of = "bookingId", callSuper = false)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OFFENDER_BOOKINGS")
@ToString(of = {"bookingId", "bookNumber", "bookingSequence", "activeFlag", "inOutStatus"})
public class OffenderBooking extends ExtendedAuditableEntity {

    @SequenceGenerator(name = "OFFENDER_BOOK_ID", sequenceName = "OFFENDER_BOOK_ID", allocationSize = 1)
    @GeneratedValue(generator = "OFFENDER_BOOK_ID")
    @Id
    @Column(name = "OFFENDER_BOOK_ID")
    private Long bookingId;

    @Column(name = "BOOKING_NO")
    private String bookNumber;

    @Column(name = "BOOKING_TYPE")
    private String bookingType;

    @OneToMany(mappedBy = "id.offenderBooking", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OffenderProfileDetail> profileDetails = new ArrayList<>();

    @OrderColumn(name = "MILITARY_SEQ")
    @ListIndexBase(1)
    @OneToMany(mappedBy = "bookingAndSequence.offenderBooking", cascade = CascadeType.ALL)
    private List<OffenderMilitaryRecord> militaryRecords;

    @OrderColumn(name = "CASE_SEQ")
    @ListIndexBase(1)
    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OffenderCourtCase> courtCases = new ArrayList<>();

    @ListIndexBase(1)
    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    private List<OffenderPropertyContainer> propertyContainers;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "AGY_LOC_ID", nullable = false)
    private AgencyLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATE_AGY_LOC_ID")
    private AgencyLocation createLocation;

    @Setter(AccessLevel.NONE)
    @Column(name = "BOOKING_SEQ", nullable = false)
    private Integer bookingSequence;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "OFFENDER_ID", nullable = false)
    private Offender offender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIVING_UNIT_ID")
    private AgencyInternalLocation assignedLivingUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNED_STAFF_ID")
    private Staff assignedStaff;

    @Column(name = "AGENCY_IML_ID")
    private Long livingUnitMv;

    @Column(name = "ACTIVE_FLAG", nullable = false)
    @Default
    private String activeFlag = "N";

    @OrderBy("effectiveDate ASC")
    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<OffenderNonAssociationDetail> nonAssociationDetails = new ArrayList<>();

    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<ExternalMovement> externalMovements = new ArrayList<>();

    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<OffenderImprisonmentStatus> imprisonmentStatuses = new ArrayList<>();

    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<OffenderCaseNote> caseNotes = new ArrayList<>();

    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<OffenderCharge> charges = new ArrayList<>();

    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<SentenceCalculation> sentenceCalculations = new ArrayList<>();

    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<KeyDateAdjustment> keyDateAdjustments = new ArrayList<>();

    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<SentenceAdjustment> sentenceAdjustments = new ArrayList<>();

    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<SentenceTerm> terms = new ArrayList<>();

    @OneToMany(mappedBy = "offenderBooking", cascade = CascadeType.ALL)
    @Default
    private List<OffenderSentence> sentences = new ArrayList<>();

    @Column(name = "ROOT_OFFENDER_ID")
    private Long rootOffenderId;

    @Column(name = "BOOKING_STATUS")
    private String bookingStatus;

    @Column(name = "STATUS_REASON")
    private String statusReason;

    @Column(name = "DISCLOSURE_FLAG", nullable = false)
    @Default
    private String disclosureFlag = "Y";

    @Column(name = "COMMUNITY_ACTIVE_FLAG", nullable = false)
    @Default
    private String communityActiveFlag = "N";

    @Column(name = "SERVICE_FEE_FLAG", nullable = false)
    @Default
    private String serviceFeeFlag = "N";

    @Column(name = "COMM_STATUS")
    private String commStatus;

    @Column(name = "YOUTH_ADULT_CODE", nullable = false)
    private String youthAdultCode;

    @Column(name = "BOOKING_BEGIN_DATE", nullable = false)
    private LocalDateTime bookingBeginDate;

    @Column(name = "BOOKING_END_DATE")
    private LocalDateTime bookingEndDate;

    @Column(name = "IN_OUT_STATUS", nullable = false)
    private String inOutStatus;

    @Column(name = "ADMISSION_REASON")
    private String admissionReason;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ReleaseDetail releaseDetail;

    public Optional<SentenceCalculation> getLatestCalculation() {
        return sentenceCalculations.stream().max(Comparator.comparing(SentenceCalculation::getId));
    }

    public LocalDate getReleaseDate() {
        return getLatestCalculation().map(
            sc -> deriveKeyDates(new KeyDateValues(
                sc.getArdCalculatedDate(),
                sc.getArdOverridedDate(),
                sc.getCrdCalculatedDate(),
                sc.getCrdOverridedDate(),
                sc.getNpdCalculatedDate(),
                sc.getNpdOverridedDate(),
                sc.getPrrdCalculatedDate(),
                sc.getPrrdOverridedDate(),
                sc.getActualParoleDate(),
                sc.getHomeDetentionCurfewActualDate(),
                sc.getMidTermDate(),
                getConfirmedReleaseDate().orElse(null)))
        ).orElse(deriveKeyDates(new KeyDateValues(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            getConfirmedReleaseDate().orElse(null))))
            .releaseDate();
    }

    // TODO: Add all the other dates in!
    public SentenceCalcDates getSentenceCalcDates() {
        return getLatestCalculation().map(
            sc -> SentenceCalcDates.sentenceCalcDatesBuilder()
                .bookingId(getBookingId())
                .sentenceStartDate(getSentenceStartDate().orElse(null))
                .effectiveSentenceEndDate(sc.getEffectiveSentenceEndDate())
                .additionalDaysAwarded(getAdditionalDaysAwarded())
                .automaticReleaseDate(sc.getArdCalculatedDate())
                .automaticReleaseOverrideDate(sc.getArdOverridedDate())
                .conditionalReleaseDate(sc.getCrdCalculatedDate())
                .conditionalReleaseOverrideDate(sc.getCrdOverridedDate())
                .sentenceExpiryDate(sc.getSentenceExpiryDate())
                .postRecallReleaseDate(sc.getPrrdCalculatedDate())
                .postRecallReleaseOverrideDate(sc.getPrrdOverridedDate())
                .licenceExpiryDate(sc.getLicenceExpiryDate())
                .homeDetentionCurfewEligibilityDate(sc.getHomeDetentionCurfewEligibilityDate())
                .paroleEligibilityDate(sc.getParoleEligibilityDate())
                .homeDetentionCurfewActualDate(sc.getHomeDetentionCurfewActualDate())
                .actualParoleDate(sc.getActualParoleDate())
                .releaseOnTemporaryLicenceDate(sc.getRotlOverridedDate())
                .earlyRemovalSchemeEligibilityDate(sc.getErsedOverridedDate())
                .tariffEarlyRemovalSchemeEligibilityDate(sc.getTersedOverridedDate())
                .earlyTermDate(sc.getEarlyTermDate())
                .midTermDate(sc.getMidTermDate())
                .lateTermDate(sc.getLateTermDate())
                .topupSupervisionExpiryDate(sc.getTopupSupervisionExpiryDate())
                .tariffDate(sc.getTariffDate())
                .dtoPostRecallReleaseDate(sc.getDprrdCalculatedDate())
                .dtoPostRecallReleaseDateOverride(sc.getDprrdOverridedDate())
                .nonParoleDate(sc.getNpdCalculatedDate())
                .nonParoleOverrideDate(sc.getNpdOverridedDate())
                .nonDtoReleaseDate(sc.getNonDtoReleaseDate())
                .nonDtoReleaseDateType(sc.getNonDtoReleaseDateType())
                .releaseDate(getReleaseDate())
                .confirmedReleaseDate(getConfirmedReleaseDate().orElse(null))
                .build())
            .orElse(
                SentenceCalcDates.sentenceCalcDatesBuilder()
                    .bookingId(getBookingId())
                    .sentenceStartDate(getSentenceStartDate().orElse(null))
                    .additionalDaysAwarded(getAdditionalDaysAwarded())
                    .releaseDate(getReleaseDate())
                    .confirmedReleaseDate(getConfirmedReleaseDate().orElse(null))
                    .build());
    }

    public record DerivedKeyDates(NonDtoReleaseDate nonDtoReleaseDate, LocalDate releaseDate) {
    }

    public static DerivedKeyDates deriveKeyDates(final KeyDateValues keyDateValues) {

        // Determine non-DTO release date
        final var nonDtoReleaseDate = SentenceCalculation.deriveNonDtoReleaseDate(keyDateValues).orElse(null);

        // Determine offender release date
        final var releaseDate = deriveOffenderReleaseDate(keyDateValues, nonDtoReleaseDate);

        return new DerivedKeyDates(nonDtoReleaseDate, releaseDate);
    }

    /**
     * Offender release date is determined according to algorithm.
     * <p>
     * 1. If there is a confirmed release date, the offender release date is the confirmed release date.
     * <p>
     * 2. If there is no confirmed release date for the offender, the offender release date is either the actual
     * parole date or the home detention curfew actual date.
     * <p>
     * 3. If there is no confirmed release date, actual parole date or home detention curfew actual date for the
     * offender, the release date is the later of the nonDtoReleaseDate or midTermDate value (if either or both
     * are present).
     *
     * @param keyDateValues     a set of key date values used to determine the Non deterministic release date
     * @param nonDtoReleaseDate derived Non deterministic release date information
     * @return releaseDate
     */
    private static LocalDate deriveOffenderReleaseDate(final KeyDateValues keyDateValues, final NonDtoReleaseDate nonDtoReleaseDate) {

        final LocalDate releaseDate;

        if (Objects.nonNull(keyDateValues.confirmedReleaseDate())) {
            releaseDate = keyDateValues.confirmedReleaseDate();
        } else if (Objects.nonNull(keyDateValues.actualParoleDate())) {
            releaseDate = keyDateValues.actualParoleDate();
        } else if (Objects.nonNull(keyDateValues.homeDetentionCurfewActualDate())) {
            releaseDate = keyDateValues.homeDetentionCurfewActualDate();
        } else {
            final var midTermDate = keyDateValues.midTermDate();

            if (Objects.nonNull(nonDtoReleaseDate)) {
                if (Objects.isNull(midTermDate)) {
                    releaseDate = nonDtoReleaseDate.getReleaseDate();
                } else {
                    releaseDate = midTermDate.isAfter(nonDtoReleaseDate.getReleaseDate()) ? midTermDate : nonDtoReleaseDate.getReleaseDate();
                }
            } else {
                releaseDate = midTermDate;
            }
        }

        return releaseDate;
    }


    public Optional<LocalDate> getConfirmedReleaseDate() {
        return Optional.ofNullable(releaseDetail != null ? releaseDetail.getReleaseDate() != null ? releaseDetail.getReleaseDate() : releaseDetail.getAutoReleaseDate() : null);
    }

    public Optional<LocalDate> getSentenceStartDate() {
        return sentences.stream()
            .filter(s -> "A".equals(s.getStatus()))
            .flatMap(s -> s.getTerms().stream())
            .min(Comparator.comparing(SentenceTerm::getStartDate))
            .map(SentenceTerm::getStartDate);
    }

    public Integer getAdditionalDaysAwarded() {
        final var adjustedDays = keyDateAdjustments.stream().filter(kda -> "ADA".equals(kda.getSentenceAdjustCode()) && kda.isActive()).mapToInt(KeyDateAdjustment::getAdjustDays).sum();
        return adjustedDays == 0 ? null : adjustedDays;
    }

    public void add(final OffenderMilitaryRecord omr) {
        militaryRecords.add(omr);
        omr.setBookingAndSequence(new BookingAndSequence(this, militaryRecords.size()));
    }

    public void add(final OffenderCourtCase courtCase) {
        courtCases.add(courtCase);
        courtCase.setOffenderBooking(this);
    }

    public void add(final ProfileType profileType, final ProfileCode code) {
        profileDetails.stream()
            .filter(pd -> profileType.equals(pd.getId().getType()))
            .max(Comparator.comparing(op -> op.getId().getSequence()))
            .ifPresentOrElse(
                y -> y.setCode(code)
                , () -> profileDetails.add(OffenderProfileDetail.builder()
                    .id(new PK(this, profileType, 1))
                    .caseloadType("INST")
                    .code(code)
                    .listSequence(profileType.getListSequence())
                    .build()));
    }

    public Optional<OffenderCourtCase> getCourtCaseBy(final Long courtCaseId) {
        return courtCases == null ? Optional.empty() : courtCases.stream().filter(Objects::nonNull).filter(cc -> cc.getId().equals(courtCaseId)).findFirst();
    }

    public boolean isActive() {
        return activeFlag != null && activeFlag.equals("Y");
    }

    public List<OffenderCourtCase> getActiveCourtCases() {
        return courtCases.stream().filter(offenderCourtCase -> offenderCourtCase != null && offenderCourtCase.isActive()).toList();
    }

    public List<OffenderPropertyContainer> getActivePropertyContainers() {
        return propertyContainers.stream().filter(OffenderPropertyContainer::isActive).toList();
    }

    public List<OffenderProfileDetail> getActiveProfileDetails() {
        return profileDetails.stream()
            .filter(pd -> {
                final var profileType = pd.getId().getType();
                return profileType.getCategory().equals("PI") && (profileType.getActiveFlag().isActive() || profileType.getType().equals("RELF"));
            })
            .collect(
                Collectors.groupingBy(pd -> pd.getId().getType())
            ).entrySet().stream()
            .flatMap(pd -> pd.getValue().stream()
                .max(Comparator.comparing(op -> op.getId().getSequence()))
                .stream())
            .collect(Collectors.toList());
    }

    public int incBookingSequence() {
        if (bookingSequence == null) bookingSequence = 0;
        bookingSequence = bookingSequence + 1;
        return bookingSequence;
    }

    public ExternalMovement addExternalMovement(final ExternalMovement externalMovement) {
        externalMovement.setMovementSequence(getNextMovementSequence());
        externalMovement.setOffenderBooking(this);
        externalMovements.add(externalMovement);
        return externalMovement;
    }

    public Optional<ExternalMovement> getLastMovement() {
        return getMovementsRecentFirst().stream().findFirst();
    }


    public OffenderImprisonmentStatus setImprisonmentStatus(final OffenderImprisonmentStatus offenderImprisonmentStatus, final LocalDateTime effectiveFrom) {
        setPreviousImprisonmentStatusToInactive(effectiveFrom);
        offenderImprisonmentStatus.setImprisonStatusSeq(getNextImprisonmentStatusSequence());
        offenderImprisonmentStatus.setOffenderBooking(this);
        offenderImprisonmentStatus.makeActive();
        offenderImprisonmentStatus.setEffectiveDate(effectiveFrom.toLocalDate());
        offenderImprisonmentStatus.setEffectiveTime(effectiveFrom);
        imprisonmentStatuses.add(offenderImprisonmentStatus);
        return offenderImprisonmentStatus;
    }

    public Optional<OffenderImprisonmentStatus> getActiveImprisonmentStatus() {
        return getImprisonmentStatusesRecentFirst().stream().filter(OffenderImprisonmentStatus::isActiveLatestStatus).findFirst();
    }

    public List<ExternalMovement> getMovementsRecentFirst() {
        return externalMovements.stream()
            .sorted(Comparator.comparingLong(ExternalMovement::getMovementSequence)
                .reversed())
            .collect(Collectors.toList());
    }

    public List<OffenderImprisonmentStatus> getImprisonmentStatusesRecentFirst() {
        return imprisonmentStatuses.stream()
            .sorted(Comparator.comparingLong(OffenderImprisonmentStatus::getImprisonStatusSeq)
                .reversed())
            .collect(Collectors.toList());
    }

    public Long getNextImprisonmentStatusSequence() {
        return getImprisonmentStatusesRecentFirst().stream().findFirst().map(OffenderImprisonmentStatus::getImprisonStatusSeq).orElse(0L) + 1;
    }

    public Long getNextMovementSequence() {
        return getLastMovement().map(ExternalMovement::getMovementSequence).orElse(0L) + 1;
    }

    public void setPreviousMovementsToInactive() {
        externalMovements.stream().filter(m -> m.getActiveFlag().isActive()).forEach(m -> m.setActiveFlag(ActiveFlag.N));
    }

    public void setPreviousImprisonmentStatusToInactive(final LocalDateTime expiryTime) {
        imprisonmentStatuses.stream().filter(OffenderImprisonmentStatus::isActiveLatestStatus).forEach(m -> m.makeInactive(expiryTime));
    }

    public LegalStatus getLegalStatus() {
        return getActiveImprisonmentStatus().map(
            is -> is.getImprisonmentStatus().getLegalStatus()
        ).orElse(null);
    }

    public String getConvictedStatus() {
        return getActiveImprisonmentStatus().map(
            is -> is.getImprisonmentStatus().getConvictedStatus()
        ).orElse(null);
    }
}
