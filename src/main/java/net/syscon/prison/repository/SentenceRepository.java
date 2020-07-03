package net.syscon.prison.repository;

import net.syscon.prison.api.model.OffenceDetail;
import net.syscon.prison.api.model.OffenceHistoryDetail;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SentenceRepository {
    List<OffenceDetail> getMainOffenceDetails(Long bookingId);

    List<OffenceDetail> getMainOffenceDetails(List<Long> bookingIds);

    List<OffenceHistoryDetail> getOffenceHistory(String offenderNo, boolean convictionsOnly);

    Optional<LocalDate> getConfirmedReleaseDate(Long bookingId);
}