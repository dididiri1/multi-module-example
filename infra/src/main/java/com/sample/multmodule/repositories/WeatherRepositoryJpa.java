package com.sample.multmodule.repositories;

import com.sample.multmodule.Weather;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WeatherRepositoryJpa extends JpaRepository<Weather, Long> {

    Weather findTopByOrderByIdDesc();
}
