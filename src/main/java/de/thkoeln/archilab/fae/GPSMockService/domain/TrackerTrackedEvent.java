package de.thkoeln.archilab.fae.GPSMockService.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

public class TrackerTrackedEvent implements TrackerEvent {

  public TrackerTrackedEvent(Tracker tracker)
  {
    this.id = UUID.randomUUID();
    this.tracker = tracker;
    this.coordinate = tracker.getCurrentPosition();
    this.instant = Instant.now();
  }

  final UUID id;
  final Tracker tracker;
  final Coordinate coordinate;
  final Instant instant;

  // TODO: There should be a Event/Message interface for this part of the implementation
  // This is the DomainEvent part in the REWEdigital example (https://github.com/rewe-digital/integration-patterns)
  // The ApplicationEvent & persisting of Events/Messages is neglected here for simplicity reasons

  public String getId() {
    return id.toString();
  }

  public String getKey() {
    return tracker.getId();
  }

  //The tracker Entity doesn't implement any versioning patterns (yet), therefore event versions are always 0
  public Long getVersion() {
    return 0L;
  }

  public ZonedDateTime getTime() {
    return instant.atZone(ZoneId.systemDefault());
  }

  public byte[] getPayload(ObjectMapper objectMapper) throws JsonProcessingException {
    return objectMapper.writeValueAsBytes(tracker);
  }

  public Class<?> getEntityType() {
    return tracker.getClass();
  }

  public String getType()
  {
    return "tracker-tracked";
  }

}
