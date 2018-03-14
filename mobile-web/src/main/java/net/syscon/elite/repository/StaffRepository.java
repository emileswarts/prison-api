package net.syscon.elite.repository;

import net.syscon.elite.api.model.StaffDetail;
import net.syscon.elite.api.model.StaffLocationRole;
import net.syscon.elite.api.support.Page;
import net.syscon.elite.api.support.PageRequest;

import java.util.Optional;

public interface StaffRepository {
    Optional<StaffDetail> findByStaffId(Long staffId);

    Page<StaffLocationRole> findStaffByAgencyPositionRole(String agencyId, String position, String role, String nameFilter, PageRequest pageRequest);

    Page<StaffLocationRole> findStaffByAgencyRole(String agencyId, String role, String nameFilter, PageRequest pageRequest);
}