package com.carRental.client;

import com.carRental.config.BackEndConfiguration;
import com.carRental.domain.UserDto;
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
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BackEndConfiguration backEndConfiguration;

    public List<UserDto> getUsers() {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getUserEndpoint())
                    .build().encode().toUri();
            UserDto[] response = restTemplate.getForObject(url, UserDto[].class);
            return Arrays.asList(ofNullable(response).orElse(new UserDto[0]));
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    public void registerUser(UserDto userDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getUserEndpoint())
                .build().encode().toUri();
        restTemplate.postForObject(url, userDto, UserDto.class);
    }

    public Boolean isUserRegistered(String email) {
        URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getUserEndpoint() + "/is_user_registered")
                .queryParam("email", email)
                .build().encode().toUri();
        return restTemplate.getForObject(url, Boolean.class);
    }

    public UserDto getUserByEmail(String email) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backEndConfiguration.getUserEndpoint() + "/by_email/" + email)
                    .build().encode().toUri();
            UserDto response = restTemplate.getForObject(url, UserDto.class);
            return ofNullable(response).orElse(new UserDto());
        } catch (RestClientException e) {
            return new UserDto();
        }
    }
}
