package br.pucminas.gerenciadorfavoritos.api.services;

import java.util.List;
import java.util.Optional;

import br.pucminas.gerenciadorfavoritos.api.entities.Book;

public interface BookService {
	
	/**
	 * Retorna uma listagem de todos os livros existentes na base de dados.
	 * 
	 * @return List<Favorite>
	 */
	List<Book> findAll();

	/**
	 * Retorna as informações de um livro conforme isbn passado como parâmetro.
	 * 
	 * @return Optional<Favorite>
	 */
	Optional<Book> findByIsbn(String isbn);
	
	/**
	 * Insere um novo registro de Book.
	 * 
	 * @return Book
	 */
	Book insert(Book book);
	
	/**
	 * Atualiza um registro de book.
	 * 
	 * @return Book
	 */
	Book update(Book book);
}
