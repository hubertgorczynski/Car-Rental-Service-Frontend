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

@UIScope
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
    private UserDto userDto = new UserDto();

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
            binder.writeBeanIfValid(userDto);
            save(userDto);
        });

        add(formTitle, name, lastName, email, password, phoneNumber, loginButton, registerButton);
        setHorizontalComponentAlignment(Alignment.CENTER, formTitle, name, lastName, email, password, phoneNumber,
                loginButton, registerButton);
    }

    private void save(UserDto userDto) {
        if (!userClient.isUserRegistered(userDto.getEmail())) {
            userClient.registerUser(userDto);
            getUI().get().navigate("loginView");
        } else {
            System.out.println("User is already registered");
        }
    }
}
