package br.pucminas.gerenciadorfavoritos.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.gerenciadorfavoritos.api.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
