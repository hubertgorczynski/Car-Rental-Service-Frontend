package com.carRental.client;

import com.carRental.config.BackEndConfiguration;
import com.carRental.domain.hereApi.CarAgenciesSearcher.CarAgencyDto;
import com.carRental.domain.hereApi.Geocode.CoordinatesDto;
import com.carRental.domain.hereApi.Geocode.GeocodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static java.util.Optional.ofNullable;

@Component
public class HereApiClient {

    private final RestTemplate restTemplate;
    private final BackEndConfiguration backEndConfiguration;

    @Autowired
    public HereApiClient(RestTemplate restTemplate, BackEndConfiguration backEndConfiguration) {
        this.restTemplate = restTemplate;
        this.backEndConfiguration = backEndConfiguration;
    }

    public GeocodeDto getCoordinates(String postalCode) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getHereApiEndpoint() +
                    "/check_coordinates_by_postal_code/" + postalCode)
                    .build().encode().toUri();
            GeocodeDto response = restTemplate.getForObject(url, GeocodeDto.class);
            return ofNullable(response).orElse(new GeocodeDto());
        } catch (RestClientException e) {
            return new GeocodeDto();
        }
    }

    public CarAgencyDto searchCarRentalAgencies(CoordinatesDto coordinatesDto) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getHereApiEndpoint() +
                    "/search_nearest_agencies_by_coordinates/")
                    .queryParam("latitude", coordinatesDto.getLatitude())
                    .queryParam("longitude", coordinatesDto.getLongitude())
                    .build().encode().toUri();
            CarAgencyDto response = restTemplate.getForObject(url, CarAgencyDto.class);
            return ofNullable(response).orElse(new CarAgencyDto());
        } catch (RestClientException e) {
            return new CarAgencyDto();
        }
    }
}

