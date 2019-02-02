package br.pucminas.gerenciadorfavoritos.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.gerenciadorfavoritos.api.entities.Favorite;
import br.pucminas.gerenciadorfavoritos.api.entities.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>{

	Optional<Favorite> findByTitleAndUser(String title, User user);

}
