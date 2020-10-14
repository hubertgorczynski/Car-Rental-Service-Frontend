package com.carRental.views.registration;

import com.carRental.client.UserClient;
import com.carRental.domain.UserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        add(welcomeTitle, spaceSpan, registrationTitle, name, lastName, email, password, phoneNumber, registerButton,
                loginButton);
        setAlignItems(Alignment.CENTER);
    }

    private void save(UserDto userDto) {
        if (!userClient.isUserRegistered(userDto.getEmail())) {
            userClient.registerUser(userDto);
            getUI().ifPresent(ui -> ui.navigate("loginView"));
            clearFields();
        } else {
            Dialog alertDialog = createUserAlreadyRegisteredDialog();
            alertDialog.open();
        }
    }

    private Button createLoginButton() {
        return new Button("I already have account. Log me in.", event ->
                getUI().ifPresent(ui -> ui.navigate("loginView")));
    }

    private Button createRegisterButton() {
        return new Button("Create account", event -> {
            if (areFieldsFilled()) {
                binder.writeBeanIfValid(userDto);
                save(userDto);
            } else {
                Dialog alertDialog = createEmptyFieldsDialog();
                alertDialog.open();
            }
        });
    }

    private boolean areFieldsFilled() {
        return (!name.getValue().equals("") &&
                !lastName.getValue().equals("") &&
                !email.getValue().equals("") &&
                !password.getValue().equals("") &&
                phoneNumber.getValue() != null);
    }

    private Dialog createEmptyFieldsDialog() {
        Dialog alertDialog = new Dialog();
        VerticalLayout alertLayout = new VerticalLayout();
        Button cancelAlertButton = new Button("Cancel", event -> alertDialog.close());
        Label alertLabel = new Label("All fields must be filled!");
        alertLayout.add(alertLabel, cancelAlertButton);
        alertDialog.add(alertLayout);
        return alertDialog;
    }

    private Dialog createUserAlreadyRegisteredDialog() {
        Dialog alertDialog = new Dialog();
        VerticalLayout alertLayout = new VerticalLayout();
        Button cancelAlertButton = new Button("Cancel", event -> alertDialog.close());
        Label alertLabel = new Label("User is already registered!");
        alertLayout.add(alertLabel, cancelAlertButton);
        alertDialog.add(alertLayout);
        return alertDialog;
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
