package com.tickety.dtos.responses;

import java.util.UUID;

public record QrCodeResponse(UUID id, String value, String imageDataBase64) {}
