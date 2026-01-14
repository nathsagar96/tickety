package com.tickety.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record QrCodeResponse(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the QR code")
                UUID id,
        @Schema(example = "QR123456789", description = "QR code value") String value,
        @Schema(example = "iVBORw0KGgoAAAANSUhEUgAA...", description = "Base64 encoded QR code image data")
                String imageDataBase64) {}
