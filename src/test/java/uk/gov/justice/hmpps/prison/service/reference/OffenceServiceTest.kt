package uk.gov.justice.hmpps.prison.service.reference

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.hmpps.prison.api.model.HOCodeDto
import uk.gov.justice.hmpps.prison.repository.jpa.model.HOCode
import uk.gov.justice.hmpps.prison.repository.jpa.repository.HOCodeRepository
import uk.gov.justice.hmpps.prison.repository.jpa.repository.OffenceRepository
import uk.gov.justice.hmpps.prison.service.EntityAlreadyExistsException
import java.util.Optional

internal class OffenceServiceTest {
  private val offenceRepository: OffenceRepository = mock()
  private val hoCodeRepository: HOCodeRepository = mock()
  private val service = OffenceService(
    offenceRepository,
    hoCodeRepository,
  )

  @Nested
  @DisplayName("HO Codes related tests")
  inner class HOCodesTest {
    @Test
    internal fun `Will create a new HO Code`() {
      service.createHomeOfficeCode(
        HOCodeDto.builder()
          .code("2XX")
          .description("2XX Description")
          .build()
      )

      verify(hoCodeRepository, times(1)).save(
        HOCode.builder()
          .code("2XX")
          .description("2XX Description")
          .build()
      )
    }

    @Test
    internal fun `Will fail when trying to create a HO Code if HO Code already exists`() {
      val hoCode = HOCode.builder().code("2XX").build()
      whenever(hoCodeRepository.findById("2XX")).thenReturn(Optional.of(hoCode))

      assertThrows<EntityAlreadyExistsException> {
        service.createHomeOfficeCode(HOCodeDto.builder().code("2XX").build())
      }
    }
  }
}