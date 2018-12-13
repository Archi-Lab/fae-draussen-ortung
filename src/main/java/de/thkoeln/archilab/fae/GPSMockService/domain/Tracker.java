package de.thkoeln.archilab.fae.GPSMockService.domain;

import de.thkoeln.archilab.fae.GPSMockService.infrastructure.eventing.EventSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tracker implements EventSource
{
  public Tracker() {
  }
  public Tracker(File kmlfolder) {
    this.trackerId = UUID.nameUUIDFromBytes(kmlfolder.toString().getBytes());
    this.routes = Arrays.stream(kmlfolder.listFiles()).collect(() -> new ArrayList<>(), (c,e) -> c.add(new Route(e)), (c1,c2) -> c2.addAll(c1));
    this.currentRoute = routes.get(random.nextInt(routes.size()));
    this.currentPosition = this.currentRoute.getPosition();
  }

  private static final Logger log = LoggerFactory.getLogger(Tracker.class);
  private static final Random random = new Random(2108);

  private UUID trackerId;
  private Coordinate currentPosition;

  private List<Route> routes;
  private Route currentRoute;

  public Coordinate UpdateCurrentPosition(){
    if(currentRoute.hasEnded())
    {
      log.info("Route has ended!");
      int nextRouteIndex = random.nextInt(routes.size());
      currentRoute = routes.get(nextRouteIndex);
      currentRoute.resetRoute();
    }
    this.currentPosition = currentRoute.getPosition();
    return this.currentPosition;
  }

  public UUID getTrackerId() {
    return trackerId;
  }

  public Coordinate getCurrentPosition(){
    return this.currentPosition;
  }

  @Override
  @JsonIgnore
  public String getId() {
    return trackerId.toString();
  }

  @Override
  @JsonIgnore
  public Long getVersion() {
    //We dont need Versions in this context in my opinion, a tracking event is created once and never updated or altered
    return 1L;
  }

  @Override
  @JsonIgnore
  public String getAggregateName() {
    return "tracker";
  }
}
