package de.thkoeln.archilab.fae.GPSMockService.domain;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface TrackerEvent {

  String getId();

  String getKey();

  Long getVersion();

  ZonedDateTime getTime();

  byte[] getPayload(ObjectMapper objectMapper) throws JsonProcessingException;

  Class<?> getEntityType();

  String getType();
}
