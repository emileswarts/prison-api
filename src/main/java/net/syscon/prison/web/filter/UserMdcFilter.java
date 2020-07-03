package net.syscon.prison.web.filter;

import com.microsoft.applicationinsights.TelemetryClient;
import lombok.extern.slf4j.Slf4j;
import net.syscon.prison.security.AuthenticationFacade;
import net.syscon.util.IpAddressHelper;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static net.syscon.util.MdcUtility.IP_ADDRESS;
import static net.syscon.util.MdcUtility.USER_ID_HEADER;

@Slf4j
@Component
@Order(1)
public class UserMdcFilter implements Filter {

    private final AuthenticationFacade userSecurityUtils;
    private final TelemetryClient telemetryClient;

    @Autowired(required = false)
    private BuildProperties buildProperties;

    @Autowired
    public UserMdcFilter(final AuthenticationFacade userSecurityUtils, TelemetryClient telemetryClient) {
        this.userSecurityUtils = userSecurityUtils;
        this.telemetryClient = telemetryClient;
    }

    @Override
    public void init(final FilterConfig filterConfig) {
        // Initialise - no functionality
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final var currentUsername = userSecurityUtils.getCurrentUsername();

        try {
            if (currentUsername != null) {
                MDC.put(USER_ID_HEADER, currentUsername);
                telemetryClient.getContext().getUser().setId(currentUsername);
            }
            telemetryClient.getContext().getComponent().setVersion(getVersion());
            final var ip = IpAddressHelper.retrieveIpFromRemoteAddr((HttpServletRequest) request);
            MDC.put(IP_ADDRESS, ip);
            telemetryClient.getContext().getLocation().setIp(ip);

            chain.doFilter(request, response);
        } finally {
            if (currentUsername != null) {
                MDC.remove(USER_ID_HEADER);
            }
            MDC.remove(IP_ADDRESS);
        }
    }

    @Override
    public void destroy() {
        // Destroy - no functionality
    }

    private String getVersion() {
        return buildProperties == null ? "N/A" : buildProperties.getVersion();
    }

}