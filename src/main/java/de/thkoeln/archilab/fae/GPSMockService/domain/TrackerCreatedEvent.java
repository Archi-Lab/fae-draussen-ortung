package de.thkoeln.archilab.fae.GPSMockService.domain;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

public class TrackerCreatedEvent implements TrackerEvent {

  public TrackerCreatedEvent(Tracker tracker)
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
    return "tracker-created";
  }
}
