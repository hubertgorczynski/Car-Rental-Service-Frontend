package com.carRental.client;

import com.carRental.config.BackEndConfiguration;
import com.carRental.domain.CarDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
public class CarClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BackEndConfiguration backEndConfiguration;

    public List<CarDto> getCars() {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getCarEndpoint()).build().encode().toUri();
            CarDto[] response = restTemplate.getForObject(url, CarDto[].class);
            return Arrays.asList(ofNullable(response).orElse(new CarDto[0]));

        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }
}
