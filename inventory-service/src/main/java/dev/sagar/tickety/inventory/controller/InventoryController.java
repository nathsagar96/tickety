package dev.sagar.tickety.inventory.controller;

import dev.sagar.tickety.inventory.dto.EventResponse;
import dev.sagar.tickety.inventory.dto.VenueResponse;
import dev.sagar.tickety.inventory.service.InventoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  @GetMapping("/events")
  public List<EventResponse> getAllEvents() {
    return inventoryService.getAllEvents();
  }

  @GetMapping("/venues/{venueId}")
  public VenueResponse getVenueInformation(@PathVariable Long venueId) {
    return inventoryService.getVenueInformation(venueId);
  }
}
