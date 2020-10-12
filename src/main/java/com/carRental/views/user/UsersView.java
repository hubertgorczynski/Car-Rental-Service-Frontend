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
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class UsersView extends VerticalLayout {

    private final Grid<UserDto> userGrid = new Grid<>();
    private final UserClient userClient;
    private Dialog dialog = new Dialog();
    private Binder<UserDto> binder = new Binder<>();
    private TextField name = new TextField("Name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email address");
    private TextField password = new TextField("Password");
    private IntegerField phoneNumber = new IntegerField("Phone number");
    private UserDto userDto = new UserDto();

    @Autowired
    public UsersView(UserClient userClient) {
        this.userClient = userClient;

        bindFields();

        VerticalLayout dialogLayout = new VerticalLayout();
        Button saveUserButton = createSaveUserButton();
        dialogLayout.add(name, lastName, email, password, phoneNumber, saveUserButton);

        dialog.isCloseOnOutsideClick();
        dialog.add(dialogLayout);

        setColumns();

        Button addUserButton = createAddUserButton();
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

    private Button createAddUserButton() {
        return new Button("Add new user", event -> dialog.open());
    }

    private Button createSaveUserButton() {
        return new Button("Save user", event -> {
            binder.writeBeanIfValid(userDto);
            saveUser(userDto);
        });
    }

    private void setColumns() {
        userGrid.addColumn(UserDto::getId).setHeader("Id");
        userGrid.addColumn(UserDto::getName).setHeader("Name");
        userGrid.addColumn(UserDto::getLastName).setHeader("Surname");
        userGrid.addColumn(UserDto::getEmail).setHeader("Email");
        userGrid.addColumn(UserDto::getPassword).setHeader("Password");
        userGrid.addColumn(UserDto::getPhoneNumber).setHeader("Phone");
        userGrid.addColumn(UserDto::getAccountCreated).setHeader("Created");
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
