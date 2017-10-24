package net.syscon.elite.api.resource.impl;

import net.syscon.elite.api.model.OffenderRelease;
import net.syscon.elite.api.resource.OffenderReleaseResource;
import net.syscon.elite.core.RestResource;
import net.syscon.elite.service.BookingService;
import net.syscon.util.MetaDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.Path;
import java.util.List;

import static net.syscon.util.ResourceUtils.nvl;

@RestResource
@Path("/offender-releases")
public class OffenderReleaseResourceImpl implements OffenderReleaseResource {

    private final BookingService bookingService;

    @Autowired
    public OffenderReleaseResourceImpl(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    @PreAuthorize("authentication.authorities.?[authority.contains('_ADMIN')].size() != 0")
    public GetOffenderReleasesResponse getOffenderReleases(Long pageOffset, Long pageLimit, List<String> offenderNos) {
        final List<OffenderRelease> releaseResponse = bookingService.getReleases(offenderNos, nvl(pageOffset, 0L), nvl(pageLimit, 10L));
        return GetOffenderReleasesResponse.respond200WithApplicationJson(releaseResponse, MetaDataFactory.getTotalRecords(releaseResponse), nvl(pageOffset, 0L), nvl(pageLimit, 10L));
    }
}
