package com.carRental.views.registration;

import com.carRental.client.UserClient;
import com.carRental.domain.UserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Route(value = "registrationView")
public class RegistrationView extends VerticalLayout {

    private final UserClient userClient;
    private Binder<UserDto> binder = new Binder<>();
    private TextField name = new TextField("Name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("E-mail address");
    private PasswordField password = new PasswordField("Password");
    private IntegerField phoneNumber = new IntegerField("Phone number");

    private Button loginButton = new Button("I have account. Log me in.");
    private Button registerButton = new Button("Register");

    @Autowired
    public RegistrationView(UserClient userClient) {
        this.userClient = userClient;

        Span formTitle = new Span("Registration");

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

        loginButton.addClickListener(e ->
                getUI().get().navigate("loginView"));

        registerButton.addClickListener(e -> {
            save();
            getUI().get().navigate("loginView");
        });

        add(formTitle, name, lastName, email, password, phoneNumber, loginButton, registerButton);
        setHorizontalComponentAlignment(Alignment.CENTER, formTitle, name, lastName, email, password, phoneNumber,
                loginButton, registerButton);
    }

    private void save() {
        UserDto userDto = new UserDto();
        userDto.setName(name.getValue());
        userDto.setLastName(lastName.getValue());
        userDto.setEmail(email.getValue());
        userDto.setPassword(password.getValue());
        userDto.setPhoneNumber(phoneNumber.getValue());

        if (!userClient.isUserRegistered(userDto.getEmail())) {
            userClient.registerUser(userDto);
        } else {
            System.out.println("User is already registered");
        }
    }
}
