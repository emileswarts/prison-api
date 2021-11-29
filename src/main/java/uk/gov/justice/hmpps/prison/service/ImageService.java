package uk.gov.justice.hmpps.prison.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.hmpps.prison.api.model.ImageDetail;
import uk.gov.justice.hmpps.prison.core.HasWriteScope;
import uk.gov.justice.hmpps.prison.repository.jpa.model.OffenderImage;
import uk.gov.justice.hmpps.prison.repository.jpa.repository.OffenderImageRepository;
import uk.gov.justice.hmpps.prison.repository.jpa.repository.OffenderRepository;
import java.util.List;
import java.util.Optional;
import uk.gov.justice.hmpps.prison.security.VerifyOffenderAccess;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ImageService {

    private final OffenderImageRepository offenderImageRepository;

    private final OffenderRepository offenderRepository;

    public List<ImageDetail> findOffenderImagesFor(final String offenderNumber) {
        if (offenderRepository.findByNomsId(offenderNumber).isEmpty()) throw EntityNotFoundException.withId(offenderNumber);
        return offenderImageRepository.getImagesByOffenderNumber(offenderNumber).stream()
                .map(OffenderImage::transform)
                .collect(toList());
    }

    public ImageDetail findImageDetail(final Long imageId) {
        return offenderImageRepository.findById(imageId)
            .map(OffenderImage::transform)
            .orElseThrow(EntityNotFoundException.withId(imageId));
    }

    public Optional<byte[]> getImageContent(final Long imageId, final boolean fullSizeImage) {
        return offenderImageRepository.findById(imageId)
          .map(i -> fullSizeImage ? i.getFullSizeImage() : i.getThumbnailImage());
    }

    public Optional<byte[]> getImageContent(final String offenderNo, final boolean fullSizeImage) {
        return offenderImageRepository.findLatestByOffenderNumber(offenderNo)
            .map(i -> fullSizeImage ? i.getFullSizeImage() : i.getThumbnailImage());
    }

    @PreAuthorize("hasRole('IMAGE_UPLOAD')")
    @VerifyOffenderAccess(overrideRoles = {"IMAGE_UPLOAD"})
    @HasWriteScope
    @Transactional
    public ImageDetail putImageForOffender(final String offenderNumber, final String imageData) {
        // Uses a 4:3 aspect ratio - will distort square photos! Compact cameras and phones use 4:3 for portrait.
        final int fullWidth = 427, fullHeight = 570;
        final int thumbWidth = 150, thumbHeight = 200;

        final var offender = offenderRepository
            .findOffenderByNomsId(offenderNumber)
            .orElseThrow(EntityNotFoundException.withMessage(format("No prisoner found for prisoner number %s", offenderNumber)));

        var booking = offender
            .getLatestBooking()
            .orElseThrow(EntityNotFoundException.withMessage(format("There are no bookings for %s", offenderNumber)));

        // Set the previously active facial image for this bookingId to inactive
        var previousImage = offenderImageRepository.findLatestByBookingId(booking.getBookingId());
        if (previousImage.isPresent()) {
            var prev = previousImage.get();
            log.info("Setting previous facial image to active=false - Id {}, bookingId {}, bookingSeq {}, offenderNo {}",
                prev.getId(), booking.getBookingId(), booking.getBookingSequence(), offenderNumber);
            prev.setActive(false);
            offenderImageRepository.save(prev);
        }

        try {
          byte[] receivedImage = Base64.getDecoder().decode(imageData);
          var fullImage = scaleImage(fullWidth, fullHeight, receivedImage);
          var thumbImage = scaleImage(thumbWidth, thumbHeight, receivedImage);

          var newImage = OffenderImage
            .builder()
            .captureDateTime(LocalDateTime.now())
            .orientationType("FRONT")
            .viewType("FACE")
            .imageType("OFF_BKG")
            .active(true)
            .sourceCode("GEN")
            .offenderBooking(booking)
            .thumbnailImage(thumbImage)
            .fullSizeImage(fullImage)
            .build();

            // Suggested method - does add the image but does not flush or return the imageId for the response
            // var savedImage = booking.addImage(newImage);

            var savedImage = offenderImageRepository.save(newImage);

            log.info("Saved image - Id {}, bookingId {}, bookingSeq {}, offenderNo {}",
                savedImage.getId(), booking.getBookingId(), booking.getBookingSequence(), offenderNumber);

            return savedImage.transform();
        } catch (Exception e) {
            throw BadRequestException.withMessage("Error scaling the image. Must be in JPEG format.");
        }
    }

    private byte[] scaleImage(int width, int height, byte[] source) throws IOException, IllegalArgumentException, InterruptedException {
        InputStream is = new ByteArrayInputStream(source);
        BufferedImage original = ImageIO.read(is);
        Image scaled = original.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        boolean ready = outputImage.getGraphics().drawImage(scaled, 0, 0, new ImageWait());
        if (!ready) {
            // Large images may take slightly longer to scale - not seen any (so far) though
            log.info("Initial image response not ready - waiting 500 ms");
            Thread.sleep(500);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpg", baos);
        return baos.toByteArray();
    }

    private static class ImageWait implements ImageObserver {
        @Override
        public boolean imageUpdate(Image img, int infoFlags, int x, int y, int width, int height) {
            log.info("Image update received a response for image {} x {}", width, height);
            return true;
        }
    }
}
