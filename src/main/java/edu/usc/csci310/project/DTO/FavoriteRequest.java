package edu.usc.csci310.project.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteRequest {
    private String username;
    private String parkName;
    private String apiId;
    private int ranking;
}
