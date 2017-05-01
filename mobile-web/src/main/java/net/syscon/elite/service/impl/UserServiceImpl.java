package net.syscon.elite.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import net.syscon.elite.persistence.CaseLoadRepository;
import net.syscon.elite.persistence.UserRepository;
import net.syscon.elite.service.UserService;
import net.syscon.elite.web.api.model.CaseLoad;
import net.syscon.elite.web.api.model.UserDetails;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;
	private CaseLoadRepository caseLoadRepository;

	@Inject
	public void setCaseLoadRepository(final CaseLoadRepository caseLoadRepository) {
		this.caseLoadRepository = caseLoadRepository;
	}
	
	@Inject
	public void setUserRepository(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails getUserByStaffId(final Long staffId) {
		return userRepository.findByStaffId(staffId);
	}

	@Override
	public UserDetails getUserByUsername(final String username) {
		return userRepository.findByUsername(username);
	}
	

	@Override
	public CaseLoad getActiveCaseLoad(final Long staffId) {
		final UserDetails userDetails = userRepository.findByStaffId(staffId);
		Assert.notNull(userDetails, String.format("User with staffId %d was not found!", staffId));
		return caseLoadRepository.find(userDetails.getActiveCaseLoadId());
	}

	@Override
	public List<CaseLoad> getCaseLoads(final Long staffId) {
		return caseLoadRepository.findCaseLoadsByStaffId(staffId);
	}

	@Override
	public void setActiveCaseLoad(final Long staffId, final String caseLoadId) {
		final Iterator<CaseLoad> it = caseLoadRepository.findCaseLoadsByStaffId(staffId).iterator();
		boolean found = false;
		while (!found && it.hasNext()) {
			final CaseLoad caseLoad = it.next();
			found = caseLoadId.equals(caseLoad.getCaseLoadId());
		}
		if (!found) {
			throw new AccessDeniedException(String.format("The user does not have access to the caseLoadid = %d", caseLoadId));
		} else {
			caseLoadRepository.updateCurrentLoad(staffId, caseLoadId);
		}
	}

}
