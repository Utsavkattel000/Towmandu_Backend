package com.tow.mandu.utils;

import ch.hsr.geohash.GeoHash;
import com.tow.mandu.pojo.DistanceCalculationPojo;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Component
public class LocationUtil {
    private static final double WGS84_A = 6378137.0; // Semi-major axis (meters)
    private static final double WGS84_B = 6356752.314245; // Semi-minor axis (meters)
    private static final double WGS84_F = (WGS84_A - WGS84_B) / WGS84_A; // Flattening
    private static final int MAX_ITERATIONS = 1000; // Maximum iterations for convergence
    private static final double CONVERGENCE_THRESHOLD = 1e-12; // Convergence threshold
    private static final long MIN_REQUEST_INTERVAL = 3000; // 3 seconds in milliseconds
    private static long lastRequestTime = 0;

    /**
     * Calculates the distance between two points on Earth using the Vincenty formula.
     *
     * @param point1 First point with latitude and longitude
     * @param point2 Second point with latitude and longitude
     * @return Distance in meters
     * @throws IllegalArgumentException if coordinates are invalid
     */
    public Double calculateVincentyDistance(DistanceCalculationPojo point1, DistanceCalculationPojo point2) {
        // Validate inputs
        if (point1 == null || point2 == null ||
                point1.getLatitude() == null || point1.getLongitude() == null ||
                point2.getLatitude() == null || point2.getLongitude() == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }

        double lat1 = Math.toRadians(point1.getLatitude());
        double lon1 = Math.toRadians(point1.getLongitude());
        double lat2 = Math.toRadians(point2.getLatitude());
        double lon2 = Math.toRadians(point2.getLongitude());

        // Validate coordinate ranges
        if (Math.abs(point1.getLatitude()) > 90 || Math.abs(point2.getLatitude()) > 90 ||
                Math.abs(point1.getLongitude()) > 180 || Math.abs(point2.getLongitude()) > 180) {
            return null;
        }

        // Vincenty formula implementation
        double U1 = Math.atan((1 - WGS84_F) * Math.tan(lat1)); // Reduced latitude
        double U2 = Math.atan((1 - WGS84_F) * Math.tan(lat2));
        double L = lon2 - lon1; // Difference in longitude
        double lambda = L; // Initial approximation of longitude difference

        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
        double C, lambdaPrev;

        int iteration = 0;
        do {
            sinLambda = Math.sin(lambda);
            cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) +
                    (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            if (sinSigma == 0) return 0.0; // Coincident points

            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;

            if (Double.isNaN(cos2SigmaM)) cos2SigmaM = 0; // Equatorial case
            C = WGS84_F / 16 * cosSqAlpha * (4 + WGS84_F * (4 - 3 * cosSqAlpha));
            lambdaPrev = lambda;
            lambda = L + (1 - C) * WGS84_F * sinAlpha *
                    (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
            iteration++;
        } while (Math.abs(lambda - lambdaPrev) > CONVERGENCE_THRESHOLD && iteration < MAX_ITERATIONS);

        if (iteration >= MAX_ITERATIONS) {
            throw new RuntimeException("Vincenty formula failed to converge");
        }

        double uSq = cosSqAlpha * (WGS84_A * WGS84_A - WGS84_B * WGS84_B) / (WGS84_B * WGS84_B);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) -
                B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));

        double s = WGS84_B * A * (sigma - deltaSigma); // Distance in meters
        return Math.round(s * 1000.0) / 1000.0; // Round to 3 decimal places
    }

    public String createGoogleMapsRedirectLink(Double latitude, Double longitude) {
        return "https://www.google.com/maps?q=" + latitude + "," + longitude;
    }


    public synchronized String getPlaceName(Double latitude, Double longitude) {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastRequest = currentTime - lastRequestTime;

        // Wait if less than 3 seconds have passed since the last request
        if (timeSinceLastRequest < MIN_REQUEST_INTERVAL) {
            try {
                Thread.sleep(MIN_REQUEST_INTERVAL - timeSinceLastRequest);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        // Update the last request time
        lastRequestTime = System.currentTimeMillis();

        String url = "https://nominatim.openstreetmap.org/reverse?lat=" + latitude + "&lon=" + longitude + "&format=json";

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject json = new JSONObject(response.toString());
                return json.getString("display_name");
            } finally {
                connection.disconnect();
            }
        } catch (IOException | JSONException e) {
            // Log the error if needed, but don't throw
            return null; // Return null on any API or JSON parsing failure
        }
    }

    public String getGeoHash(Double latitude, Double longitude){
        return GeoHash.withCharacterPrecision(latitude, longitude, 6).toBase32();
    }
    public List<String> getGeoHashWithNeighbors(Double latitude, Double longitude) {
        int precision = 6;
        double latStep = 0.61;
        double lonStep = 1.2;

        double latDegStep = latStep / 111.0;
        double lonDegStep = lonStep / 111.0;

        int latCount = (int) Math.ceil(10.0 / latStep);
        int lonCount = (int) Math.ceil(10.0 / lonStep);

        List<String> geohashes = new ArrayList<>();
        Set<String> uniqueHashes = new HashSet<>();

        double startLat = latitude - (latCount / 2.0) * latDegStep;
        double startLon = longitude - (lonCount / 2.0) * lonDegStep;

        for (int i = 0; i <= latCount; i++) {
            for (int j = 0; j <= lonCount; j++) {
                double lat = startLat + i * latDegStep;
                double lon = startLon + j * lonDegStep;
                String hash = GeoHash.withCharacterPrecision(lat, lon, precision).toBase32();
                if (uniqueHashes.add(hash)) {
                    geohashes.add(hash);
                }
            }
        }

        return geohashes;
    }
}
