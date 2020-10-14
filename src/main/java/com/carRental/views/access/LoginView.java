package com.carRental.views.access;

import com.carRental.client.LoginClient;
import com.carRental.client.UserClient;
import com.carRental.domain.LoginDto;
import com.carRental.domain.UserDto;
import com.carRental.views.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
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
@Route(value = "loginView")
public class LoginView extends VerticalLayout {

    private final MainView mainView;
    private final LoginClient loginClient;
    private final UserClient userClient;

    private EmailField email = new EmailField("E-mail address");
    private PasswordField password = new PasswordField("Password");
    private Binder<LoginDto> binder = new Binder<>();
    private UserDto userDto;

    @Autowired
    public LoginView(MainView mainView, LoginClient loginClient, UserClient userClient) {
        this.mainView = mainView;
        this.loginClient = loginClient;
        this.userClient = userClient;

        bindFields();

        Button loginButton = createLoginButton();
        Button registerButton = createRegisterButton();
        add(email, password, loginButton, registerButton);
        setAlignItems(Alignment.CENTER);
    }

    private void logIn() {
        LoginDto loginDto = new LoginDto();
        binder.writeBeanIfValid(loginDto);
        clearFields();

        if (loginDto.getEmail().equals("admin") && (loginDto.getPassword().equals("admin"))) {
            mainView.adminViewSetup();
            mainView.setBackStartingTab();
            getUI().ifPresent(ui -> ui.navigate("mainView"));
        } else {
            if (loginClient.isLoginRegistered(loginDto)) {
                userDto = userClient.getUserByEmail(loginDto.getEmail());
                mainView.userViewSetup(userDto);
                mainView.setBackStartingTab();
                getUI().ifPresent(ui -> ui.navigate("mainView"));
            } else {
                Dialog invalidUserDialog = createInvalidUserDialog();
                invalidUserDialog.open();
            }
        }
    }

    private Dialog createInvalidUserDialog() {
        Dialog invalidUserDialog = new Dialog();
        VerticalLayout invalidUserLayout = new VerticalLayout();
        Button cancelInvalidDialogButton = new Button("Cancel", event -> invalidUserDialog.close());
        Label invalidUserLabel = new Label("Incorrect email address or password!");
        invalidUserLayout.add(invalidUserLabel, cancelInvalidDialogButton);
        invalidUserDialog.add(invalidUserLayout);
        return invalidUserDialog;
    }

    private Button createLoginButton() {
        return new Button("Log in", event -> logIn());
    }

    private Button createRegisterButton() {
        return new Button("You don't have account? Create it here!", event -> {
            getUI().ifPresent(ui -> ui.navigate(""));
            clearFields();
        });
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

