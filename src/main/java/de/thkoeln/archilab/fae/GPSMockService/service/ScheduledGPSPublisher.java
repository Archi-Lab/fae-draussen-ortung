package de.thkoeln.archilab.fae.GPSMockService.service;

import de.thkoeln.archilab.fae.GPSMockService.domain.Tracker;
import de.thkoeln.archilab.fae.GPSMockService.domain.TrackerCreatedEvent;
import de.thkoeln.archilab.fae.GPSMockService.domain.TrackerEvent;
import de.thkoeln.archilab.fae.GPSMockService.domain.TrackerTrackedEvent;
import de.thkoeln.archilab.fae.GPSMockService.infrastructure.eventing.KafkaGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledGPSPublisher {

  private static final Logger log = LoggerFactory.getLogger(ScheduledGPSPublisher.class);
  private final KafkaGateway eventPublisher;

  private static File gPSDataFolder;
  private static File[] trackerFolders;

  private static ArrayList<Tracker> trackers;


  @Autowired
  ScheduledGPSPublisher(@Value("${tracker.path}") final String pathToFolder, final KafkaGateway eventPublisher)
  {
    log.info("Search for GPS-Data in \"{}\"",pathToFolder);

    this.eventPublisher = eventPublisher;

    gPSDataFolder = new File(pathToFolder);
    trackerFolders = gPSDataFolder.listFiles(File::isDirectory);

    trackers = Arrays.stream(trackerFolders).collect(() -> new ArrayList<>(), (c,e) -> c.add(new Tracker(e)), (c1,c2) -> c2.addAll(c1));

    trackers.forEach(tracker -> {
      log.info("Tracker with trackerId: {} created", tracker.getTrackerId());
      publishTrackerCreatedEvent(tracker);
    });
    log.info(trackers.size() + " Trackers created!");
  }

  @Scheduled(fixedRateString = ("${tracker.publishRate}"))
  public void reportCurrentTrackerPosition() {
    trackers.forEach(tracker -> {
      //Debug logging
      log.info("\nTrackerID: " + tracker.getTrackerId().toString() + "\nPosition: " + tracker.getCurrentPosition().toString());

      //Publish Events via Kafka
      publishTrackerTrackedEvent(tracker);

      //Event published, now update the trackers position
      tracker.UpdateCurrentPosition();
    });
  }

  private void publishTrackerCreatedEvent(Tracker tracker)
  {
    TrackerEvent trackingEvent = new TrackerCreatedEvent(tracker);
    try {
      SendResult<String, String> sendResult = eventPublisher.publishTrackingEvent(trackingEvent)
          .get(1, TimeUnit.SECONDS);

      log.info(sendResult.toString());
    } catch (final Exception ex) {
      log.info("An " + ex.getClass() + "occured!");
    }
  }

  private void publishTrackerTrackedEvent(Tracker tracker)
  {
    TrackerEvent trackingEvent = new TrackerTrackedEvent(tracker);
    try {
      SendResult<String, String> sendResult = eventPublisher.publishTrackingEvent(trackingEvent)
          .get(1, TimeUnit.SECONDS);

      log.info(sendResult.toString());
    } catch (final Exception ex) {
      log.info("An " + ex.getClass() + "occured!");
    }
  }

}
