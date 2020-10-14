package com.carRental.views.registration;

import com.carRental.client.UserClient;
import com.carRental.domain.UserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

@UIScope
@Component
@Route(value = "")
public class RegistrationView extends VerticalLayout {

    private final UserClient userClient;
    private UserDto userDto = new UserDto();

    private Binder<UserDto> binder = new Binder<>();
    private TextField name = new TextField("Name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("E-mail address");
    private PasswordField password = new PasswordField("Password");
    private IntegerField phoneNumber = new IntegerField("Phone number");

    @Autowired
    public RegistrationView(UserClient userClient) {
        this.userClient = userClient;

        bindFields();

        Span welcomeTitle = new Span("Welcome in Car Rental Service!");
        Span spaceSpan = new Span(" ");
        Span registrationTitle = new Span("Create new account below or log in.");
        Button loginButton = createLoginButton();
        Button registerButton = createRegisterButton();

        add(welcomeTitle,spaceSpan, registrationTitle, name, lastName, email, password, phoneNumber, loginButton, registerButton);
        setAlignItems(Alignment.CENTER);
    }

    private void save(UserDto userDto) {
        if (!userClient.isUserRegistered(userDto.getEmail())) {
            userClient.registerUser(userDto);
            getUI().ifPresent(ui -> ui.navigate("loginView"));
            clearFields();
        } else {
            System.out.println("User is already registered");
        }
    }

    private Button createLoginButton() {
        return new Button("I have account. Log me in.", event ->
                getUI().ifPresent(ui -> ui.navigate("loginView")));
    }

    private Button createRegisterButton() {
        return new Button("Create account", event -> {
            binder.writeBeanIfValid(userDto);
            save(userDto);
        });
    }

    private void clearFields() {
        name.clear();
        lastName.clear();
        email.clear();
        phoneNumber.clear();
        password.clear();
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
