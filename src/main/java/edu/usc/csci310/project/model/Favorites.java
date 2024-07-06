package edu.usc.csci310.project.model;

import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "favorites")
@Setter
@Getter
@NoArgsConstructor
public class Favorites {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @jakarta.persistence.ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Getter
    @Column(name = "park_name")
    private String parkName;

    @Getter
    @Setter
    @Column(name = "api_id")
    private String apiId;


    @Getter
    @Setter
    @Column(name = "ranking")
    private int ranking;

    public Favorites(User user, String parkName, int ranking, String apiId) {
        this.user = user;
        this.parkName = parkName;
        this.ranking = ranking;
        this.apiId = apiId;
    }
}

