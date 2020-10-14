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
        Button logOutButton = createLogoutButton();
        Button cancelButton = createCancelButton();

        HorizontalLayout dialogLayout = new HorizontalLayout();
        dialogLayout.add(logOutButton, cancelButton);

        logOutDialog.add(dialogLayout);
    }

    public void logOut() {
        logOutDialog.close();
        getUI().ifPresent(ui -> ui.navigate(""));
    }

    public void cancel() {
        logOutDialog.close();
    }

    public void displayLogOutDialog() {
        logOutDialog.open();
    }

    private Button createLogoutButton() {
        return new Button("Log out", event -> logOut());
    }

    private Button createCancelButton() {
        return new Button("Cancel", event -> cancel());
    }
}
