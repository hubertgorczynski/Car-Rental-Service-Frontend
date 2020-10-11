package com.carRental.views;

import com.carRental.domain.UserDto;
import com.carRental.views.car.CarsView;
import com.carRental.views.access.LogoutView;
import com.carRental.views.rental.RentalsView;
import com.carRental.views.user.UserAccountView;
import com.carRental.views.user.UsersView;
import com.carRental.views.utils.PagedTabs;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Route(value = "mainView")
public class MainView extends VerticalLayout {

    private final CarsView carsView;
    private final UsersView usersView;
    private final RentalsView rentalsView;
    private final UserAccountView userAccountView;
    private final LogoutView logoutView;

    PagedTabs tabs = new PagedTabs();
    Tab carsTab = new Tab("Cars");
    Tab usersTab = new Tab("Users");
    Tab rentalsTab = new Tab("Rentals");
    Tab userAccountTab = new Tab("User account");
    Tab logoutTab = new Tab();

    UserDto loggedUserDto;

    @Autowired
    public MainView(CarsView carsView, UsersView usersView, RentalsView rentalsView, UserAccountView userAccountView,
                    LogoutView logoutView) {
        this.carsView = carsView;
        this.usersView = usersView;
        this.rentalsView = rentalsView;
        this.userAccountView = userAccountView;
        this.logoutView = logoutView;

        tabs.add(carsView, carsTab);
        tabs.add(usersView, usersTab);
        tabs.add(rentalsView, rentalsTab);
        tabs.add(userAccountView, userAccountTab);
        tabs.add(logoutView, logoutTab);

        Button logoutButton = new Button("Log out");
        logoutTab.add(logoutButton);
        logoutButton.addClickListener(e -> {
            logoutView.displayLogOutDialog();
        });

        add(tabs);
    }

    public void adminViewSetup() {
        loggedUserDto = null;
        usersTab.setEnabled(true);
        carsView.refreshCarsForAdmin();
        usersView.refreshUsers();
        rentalsView.refreshRentalsForAdmin();
    }

    public void userViewSetup(UserDto userDto) {
        loggedUserDto = userDto;
        carsView.refreshCarsForUser(userDto);
        rentalsView.refreshRentalsForUser(userDto);
        usersTab.setEnabled(false);
        userAccountView.refreshForUser(userDto);
    }

    public void setBackStartingTab() {
        tabs.select(carsTab);
    }
}
