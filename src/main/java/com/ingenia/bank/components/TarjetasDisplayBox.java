package com.ingenia.bank.components;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import com.github.appreciated.card.ClickableCard;
import com.ingenia.bank.backend.model.Movimiento;
import com.ingenia.bank.backend.model.Tarjeta;
import com.ingenia.bank.backend.service.MovimientoService;
import com.ingenia.bank.backend.service.TarjetaService;
import com.ingenia.bank.backend.utils.Utils;
import com.ingenia.bank.views.tarjeta.form.TarjetaDialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TarjetasDisplayBox extends ClickableCard {

	MovimientoService movimientoService;

	public TarjetasDisplayBox(Tarjeta tarjeta,MovimientoService movimientoService,TarjetaService tarjetaService) {
//		super();
		super(componentEvent -> new TarjetaDialog(movimientoService, tarjetaService, tarjeta.getId()).open()); // TODO implementar click tarjeta especifica
		this.movimientoService = movimientoService;
		
		// Set some style
		this.setWidth("255px");
		this.setHeight("150px");
		this.getElement().getStyle().set("radius", "24px");
		
		// Create layout
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        
        HorizontalLayout icon = new HorizontalLayout();
        // Icono entidad
        Image imgLogo1 = new Image("images/ingenia.svg", "Ingenia Bank");
        imgLogo1.setWidth("40px");
        imgLogo1.setHeight("25px");
        Image imgLogo2 = new Image("images/bank.svg", "Ingenia Bank");
        imgLogo2.setWidth("30px");
        imgLogo2.setHeight("20px");
        imgLogo2.getElement().getStyle().set("margin-left", "1px");
        icon.add(imgLogo1,imgLogo2);

        layout.add(icon);
//        Span bancoEntidad = new Span("Ingenia Bank");
//        bancoEntidad.getElement().getStyle().set("font-family", "DM Sans");
//        bancoEntidad.getElement().getStyle().set("font-weight", "bold");
//        bancoEntidad.getElement().getStyle().set("color", "#090A25");
//        layout.add(bancoEntidad);
        
      
        DecimalFormat df = new DecimalFormat("#,###.##");
        Span saldoTexto = new Span();
        saldoTexto.getElement().getStyle().set("color", "#D01E69");
        saldoTexto.add(df.format(tarjeta.getCuenta().getSaldo())+" €");
        saldoTexto.getElement().getStyle().set("font-weight", "bold");
        layout.setHorizontalComponentAlignment(Alignment.CENTER,
        		saldoTexto);
        layout.add(saldoTexto);

        
        HorizontalLayout downPart = new HorizontalLayout();
        downPart.setWidthFull();
        Image img = new Image("images/Vector.png", "visa");
        img.setWidth("35px");
        img.setHeight("15px");
        img.getElement().getStyle().set("padding-top", "10px");
        
        downPart.add(img);
                

        Span tarjetaNumber = new Span(Utils.enmascararNumeroTarjeta(tarjeta.getNumero()));
        tarjetaNumber.getElement().getStyle().set("margin-left", "auto");
        tarjetaNumber.getElement().getStyle().set("color", "#D01E69");
        tarjetaNumber.getElement().getStyle().set("font-weight", "bold");
        downPart.add(tarjetaNumber);
        
        downPart.getElement().getStyle().set("margin-top", "auto");
        downPart.setPadding(true);
        
        layout.add(downPart);
        add(layout);
        
    }
	
}
