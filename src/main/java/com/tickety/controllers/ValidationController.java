package com.tickety.controllers;

import com.tickety.dtos.requests.ValidateTicketRequest;
import com.tickety.dtos.responses.ValidationListResponse;
import com.tickety.dtos.responses.ValidationResponse;
import com.tickety.services.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/events/{eventId}/validations")
@RequiredArgsConstructor
@Tag(name = "Validations", description = "Ticket validation APIs for event staff")
@SecurityRequirement(name = "bearerAuth")
public class ValidationController {

    private final ValidationService validationService;

    @Operation(summary = "Validate ticket", description = "Validate a ticket for a specific event")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Ticket validated successfully",
                        content = @Content(schema = @Schema(implementation = ValidationResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Ticket validation error",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - Staff role required",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ValidationResponse> validateTicket(
            @PathVariable UUID eventId, @Valid @RequestBody ValidateTicketRequest request) {
        log.info("Validating ticket for event: {}", eventId);
        ValidationResponse response = validationService.validateTicket(eventId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get event validations",
            description = "Retrieve a paginated list of ticket validations for a specific event")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Validations retrieved successfully",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema =
                                                                @Schema(
                                                                        implementation =
                                                                                ValidationListResponse.class)))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - Staff or Organizer role required",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    @PreAuthorize("hasAnyRole('STAFF', 'ORGANIZER')")
    public ResponseEntity<Page<ValidationListResponse>> getEventValidations(
            @PathVariable UUID eventId, @PageableDefault(size = 50) Pageable pageable) {
        log.info("Fetching validations for event: {}", eventId);
        Page<ValidationListResponse> response = validationService.getEventValidations(eventId, pageable);
        return ResponseEntity.ok(response);
    }
}
