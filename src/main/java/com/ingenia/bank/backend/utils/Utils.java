package com.ingenia.bank.backend.utils;

import com.ingenia.bank.backend.model.Movimiento;
import com.ingenia.bank.backend.model.TipoMovimiento;
import com.ingenia.bank.backend.model.Usuario;
import com.ingenia.bank.backend.service.UsuarioService;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Utils {
	
	public static Optional<Usuario> getCurrentUser(UsuarioService usuarioService){
	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    UserDetails userDetails = ((UserDetails)principal);
	    Optional<Usuario> usuarioActual = usuarioService.obtenerUsuarioByUsername(userDetails.getUsername());
	    return usuarioActual;
	}
	
	public static String getMonthForInt(int num) {
	        String month = "wrong";
	    DateFormatSymbols dfs = new DateFormatSymbols();
	    String[] months = dfs.getMonths();
	    if (num >= 0 && num <= 11 ) {
	        month = months[num];
	    }
	    return month;
	}
  
	public static double obtenerIngresos(List<Movimiento> listaMovimientos) {
		 double ingreso = 0;
		 for (Iterator iterator = listaMovimientos.iterator(); iterator.hasNext();) {
			 Movimiento movimiento = (Movimiento) iterator.next();
			 if(movimiento.getTipo().equals(TipoMovimiento.INGRESO)) {
				 ingreso += movimiento.getCantidad();
			 }
		 }
		 return ingreso;
	}

	public static String obtenerSaldoDeMovimientosFormateado(List<Movimiento> listaMovimientos){
		double balance = 0;
		for (Iterator iterator = listaMovimientos.iterator(); iterator.hasNext();) {
			Movimiento movimiento = (Movimiento) iterator.next();
			if(movimiento.getTipo().equals(TipoMovimiento.INGRESO)) {
				balance += movimiento.getCantidad();
			}else {
				balance -= movimiento.getCantidad();
			}
		}
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(balance);
	}

	public static double obtenerSaldoDeMovimientos(List<Movimiento> listaMovimientos){
		double balance = 0;
		for (Iterator iterator = listaMovimientos.iterator(); iterator.hasNext();) {
			Movimiento movimiento = (Movimiento) iterator.next();
			if(movimiento.getTipo().equals(TipoMovimiento.INGRESO)) {
				balance += movimiento.getCantidad();
			}else {
				balance -= movimiento.getCantidad();
			}
		}
		return balance;
	}

	public static double obtenerGastos(List<Movimiento> listaMovimientos) {
		double gastos = 0;
		for (Iterator iterator = listaMovimientos.iterator(); iterator.hasNext();) {
			Movimiento movimiento = (Movimiento) iterator.next();
			if(movimiento.getTipo().equals(TipoMovimiento.GASTO)) {
				gastos += movimiento.getCantidad();
			}
		}
		return gastos;
	}

	public static String enmascararNumeroTarjeta(Long numeroTarjeta) {
		String numeroTarjetaString ;			
		if(numeroTarjeta != null) {
			numeroTarjetaString = String.valueOf(numeroTarjeta);
		}else {
			numeroTarjetaString = "";
		}
		
		String tarjetaMascarade = numeroTarjetaString.replaceAll("\\d(?=\\d{4})", "*");
		
		for (int i = 0; 8 < tarjetaMascarade.length(); i++) {
			tarjetaMascarade = tarjetaMascarade.substring(1);
		}
		
		return tarjetaMascarade;
		
	}

}
