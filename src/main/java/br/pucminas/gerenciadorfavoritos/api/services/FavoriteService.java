package br.pucminas.gerenciadorfavoritos.api.services;

import java.util.List;
import java.util.Optional;

import br.pucminas.gerenciadorfavoritos.api.entities.Favorite;
import br.pucminas.gerenciadorfavoritos.api.entities.User;


public interface FavoriteService {
	
	/**
	 * Retorna uma listagem de todos os favoritos existentes na base de dados.
	 * 
	 * @return List<Favorite>
	 */
	List<Favorite> findAll();

	/**
	 * Retorna as informações de um favorito conforme identificador passado como parâmetro.
	 * 
	 * @return Optional<Favorite>
	 */
	Optional<Favorite> findById(Long favoriteId);
	
	/**
	 * Retorna as informações de um favorito conforme titulo e usuario passado como parâmetro.
	 * 
	 * @return Optional<Favorite>
	 */
	Optional<Favorite> findByTitleAndUser(String title, User user);
	
	/**
	 * Insere um novo registro de favorite.
	 * 
	 * @return Favorite
	 */
	Favorite insert(Favorite favorite);
	
	/**
	 * Atualiza um registro de favorite.
	 * 
	 * @return Favorite
	 */
	Favorite update(Favorite favorite);
	
	/**
	 * Remove da base de dados um registro de favorite conforme identificador passado via parâmetro.
	 * 
	 * @return Favorite
	 */
	void delete(Long favoriteId);


}
