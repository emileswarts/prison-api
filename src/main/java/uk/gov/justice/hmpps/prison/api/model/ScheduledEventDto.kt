package uk.gov.justice.hmpps.prison.api.model
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class ScheduledEventDto(
  val bookingId: Long?,
  val eventClass: String?,
  val eventId: Long?,
  val eventStatus: String?,
  val eventType: String?,
  val eventTypeDesc: String?,
  val eventSubType: String?,
  val eventSubTypeDesc: String?,
  val eventDate: LocalDate?,
  val startTime: LocalDateTime?,
  val endTime: LocalDateTime?,
  val eventLocation: String?,
  val eventLocationId: Long?,
  val agencyId: String?,
  val eventSource: String?,
  val eventSourceCode: String?,
  val eventSourceDesc: String?,
  val eventOutcome: String?,
  val performance: String?,
  val outcomeComment: String?,
  val paid: Boolean?,
  val payRate: BigDecimal?,
  val locationCode: String?,
  val createUserId: String?,
) {
  fun toScheduledEvent() = ScheduledEvent(
    this.bookingId,
    this.eventClass,
    this.eventId,
    this.eventStatus,
    this.eventType,
    this.eventTypeDesc,
    this.eventSubType,
    this.eventSubTypeDesc,
    this.eventDate,
    this.startTime,
    this.endTime,
    this.eventLocation,
    this.eventLocationId,
    this.agencyId,
    this.eventSource,
    this.eventSourceCode,
    this.eventSourceDesc,
    this.eventOutcome,
    this.performance,
    this.outcomeComment,
    this.paid,
    this.payRate,
    this.locationCode,
    this.createUserId,
  )
}
