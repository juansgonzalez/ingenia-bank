package com.ingenia.bank.views.login;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm login = new LoginForm(); // Instantiates a LoginForm component to capture username and password

    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);  // not show link forgot password
        
        HorizontalLayout icon = new HorizontalLayout();
        Image imgLogo1 = new Image("images/ingenia.svg", "Ingenia Bank");
        imgLogo1.setWidth("200px");
        imgLogo1.setHeight("150px");
        Image imgLogo2 = new Image("images/bank.svg", "Ingenia Bank");
        imgLogo2.setWidth("200px");
        imgLogo2.setHeight("150px");
        imgLogo2.getElement().getStyle().set("margin-left", "1px");
        icon.add(imgLogo1,imgLogo2);

        add(icon, login);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(beforeEnterEvent.getLocation()   // Reads query parameters and shows an error if a login attempt fails
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

}
