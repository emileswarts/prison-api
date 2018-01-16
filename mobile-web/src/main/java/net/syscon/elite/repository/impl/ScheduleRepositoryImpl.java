package net.syscon.elite.repository.impl;

import net.syscon.elite.api.model.PrisonerSchedule;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.repository.ScheduleRepository;
import net.syscon.elite.repository.mapping.StandardBeanPropertyRowMapper;
import net.syscon.util.DateTimeConverter;
import net.syscon.util.IQueryBuilder;

import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class ScheduleRepositoryImpl extends RepositoryBase implements ScheduleRepository {

    private static final StandardBeanPropertyRowMapper<PrisonerSchedule> EVENT_ROW_MAPPER = new StandardBeanPropertyRowMapper<>(PrisonerSchedule.class);

    
    @Override
    public List<PrisonerSchedule> getLocationActivities(Long locationId, LocalDate fromDate, LocalDate toDate, String orderByFields, Order order) {
        Objects.requireNonNull(locationId, "locationId is a required parameter");

        String initialSql = getQuery("GET_ACTIVITIES_AT_LOCATION");
        IQueryBuilder builder = queryBuilderFactory.getQueryBuilder(initialSql, EVENT_ROW_MAPPER.getFieldMap());

        String sql = builder
                .addOrderBy(order, orderByFields)
                .build();

        return jdbcTemplate.query(
                sql,
                createParams("locationId", locationId,
                        "fromDate", new SqlParameterValue(Types.DATE,  DateTimeConverter.toDate(fromDate)),
                        "toDate", new SqlParameterValue(Types.DATE,  DateTimeConverter.toDate(toDate))),
                EVENT_ROW_MAPPER);
    }
    
    @Override
    public List<PrisonerSchedule> getLocationAppointments(Long locationId, LocalDate fromDate, LocalDate toDate, String orderByFields, Order order) {
        Objects.requireNonNull(locationId, "locationId is a required parameter");

        String initialSql = getQuery("GET_APPOINTMENTS_AT_LOCATION");
        IQueryBuilder builder = queryBuilderFactory.getQueryBuilder(initialSql, EVENT_ROW_MAPPER.getFieldMap());

        String sql = builder
                .addOrderBy(order, orderByFields)
                .build();

        return jdbcTemplate.query(
                sql,
                createParams("locationId", locationId,
                        "fromDate", new SqlParameterValue(Types.DATE,  DateTimeConverter.toDate(fromDate)),
                        "toDate", new SqlParameterValue(Types.DATE,  DateTimeConverter.toDate(toDate))),
                EVENT_ROW_MAPPER);
    }
    
    @Override
    public List<PrisonerSchedule> getLocationVisits(Long locationId, LocalDate fromDate, LocalDate toDate, String orderByFields, Order order) {
        Objects.requireNonNull(locationId, "locationId is a required parameter");

        String initialSql = getQuery("GET_VISITS_AT_LOCATION");
        IQueryBuilder builder = queryBuilderFactory.getQueryBuilder(initialSql, EVENT_ROW_MAPPER.getFieldMap());

        String sql = builder
                .addOrderBy(order, orderByFields)
                .build();


        return jdbcTemplate.query(
                sql,
                createParams("locationId", locationId,
                        "fromDate", new SqlParameterValue(Types.DATE,  DateTimeConverter.toDate(fromDate)),
                        "toDate", new SqlParameterValue(Types.DATE,  DateTimeConverter.toDate(toDate))),
                EVENT_ROW_MAPPER);
    }
}