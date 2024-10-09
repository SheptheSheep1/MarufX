import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

/*
* Accessory class for {org.MarufX.PrayerTimes}
* @author Shep
 */
public class Location {
    private double latitude;
    private double longitude;
    private String locationName;
    private boolean debug;

    public Location(String query, boolean debug) {
        try {
            setLocationQuery(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.debug = debug;
    }
    public Location(boolean debug){
        try {
            setLocationIP();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        this.debug = debug;
    }

    private void setLocationIP() throws Exception {
        String urlString = "http://ip-api.com/json/";
        URL url = new URL(urlString);
        URLConnection hc = url.openConnection();
        String out = new Scanner(hc.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        //System.err.println(out);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(out);
        double latitude = jsonNode.get("lat").doubleValue();
        double longitude = jsonNode.get("lon").doubleValue();
        String locationName = String.format("%s, %s, %s", jsonNode.get("city").asText(), jsonNode.get("regionName").asText(), jsonNode.get("country").asText());

        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
    }

    private void setLocationQuery(String query) throws Exception {
        // filter query for url format
        String queryF = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = String.format("https://nominatim.openstreetmap.org/search?format=%s&q=%s", "json", queryF);
        URL url = new URL(urlString);
        URLConnection hc = url.openConnection();
        hc.setRequestProperty("User-Agent", "MarufX");
        String out = new Scanner(hc.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        //System.err.println(out);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(out);
        double latitude = 0.0;
        double longitude = 0.0;
        String locationName = "";
        if (jsonNode.isArray()) {
            int count = 0;
            for (JsonNode node : jsonNode) {
                if(count == 0){
                    latitude = Double.parseDouble(node.get("lat").asText());
                    longitude = Double.parseDouble(node.get("lon").asText());
                    locationName = node.get("display_name").asText();
                }
                count++;
            }
            if(count != 1){System.err.println("too generic; be more specific");}
        }

        this.longitude = longitude;
        this.latitude = latitude;
        this.locationName = locationName;

        //System.err.println("Latitude: " + latitude);
        //System.err.println("Longitude: " + longitude);
        //System.err.println("Location Name: " + locationName);
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public String getLocationName(){
        return this.locationName;
    }

    public static void main(String[] args){
        Location location = new Location(true);
        Location location_query = new Location("tempe, az",true);
        System.out.printf("\nlatitude: %f longitude: %f Location Name: %s\n", location.getLatitude(), location.getLongitude(), location.getLocationName());
        System.out.printf("\nlatitude: %f longitude: %f Location Name: %s\n", location_query.getLatitude(), location_query.getLongitude(), location_query.getLocationName());
    }
}
