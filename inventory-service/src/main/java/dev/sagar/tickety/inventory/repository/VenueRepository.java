package dev.sagar.tickety.inventory.repository;

import dev.sagar.tickety.inventory.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {}
