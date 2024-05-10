package com.sample.multmodule;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int temperature;


    private String skyState;

    private String precipitationState;

    private int humidity;

    private String baseTime;

    @Builder
    public Weather(Long id, int temperature, String skyState, String precipitationState, int humidity, String baseTime) {
        this.id = id;
        this.temperature = temperature;
        this.skyState = skyState;
        this.precipitationState = precipitationState;
        this.humidity = humidity;
        this.baseTime = baseTime;
    }
}
