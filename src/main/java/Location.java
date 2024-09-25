import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Scanner;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class Location {
    private double latitude;
    private double longitude;
    private String locationName;
    private boolean debug;

    public Location(String query, boolean debug) {

    }
    public Location(InetAddress IPaddress, boolean debug){

    }
    private static void setLocationQuery(String query) throws Exception{
        String urlString = String.format("https://nominatim.openstreetmap.org/search?format=%s&q=%s", "json", query);
        URL url = new URL(urlString);
        URLConnection hc = url.openConnection();
        hc.setRequestProperty("User-Agent", "MarufX");
        String out = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
        System.out.println(out);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(out);
        if (jsonNode.isArray()) {
            int count = 0;
            // yes, loop the JsonNode and display one by one
            for (JsonNode node : jsonNode) {
                System.out.println("Name: " + node.get("lat").asText());
                System.out.println("Age: " + node.get("lon").asText());
                System.out.println("Location Name: " + node.get("display_name").asText());
                count++;
            }
            if(count != 1){System.out.println("too generic; be more specific");}
        }

        System.out.printf("latitude: %s longitude: %s", latitude, longitude);
    } // for debug purposes
    public static void main(String[] args){
        try{
        Location.setLocationQuery("tempe,%20az");}
        catch(Exception e){System.out.println(e);}
    }
}
