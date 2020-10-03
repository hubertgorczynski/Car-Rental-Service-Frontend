package com.carRental.views.access;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
public class LogoutView extends VerticalLayout {

    public LogoutView() {
        Button logoutButton = new Button("Log out");
        add(logoutButton);
    }
}
