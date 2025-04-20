package dev.sagar.tickety.inventory.service;

import dev.sagar.tickety.inventory.dto.EventResponse;
import dev.sagar.tickety.inventory.dto.VenueResponse;
import dev.sagar.tickety.inventory.repository.EventRepository;
import dev.sagar.tickety.inventory.repository.VenueRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final EventRepository eventRepository;
  private final VenueRepository venueRepository;

  public List<EventResponse> getAllEvents() {
    return eventRepository.findAll().stream()
        .map(
            event ->
                EventResponse.builder()
                    .event(event.getName())
                    .capacity(event.getLeftCapacity())
                    .venue(
                        VenueResponse.builder()
                            .id(event.getVenue().getId())
                            .name(event.getVenue().getName())
                            .totalCapacity(event.getVenue().getTotalCapacity())
                            .build())
                    .build())
        .toList();
  }

  public VenueResponse getVenueInformation(Long venueId) {
    return venueRepository
        .findById(venueId)
        .map(
            venue ->
                VenueResponse.builder()
                    .id(venue.getId())
                    .name(venue.getName())
                    .totalCapacity(venue.getTotalCapacity())
                    .build())
        .orElse(null);
  }
}
