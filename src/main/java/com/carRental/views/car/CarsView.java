package com.carRental.views.car;

import com.carRental.client.CarClient;
import com.carRental.domain.CarDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarsView extends VerticalLayout {

    private final Grid<CarDto> carGrid = new Grid<>(CarDto.class);
    private final CarClient carClient;

    private Dialog dialog = new Dialog();
    private TextField vin = new TextField("Vin");
    private TextField brand = new TextField("Brand");
    private TextField model = new TextField("Model");
    private IntegerField productionYear = new IntegerField("Production year");
    private TextField fuelType = new TextField("Fuel type");
    private NumberField engineCapacity = new NumberField("Engine capacity");
    private TextField bodyClass = new TextField("Body class");
    private IntegerField mileage = new IntegerField("Mileage");
    private BigDecimalField costPerDay = new BigDecimalField("Cost / day");

    private Binder<CarDto> binder = new Binder<>();
    private CarDto carDto = new CarDto();

    @Autowired
    public CarsView(CarClient carClient) {
        this.carClient = carClient;

        VerticalLayout dialogLayout = new VerticalLayout();
        Button addCarButton = new Button("Add new car");
        Button saveCarButton = new Button("Save car");
        bindFields();

        addCarButton.addClickListener(e -> dialog.open());
        saveCarButton.addClickListener(e -> {
            binder.writeBeanIfValid(carDto);
            saveCar(carDto);
        });

        dialogLayout.add(vin, brand, model, productionYear, fuelType, engineCapacity, bodyClass, mileage, costPerDay, saveCarButton);

        dialog.isCloseOnOutsideClick();
        dialog.add(dialogLayout);

        carGrid.setColumns(
                "id",
                "vin",
                "brand",
                "model",
                "productionYear",
                "fuelType",
                "engineCapacity",
                "bodyClass",
                "mileage",
                "costPerDay",
                "status");

        add(addCarButton, carGrid, dialog);
    }

    public void refreshCars() {
        List<CarDto> cars = carClient.getCars();
        carGrid.setItems(cars);
    }

    private void saveCar(CarDto carDto) {
        carClient.saveCar(carDto);
        refreshCars();
        dialog.close();
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

    private void bindFields() {
        binder.forField(vin)
                .bind(CarDto::getVin, CarDto::setVin);
        binder.forField(brand)
                .bind(CarDto::getBrand, CarDto::setBrand);
        binder.forField(model)
                .bind(CarDto::getModel, CarDto::setModel);
        binder.forField(productionYear)
                .bind(CarDto::getProductionYear, CarDto::setProductionYear);
        binder.forField(fuelType)
                .bind(CarDto::getFuelType, CarDto::setFuelType);
        binder.forField(engineCapacity)
                .bind(CarDto::getEngineCapacity, CarDto::setEngineCapacity);
        binder.forField(bodyClass)
                .bind(CarDto::getBodyClass, CarDto::setBodyClass);
        binder.forField(mileage)
                .bind(CarDto::getMileage, CarDto::setMileage);
        binder.forField(costPerDay)
                .bind(CarDto::getCostPerDay, CarDto::setCostPerDay);
    }
}
