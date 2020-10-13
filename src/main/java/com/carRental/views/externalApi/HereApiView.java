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

import java.util.List;

@UIScope
@Component
public class HereApiView extends VerticalLayout {

    private final Grid<CarAgencyResultDto> grid = new Grid<>();
    private final HereApiClient hereApiClient;

    private CarAgencyAddressDto carAgencyAddressDto = new CarAgencyAddressDto();
    private CarAgencyResultDto carAgencyResultDto = new CarAgencyResultDto();

    private Dialog dialog = new Dialog();
    private CoordinatesDto coordinatesDto = new CoordinatesDto();
    private Binder<CoordinatesDto> binder = new Binder<>();
    private TextField latitude = new TextField("Latitude");
    private TextField longitude = new TextField("Longitude");

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
        grid.setItems(agenciesList);
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
        grid.addColumn(CarAgencyResultDto::getTitle).setHeader("Name");
        grid.addColumn(CarAgencyResultDto::getAddress).setHeader("Address");
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
}

