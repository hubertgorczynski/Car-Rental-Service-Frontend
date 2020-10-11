package com.carRental.views.rental;

import com.carRental.client.RentalClient;
import com.carRental.domain.RentalComplexDto;
import com.carRental.domain.RentalDto;
import com.carRental.domain.RentalExtensionDto;
import com.carRental.domain.UserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class RentalsView extends VerticalLayout {

    private final Grid<RentalComplexDto> rentalGrid = new Grid<>(RentalComplexDto.class);
    private final RentalClient rentalClient;

    private Dialog extendRentalDialog = new Dialog();
    private Binder<RentalExtensionDto> binderForExtendRental = new Binder<>();
    private IntegerField extension = new IntegerField("Extend by (days)");

    private Dialog modifyRentalDialog = new Dialog();
    private Binder<RentalDto> binderForModifyRental = new Binder<>();
    private DatePicker modifyStartDate = new DatePicker("Rented from");
    private DatePicker modifyEndDate = new DatePicker("Rented to");

    private UserDto loggedUserDto;
    private Long rentalId;
    private Long carId;

    @Autowired
    public RentalsView(RentalClient rentalClient) {
        this.rentalClient = rentalClient;

        bindFields();

        Button confirmExtendRentalButton = new Button("Confirm");
        confirmExtendRentalButton.addClickListener(event -> {
            RentalExtensionDto rentalExtensionDto = new RentalExtensionDto();
            binderForExtendRental.writeBeanIfValid(rentalExtensionDto);
            rentalExtensionDto.setRentalId(rentalId);
            rentalClient.extendRental(rentalExtensionDto);
            refreshRentalsForUser(loggedUserDto);
            extendRentalDialog.close();
        });

        VerticalLayout extendRentalDialogLayout = new VerticalLayout();
        extendRentalDialogLayout.add(extension, confirmExtendRentalButton);
        extendRentalDialog.isCloseOnOutsideClick();
        extendRentalDialog.add(extendRentalDialogLayout);

        Button confirmModifyRentalButton = new Button("Confirm");
        confirmModifyRentalButton.addClickListener(event -> {
            RentalDto rentalDto = new RentalDto();
            binderForModifyRental.writeBeanIfValid(rentalDto);
            rentalDto.setId(rentalId);
            rentalDto.setCarId(carId);
            rentalDto.setUserId(loggedUserDto.getId());
            rentalClient.modifyRental(rentalDto);
            refreshRentalsForUser(loggedUserDto);
            modifyRentalDialog.close();
        });

        VerticalLayout modifyRentalDialogLayout = new VerticalLayout();
        modifyRentalDialogLayout.add(modifyStartDate, modifyEndDate, confirmModifyRentalButton);
        modifyRentalDialog.isCloseOnOutsideClick();
        modifyRentalDialog.add(modifyRentalDialogLayout);

        rentalGrid.setColumns(
                "id",
                "rentedFrom",
                "rentedTo",
                "rentalCost",
                "carId",
                "carBrand",
                "carModel",
                "userName",
                "userLastName",
                "userEmail",
                "userPhoneNumber");

        rentalGrid.addComponentColumn(this::createExtendRentalButton);
        rentalGrid.addComponentColumn(this::createModifyRentalButton);
        rentalGrid.addComponentColumn(this::createCloseRentalButton);

        add(rentalGrid);
    }

    public void refreshRentalsForAdmin() {
        loggedUserDto = null;
        List<RentalComplexDto> rentals = rentalClient.getRentals();
        rentalGrid.setItems(rentals);
    }

    public void refreshRentalsForUser(UserDto userDto) {
        loggedUserDto = userDto;
        List<RentalComplexDto> rentals = rentalClient.getRentalsByUserId(userDto.getId());
        rentalGrid.setItems(rentals);
    }

    private Button createCloseRentalButton(RentalComplexDto rentalComplexDto) {
        return new Button("Close rental", event -> {
            rentalId = rentalComplexDto.getId();
            closeRental(rentalId);
        });
    }

    private Button createExtendRentalButton(RentalComplexDto rentalComplexDto) {
        Button extendRentalButton = new Button("Extend rental");
        extendRentalButton.addClickListener(event -> {
            rentalId = rentalComplexDto.getId();
            extendRentalDialog.open();
        });
        if (loggedUserDto != null) {
            extendRentalButton.setEnabled(true);
        }
        return extendRentalButton;
    }

    private Button createModifyRentalButton(RentalComplexDto rentalComplexDto) {
        Button modifyRentalButton = new Button("Modify rental");
        modifyRentalButton.addClickListener(event -> {
            rentalId = rentalComplexDto.getId();
            carId = rentalComplexDto.getCarId();
            modifyRentalDialog.open();
        });
        if (loggedUserDto != null) {
            modifyRentalButton.setEnabled(true);
        }
        return modifyRentalButton;
    }

    private void closeRental(Long rentalId) {
        rentalClient.closeRental(rentalId);
        refreshRentalsForUser(loggedUserDto);
    }

    private void bindFields() {
        binderForExtendRental.forField(extension)
                .bind(RentalExtensionDto::getExtension, RentalExtensionDto::setExtension);

        binderForModifyRental.forField(modifyStartDate)
                .bind(RentalDto::getRentedFrom, RentalDto::setRentedFrom);
        binderForModifyRental.forField(modifyEndDate)
                .bind(RentalDto::getRentedTo, RentalDto::setRentedTo);
    }
}
