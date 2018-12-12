package net.syscon.elite.repository.impl;

import net.syscon.elite.api.model.*;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.repository.MovementsRepository;
import net.syscon.elite.repository.mapping.StandardBeanPropertyRowMapper;
import net.syscon.util.DateTimeConverter;
import net.syscon.util.IQueryBuilder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Repository
public class MovementsRepositoryImpl extends RepositoryBase implements MovementsRepository {

    private final StandardBeanPropertyRowMapper<Movement> MOVEMENT_MAPPER = new StandardBeanPropertyRowMapper<>(Movement.class);
    private final StandardBeanPropertyRowMapper<OffenderMovement> OFFENDER_MOVEMENT_MAPPER = new StandardBeanPropertyRowMapper<>(OffenderMovement.class);
    private final StandardBeanPropertyRowMapper<RollCount> ROLLCOUNT_MAPPER = new StandardBeanPropertyRowMapper<>(RollCount.class);
    private final StandardBeanPropertyRowMapper<OffenderIn> OFFENDER_IN_MAPPER = new StandardBeanPropertyRowMapper<>(OffenderIn.class);

    private static final String MOVEMENT_DATE_CLAUSE = " AND OEM.MOVEMENT_DATE = :movementDate";

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
    public List<OffenderMovement> getOffendersOut(String agencyId, LocalDate movementDate) {
        String sql = getQuery("GET_OFFENDERS_OUT_TODAY");
        return jdbcTemplate.query(sql, createParams(
                "agencyId", agencyId,
                "movementDate", DateTimeConverter.toDate(movementDate)),
                OFFENDER_MOVEMENT_MAPPER);
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

        int outMovements = movementsGroupedByDirection.containsKey("OUT") ? movementsGroupedByDirection.get("OUT").size() : 0;
        int inMovements = movementsGroupedByDirection.containsKey("IN") ? movementsGroupedByDirection.get("IN").size() : 0;

        return MovementCount.builder()
                .out(outMovements)
                .in(inMovements)
                .build();
    }

    @Override
    public List<OffenderMovement> getEnrouteMovementsOffenderMovementList(String agencyId, LocalDate date, String orderByFields, Order order) {

        String initialSql = getQuery("GET_ENROUTE_OFFENDER_MOVEMENTS");

        initialSql = date == null ? initialSql : initialSql + MOVEMENT_DATE_CLAUSE;

        IQueryBuilder builder = queryBuilderFactory.getQueryBuilder(initialSql, OFFENDER_MOVEMENT_MAPPER.getFieldMap());

        String sql = builder
                .addOrderBy(order, orderByFields)
                .build();

        return jdbcTemplate.query(sql, createParams(
                "agencyId", agencyId, "movementDate", DateTimeConverter.toDate(date)), OFFENDER_MOVEMENT_MAPPER);
    }

    @Override
    public int getEnrouteMovementsOffenderCount(String agencyId, LocalDate date) {

        return jdbcTemplate.queryForObject(getQuery("GET_ENROUTE_OFFENDER_COUNT"), createParams(
                "agencyId", agencyId, "movementDate", DateTimeConverter.toDate(date)), Integer.class);
    }

    @Override
    public List<OffenderIn> getOffendersIn(String agencyId, LocalDate movementDate) {
        return jdbcTemplate.query(getQuery("GET_OFFENDER_MOVEMENTS_IN"),
                createParams(
                    "agencyId", agencyId,
                    "movementDate", DateTimeConverter.toDate(movementDate)),
                OFFENDER_IN_MAPPER);
    }
}
