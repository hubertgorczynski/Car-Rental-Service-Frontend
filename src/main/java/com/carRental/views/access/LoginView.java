package com.carRental.views.access;

import com.carRental.client.LoginClient;
import com.carRental.domain.LoginDto;
import com.carRental.views.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Route("loginView")
public class LoginView extends VerticalLayout {

    private final MainView mainView;
    private final LoginClient loginClient;
    private EmailField email = new EmailField("E-mail address");
    private PasswordField password = new PasswordField("Password");
    private Button logIn = new Button("Log in");
    private Button register = new Button("You don't have account? Create it here.");
    private Binder<LoginDto> binder = new Binder<>();

    @Autowired
    public LoginView(MainView mainView, LoginClient loginClient) {
        this.mainView = mainView;
        this.loginClient = loginClient;

        add(email, password, logIn, register);
        setHorizontalComponentAlignment(Alignment.CENTER, email, password, logIn, register);

        binder.forField(email)
                .bind(LoginDto::getEmail, LoginDto::setEmail);
        binder.forField(password)
                .bind(LoginDto::getPassword, LoginDto::setPassword);

        logIn.addClickListener(e -> logIn());
        register.addClickListener(e ->
                getUI().get().navigate("registrationView"));
    }

    private void logIn() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email.getValue());
        loginDto.setPassword(password.getValue());

        if (loginDto.getEmail().equals("admin@gmail.com") && (loginDto.getPassword().equals("admin"))) {
            mainView.refresh();
            mainView.setBackStartingTab();
            getUI().get().navigate("mainView");
        } else {
            if (loginClient.isLoginRegistered(loginDto.getEmail(), loginDto.getPassword())) {
                mainView.userViewSetup();
                mainView.setBackStartingTab();
                getUI().get().navigate("mainView");
            } else {
                System.out.println("User does not exist");
            }
        }
    }
}

