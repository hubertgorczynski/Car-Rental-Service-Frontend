package com.carRental.views.user;

import com.carRental.client.UserClient;
import com.carRental.domain.UserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersView extends VerticalLayout {

    private final Grid<UserDto> userGrid = new Grid<>(UserDto.class);
    private final UserClient userClient;

    private Dialog dialog = new Dialog();
    private TextField name = new TextField("Name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email address");
    private TextField password = new TextField("Password");
    private IntegerField phoneNumber = new IntegerField("Phone number");
    private Binder<UserDto> binder = new Binder<>();
    private UserDto userDto = new UserDto();

    @Autowired
    public UsersView(UserClient userClient) {
        this.userClient = userClient;

        VerticalLayout dialogLayout = new VerticalLayout();
        Button addUserButton = new Button("Add new user");
        Button saveUserButton = new Button("Save user");

        bindFields();

        addUserButton.addClickListener(e -> dialog.open());
        saveUserButton.addClickListener(e -> {
            binder.writeBeanIfValid(userDto);
            saveUser(userDto);
        });

        dialogLayout.add(name, lastName, email, password, phoneNumber, saveUserButton);
        dialog.isCloseOnOutsideClick();
        dialog.add(dialogLayout);

        userGrid.setColumns(
                "id",
                "name",
                "lastName",
                "email",
                "password",
                "phoneNumber",
                "accountCreated");

        add(addUserButton, userGrid, dialog);
    }

    public void refreshUsers() {
        List<UserDto> users = userClient.getUsers();
        userGrid.setItems(users);
    }

    private void saveUser(UserDto userDto) {
        if (!userClient.isUserRegistered(userDto.getEmail())) {
            userClient.registerUser(userDto);
            refreshUsers();
            dialog.close();
            clearFields();
        } else {
            System.out.println("User is already registered");
        }
    }

    private void clearFields() {
        name.clear();
        lastName.clear();
        email.clear();
        password.clear();
        phoneNumber.clear();
    }

    private void bindFields() {
        binder.forField(name)
                .bind(UserDto::getName, UserDto::setName);
        binder.forField(lastName)
                .bind(UserDto::getLastName, UserDto::setLastName);
        binder.forField(email)
                .bind(UserDto::getEmail, UserDto::setEmail);
        binder.forField(password)
                .bind(UserDto::getPassword, UserDto::setPassword);
        binder.forField(phoneNumber)
                .bind(UserDto::getPhoneNumber, UserDto::setPhoneNumber);
    }
}
