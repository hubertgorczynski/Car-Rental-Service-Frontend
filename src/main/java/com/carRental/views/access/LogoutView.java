package com.carRental.views.access;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class LogoutView extends VerticalLayout {

    Dialog logOutDialog = new Dialog();

    public LogoutView() {
        Button logOutButton = new Button("Log out");
        logOutButton.addClickListener(e -> logOut());

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> cancel());

        HorizontalLayout dialogLayout = new HorizontalLayout();
        dialogLayout.add(logOutButton, cancelButton);
        logOutDialog.add(dialogLayout);
    }

    public void logOut() {
        logOutDialog.close();
        getUI().get().navigate("loginView");
    }

    public void cancel() {
        logOutDialog.close();
    }

    public void displayLogOutDialog() {
        logOutDialog.open();
    }
}
