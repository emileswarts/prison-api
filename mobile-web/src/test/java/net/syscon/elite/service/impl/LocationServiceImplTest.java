package net.syscon.elite.service.impl;

import net.syscon.elite.api.model.Agency;
import net.syscon.elite.api.model.Location;
import net.syscon.elite.repository.AgencyRepository;
import net.syscon.elite.repository.LocationRepository;
import net.syscon.elite.service.ConfigException;
import net.syscon.elite.service.EntityNotFoundException;
import net.syscon.elite.service.LocationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.PatternSyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test cases for {@link LocationServiceImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationServiceImplTest {

    @Mock private LocationRepository locationRepository;
    @Mock private AgencyRepository agencyRepository;
    private Properties groupsProperties;

    private LocationService locationService;
    private Location cell1 = Location.builder().locationPrefix("cell1").build();
    private Location cell2 = Location.builder().locationPrefix("cell2").build();
    private Location cell3 = Location.builder().locationPrefix("cell3").build();
    private Location cell4 = Location.builder().locationPrefix("cell4").build();

    @Before
    public void init() throws IOException {
        locationService = new LocationServiceImpl(agencyRepository, locationRepository, null, null, "WING", 2, null);
        groupsProperties = new Properties();
        ReflectionTestUtils.setField(locationService, "groupsProperties", groupsProperties);
    }

    @Test
    public void getUserLocations() throws Exception {
        
        List<Agency> agencies =  Collections.singletonList(Agency.builder().agencyId("LEI").build());
 
        Mockito.when(agencyRepository.findAgenciesForCurrentCaseloadByUsername("me")).thenReturn(agencies);
        
        List<Location> locations = new ArrayList<>();
        Location location = createTestLocation();
        locations.add(location);
        Mockito.when(locationRepository.findLocationsByAgencyAndType("LEI","WING",2)).thenReturn(locations);

        List<Location> returnedLocations = locationService.getUserLocations("me");

        assertFalse(returnedLocations.isEmpty());
        Location returnedLocation = returnedLocations.get(1);
        assertEquals(location.getLocationId().longValue(), returnedLocation.getLocationId().longValue());
        assertEquals(location.getAgencyId(), returnedLocation.getAgencyId());
        assertEquals(location.getLocationType(), returnedLocation.getLocationType());
        assertEquals(location.getDescription(), returnedLocation.getDescription());
    }

    private static Location createTestLocation() {
        Location location = new Location();

        location.setLocationId(1L);
        location.setAgencyId("LEI");
        location.setLocationType("WING");
        location.setDescription("LEI-A");

        return location;
    }

    @Test
    public void testGetGroupSinglePattern() {

        Mockito.when(locationRepository.findLocationsByAgencyAndType("LEI", "CELL", 1)).thenReturn(Arrays.asList(//
                cell1, cell2, cell3, cell4));
        groupsProperties.setProperty("LEI_mylist", "cell[13]||cell4");

        final List<Location> group = locationService.getGroup("LEI", "mylist");

        assertThat(group).asList().containsExactly(cell1, cell3, cell4);
    }

    @Test
    public void testGetGroupMultipleMatches() {

        Mockito.when(locationRepository.findLocationsByAgencyAndType("LEI", "CELL", 1)).thenReturn(Arrays.asList(//
                cell1, cell2, cell3, cell4));
        groupsProperties.setProperty("LEI_mylist", "cell3,cell[13]");

        final List<Location> group = locationService.getGroup("LEI", "mylist");

        assertThat(group).asList().containsExactly(cell3, cell1);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetGroupNoName() throws Exception {

        locationService.getGroup("LEI", "does-not-exist");
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetGroupNoAgency() throws Exception {

        locationService.getGroup("does-not-exist", "mylist");
    }

    @Test(expected = PatternSyntaxException.class)
    public void testGetGroupInvalidPattern() throws Exception {
        Mockito.when(locationRepository.findLocationsByAgencyAndType("LEI", "CELL", 1)).thenReturn(Arrays.asList(//
                cell1, cell2, cell3, cell4));
        groupsProperties.setProperty("LEI_mylist", "cell[13]||[");

        locationService.getGroup("LEI", "mylist");
    }

    @Test(expected=ConfigException.class)
    public void testGetGroupNoCells() throws Exception {
        Mockito.when(locationRepository.findLocationsByAgencyAndType("LEI", "CELL", 1)).thenReturn(Arrays.asList(//
                cell1, cell2, cell3, cell4));
        groupsProperties.setProperty("LEI_mylist", "");

        locationService.getGroup("LEI", "mylist");
    }

    @Test
    public void testGetAvailableGroups() {

        groupsProperties.setProperty("LEI_mylist1", "cell1,cell2");
        groupsProperties.setProperty("LEI_mylist2", "cell3,cell4");
        groupsProperties.setProperty("BXI_mylist1", "cell5,cell6");

        final List<String> groups = locationService.getAvailableGroups("LEI");

        assertThat(groups).asList().contains("mylist1", "mylist2");
    }

    @Test
    public void testGetAvailableGroupsNone() {

        groupsProperties.setProperty("LEI_mylist1", "cell1,cell2");
        groupsProperties.setProperty("LEI_mylist2", "cell3,cell4");
        groupsProperties.setProperty("BXI_mylist1", "cell5,cell6");

        final List<String> groups = locationService.getAvailableGroups("OTHER");

        assertThat(groups).asList().isEmpty();
    }
}
