package com.carRental.domain.hereApi.CarAgenciesSearcher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarAgencyAddressDto {

    @JsonProperty("street")
    private String street;

    @JsonProperty("houseNumber")
    private String houseNumber;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("countryName")
    private String countryName;
}
