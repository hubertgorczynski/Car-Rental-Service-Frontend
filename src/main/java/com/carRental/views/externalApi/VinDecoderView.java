package com.carRental.views.externalApi;

import com.carRental.client.VinDecoderClient;
import com.carRental.domain.vinDecoderApi.VinDecodedDto;
import com.carRental.domain.vinDecoderApi.VinDto;
import com.carRental.domain.vinDecoderApi.VinResultDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@UIScope
@Component
public class VinDecoderView extends VerticalLayout {

    private final Grid<VinResultDto> grid = new Grid<>();
    private final VinDecoderClient vinDecoderClient;

    private Dialog dialog = new Dialog();
    private VinResultDto vinResultDto = new VinResultDto();

    private VinDto vinDto = new VinDto();
    private Binder<VinDto> binder = new Binder<>();
    private TextField vinField = new TextField("Enter VIN below:");

    public VinDecoderView(VinDecoderClient vinDecoderClient) {
        this.vinDecoderClient = vinDecoderClient;

        bindFields();

        VerticalLayout dialogLayout = new VerticalLayout();
        Button confirmDecodeButton = createConfirmDecodeButton();
        dialogLayout.add(vinField, confirmDecodeButton);

        dialog.isCloseOnOutsideClick();
        dialog.add(dialogLayout);

        setColumns();

        Button decodeButton = createDecodeButton();
        add(decodeButton, grid, dialog);

    }

    private Button createConfirmDecodeButton() {
        return new Button("Decode", event -> {
            binder.writeBeanIfValid(vinDto);
            VinDecodedDto vinDecodedDto = vinDecoderClient.decodeVinNumber(vinDto);
            List<VinResultDto> resultsDtoList = vinDecodedDto.getResultsDtoList();
            grid.setItems(resultsDtoList);
            dialog.close();
            clearFields();
        });
    }

    private Button createDecodeButton() {
        return new Button("Decode VIN number", event -> dialog.open());
    }

    private void setColumns() {
        grid.addColumn(VinResultDto::getManufacturer).setHeader("Brand");
        grid.addColumn(VinResultDto::getModel).setHeader("Model");
        grid.addColumn(VinResultDto::getProductionYear).setHeader("Year");
        grid.addColumn(VinResultDto::getFuelType).setHeader("Fuel");
        grid.addColumn(VinResultDto::getPlantCity).setHeader("City");
        grid.addColumn(VinResultDto::getPlantCountry).setHeader("Country");
    }

    private void bindFields() {
        binder.forField(vinField)
                .bind(VinDto::getVinNumber, VinDto::setVinNumber);
    }

    private void clearFields() {
        vinField.clear();
    }

    public void clearGrid() {
        List<VinResultDto> emptyList = new ArrayList<>();
        grid.setItems(emptyList);
    }
}
