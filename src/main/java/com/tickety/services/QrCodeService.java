package com.tickety.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tickety.dtos.responses.QrCodeResponse;
import com.tickety.entities.QrCode;
import com.tickety.entities.Ticket;
import com.tickety.enums.QrCodeStatus;
import com.tickety.exceptions.QrCodeGenerationException;
import com.tickety.exceptions.ResourceNotFoundException;
import com.tickety.repositories.QrCodeRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;

    @Value("${app.qr.image-size:300}")
    private int imageSize;

    @Value("${app.qr.image-format:PNG}")
    private String imageFormat;

    @Transactional
    public QrCode generateQrCode(Ticket ticket) {
        String qrValue = generateUniqueValue(ticket);

        byte[] imageData = generateQrCodeImage(qrValue);

        QrCode qrCode = QrCode.builder()
                .value(qrValue)
                .status(QrCodeStatus.ACTIVE)
                .imageData(imageData)
                .ticket(ticket)
                .build();

        return qrCodeRepository.save(qrCode);
    }

    @Transactional(readOnly = true)
    public QrCodeResponse getQrCodeByTicket(Ticket ticket) {
        QrCode qrCode = qrCodeRepository
                .findByTicket(ticket)
                .orElseThrow(() -> ResourceNotFoundException.qrCode("for ticket " + ticket.getId()));

        return toResponse(qrCode);
    }

    @Transactional(readOnly = true)
    public QrCode findByValue(String value) {
        return qrCodeRepository.findByValue(value).orElseThrow(() -> ResourceNotFoundException.qrCode(value));
    }

    private String generateUniqueValue(Ticket ticket) {
        String baseValue;
        int attempt = 0;

        do {
            String uniquePart = UUID.randomUUID().toString();
            baseValue = String.format(
                    "TICKET-%s-%s-%s",
                    ticket.getId(), ticket.getTicketType().getEvent().getId(), uniquePart);
            attempt++;
        } while (qrCodeRepository.existsByValue(baseValue) && attempt < 5);

        if (qrCodeRepository.existsByValue(baseValue)) {
            throw new QrCodeGenerationException("Failed to generate unique QR code value", null);
        }

        return baseValue;
    }

    private byte[] generateQrCodeImage(String data) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, imageSize, imageSize, hints);

            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, imageFormat, outputStream);

            return outputStream.toByteArray();

        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code image for data: {}", data);
            throw new QrCodeGenerationException("Failed to generate QR code image", e);
        }
    }

    private QrCodeResponse toResponse(QrCode qrCode) {
        String base64Image = Base64.getEncoder().encodeToString(qrCode.getImageData());

        return new QrCodeResponse(qrCode.getId(), qrCode.getValue(), base64Image);
    }
}
