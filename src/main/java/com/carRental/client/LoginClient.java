package com.carRental.client;

import com.carRental.config.BackEndConfiguration;
import com.carRental.domain.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class LoginClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BackEndConfiguration backEndConfiguration;

    public Boolean isLoginRegistered(LoginDto loginDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getLoginEndpoint() + "/is_already_registered")
                .queryParam("email", loginDto.getEmail())
                .queryParam("password", loginDto.getPassword())
                .build().encode().toUri();
        return restTemplate.getForObject(url, Boolean.class);
    }
}
