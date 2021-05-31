package com.ingenia.bank.components;

import com.github.appreciated.card.ClickableCard;
import com.ingenia.bank.backend.model.Cuenta;
import com.ingenia.bank.backend.service.CuentaService;
import com.ingenia.bank.backend.service.MovimientoService;
import com.ingenia.bank.views.cuenta.form.CuentaDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CardCuenta extends ClickableCard {

    public CardCuenta(Cuenta cuenta, CuentaService cuentaService, MovimientoService movimientoService) {
        super(event -> {
            UI.getCurrent().getSession().setAttribute("idCuenta", cuenta.getId());
            new CuentaDialog(cuentaService, movimientoService, cuenta.getId()).open();
        });

        // estilo del card
        this.setWidth("300px");
        this.setHeight("150px");
        this.getElement().getStyle().set("radius", "24px");

        // layout principal que contendrá los layouts posteriores
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        // layout con el logo de ingenia bank
        HorizontalLayout imagenLayout = new HorizontalLayout();
        Image ingeniaImage = new Image("images/ingenia.svg", "Ingenia logo");
        Image bankImage = new Image("images/bank.svg", "Bank");
        ingeniaImage.setWidth("40%");
        bankImage.setWidth("25%");
        bankImage.getElement().getStyle().set("margin-left", "1px");
        imagenLayout.add(ingeniaImage, bankImage);

        // layout con el iban de la cuenta
        HorizontalLayout ibanLayout = new HorizontalLayout();
        Span ibanSpan = new Span();
        ibanSpan.add(cuenta.getIban());
        ibanSpan.getElement().getStyle().set("font-weight", "bold");
        ibanLayout.add(ibanSpan);


        // layout con el saldo de la cuenta
        HorizontalLayout saldoLayout = new HorizontalLayout();
        Span saldoSpan = new Span();
        saldoSpan.add(cuenta.getSaldo() +" €");
        saldoSpan.getElement().getStyle().set("color", "#D01E69");
        saldoSpan.getElement().getStyle().set("font-weight", "bold");
        saldoLayout.add(saldoSpan);


        // añadimos todos los layouts horizontales al layout principal
        layout.add(imagenLayout);
        layout.add(ibanLayout);
        layout.add(saldoLayout);

        add(layout);
    }

}
