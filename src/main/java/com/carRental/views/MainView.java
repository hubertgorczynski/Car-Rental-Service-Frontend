package com.carRental.views;

import com.carRental.views.car.CarsView;
import com.carRental.views.access.LoginView;
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
@Route
public class MainView extends VerticalLayout {

    private final CarsView carsView;
    private final UsersView usersView;
    private final RentalsView rentalsView;
    private final LoginView loginView;
    private final LogoutView logoutView;

    @Autowired
    public MainView(CarsView carsView, UsersView usersView, RentalsView rentalsView, LoginView loginView,
                    LogoutView logoutView) {
        this.carsView = carsView;
        this.usersView = usersView;
        this.rentalsView = rentalsView;
        this.loginView = loginView;
        this.logoutView = logoutView;

        PagedTabs tabs = new PagedTabs();
        Tab carsTab = new Tab("Cars");
        Tab usersTab = new Tab("Users");
        Tab rentalsTab = new Tab("Rentals");
        Tab loginTab = new Tab("Login");
        Tab logoutTab = new Tab("Logout");

        tabs.add(loginView, loginTab);
        tabs.add(carsView, carsTab);
        tabs.add(usersView, usersTab);
        tabs.add(rentalsView, rentalsTab);
        tabs.add(logoutView, logoutTab);

        carsTab.setEnabled(true);
        usersTab.setEnabled(false);
        rentalsTab.setEnabled(false);
        logoutTab.setEnabled(false);

        add(tabs);
    }
}
