package com.carRental.views.access;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
public class LogoutView extends VerticalLayout {

    public LogoutView() {
        Button button = new Button("Log out", new Icon(VaadinIcon.EXIT));
        button.setIconAfterText(true);

        add(button);
        setHorizontalComponentAlignment(Alignment.CENTER, button);
    }
}
