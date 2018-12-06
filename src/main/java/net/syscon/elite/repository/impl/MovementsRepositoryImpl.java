package net.syscon.elite.repository.impl;

import net.syscon.elite.api.model.Movement;
import net.syscon.elite.api.model.MovementCount;
import net.syscon.elite.api.model.OffenderMovement;
import net.syscon.elite.api.model.RollCount;
import net.syscon.elite.repository.MovementsRepository;
import net.syscon.elite.repository.mapping.StandardBeanPropertyRowMapper;
import net.syscon.util.DateTimeConverter;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Repository
public class MovementsRepositoryImpl extends RepositoryBase implements MovementsRepository {

    private final StandardBeanPropertyRowMapper<Movement> MOVEMENT_MAPPER = new StandardBeanPropertyRowMapper<>(Movement.class);
    private final StandardBeanPropertyRowMapper<OffenderMovement> OFFENDER_MOVEMENT_MAPPER = new StandardBeanPropertyRowMapper<>(OffenderMovement.class);
    private final StandardBeanPropertyRowMapper<RollCount> ROLLCOUNT_MAPPER = new StandardBeanPropertyRowMapper<>(RollCount.class);

    @Override
    public List<Movement> getRecentMovementsByDate(LocalDateTime fromDateTime, LocalDate movementDate) {
        String sql = getQuery("GET_RECENT_MOVEMENTS");
        return jdbcTemplate.query(sql, createParams("fromDateTime", DateTimeConverter.fromLocalDateTime(fromDateTime),
                "movementDate", DateTimeConverter.toDate(movementDate)), MOVEMENT_MAPPER);
    }

    @Override
    public List<Movement> getRecentMovementsByOffenders(List<String> offenderNumbers, List<String> movementTypes) {
        if (movementTypes.size() != 0) {
            return jdbcTemplate.query(getQuery("GET_RECENT_MOVEMENTS_BY_OFFENDERS_AND_MOVEMENT_TYPES"), createParams(
                    "offenderNumbers", offenderNumbers,
                    "movementTypes", movementTypes),
                    MOVEMENT_MAPPER);
        }

        return jdbcTemplate.query(getQuery("GET_RECENT_MOVEMENTS_BY_OFFENDERS"), createParams(
                "offenderNumbers", offenderNumbers),
                MOVEMENT_MAPPER);
    }

    @Override
    public List<RollCount> getRollCount(String agencyId, String certifiedFlag) {
        String sql = getQuery("GET_ROLL_COUNT");
        return jdbcTemplate.query(sql, createParams(
                "agencyId", agencyId,
                "certifiedFlag", certifiedFlag,
                "livingUnitId", null),
                ROLLCOUNT_MAPPER);
    }

    @Override
    public MovementCount getMovementCount(String agencyId, LocalDate date) {

        List<Movement> movements = jdbcTemplate.query(
                getQuery("GET_ROLLCOUNT_MOVEMENTS"),
                createParams("agencyId", agencyId, "movementDate", DateTimeConverter.toDate(date)), MOVEMENT_MAPPER);

        Map<String, List<Movement>> movementsGroupedByDirection = movements.stream().filter(movement ->
                (movement.getDirectionCode().equals("IN") && movement.getToAgency().equals(agencyId)) ||
                (movement.getDirectionCode().equals("OUT") && movement.getFromAgency().equals(agencyId)))
                .collect(groupingBy(Movement::getDirectionCode));

        List<String> outOffenders =  movementsGroupedByDirection.containsKey("OUT") ?
                movementsGroupedByDirection
                .get("OUT")
                .stream()
                .map(Movement::getOffenderNo)
                .collect(Collectors.toList())
                :
                Collections.emptyList();

        List<String> inOffenders = movementsGroupedByDirection.containsKey("IN") ?
                movementsGroupedByDirection
                .get("IN")
                .stream()
                .map(Movement::getOffenderNo)
                .collect(Collectors.toList())
                :
                Collections.emptyList();

        return MovementCount.builder()
                .offendersOut(outOffenders)
                .offendersIn(inOffenders)
                .build();
    }

    @Override
    public List<OffenderMovement> getEnrouteMovementsOffenderMovementList(String agencyId, LocalDate date) {

        return jdbcTemplate.query(getQuery("GET_ENROUTE_OFFENDER_MOVEMENTS"), createParams(
                "agencyId", agencyId, "movementDate", DateTimeConverter.toDate(date)), OFFENDER_MOVEMENT_MAPPER);
    }

    @Override
    public int getEnrouteMovementsOffenderCount(String agencyId, LocalDate date) {

        return jdbcTemplate.queryForObject(getQuery("GET_ENROUTE_OFFENDER_COUNT"), createParams(
                "agencyId", agencyId, "movementDate", DateTimeConverter.toDate(date)), Integer.class);
    }
}