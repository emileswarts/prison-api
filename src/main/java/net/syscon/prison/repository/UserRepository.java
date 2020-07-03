package net.syscon.prison.repository;

import net.syscon.prison.api.model.AccessRole;
import net.syscon.prison.api.model.StaffUserRole;
import net.syscon.prison.api.model.UserDetail;
import net.syscon.prison.api.model.UserRole;
import net.syscon.prison.api.support.Page;
import net.syscon.prison.api.support.PageRequest;
import net.syscon.prison.service.filters.NameFilter;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<UserDetail> findByUsername(String username);

    List<UserRole> findRolesByUsername(String username, String query);

    List<AccessRole> findAccessRolesByUsernameAndCaseload(String username, String caseload, boolean includeAdmin);

    void updateWorkingCaseLoad(String username, String caseLoadId);

    Optional<UserDetail> findByStaffIdAndStaffUserType(Long staffId, String userType);

    Optional<Long> getRoleIdForCode(String roleCode);

    Optional<AccessRole> getRoleByCode(String roleCode);

    boolean isUserAssessibleCaseloadAvailable(String caseload, String username);

    void addUserAssessibleCaseload(String caseload, String username);

    List<StaffUserRole> getAllStaffRolesForCaseload(String caseload, String roleCode);

    boolean isRoleAssigned(String username, String caseload, long roleId);

    void addRole(String username, String caseload, Long roleId);

    void removeRole(String username, String caseload, Long roleId);

    List<UserDetail> findAllUsersWithCaseload(String caseloadId, String missingCaseloadId);

    List<UserDetail> getUserListByUsernames(List<String> usernames);

    Page<UserDetail> findUsersByCaseload(String agencyId, String accessRole, NameFilter nameFilter, PageRequest pageRequest);

    Page<UserDetail> getUsersAsLocalAdministrator(String laaUsername, String accessRole, NameFilter nameFilter, PageRequest pageRequest);

    Page<UserDetail> findUsers(String accessRole, NameFilter nameFilter, PageRequest pageWithDefaults);
}