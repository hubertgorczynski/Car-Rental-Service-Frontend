package com.carRental.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BackEndConfiguration {

    @Value("${car.api.endpoint}")
    private String carEndpoint;

    @Value("${rental.api.endpoint}")
    private String rentalEndpoint;

    @Value("${user.api.endpoint}")
    private String userEndpoint;

    @Value("${login.api.endpoint}")
    private String loginEndpoint;

    @Value("${vinDecoder.api.endpoint}")
    private String vinDecoderEndpoint;

    @Value("${hereApi.api.endpoint}")
    private String hereApiEndpoint;
}
