package de.thkoeln.archilab.fae.GPSMockService.infrastructure.eventing;

public interface EventSource {

  String getId();
  Long getVersion();
  String getAggregateName();

}
