package br.pucminas.gerenciadorfavoritos.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.pucminas.gerenciadorfavoritos.api.entities.Favorite;
import br.pucminas.gerenciadorfavoritos.api.entities.User;
import br.pucminas.gerenciadorfavoritos.api.repositories.FavoriteRepository;
import br.pucminas.gerenciadorfavoritos.api.services.FavoriteService;


@Service
public class FavoriteServiceImpl  implements FavoriteService{

	private static final Logger log = LoggerFactory.getLogger(FavoriteServiceImpl.class);
	
	@Autowired
	FavoriteRepository favoriteRepository;
	
	@Override
	public List<Favorite> findAll() {
		log.info("Buscando todos os favoritos da base! {}");
		return favoriteRepository.findAll();
	}

	@Override
	public Optional<Favorite> findById(Long favoriteId) {
		log.info("Buscando favorito: {}" + favoriteId);
		return favoriteRepository.findById(favoriteId);
	}

	@Override
	public Optional<Favorite> findByTitleAndUser(String title, User user) {
		log.info("Buscando favorito: {}" + title);
		return favoriteRepository.findByTitleAndUser(title, user);
	}
	
	@Override
	public Favorite insert(Favorite favorite) {
		
		log.info("Inserindo novo favorito");
		return favoriteRepository.save(favorite);
	}
	
	@Override
	public Favorite update(Favorite favorite) {
		
		log.info("Atualizando o favorito: {}"+ favorite.getId());
		return favoriteRepository.save(favorite);
	}

	@Override
	public void delete(Long favoriteId) {
		log.info("Revomendo o favorito: {}"+ favoriteId);
		favoriteRepository.deleteById(favoriteId);
	}

}
