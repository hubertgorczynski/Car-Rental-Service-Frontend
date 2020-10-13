package com.carRental.domain.hereApi.CarAgenciesSearcher;

import com.carRental.domain.hereApi.CarAgenciesSearcher.CarAgencyResultDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarAgencyDto {

    @JsonProperty("items")
    private List<CarAgencyResultDto> agenciesDtoList;
}
