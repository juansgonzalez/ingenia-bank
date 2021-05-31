package com.ingenia.bank.views.inicio;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.ingenia.bank.components.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.helper.Series;
import com.ingenia.bank.backend.model.Categoria;
import com.ingenia.bank.backend.model.Cuenta;
import com.ingenia.bank.backend.model.Movimiento;
import com.ingenia.bank.backend.model.Tarjeta;
import com.ingenia.bank.backend.model.TipoMovimiento;
import com.ingenia.bank.backend.model.Usuario;
import com.ingenia.bank.backend.payload.filter.MovimientoMesFilter;
import com.ingenia.bank.backend.service.CategoriaService;
import com.ingenia.bank.backend.service.CuentaService;
import com.ingenia.bank.backend.service.MovimientoService;
import com.ingenia.bank.backend.service.TarjetaService;
import com.ingenia.bank.backend.service.UsuarioService;
import com.ingenia.bank.backend.utils.Utils;
import com.ingenia.bank.views.main.MainView;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.GeneratedVaadinDialog;
import com.vaadin.flow.component.dialog.GeneratedVaadinDialog.OpenedChangeEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "inicio", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Inicio")
public class InicioView extends HorizontalLayout {
	
	private long idCuenta;

	private Grid<Movimiento> grid;
	
	@Autowired
	private MovimientoService movimientoService;
	
	@Autowired
	private TarjetaService tarjetaService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private CuentaService cuentaService;
	
	private String[] fechasDiagrama;
	
	private Double[] listadoGastosDiariosDiagrama;
	
	private CuentaSelectComponent cuentaSelect;

	
	public InicioView(CuentaService cuentaService, UsuarioService usuarioService, TarjetaService tarjetaService,MovimientoService movimientoService,CategoriaService categoriaService) {
		// Iicializamos los services
		this.cuentaService = cuentaService;
		this.movimientoService = movimientoService;
		this.tarjetaService =  tarjetaService;
		this.categoriaService = categoriaService;
		this.usuarioService = usuarioService;
		
		// Configuracion general de la pantalla incio
		setSizeFull();
		setPadding(true);
		
		Optional<Usuario> user = Utils.getCurrentUser(this.usuarioService);
        
        if(user.isPresent()) {
        	List<Cuenta> listaCuentasUsuario = this.cuentaService.obtenerTodasCuentasByUsuarioId(user.get().getId());
        	if(UI.getCurrent().getSession().getAttribute("idCuenta") == null) {
        		if(listaCuentasUsuario.size() > 1) {
        			cuentaSelect = new CuentaSelectComponent(listaCuentasUsuario);
        			cuentaSelect.open();
        			
        			cuentaSelect.addOpenedChangeListener(new ComponentEventListener<GeneratedVaadinDialog.OpenedChangeEvent<Dialog>>() {
        				
        				@Override
        				public void onComponentEvent(OpenedChangeEvent<Dialog> event) {
        					if(!event.isOpened()) { // Check if the form was closed
        						UI.getCurrent().getSession().setAttribute("idCuenta", cuentaSelect.getCuenta().getId());     
        						UI.getCurrent().getPage().reload();
        					}
        					
        				}
        			});
        			
        		}else if(listaCuentasUsuario.size() == 1) {
        			Cuenta cuenta = listaCuentasUsuario.get(0);
        			UI.getCurrent().getSession().setAttribute("idCuenta", cuenta.getId());
        			UI.getCurrent().getPage().reload();
        		}else {
        			Notification.show("Error al obtener Cuentas del usuario.");
        		}        		
        	} else {
        		this.idCuenta = (long) UI.getCurrent().getSession().getAttribute("idCuenta");
        		
        		// Creamos el layout de la parte izquierda
        		VerticalLayout leftLayout = new VerticalLayout();
        		leftLayout.setWidth("60%");
        		
        		// añadimos el layout de titulo de las tarjetas a la parte izquierda
        		leftLayout.add(new TitleWithLink("Tarjetas","Ver Tarjetas","tarjetas"));
        		
        		// creamos el layout para las tarjetas
        		HorizontalLayout layoutTarjetasCredito = new HorizontalLayout();
        		layoutTarjetasCredito.setWidthFull();
        		layoutTarjetasCredito.setPadding(true);
        		
        		//Obtenemos las tarjetas de una cuenta
        		List<Tarjeta> listaTarjetas = this.tarjetaService.obtenerTarjetaByCuenta(idCuenta);
        		
        		// Creamos el card para cada tarjeta hasta un maximo de 3 
        		for (int i = 0; i < listaTarjetas.size() && i < 3; i++) {
        			TarjetasDisplayBox displayTarjeta = new TarjetasDisplayBox(listaTarjetas.get(i), this.movimientoService, this.tarjetaService);
        			layoutTarjetasCredito.add(displayTarjeta);
        		}
        		
        		// Añadimos las tarjetas al layout izquierdo
        		leftLayout.add(layoutTarjetasCredito);
        		
        		// Añadimos una separacion
        		Hr limiter = new Hr();
        		limiter.setWidthFull();
        		leftLayout.add(limiter);
        		
        		// Creamos el layout para los movimientos
        		createGrid();
        		List<Movimiento> listaMovimientos = this.movimientoService.obtenerMovimientosDeCuentaOrdenadosFecha(idCuenta);
        		// Obtenemos solo los primeros 6 movimientos para mostrar en la pantalla principal
        		if(listaMovimientos.size() > 6) {
        			listaMovimientos = listaMovimientos.subList(0, 6);
        		}
        		grid.setDataProvider(new ListDataProvider<>(listaMovimientos));
        		
        		// Añadimos el titulo y el grid para moviminetos a la vista Izquierda
        		leftLayout.add(new TitleWithLink("Movimientos","Ver mas","movimientos"),grid);
        		
        		
        		
        		// Creamos la parte derecha de la vista
        		VerticalLayout rightLayout = new VerticalLayout();
        		rightLayout.setWidth("40%");
        		
        		// Creamos el titulo para el analisis
        		HorizontalLayout textAnalisis = new HorizontalLayout();
        		textAnalisis.setWidthFull();
        		H2 tituloAnalisis = new H2("Balance Cuenta");
        		tituloAnalisis.getElement().getStyle().set("margin-top", "0");
        		tituloAnalisis.getElement().getStyle().set("margin-right", "auto");
        		textAnalisis.add(tituloAnalisis);
        		
        		// añadimos el titulo a la parte derecha de la pantalla
        		rightLayout.add(textAnalisis);
        		
        		// Creamos la grafica con los gastos del mes actual
        		rightLayout.add(crearGraficagastosMesActual());
        		
        		// Creamos la linea de informacion texto sobre ingresos y retiradas de dinero del mes actual
        		HorizontalLayout gastos = new HorizontalLayout();
        		gastos.setWidthFull();
        		
        		// Obtenemos la fecha del mes actual
        		Calendar calendar = Calendar.getInstance();
        		calendar.setTime(new Date());
        		int diaFinalMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        		
        		LocalDate fechaInit = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        		LocalDate fechaFin = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), diaFinalMes);
        		
        		// Obtenemos la lista de movimientos del mes actual para la cuenta actual y los gastos y los ingresos del mes
        		List<Movimiento> movimientosMes =  this.movimientoService.obtenerMovimientoFechaCuenta(idCuenta,fechaInit,fechaFin);
        		double gastosMes = Utils.obtenerGastos(movimientosMes); 
        		double ingresosMes = Utils.obtenerIngresos(movimientosMes); 
        		DecimalFormat df = new DecimalFormat("#.##");
        		
        		Span ingresosMensaual = new Span("Ingresos del mes: "+df.format(ingresosMes)+" €");
        		ingresosMensaual.getElement().getStyle().set("margin-top", "0");
        		ingresosMensaual.getElement().getStyle().set("margin-right", "auto");
        		ingresosMensaual.getElement().getStyle().set("color", "#20F14E");
        		ingresosMensaual.getElement().getStyle().set("size", "16px");
        		gastos.add(ingresosMensaual);
        		
        		Span gastosMensaual = new Span("Gastos del mes: "+df.format(gastosMes)+" €");
        		gastosMensaual.getElement().getStyle().set("margin-top", "0");
        		gastosMensaual.getElement().getStyle().set("margin-left", "auto");
        		gastosMensaual.getElement().getStyle().set("color", "#FF0F0F");
        		gastosMensaual.getElement().getStyle().set("size", "16px");
        		gastos.add(gastosMensaual);
        		
        		
        		rightLayout.add(gastos);
        		
        		// Creamos el grafico de donut de los gastos por categoria
        		rightLayout.add(crearGraficoDonutGastosMesActualPorCategoria());
        		
        		// Añadimos la parte derecha y la parte izquierda separadas por un separador a la vista principal
        		add(leftLayout,new Divider(),rightLayout);
        	}
        }
		
		
		
	}
	
    /**
     * Metodo que se encarga de crear un Grid para la visualizacion de los datos de un movimiento
     * @return Devuelve un grid con los campos para representar Movimientos
     */
	private Grid<Movimiento> createGrid() {
		grid = new Grid<>();
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		grid.addComponentColumn(c -> new IconoMovimientoTarjeta(c))
		.setWidth("100px").setHeader("Tarjeta").setFlexGrow(1);
		grid.addColumn(c -> c.getCantidad()+" €").setHeader("Cantidad").setFlexGrow(1);
        grid.addColumn(c -> c.getConcepto()).setHeader("Concepto").setFlexGrow(1);
        grid.addColumn(c -> dateFormat.format(c.getFecha())).setHeader("Fecha").setWidth("125px").setFlexGrow(0);
        
        return grid;
	}
	
	/**
	 * Metodo para crear la grafica de gastos del mes actual
	 * @return Devuelve un grafico con los datos de los gastos mensuales del mes actual
	 */
    private ApexCharts crearGraficagastosMesActual() {
    	obtenerGastosDiariosMes();
    	
        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.area)
                        .withZoom(ZoomBuilder.get()
                                .withEnabled(false)
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withStroke(StrokeBuilder.get().withCurve(Curve.straight).build())
                .withSeries(new Series<>("Gastos",this.listadoGastosDiariosDiagrama))
                .withLabels(this.fechasDiagrama)
                .withYaxis(YAxisBuilder.get()
                        .withOpposite(true).build())
                .withLegend(LegendBuilder.get().withHorizontalAlign(HorizontalAlign.left).build())
                .withColors("#D01E69")
                .build();
    }

	/**
	 * Metodo encargado de obtenemos los datos de los movimientos para obtener lista de los movimientos y los dias realizados
	 */
	private void obtenerGastosDiariosMes() {
		// Obtenemos las fechas actuales inicial y final
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaFinalMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        
		List<Movimiento> movimientos = this.movimientoService.obtenerMovimientoFechaCuenta(idCuenta, LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(),1),
																							LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), diaFinalMes));
		List<Double> listaGastos = new ArrayList<Double>();
		List<String> listaFechas = new ArrayList<String>();
		
		Date fechaUtilizadaActual = null;
		double gastoDiario = 0;
		
		for (Iterator iterator = movimientos.iterator(); iterator.hasNext();) {
			Movimiento movimiento = (Movimiento) iterator.next();
			if(fechaUtilizadaActual == null) { //Comprobamos si se acaba de entrar en el bucle para inicializar la fecha del movimiento
				fechaUtilizadaActual = movimiento.getFecha();		
			}
			
			// Si hemos cambiado de fecha del movimiento se procede a guardar los datos recogidos
			if(!fechaUtilizadaActual.equals(movimiento.getFecha())) {
				listaGastos.add(gastoDiario);
				listaFechas.add(fechaUtilizadaActual.getDate()+"/"+Utils.getMonthForInt(fechaUtilizadaActual.getMonth()).substring(0, 3));
				gastoDiario = 0; // Reiniciamos el contador del gasto
				fechaUtilizadaActual = movimiento.getFecha(); // Almacenemos la fecha del nuevo gasto
			}
			
			// Si el movimiento que se ha realizado es un gasto lo sumamos al contador de gasto
			if(movimiento.getTipo().equals(TipoMovimiento.GASTO)) {
				gastoDiario += movimiento.getCantidad();					
			}
			
			// Comprobamos si es el ultimo item del iterador y almacenamos sus datos
			if(!iterator.hasNext()) {
				listaGastos.add(gastoDiario);
				listaFechas.add(fechaUtilizadaActual.getDate()+"/"+Utils.getMonthForInt(fechaUtilizadaActual.getMonth()).substring(0, 3));
			}
			
			
		}
		
		this.listadoGastosDiariosDiagrama = new Double[listaGastos.size()];
		this.listadoGastosDiariosDiagrama = listaGastos.toArray(this.listadoGastosDiariosDiagrama);
		this.fechasDiagrama = new String[listaFechas.size()];
		this.fechasDiagrama =  listaFechas.toArray(this.fechasDiagrama);
		
	}

	/**
	 * Metodo encargado de crar el grafico de donut de los gastos por categoria del mes actual
	 * @return Devuelve el grafico del donut con los datos de gastos del mes actual
	 */
	private ApexCharts crearGraficoDonutGastosMesActualPorCategoria() {
		List<Categoria> listaCategorias = categoriaService.obtenerTodasCategorias();
		// Obtenemos los nombres de las categorias
		String[] labes = obtenerLabes(listaCategorias);

		// Obtenemos el valor del gasto por cada categoria
		Double[] serie = obtenerMovimientos(listaCategorias);
		
		// Obtenemos colores aleatorios para cada fragmento del grafico
		String[] colors = obtenerColores(listaCategorias);
		
		return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.donut).build())
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.right)
                        .build())
                .withSeries(serie)
                .withResponsive(ResponsiveBuilder.get()
                        .withBreakpoint(480.0)
                        .build())
                .withLabels(labes)
                		.withColors(colors)
                .build();
	}

	
	/**
	 * Metodo que se encarga de generar Array con colores en hexadecimal en funcion de la cantidad de categorias que haya
	 * @param listaCategorias lista de categoria de las que se mirara cuantos colores se tienen que obtener
	 * @return Devuelve un Array de string con los colore spara las categorias
	 */
	private String[] obtenerColores(List<Categoria> listaCategorias) {
		String[] colores = new String[listaCategorias.size()];
		Random obj = new Random();
		for (int i = 0; i < listaCategorias.size(); i++) {
			int rand_num = obj.nextInt(0xffffff + 1);
			colores[i] = String.format("#%06x", rand_num);
		}
		return colores;
	}

	/**
	 * Metodo encargado de obtener un array con el valor del gasto que se ha realizado durante el mes actual
	 * @param listaCategorias
	 * @return Devuelve un array de double con el valor del gasto diario que se ha realizado dicho mes
	 */
	private Double[] obtenerMovimientos(List<Categoria> listaCategorias) {
		Series<Double> serie = new Series<>();
		Double[] serieData = new Double[listaCategorias.size()];
		for (int i = 0; i < listaCategorias.size(); i++) {
			Categoria categoriaActual = listaCategorias.get(i);
			serieData[i] = Utils.obtenerGastos(this.movimientoService.obtenerMovimientosCuentaByCategoria(idCuenta,new MovimientoMesFilter("05", categoriaActual.getId())));
		}
		serie.setData(serieData);
		return serieData;
	}

	/**
	 * Metodo encargado de obtener una lista con los nombres de todas las categorias 
	 * @param listaCategorias
	 * @return Devuelve un array de string con los nombres de las categorias
	 */
	private String[] obtenerLabes(List<Categoria> listaCategorias) {
		String[] labelsCategorias = new String[listaCategorias.size()];
		for (int i = 0; i < listaCategorias.size(); i++) {
			labelsCategorias[i] = listaCategorias.get(i).getNombre();
		}
		return labelsCategorias;
	}

	
}
