package net.syscon.elite.service.support;

import lombok.*;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.api.support.PageRequest;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class SearchOffenderRequest extends PageRequest {
    private String username;
    private String keywords;
    private boolean returnIep;
    private boolean returnAlerts;
    private boolean returnCategory;
    private String convictedStatus;
    private String locationPrefix;
    private List<String> alerts;
    private LocalDate fromDob;
    private LocalDate toDob;

    @Builder(toBuilder = true)
    public SearchOffenderRequest(final String orderBy, final Order order, final long offset, final long limit, final String username, final String keywords,
                                 final String locationPrefix, final List<String> alerts, final boolean returnIep, final boolean returnAlerts, final boolean returnCategory, final String convictedStatus,
                                 final LocalDate fromDob, final LocalDate toDob) {
        super(orderBy, order, offset, limit);

        this.username = username;
        this.keywords = keywords;
        this.locationPrefix = locationPrefix;
        this.alerts = alerts;
        this.returnAlerts = returnAlerts;
        this.returnIep = returnIep;
        this.returnCategory = returnCategory;
        this.convictedStatus = convictedStatus;
        this.fromDob = fromDob;
        this.toDob = toDob;
    }
}
