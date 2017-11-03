package net.syscon.elite.security;

import net.syscon.elite.api.model.UserDetail;
import net.syscon.elite.api.model.UserRole;
import net.syscon.elite.repository.UserRepository;
import net.syscon.elite.service.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Cacheable("loadUserByUsername")
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final UserDetail userDetail = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException.withId(username));
		List<UserRole> roles = userRepository.findRolesByUsername(username);

		Set<GrantedAuthority> authorities = roles.stream()
				.filter(Objects::nonNull)
				.map(role -> new SimpleGrantedAuthority("ROLE_" + StringUtils.upperCase(StringUtils.replaceAll(role.getRoleCode(),"-", "_"))))
				.collect(Collectors.toSet());

		return new UserDetailsImpl(username, null, authorities, userDetail.getAdditionalProperties());
	}
}
