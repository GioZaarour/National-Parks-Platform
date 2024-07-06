package edu.usc.csci310.project.controller;

import edu.usc.csci310.project.service.Search;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpClient;

@CrossOrigin (origins = "*")
@RestController
public class ParkSearchController {

    private final Search searchService;

    @Autowired
    public ParkSearchController(HttpClient httpClient) {
        this.searchService = new Search(httpClient);
    }

    @GetMapping("/searchParks")
    public String searchParks(@RequestParam(name = "parkName", required = false) String parkName,
                              @RequestParam(name = "stateCode", required = false) String stateCode,
                              @RequestParam(name = "amenities", required = false) String[] amenities,
                              @RequestParam(name = "activities", required = false) String[] activities) throws UnsupportedEncodingException {
        JSONArray parks = searchService.getParks(parkName, stateCode, amenities, activities);
        return parks != null ? parks.toString() : "[]";
    }
}

