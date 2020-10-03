package com.carRental.views.access;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
public class LoginView extends VerticalLayout {

    LoginForm loginForm = new LoginForm();

    public LoginView() {
        loginForm.setForgotPasswordButtonVisible(true);
        add(loginForm);
        setHorizontalComponentAlignment(Alignment.CENTER, loginForm);
    }
}

