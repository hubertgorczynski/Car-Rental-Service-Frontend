package com.carRental.views.externalApi;

import com.carRental.client.HereApiClient;
import com.carRental.domain.hereApi.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UIScope
@Component
public class HereApiView extends VerticalLayout {

    private final Grid<CarAgencyComplexDto> grid = new Grid<>();
    private final HereApiClient hereApiClient;

    private Dialog dialog = new Dialog();
    private CoordinatesDto coordinatesDto = new CoordinatesDto();
    private Binder<CoordinatesDto> binder = new Binder<>();
    private TextField latitude = new TextField("Latitude (e.g. 52.39917)");
    private TextField longitude = new TextField("Longitude (e.g. 16.94538)");

    @Autowired
    public HereApiView(HereApiClient hereApiClient) {
        this.hereApiClient = hereApiClient;

        bindFields();

        VerticalLayout dialogLayout = new VerticalLayout();
        Button confirmFindAgenciesButton = createConfirmFindAgenciesButton();
        dialogLayout.add(latitude, longitude, confirmFindAgenciesButton);

        dialog.isCloseOnOutsideClick();
        dialog.add(dialogLayout);

        setColumns();

        Button findAgenciesButton = createFindAgenciesButton();
        add(findAgenciesButton, grid, dialog);
    }

    private void findAgencies(CoordinatesDto coordinatesDto) {
        CarAgencyDto carAgencyDto = hereApiClient.searchCarRentalAgencies(coordinatesDto);
        List<CarAgencyResultDto> responseList = carAgencyDto.getAgenciesDtoList();

        List<CarAgencyComplexDto> resultList = mapToCarAgencyComplexDtoList(responseList);

        grid.setItems(resultList);
        dialog.close();
        clearFields();
    }

    private List<CarAgencyComplexDto> mapToCarAgencyComplexDtoList(final List<CarAgencyResultDto> inputList) {
        return inputList.stream()
                .map(carAgencyResultDto -> new CarAgencyComplexDto(
                        carAgencyResultDto.getTitle(),
                        carAgencyResultDto.getAddress().getStreet(),
                        carAgencyResultDto.getAddress().getHouseNumber(),
                        carAgencyResultDto.getAddress().getPostalCode(),
                        carAgencyResultDto.getAddress().getCity(),
                        carAgencyResultDto.getAddress().getState(),
                        carAgencyResultDto.getAddress().getCountryName()))
                .collect(Collectors.toList());
    }

    private Button createConfirmFindAgenciesButton() {
        return new Button("Search", event -> {
            binder.writeBeanIfValid(coordinatesDto);
            findAgencies(coordinatesDto);
        });
    }

    private Button createFindAgenciesButton() {
        return new Button("Find nearest car agencies", event -> dialog.open());
    }

    private void setColumns() {
        grid.addColumn(CarAgencyComplexDto::getName).setHeader("Name");
        grid.addColumn(CarAgencyComplexDto::getStreet).setHeader("Street");
        grid.addColumn(CarAgencyComplexDto::getHouseNumber).setHeader("Number");
        grid.addColumn(CarAgencyComplexDto::getPostalCode).setHeader("Code");
        grid.addColumn(CarAgencyComplexDto::getCity).setHeader("City");
    }

    private void bindFields() {
        binder.forField(latitude)
                .bind(CoordinatesDto::getLatitude, CoordinatesDto::setLatitude);
        binder.forField(longitude)
                .bind(CoordinatesDto::getLongitude, CoordinatesDto::setLongitude);
    }

    private void clearFields() {
        latitude.clear();
        longitude.clear();
    }

    public void clearGrid() {
        List<CarAgencyComplexDto> emptyList = new ArrayList<>();
        grid.setItems(emptyList);
    }
}

