package de.thkoeln.archilab.fae.GPSMockService.infrastructure.eventing;

import de.thkoeln.archilab.fae.GPSMockService.domain.TrackerEvent;
import de.thkoeln.archilab.fae.GPSMockService.domain.TrackerTrackedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaGateway {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaGateway.class);


  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;

  @Autowired
  public KafkaGateway(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper,
      @Value("${eventing.topic}") final String topic) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
  }

  public ListenableFuture<SendResult<String, String>> publishTrackingEvent(TrackerEvent trackingEvent) {
    LOGGER.info("publishing event {} to topic {}", trackingEvent.getId(), topic);
    return kafkaTemplate.send(topic, trackingEvent.getKey(), toTrackerEventMessage(trackingEvent));
  }

  private String toTrackerEventMessage(TrackerEvent trackingEvent) {
    try {
      final Map<String, Object> message = new HashMap<>();
      message.put("id", trackingEvent.getId());
      message.put("key", trackingEvent.getKey());
      message.put("time", trackingEvent.getTime());
      message.put("type", trackingEvent.getType());
      message.put("version", trackingEvent.getVersion());
      message.put("payload", objectMapper.readValue(trackingEvent.getPayload(objectMapper), trackingEvent.getEntityType()));
      return objectMapper.writeValueAsString(message);
    } catch (final JsonProcessingException e) {
      LOGGER.error("Could not serialize event with id {}", trackingEvent.getId(), e);
      // FIXME error handling?
      return "";
    } catch (IOException e) {
      LOGGER.error("Could not read payload for event with id {}", trackingEvent.getId(), e);
      return "";
    }
  }
}
