package com.carRental.domain.vinDecoderApi;

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
public class VinResultDto {

    @JsonProperty("Make")
    private String manufacturer;

    @JsonProperty("Model")
    private String model;

    @JsonProperty("ModelYear")
    private String productionYear;

    @JsonProperty("FuelTypePrimary")
    private String fuelType;

    @JsonProperty("Body Class")
    private String bodyClass;

    @JsonProperty("VehicleType")
    private String vehicleType;

    @JsonProperty("PlantCountry")
    private String plantCountry;

    @JsonProperty("PlantCity")
    private String plantCity;
}
