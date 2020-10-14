package com.carRental.views.car;

import com.carRental.client.CarClient;
import com.carRental.client.RentalClient;
import com.carRental.domain.CarDto;
import com.carRental.domain.RentalDto;
import com.carRental.domain.Status;
import com.carRental.domain.UserDto;
import com.carRental.views.rental.RentalsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class CarsView extends VerticalLayout {

    private final Grid<CarDto> carGrid = new Grid<>();
    private final CarClient carClient;
    private final RentalClient rentalClient;
    private final RentalsView rentalsView;

    private CarDto carDto = new CarDto();
    private UserDto loggedUserDto;
    private Long carId;

    Button addCarButton = new Button("Add new car");

    private Dialog addCarDialog = new Dialog();
    private Binder<CarDto> binderForSavingCar = new Binder<>();
    private TextField vin = new TextField("Vin");
    private TextField brand = new TextField("Brand");
    private TextField model = new TextField("Model");
    private IntegerField productionYear = new IntegerField("Production year");
    private TextField fuelType = new TextField("Fuel type");
    private NumberField engineCapacity = new NumberField("Engine capacity");
    private TextField bodyClass = new TextField("Body class");
    private IntegerField mileage = new IntegerField("Mileage");
    private BigDecimalField costPerDay = new BigDecimalField("Cost / day");

    private Dialog updateCarDialog = new Dialog();
    private Binder<CarDto> binderForUpdatingCar = new Binder<>();
    private TextField vinUpdate = new TextField("Vin");
    private TextField brandUpdate = new TextField("Brand");
    private TextField modelUpdate = new TextField("Model");
    private IntegerField productionYearUpdate = new IntegerField("Production year");
    private TextField fuelTypeUpdate = new TextField("Fuel type");
    private NumberField engineCapacityUpdate = new NumberField("Engine capacity");
    private TextField bodyClassUpdate = new TextField("Body class");
    private IntegerField mileageUpdate = new IntegerField("Mileage");
    private BigDecimalField costPerDayUpdate = new BigDecimalField("Cost / day");

    private Dialog newRentalDialog = new Dialog();
    private Binder<RentalDto> binderForRental = new Binder<>();
    private DatePicker startRentDate = new DatePicker("Rented from");
    private DatePicker endRentDate = new DatePicker("Rented to");

    @Autowired
    public CarsView(CarClient carClient, RentalClient rentalClient, RentalsView rentalsView) {
        this.carClient = carClient;
        this.rentalClient = rentalClient;
        this.rentalsView = rentalsView;

        bindFields();

        VerticalLayout newCarDialogLayout = new VerticalLayout();
        Button saveCarButton = createSaveCarButton();
        newCarDialogLayout.add(vin, brand, model, productionYear, fuelType, engineCapacity, bodyClass, mileage,
                costPerDay, saveCarButton);
        addCarDialog.isCloseOnOutsideClick();
        addCarDialog.add(newCarDialogLayout);

        VerticalLayout updateCarDialogLayout = new VerticalLayout();
        Button confirmUpdateButton = createConfirmUpdateButton();
        updateCarDialogLayout.add(vinUpdate, brandUpdate, modelUpdate, productionYearUpdate, fuelTypeUpdate,
                engineCapacityUpdate, bodyClassUpdate, mileageUpdate, costPerDayUpdate, confirmUpdateButton);
        updateCarDialog.isCloseOnOutsideClick();
        updateCarDialog.add(updateCarDialogLayout);

        VerticalLayout newRentalDialogLayout = new VerticalLayout();
        Button confirmRentButton = createConfirmRentalButton();
        newRentalDialogLayout.add(startRentDate, endRentDate, confirmRentButton);
        newRentalDialog.isCloseOnOutsideClick();
        newRentalDialog.add(newRentalDialogLayout);

        setColumns();

        carGrid.addComponentColumn(this::createRentalButton);
        carGrid.addComponentColumn(this::createUpdateButton);
        carGrid.addComponentColumn(this::createDeleteButton);

        addCarButton.addClickListener(e -> addCarDialog.open());

        carGrid.setHeightByRows(true);
        carGrid.setMaxHeight("200px");
        carGrid.setSizeFull();

        add(addCarButton, carGrid, addCarDialog);
    }

    public void refreshCarsForAdmin() {
        loggedUserDto = null;
        addCarButton.setEnabled(true);
        List<CarDto> cars = carClient.getCars();
        carGrid.setItems(cars);
    }

    public void refreshCarsForUser(UserDto userDto) {
        loggedUserDto = userDto;
        addCarButton.setEnabled(false);
        List<CarDto> cars = carClient.getCars();
        carGrid.setItems(cars);
    }

    private void saveCar(CarDto carDto) {
        carClient.saveCar(carDto);
        refreshCarsForAdmin();
        addCarDialog.close();
        clearFields();
    }

    private void clearFields() {
        vin.clear();
        brand.clear();
        model.clear();
        productionYear.clear();
        fuelType.clear();
        engineCapacity.clear();
        bodyClass.clear();
        mileage.clear();
        costPerDay.clear();
    }

    private Button createSaveCarButton() {
        return new Button("Save car", event -> {
            binderForSavingCar.writeBeanIfValid(carDto);
            saveCar(carDto);
        });
    }

    private Button createUpdateButton(CarDto carDto) {
        Button updateButton = new Button("Update");
        updateButton.addClickListener(e -> {
            carId = carDto.getId();
            binderForUpdatingCar.readBean(carDto);
            updateCarDialog.open();
        });
        if (loggedUserDto == null) {
            updateButton.setEnabled(true);
        } else {
            updateButton.setEnabled(false);
        }
        return updateButton;
    }

    private Button createConfirmUpdateButton() {
        return new Button("Confirm", event -> {
            binderForUpdatingCar.writeBeanIfValid(carDto);
            carDto.setId(carId);
            carClient.updateCar(carDto);
            refreshCarsForAdmin();
            updateCarDialog.close();
        });
    }

    private Button createRentalButton(CarDto carDto) {
        Button rentalButton = new Button("Rent");
        rentalButton.addClickListener(e -> {
            carId = carDto.getId();
            newRentalDialog.open();
        });
        if (loggedUserDto == null) {
            rentalButton.setEnabled(false);
        } else {
            if (carDto.getStatus().equals(Status.RENTED)) {
                rentalButton.setEnabled(false);
            } else {
                rentalButton.setEnabled(true);
            }
        }
        return rentalButton;
    }

    private Button createConfirmRentalButton() {
        return new Button("Confirm rental", event -> {
            RentalDto rentalDto = new RentalDto();
            binderForRental.writeBeanIfValid(rentalDto);
            rentalDto.setCarId(carId);
            rentalDto.setUserId(loggedUserDto.getId());
            rentalClient.createRental(rentalDto);
            rentalsView.refreshRentalsForUser(loggedUserDto);
            refreshCarsForUser(loggedUserDto);
            newRentalDialog.close();
        });
    }

    private Button createDeleteButton(CarDto carDto) {
        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(e -> {
            carClient.deleteCar(carDto.getId());
            refreshCarsForAdmin();
        });
        if (loggedUserDto == null) {
            deleteButton.setEnabled(true);
        } else {
            deleteButton.setEnabled(false);
        }
        return deleteButton;
    }

    private void setColumns() {
        carGrid.addColumn(CarDto::getId).setHeader("Id");
        carGrid.addColumn(CarDto::getBrand).setHeader("Brand");
        carGrid.addColumn(CarDto::getModel).setHeader("Model");
        carGrid.addColumn(CarDto::getProductionYear).setHeader("Year");
        carGrid.addColumn(CarDto::getMileage).setHeader("Mileage");
        carGrid.addColumn(CarDto::getCostPerDay).setHeader("Cost/day");
        carGrid.addColumn(CarDto::getStatus).setHeader("Status");
    }

    private void bindFields() {
        binderForSavingCar.forField(vin)
                .bind(CarDto::getVin, CarDto::setVin);
        binderForSavingCar.forField(brand)
                .bind(CarDto::getBrand, CarDto::setBrand);
        binderForSavingCar.forField(model)
                .bind(CarDto::getModel, CarDto::setModel);
        binderForSavingCar.forField(productionYear)
                .bind(CarDto::getProductionYear, CarDto::setProductionYear);
        binderForSavingCar.forField(fuelType)
                .bind(CarDto::getFuelType, CarDto::setFuelType);
        binderForSavingCar.forField(engineCapacity)
                .bind(CarDto::getEngineCapacity, CarDto::setEngineCapacity);
        binderForSavingCar.forField(bodyClass)
                .bind(CarDto::getBodyClass, CarDto::setBodyClass);
        binderForSavingCar.forField(mileage)
                .bind(CarDto::getMileage, CarDto::setMileage);
        binderForSavingCar.forField(costPerDay)
                .bind(CarDto::getCostPerDay, CarDto::setCostPerDay);

        binderForUpdatingCar.forField(vinUpdate)
                .bind(CarDto::getVin, CarDto::setVin);
        binderForUpdatingCar.forField(brandUpdate)
                .bind(CarDto::getBrand, CarDto::setBrand);
        binderForUpdatingCar.forField(modelUpdate)
                .bind(CarDto::getModel, CarDto::setModel);
        binderForUpdatingCar.forField(productionYearUpdate)
                .bind(CarDto::getProductionYear, CarDto::setProductionYear);
        binderForUpdatingCar.forField(fuelTypeUpdate)
                .bind(CarDto::getFuelType, CarDto::setFuelType);
        binderForUpdatingCar.forField(engineCapacityUpdate)
                .bind(CarDto::getEngineCapacity, CarDto::setEngineCapacity);
        binderForUpdatingCar.forField(bodyClassUpdate)
                .bind(CarDto::getBodyClass, CarDto::setBodyClass);
        binderForUpdatingCar.forField(mileageUpdate)
                .bind(CarDto::getMileage, CarDto::setMileage);
        binderForUpdatingCar.forField(costPerDayUpdate)
                .bind(CarDto::getCostPerDay, CarDto::setCostPerDay);

        binderForRental.forField(startRentDate)
                .bind(RentalDto::getRentedFrom, RentalDto::setRentedFrom);
        binderForRental.forField(endRentDate)
                .bind(RentalDto::getRentedTo, RentalDto::setRentedTo);
    }
}

