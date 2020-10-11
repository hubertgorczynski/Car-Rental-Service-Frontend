package com.carRental.views.user;

import com.carRental.client.UserClient;
import com.carRental.domain.UserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class UserAccountView extends VerticalLayout {

    private final UserClient userClient;

    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Last name");
    private TextField email = new TextField("Email address");
    private IntegerField phoneNumber = new IntegerField("Phone number");
    private TextField password = new TextField("Password");

    private Binder<UserDto> binder = new Binder<>();
    private UserDto loggedUserDto = new UserDto();
    private Long userId;

    @Autowired
    public UserAccountView(UserClient userClient) {
        this.userClient = userClient;

        bindFields();

        Button updateUserButton = new Button("Modify personal data");
        updateUserButton.addClickListener(e -> {
            if (binder.writeBeanIfValid(loggedUserDto)) {
                updateUser(loggedUserDto);
            }
        });

        Button deleteUserButton = new Button("Delete account");
        deleteUserButton.addClickListener(e -> {
            deleteUser(loggedUserDto);
            getUI().get().navigate("loginView");
        });

        VerticalLayout accountLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        horizontalLayout.add(updateUserButton, deleteUserButton);
        accountLayout.add(name, surname, email, phoneNumber, password, horizontalLayout);

        add(accountLayout);
        setHorizontalComponentAlignment(Alignment.CENTER, accountLayout);
    }

    public void refreshForUser(UserDto userDto) {
        loggedUserDto = userDto;
        userId = userDto.getId();
        binder.readBean(userDto);
    }

    private void updateUser(UserDto userDto) {
        userDto.setId(userId);
        userClient.updateUser(userDto);
    }

    private void deleteUser(UserDto userDto) {
        userClient.deleteUser(userDto.getId());
    }

    private void bindFields() {
        binder.forField(name)
                .bind(UserDto::getName, UserDto::setName);
        binder.forField(surname)
                .bind(UserDto::getLastName, UserDto::setLastName);
        binder.forField(email)
                .bind(UserDto::getEmail, UserDto::setEmail);
        binder.forField(phoneNumber)
                .bind(UserDto::getPhoneNumber, UserDto::setPhoneNumber);
        binder.forField(password)
                .bind(UserDto::getPassword, UserDto::setPassword);
    }
}
