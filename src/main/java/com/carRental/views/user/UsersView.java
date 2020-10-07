package com.carRental.views.user;

import com.carRental.client.UserClient;
import com.carRental.domain.UserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersView extends VerticalLayout {

    private final Grid<UserDto> userGrid = new Grid<>(UserDto.class);
    private final UserClient userClient;

    @Autowired
    public UsersView(UserClient userClient) {
        this.userClient = userClient;

        Button addUserButton = new Button("Add new user");

        userGrid.setColumns(
                "id",
                "name",
                "lastName",
                "email",
                "password",
                "phoneNumber",
                "accountCreated");

        add(addUserButton, userGrid);
    }

    public void refreshUsers() {
        List<UserDto> users = userClient.getUsers();
        userGrid.setItems(users);
    }
}
