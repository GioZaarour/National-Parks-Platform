package edu.usc.csci310.project.DTO;

import lombok.Getter;

@Getter
public class UserParkPair {
    private String username;
    private String parkName;
    private String apiId;
    private int ranking;

    public UserParkPair(String username, String parkName, int ranking, String apiId) {
        this.username = username;
        this.parkName = parkName;
        this.apiId = apiId;
        this.ranking = ranking;
    }
}
