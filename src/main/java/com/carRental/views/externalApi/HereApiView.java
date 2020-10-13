package com.carRental.views.externalApi;

import com.carRental.client.HereApiClient;
import com.carRental.domain.hereApi.CarAgencyAddressDto;
import com.carRental.domain.hereApi.CarAgencyDto;
import com.carRental.domain.hereApi.CarAgencyResultDto;
import com.carRental.domain.hereApi.CoordinatesDto;
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

@UIScope
@Component
public class HereApiView extends VerticalLayout {

    private final Grid<CarAgencyAddressDto> grid = new Grid<>();
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
        List<CarAgencyResultDto> agenciesList = carAgencyDto.getAgenciesDtoList();
        List<CarAgencyAddressDto> addressList = new ArrayList<>();

        for (CarAgencyResultDto agencyResultDto : agenciesList) {
            if ((agencyResultDto.getAddress().getHouseNumber() != null) &&
                    (agencyResultDto.getAddress().getStreet() != null) &&
                    (agencyResultDto.getAddress().getCity() != null) &&
                    (agencyResultDto.getAddress().getPostalCode() != null))
                addressList.add(agencyResultDto.getAddress());
        }

        grid.setItems(addressList);
        dialog.close();
        clearFields();
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
        grid.addColumn(CarAgencyAddressDto::getStreet).setHeader("Street");
        grid.addColumn(CarAgencyAddressDto::getHouseNumber).setHeader("Number");
        grid.addColumn(CarAgencyAddressDto::getPostalCode).setHeader("Code");
        grid.addColumn(CarAgencyAddressDto::getCity).setHeader("City");

        /*
        grid.addColumn(CarAgencyResultDto -> carAgencyResultDto.getTitle()).setHeader("Name");
        grid.addColumn(CarAgencyResultDto -> carAgencyResultDto.getAddress().getStreet()).setHeader("Street");
        grid.addColumn(CarAgencyResultDto -> carAgencyResultDto.getAddress().getHouseNumber()).setHeader("Number");
        grid.addColumn(CarAgencyResultDto -> carAgencyResultDto.getAddress().getPostalCode()).setHeader("Code");
        grid.addColumn(CarAgencyResultDto -> carAgencyResultDto.getAddress().getCity()).setHeader("City");
        */
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
        List<CarAgencyAddressDto> emptyList = new ArrayList<>();
        grid.setItems(emptyList);
    }
}

