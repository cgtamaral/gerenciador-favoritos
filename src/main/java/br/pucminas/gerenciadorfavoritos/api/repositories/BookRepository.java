package br.pucminas.gerenciadorfavoritos.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.gerenciadorfavoritos.api.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

	Optional<Book> findByIsbn(String isbn);

}
