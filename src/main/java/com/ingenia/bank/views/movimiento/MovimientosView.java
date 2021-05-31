package com.ingenia.bank.views.movimiento;

import com.ingenia.bank.backend.model.Movimiento;
import com.ingenia.bank.backend.service.MovimientoService;
import com.ingenia.bank.components.IconoMovimientoTarjeta;
import com.ingenia.bank.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.SimpleDateFormat;
import java.util.List;

@Route(value = "movimientos", layout = MainView.class)
@PageTitle("Movimientos")
public class MovimientosView extends VerticalLayout {

    private MovimientoService movimientoService;

    private Long idCuentaActual;

    private List<Movimiento> movimientosList;
    private Grid<Movimiento> grid = new Grid<>(Movimiento.class);
    //private Optional<Usuario> currentUser;

    public MovimientosView(MovimientoService movimientoService){
        addClassName("movimientos-view");

        this.movimientoService = movimientoService;
        this.idCuentaActual = (Long) UI.getCurrent().getSession().getAttribute("idCuenta");
        this.movimientosList = movimientoService.obtenerMovimientosDeCuenta(this.idCuentaActual);

        setPadding(true);
        add(new H2("Movimientos"), createGridMovimientos());
    }


    /**
     * Carga la lista de movimientos ordenados por fecha en el grid
     */
    private void loadMovimientosGrid() {
        ListDataProvider<Movimiento> movimientosProvider;
        movimientosProvider = DataProvider.ofCollection(this.movimientosList);
        movimientosProvider.setSortOrder(Movimiento::getFecha, SortDirection.DESCENDING);
        grid.setDataProvider(movimientosProvider);
    }


    /**
     * Crea el grid de movimientos y lo configura
     * @return Component grid
     */
    private Component createGridMovimientos(){

        loadMovimientosGrid();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // indicamos columnas y el orden
        grid.setColumns();
        grid.addComponentColumn(movimiento -> new IconoMovimientoTarjeta(movimiento)).setHeader("Tarjeta").setFlexGrow(1);
        grid.addColumn(movimiento -> movimiento.getCantidad()+" €").setHeader("Cantidad").setFlexGrow(1);
        grid.addColumn(movimiento -> movimiento.getConcepto()).setHeader("Concepto").setFlexGrow(1);
        grid.addColumn(movimiento -> dateFormat.format(movimiento.getFecha())).setHeader("Fecha").setWidth("125px").setFlexGrow(0);


        // estilos del grid
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS,
                GridVariant.LUMO_ROW_STRIPES); //para que las filas pares e impares tengan colores diferentes

        return grid;
    }
}
