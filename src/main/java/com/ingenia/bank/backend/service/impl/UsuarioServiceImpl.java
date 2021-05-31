package com.ingenia.bank.backend.service.impl;

import com.ingenia.bank.backend.model.Usuario;
import com.ingenia.bank.backend.repository.UsuarioRepository;
import com.ingenia.bank.backend.service.UsuarioService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    UsuarioRepository usuarioRepositorio;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public Optional<Usuario> obtenerUsuarioById(Long id) {
        return usuarioRepositorio.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioByUsername(String username) {
        return usuarioRepositorio.findByUsername(username);
    }

    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario) {
        // miramos si ya existe un usuario con el 'username' indicado
        if(usuarioRepositorio.existsByUsername(usuario.getUsername())){
            return Optional.empty();
        }else{
            Usuario usuarioCreado = usuarioRepositorio.save(usuario);
            return Optional.of(usuarioCreado);
        }
    }

    @Override
    public Optional<Usuario> obtenerUsuarioActualConectado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = ((UserDetails)principal);
        Optional<Usuario> usuarioActual = obtenerUsuarioByUsername(userDetails.getUsername());
        return usuarioActual;
    }
}
