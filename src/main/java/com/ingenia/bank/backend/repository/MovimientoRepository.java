package com.ingenia.bank.backend.repository;

import com.ingenia.bank.backend.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
	
	@Query("SELECT m FROM Movimiento m WHERE m.tarjeta.id= :idTarjeta")
	List<Movimiento> obtenerMovimientosDeTarjeta(@Param("idTarjeta") Long idTarjeta);
	
	@Query("SELECT m FROM Movimiento m WHERE m.cuenta.id= :idCuenta")
	List<Movimiento> obtenerMovimientosDeCuenta(@Param("idCuenta") Long idcuenta);

	@Query("select m from Movimiento m where m.tarjeta.id = :idTarjeta AND m.fecha BETWEEN :fechaInit AND :fechaFin ")
	List<Movimiento> obtenerMovimientosDeTarjetaFechas(@Param("idTarjeta")Long idTarjeta, @Param("fechaInit")Date fechaInit, @Param("fechaFin")Date fechaFin);
	
	@Query("select m from Movimiento m where m.categoria.id= :idCategoria AND m.cuenta.id = :idCuenta AND m.fecha BETWEEN :fechaInit AND :fechaFin ")
	List<Movimiento> obtenerMovimientosDeCuentaByFechaAndCategoria(@Param("idCuenta")Long idCuenta,@Param("idCategoria")Long idCategoria ,@Param("fechaInit")Date fechaInit, @Param("fechaFin")Date fechaFin);
	
	@Query("select m from Movimiento m where m.categoria.id= :idCategoria AND m.tarjeta.id = :idTarjeta AND m.fecha BETWEEN :fechaInit AND :fechaFin ")
	List<Movimiento> obtenerMovimientosDeTarjetaByFechaAndCategoria(@Param("idTarjeta")Long idTarjeta,@Param("idCategoria")Long idCategoria ,@Param("fechaInit")Date fechaInit, @Param("fechaFin")Date fechaFin);
	
	@Query("select m from Movimiento m where m.categoria.id= :idCategoria AND m.cuenta.id  in (select c.id from Cuenta c join c.usuarios u where u.id = :idUsuario) and m.fecha BETWEEN :fechaInit AND :fechaFin")
	List<Movimiento> obtenerMovimientosDeUsuarioByFechaAndCategoria(@Param("idUsuario")Long idUsuario,@Param("idCategoria") Long idCategoria, @Param("fechaInit")Date fechaInit, @Param("fechaFin")Date fechaFin);

	@Query("select m from Movimiento m where m.cuenta.id = :idCuenta AND m.fecha BETWEEN :fechaInit AND :fechaFin ORDER BY m.fecha")
	List<Movimiento> obtenerMovimientosDeCuentaFechas(@Param("idCuenta")Long idCuenta, @Param("fechaInit") Date dateInit,@Param("fechaFin") Date datefin);

	@Query("SELECT m FROM Movimiento m WHERE m.cuenta.id= :idCuenta ORDER BY m.fecha DESC")
	List<Movimiento> obtenerMovimientosDeCuentaOrdenadosCuenta(Long idCuenta);
	
}
