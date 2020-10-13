package com.carRental.client;

import com.carRental.config.BackEndConfiguration;
import com.carRental.domain.vinDecoderApi.VinDecodedDto;
import com.carRental.domain.vinDecoderApi.VinDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static java.util.Optional.ofNullable;

@Component
public class VinDecoderClient {
    private final RestTemplate restTemplate;
    private final BackEndConfiguration backEndConfiguration;

    @Autowired
    public VinDecoderClient(RestTemplate restTemplate, BackEndConfiguration backEndConfiguration) {
        this.restTemplate = restTemplate;
        this.backEndConfiguration = backEndConfiguration;
    }

    public VinDecodedDto decodeVinNumber(VinDto vinDto) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getVinDecoderEndpoint() + "/" + vinDto.getVinNumber())
                    .build().encode().toUri();
            VinDecodedDto response = restTemplate.getForObject(url, VinDecodedDto.class);
            return ofNullable(response).orElse(new VinDecodedDto());
        } catch (RestClientException e) {
            return new VinDecodedDto();
        }
    }
}
