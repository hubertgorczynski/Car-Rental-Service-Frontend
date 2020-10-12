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

    private PagedTabs tabs = new PagedTabs();
    private Tab carsTab = new Tab("Cars");
    private Tab usersTab = new Tab("Users");
    private Tab rentalsTab = new Tab("Rentals");
    private Tab userAccountTab = new Tab("User account");
    private Tab logoutTab = new Tab();

    private UserDto loggedUserDto;

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

        Button logoutButton = createLogoutButton();
        logoutTab.add(logoutButton);

        add(tabs);
    }

    public void adminViewSetup() {
        loggedUserDto = null;
        userAccountTab.setVisible(false);
        usersTab.setVisible(true);
        carsView.refreshCarsForAdmin();
        rentalsView.refreshRentalsForAdmin();
        usersView.refreshUsers();
    }

    public void userViewSetup(UserDto userDto) {
        loggedUserDto = userDto;
        userAccountTab.setVisible(true);
        usersTab.setVisible(false);
        carsView.refreshCarsForUser(userDto);
        rentalsView.refreshRentalsForUser(userDto);
        userAccountView.refreshForUser(userDto);
    }

    public void setBackStartingTab() {
        tabs.select(carsTab);
    }

    private Button createLogoutButton() {
        return new Button("Log out", event -> logoutView.displayLogOutDialog());
    }
}
