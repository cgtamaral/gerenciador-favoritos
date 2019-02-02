package br.pucminas.gerenciadorfavoritos.api.services;

import java.util.List;
import java.util.Optional;

import br.pucminas.gerenciadorfavoritos.api.entities.User;

public interface UserService {
	
	/**
	 * Retorna uma listagem de todos os usuários existentes na base de dados.
	 * 
	 * @return List<Favorite>
	 */
	List<User> findAll();

	/**
	 * Retorna as informações de um usuário conforme identificador passado como parâmetro.
	 * 
	 * @return Optional<Favorite>
	 */
	Optional<User> findById(Long userId);
}
