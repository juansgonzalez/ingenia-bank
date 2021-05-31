package com.ingenia.bank.views.cuenta;

import com.ingenia.bank.backend.model.Cuenta;
import com.ingenia.bank.backend.model.Usuario;
import com.ingenia.bank.backend.service.CuentaService;
import com.ingenia.bank.backend.service.MovimientoService;
import com.ingenia.bank.backend.service.UsuarioService;
import com.ingenia.bank.components.CardCuenta;
import com.ingenia.bank.views.main.MainView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Optional;

@Route(value = "cuentas", layout = MainView.class)
@PageTitle("Cuentas")
public class CuentasView extends VerticalLayout {

    private Optional<Usuario> currentUser;
    private List<Cuenta> cuentasList;

    private CuentaService cuentaService;
    private MovimientoService movimientoService;


    public CuentasView(CuentaService cuentaService, MovimientoService movimientoService, UsuarioService usuarioService) {

        this.movimientoService = movimientoService;
        this.cuentaService = cuentaService;
        this.currentUser = usuarioService.obtenerUsuarioActualConectado();
        this.cuentasList = cuentaService.obtenerTodasCuentasByUsuarioId(currentUser.get().getId());

        setSizeFull();

        add(new H2("Cuentas"), createCardCuentas());
    }

    /**
     * Crea un componente de cards con todas las cuentas del usuario. Los cards se muestran en una misma fila hasta llegar a 3,
     * por lo que si hay más de 3 cuentas, se crearán varias filas de cards.
     * @return componente con todas las cuentas en formato card
     */
    private VerticalLayout createCardCuentas(){

        VerticalLayout vl = new VerticalLayout();
        HorizontalLayout hl = new HorizontalLayout();
        int numCardsPerRow = 3;  // en cada layout horizontal habrá 3 cards como máximo.
        int contador = 0;

        for (Cuenta cuenta: this.cuentasList){
            contador++;

            if(contador <= numCardsPerRow){
                hl.add(new CardCuenta(cuenta, this.cuentaService, this.movimientoService));
            }else{
                contador = 1;
                vl.add(hl);
                hl = new HorizontalLayout();
                hl.add(new CardCuenta(cuenta, this.cuentaService, this.movimientoService));
            }
        }

        vl.add(hl);
        return vl;
    }
}
