package com.carRental.views.access;

import com.carRental.client.LoginClient;
import com.carRental.client.UserClient;
import com.carRental.domain.LoginDto;
import com.carRental.domain.UserDto;
import com.carRental.views.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Route("loginView")
public class LoginView extends VerticalLayout {

    private final MainView mainView;
    private final LoginClient loginClient;
    private final UserClient userClient;
    private EmailField email = new EmailField("E-mail address");
    private PasswordField password = new PasswordField("Password");
    private Button logIn = new Button("Log in");
    private Button register = new Button("You don't have account? Create it here.");
    private Binder<LoginDto> binder = new Binder<>();
    private UserDto userDto;

    @Autowired
    public LoginView(MainView mainView, LoginClient loginClient, UserClient userClient) {
        this.mainView = mainView;
        this.loginClient = loginClient;
        this.userClient = userClient;

        bindFields();

        logIn.addClickListener(e -> logIn());
        register.addClickListener(e -> {
            getUI().get().navigate("registrationView");
            clearFields();
        });

        add(email, password, logIn, register);
        setAlignItems(Alignment.CENTER);
    }

    private void logIn() {
        LoginDto loginDto = new LoginDto();
        binder.writeBeanIfValid(loginDto);
        clearFields();

        if (loginDto.getEmail().equals("admin@gmail.com") && (loginDto.getPassword().equals("admin"))) {
            mainView.adminViewSetup();
            mainView.setBackStartingTab();
            getUI().get().navigate("mainView");
        } else {
            if (loginClient.isLoginRegistered(loginDto)) {
                userDto = userClient.getUserByEmail(loginDto.getEmail());
                mainView.userViewSetup(userDto);
                mainView.setBackStartingTab();
                getUI().get().navigate("mainView");
            } else {
                System.out.println("User does not exist");
            }
        }
    }

    private void bindFields() {
        binder.forField(email)
                .bind(LoginDto::getEmail, LoginDto::setEmail);
        binder.forField(password)
                .bind(LoginDto::getPassword, LoginDto::setPassword);
    }

    private void clearFields() {
        email.clear();
        password.clear();
    }
}

