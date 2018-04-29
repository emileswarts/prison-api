package net.syscon.elite.repository.impl;

import net.syscon.elite.api.model.PrisonerCustodyStatus;
import net.syscon.elite.repository.CustodyStatusRepository;
import net.syscon.elite.repository.mapping.StandardBeanPropertyRowMapper;
import net.syscon.util.DateTimeConverter;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CustodyStatusRepositoryImpl extends RepositoryBase implements CustodyStatusRepository {

    private final StandardBeanPropertyRowMapper<PrisonerCustodyStatus> CUSTODY_STATUS_MAPPER = new StandardBeanPropertyRowMapper<>(PrisonerCustodyStatus.class);

    @Override
    public List<PrisonerCustodyStatus> getRecentMovements(LocalDateTime fromDateTime) {
        String sql = getQuery("GET_RECENT_MOVEMENTS");
        return jdbcTemplate.query(sql, createParams("fromDateTime", DateTimeConverter.fromLocalDateTime(fromDateTime)), CUSTODY_STATUS_MAPPER);
    }
}

