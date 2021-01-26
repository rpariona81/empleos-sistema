package net.itinajero.service;

import java.util.List;

import net.itinajero.model.Usuario;

public interface IUsuarioService {

	void guardar(Usuario usuario);
	
	void eliminar(Integer idUsuario);
	
	List<Usuario> buscarTodos();
	
	Usuario buscarPorUsername(String username);
}
