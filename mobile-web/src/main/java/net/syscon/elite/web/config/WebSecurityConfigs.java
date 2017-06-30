package net.syscon.elite.web.config;

import net.syscon.elite.persistence.UserRepository;
import net.syscon.elite.security.ApiAuthenticationProvider;
import net.syscon.elite.security.EntryPointUnauthorizedHandler;
import net.syscon.elite.security.UserDetailsServiceImpl;
import net.syscon.elite.security.jwt.AuthenticationTokenFilter;
import net.syscon.elite.security.jwt.TokenManagement;
import net.syscon.elite.security.jwt.TokenSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.DataSourceHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Import({PersistenceConfigs.class, ServiceConfigs.class})
public class WebSecurityConfigs extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;

	@Bean
	public TokenSettings tokenSettings() {
		return new TokenSettings();
	}

	@Bean
	public HealthIndicator dbHealthIndicator(DataSource dataSource) {
		DataSourceHealthIndicator indicator = new DataSourceHealthIndicator(dataSource);
		return indicator;
	}

	@Bean
	public EntryPointUnauthorizedHandler unauthorizedHandler() {
		return new EntryPointUnauthorizedHandler();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() throws Exception {
		DaoAuthenticationProvider provider = new ApiAuthenticationProvider();

		provider.setUserDetailsService(userDetailsServiceBean());

		return provider;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public TokenManagement tokenManagement() {
		return new TokenManagement();
	}

	@Bean
	public AuthenticationTokenFilter authenticationTokenFilter() throws Exception {
		AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();

		authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());

		return authenticationTokenFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler())
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers( "/api/users/login").permitAll()
				.antMatchers("/api/management/info").permitAll()
				.antMatchers("/api/**").authenticated();
		// Custom JWT based authentication
		http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return new UserDetailsServiceImpl(userRepository);
	}
}
