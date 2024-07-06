package edu.usc.csci310.project.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Search {

    private final HttpClient httpClient;
    private static final String API_KEY = "Y3LLPjucVgC39PcoebZaw0fJIs3nX24trdrwrKAE";

    private static final String BASE_URL = "https://developer.nps.gov/api/v1/parks";

    // Spring will automatically inject an HttpClient instance here.
    public Search(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void searchAndPrintParks(JSONArray parks) {
//        JSONArray parks = getParks(parkName, stateCode, amenities, activities);
        if (parks != null) {
            for (int i = 0; i < parks.length(); i++) {
                try {
                    System.out.println(parks.getJSONObject(i).toString(2)); // Print the JSON object with indentation
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("No parks found or there was an error.");
        }
    }

    public JSONArray getParks(String parkName, String stateCode, String[] amenities, String[] activities) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                .append("?api_key=").append(API_KEY);

        if (parkName != null && !parkName.isEmpty()) {
            urlBuilder.append("&q=").append(parkName);
        }
        if (stateCode != null && !stateCode.isEmpty()) {
            urlBuilder.append("&stateCode=").append(stateCode);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

