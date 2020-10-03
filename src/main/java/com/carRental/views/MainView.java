package com.carRental.views;

import com.carRental.views.car.CarView;
import com.carRental.views.utils.PagedTabs;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Route
public class MainView extends VerticalLayout {

    private final CarView carView;

    @Autowired
    public MainView(CarView carView) {
        this.carView = carView;

        Button button3 = new Button("Click3");

        PagedTabs tabs = new PagedTabs();
        tabs.add(carView, "Cars");
        tabs.add(button3, "Tab caption 2");

        add(tabs);
    }
}
