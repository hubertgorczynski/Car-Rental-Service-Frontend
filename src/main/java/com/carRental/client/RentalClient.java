package com.carRental.client;

import com.carRental.config.BackEndConfiguration;
import com.carRental.domain.RentalComplexDto;
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
public class RentalClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BackEndConfiguration backEndConfiguration;

    public List<RentalComplexDto> getRentals() {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getRentalEndpoint()).build().encode().toUri();
            RentalComplexDto[] response = restTemplate.getForObject(url, RentalComplexDto[].class);
            return Arrays.asList(ofNullable(response).orElse(new RentalComplexDto[0]));
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }
}
