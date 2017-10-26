package net.syscon.elite.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.syscon.elite.api.model.Agency;
import net.syscon.elite.api.model.Location;
import net.syscon.elite.api.model.OffenderBooking;
import net.syscon.elite.api.model.UserDetail;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.api.support.Page;
import net.syscon.elite.repository.AgencyRepository;
import net.syscon.elite.repository.InmateRepository;
import net.syscon.elite.repository.LocationRepository;
import net.syscon.elite.repository.UserRepository;
import net.syscon.elite.security.UserSecurityUtils;
import net.syscon.elite.service.EntityNotFoundException;
import net.syscon.elite.service.LocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.syscon.elite.service.impl.InmateServiceImpl.DEFAULT_OFFENDER_SORT;

/**
 * Location API service implementation.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class LocationServiceImpl implements LocationService {
    private final AgencyRepository agencyRepository;
    private final LocationRepository locationRepository;
    private final InmateRepository inmateRepository;
    private final UserRepository userRepository;
    private final String locationTypeGranularity;
    private final Integer locationDepth;

    public LocationServiceImpl(AgencyRepository agencyRepository, LocationRepository locationRepository, InmateRepository inmateRepository, UserRepository userRepository,
                               @Value("${api.users.me.locations.locationType:WING}") String locationTypeGranularity, @Value("${api.users.me.locations.depth:1}") Integer locationDepth) {
        this.locationRepository = locationRepository;
        this.inmateRepository = inmateRepository;
        this.userRepository = userRepository;
        this.agencyRepository = agencyRepository;
        this.locationTypeGranularity = locationTypeGranularity;
        this.locationDepth = locationDepth;
    }

    @Override
    public List<Location> getUserLocations(String username) {
        final List<Location> locations = new ArrayList<>();

        // Step 1 - Get all agencies associated with user
        List<Agency> agencies = agencyRepository.findAgenciesByUsername(username);

        // Step 2 - Evaluate number of agencies to determine next step
        int agencyCount = agencies.size();

        // TODO: Implement support for hierarchical location retrieval (placeholder - may not be needed).
        if (agencyCount == 1) {
            // User has one agency so the agency will be used as a main location together with associated internal
            // locations of a granularity (e.g. 'WING') as determined by configuration setting.
            log.debug("User [{}] is associated with one agency.", username);
            Agency agency = agencies.get(0);

            // Start with agency converted to location
            locations.add(convertToLocation(agency));

            // Then retrieve all associated internal locations at configured level of granularity.
            final List<Location> agencyLocations = locationRepository.findLocationsByAgencyAndType(
                    agency.getAgencyId(), locationTypeGranularity, locationDepth);

            locations.addAll(agencyLocations);
        } else if (agencyCount > 1) {
            // User has multiple agencies so these will be used directly as locations.
            log.debug("User [{}] is associated with {} agencies.", username, agencyCount);

            // Add retrieved agencies converted to locations
            locations.addAll(agencies.stream().map(this::convertToLocation).collect(Collectors.toList()));
        } else {
            // TODO: Decide what to do if no agencies associated with current user - is this even possible?
            log.debug("User [{}] is not associated with any agencies.", username);
        }

        return locations;
    }

    @Override
    public Page<Location> getLocations(String query, String orderBy, Order order, long offset, long limit) {
        return locationRepository.findLocations(query, orderBy, order, offset, limit);
    }

    @Override
    public Page<Location> getLocationsFromAgency(final String agencyId, final String query, final long offset, final long limit, final String orderByField, final Order order) {
        return locationRepository.findLocationsByAgencyId(getCurrentCaseLoad(), agencyId, query, offset, limit, orderByField, order);
    }

    @Override
    public Page<OffenderBooking> getInmatesFromLocation(long locationId, String query, String orderByField, Order order, long offset, long limit) {
        Location location = getLocation(locationId, false);

        String colSort = StringUtils.isNotBlank(orderByField) ? orderByField : DEFAULT_OFFENDER_SORT;

        Page<OffenderBooking> inmates = inmateRepository.findInmatesByLocation(
                locationId,
                locationTypeGranularity,
                query,
                colSort,
                order,
                offset,
                limit);

        return inmates;
    }

    @Override
    public Location getLocation(long locationId, boolean withInmates) {
        Location location = locationRepository.findLocation(locationId).orElseThrow(new EntityNotFoundException(String.valueOf(locationId)));

        if (withInmates) {
            Page<OffenderBooking> inmates = inmateRepository.findInmatesByLocation(
                    locationId,
                    locationTypeGranularity,
                    null,
                    null,
                    null,
                    0,
                    1000);

            location.setAssignedInmates(inmates.getItems());
        }

        return location;
    }

    private String getCurrentCaseLoad() {
        //  get the user data from the database
        final String currentUsername = UserSecurityUtils.getCurrentUsername();
        final UserDetail userDetail = userRepository.findByUsername(currentUsername).orElseThrow(new EntityNotFoundException(currentUsername));
        return userDetail.getActiveCaseLoadId();
    }

    private Location convertToLocation(Agency agency) {
        return Location.builder()
                .locationId(-1L)
                .agencyId(agency.getAgencyId())
                .locationType(agency.getAgencyType())
                .description(agency.getDescription())
                .locationPrefix(agency.getAgencyId())
                .build();
    }
}
