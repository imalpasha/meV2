package com.metech.firefly.ui.activity.GeoFencing;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by bmote on 2/4/15.
 */
public class MyPlaces {
    private String title;
    private String snippet;
    private LatLng coordinates;
    private float fenceRadius;
    private int iconResourceId;
    private float defaultZoomLevel;
    private double lat;
    private double longi;

    public MyPlaces(String title, String snippet,Double lat , Double longi , LatLng coordinates, float fenceRadius, int defaultZoomLevel, int iconResourceId) {
        this.title = title;
        this.snippet = snippet;
        this.coordinates = coordinates;
        this.fenceRadius = fenceRadius;
        this.defaultZoomLevel = defaultZoomLevel;
        this.iconResourceId = iconResourceId;
        this.lat = lat;
        this.longi = longi;

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public float getFenceRadius() {
        return fenceRadius;
    }

    public float getDefaultZoomLevel() {
        return defaultZoomLevel;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}
