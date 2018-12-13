package de.thkoeln.archilab.fae.GPSMockService.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class Route {

  public Route(File kmlFile)
  {
    Kml kml = Kml.unmarshal(kmlFile);
    Document document = (Document)kml.getFeature();
    Placemark placemark = (Placemark)document.getFeature().get(0);
    LineString lineString = (LineString)placemark.getGeometry();
    this.coordinates = new ArrayList<>(lineString.getCoordinates());
  }
  private static final Logger log = LoggerFactory.getLogger(Route.class);
  private ArrayList<Coordinate> coordinates;
  private int currentCoordinateIndex = 0;

  public Coordinate getPosition()
  {
    log.info("Routecoordinate " + currentCoordinateIndex + " out of " + coordinates.size() + " coordinates");
    return coordinates.get(currentCoordinateIndex++ % coordinates.size());
  }
  public void resetRoute(){
    currentCoordinateIndex = 0;
  }
  public boolean hasEnded()
  {
    return currentCoordinateIndex >= coordinates.size();
  }
}
