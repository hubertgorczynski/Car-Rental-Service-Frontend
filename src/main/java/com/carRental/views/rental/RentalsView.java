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
import com.vaadin.flow.component.html.Label;
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

    private final Grid<RentalComplexDto> rentalGrid = new Grid<>();
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

        VerticalLayout extendRentalDialogLayout = new VerticalLayout();
        Button confirmExtendRentalButton = createConfirmExtendRentalButton();
        extendRentalDialogLayout.add(extension, confirmExtendRentalButton);
        extendRentalDialog.isCloseOnOutsideClick();
        extendRentalDialog.add(extendRentalDialogLayout);

        VerticalLayout modifyRentalDialogLayout = new VerticalLayout();
        Button confirmModifyRentalButton = createConfirmModifyRentalButton();
        modifyRentalDialogLayout.add(modifyStartDate, modifyEndDate, confirmModifyRentalButton);
        modifyRentalDialog.isCloseOnOutsideClick();
        modifyRentalDialog.add(modifyRentalDialogLayout);

        setColumns();

        rentalGrid.addComponentColumn(this::createExtendRentalButton);
        rentalGrid.addComponentColumn(this::createCloseRentalButton);
        rentalGrid.addComponentColumn(this::createModifyRentalButton);

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
        Dialog confirmCloseRentalDialog = createCloseRentalDialog(rentalComplexDto);

        Button closeRentalButton = new Button("Close", event -> confirmCloseRentalDialog.open());

        if (loggedUserDto == null) {
            closeRentalButton.setEnabled(false);
        } else {
            closeRentalButton.setEnabled(true);
        }
        return closeRentalButton;
    }

    private Dialog createCloseRentalDialog(RentalComplexDto rentalComplexDto){
        Dialog confirmCloseRentalDialog = new Dialog();
        VerticalLayout confirmationLayout = new VerticalLayout();
        Button confirmCloseRentalButton = createConfirmCloseRentalButton(confirmCloseRentalDialog, rentalComplexDto);
        Button cancelCloseRentalButton = createCancelConfirmationButton(confirmCloseRentalDialog);
        Label confirmationLabel = new Label("Are You sure about closing rental?");
        confirmationLayout.add(confirmationLabel, confirmCloseRentalButton, cancelCloseRentalButton);
        confirmCloseRentalDialog.add(confirmationLayout);
        return confirmCloseRentalDialog;
    }

    private Button createConfirmCloseRentalButton(Dialog dialog, RentalComplexDto rentalComplexDto) {
        return new Button("Confirm", event -> {
            rentalId = rentalComplexDto.getId();
            closeRental(rentalId);
            dialog.close();
        });
    }

    private Button createCancelConfirmationButton(Dialog dialog) {
        return new Button("Cancel", event -> dialog.close());
    }

    private Button createExtendRentalButton(RentalComplexDto rentalComplexDto) {
        Button extendRentalButton = new Button("Extend");
        extendRentalButton.addClickListener(event -> {
            rentalId = rentalComplexDto.getId();
            extendRentalDialog.open();
        });
        if (loggedUserDto == null) {
            extendRentalButton.setEnabled(false);
        } else {
            extendRentalButton.setEnabled(true);
        }
        return extendRentalButton;
    }

    private Button createModifyRentalButton(RentalComplexDto rentalComplexDto) {
        Button modifyRentalButton = new Button("Modify");
        modifyRentalButton.addClickListener(event -> {
            rentalId = rentalComplexDto.getId();
            carId = rentalComplexDto.getCarId();
            modifyRentalDialog.open();
        });
        if (loggedUserDto == null) {
            modifyRentalButton.setEnabled(true);
        } else {
            modifyRentalButton.setEnabled(false);
        }
        return modifyRentalButton;
    }

    private Button createConfirmExtendRentalButton() {
        return new Button("Confirm", event -> {
            RentalExtensionDto rentalExtensionDto = new RentalExtensionDto();
            binderForExtendRental.writeBeanIfValid(rentalExtensionDto);
            rentalExtensionDto.setRentalId(rentalId);
            rentalClient.extendRental(rentalExtensionDto);
            refreshRentalsForUser(loggedUserDto);
            extendRentalDialog.close();
        });
    }

    private Button createConfirmModifyRentalButton() {
        return new Button("Confirm", event -> {
            RentalDto rentalDto = new RentalDto();
            binderForModifyRental.writeBeanIfValid(rentalDto);
            rentalDto.setId(rentalId);
            rentalDto.setCarId(carId);
            rentalDto.setUserId(loggedUserDto.getId());
            rentalClient.modifyRental(rentalDto);
            refreshRentalsForUser(loggedUserDto);
            modifyRentalDialog.close();
        });
    }

    private void closeRental(Long rentalId) {
        rentalClient.closeRental(rentalId);
        refreshRentalsForUser(loggedUserDto);
    }

    private void setColumns() {
        rentalGrid.addColumn(RentalComplexDto::getId).setHeader("Id");
        rentalGrid.addColumn(RentalComplexDto::getRentedFrom).setHeader("Start");
        rentalGrid.addColumn(RentalComplexDto::getRentedTo).setHeader("End");
        rentalGrid.addColumn(RentalComplexDto::getRentalCost).setHeader("Cost");
        rentalGrid.addColumn(RentalComplexDto::getCarBrand).setHeader("Brand");
        rentalGrid.addColumn(RentalComplexDto::getCarModel).setHeader("Model");
        rentalGrid.addColumn(RentalComplexDto::getUserName).setHeader("Name");
        rentalGrid.addColumn(RentalComplexDto::getUserLastName).setHeader("Surname");
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
