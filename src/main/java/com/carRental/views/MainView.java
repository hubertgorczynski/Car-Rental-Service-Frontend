package com.carRental.views;

import com.carRental.views.car.CarView;
import com.carRental.views.car.RentalView;
import com.carRental.views.car.UserView;
import com.carRental.views.utils.PagedTabs;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Route
public class MainView extends VerticalLayout {

    private final CarView carView;
    private final UserView userView;
    private final RentalView rentalView;

    @Autowired
    public MainView(CarView carView, UserView userView, RentalView rentalView) {
        this.carView = carView;
        this.userView = userView;
        this.rentalView = rentalView;

        PagedTabs tabs = new PagedTabs();
        tabs.add(carView, "Cars");
        tabs.add(userView, "Users");
        tabs.add(rentalView, "Rentals");

        add(tabs);
    }
}
