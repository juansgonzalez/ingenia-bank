package com.ingenia.bank.backend.repository;

import com.ingenia.bank.backend.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

	boolean existsByIban(String iban);

	@Query("select c from Cuenta c join c.usuarios u where u.id = :idUsuario")
	List<Cuenta> obtenerCuentasByUserId(@Param("idUsuario")Long idUsuario);


}
