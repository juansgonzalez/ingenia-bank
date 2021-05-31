package com.ingenia.bank.backend.service.impl;

import com.ingenia.bank.backend.model.Cuenta;
import com.ingenia.bank.backend.repository.CuentaRepository;
import com.ingenia.bank.backend.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

	@Autowired
	CuentaRepository cuentaRepository;
	
	
	@Override
	public Cuenta crearCuenta(Cuenta cuenta) {
		 if(cuentaRepository.existsByIban(cuenta.getIban())){
			 return cuenta;
		 }
		return cuentaRepository.save(cuenta);
	}

	@Override
	public Cuenta obtenerCuentaById(Long idCuenta) {
		return cuentaRepository.findById(idCuenta).orElseThrow(()
				-> new EntityNotFoundException("No se ha encontrado categoria con id: "+idCuenta));
	}

	@Override
	public List<Cuenta> obtenerTodasCuentasByUsuarioId(Long idUsuario) {
		return cuentaRepository.obtenerCuentasByUserId(idUsuario);
	}

}
