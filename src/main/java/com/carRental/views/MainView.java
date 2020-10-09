package com.carRental.views;

import com.carRental.domain.UserDto;
import com.carRental.views.car.CarsView;
import com.carRental.views.access.LogoutView;
import com.carRental.views.rental.RentalsView;
import com.carRental.views.user.UsersView;
import com.carRental.views.utils.PagedTabs;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Route(value = "mainView")
public class MainView extends VerticalLayout {

    private final CarsView carsView;
    private final UsersView usersView;
    private final RentalsView rentalsView;
    private final LogoutView logoutView;

    PagedTabs tabs = new PagedTabs();
    Tab carsTab = new Tab("Cars");
    Tab usersTab = new Tab("Users");
    Tab rentalsTab = new Tab("Rentals");
    Tab logoutTab = new Tab("Logout");

    @Autowired
    public MainView(CarsView carsView, UsersView usersView, RentalsView rentalsView, LogoutView logoutView) {
        this.carsView = carsView;
        this.usersView = usersView;
        this.rentalsView = rentalsView;
        this.logoutView = logoutView;

        tabs.add(carsView, carsTab);
        tabs.add(usersView, usersTab);
        tabs.add(rentalsView, rentalsTab);
        tabs.add(logoutView, logoutTab);

        add(tabs);
    }

    public void adminViewSetup() {
        usersTab.setEnabled(true);
        carsView.refreshCarsForAdmin();
        usersView.refreshUsers();
        rentalsView.refreshRentalsForAdmin();
    }

    public void userViewSetup(UserDto userDto) {
        carsView.refreshCarsForUser();
        rentalsView.refreshRentalsForUser(userDto);
        usersTab.setEnabled(false);
    }

    public void setBackStartingTab() {
        tabs.select(carsTab);
    }
}
