package br.pucminas.gerenciadorfavoritos.api.services;

import java.util.Optional;

import br.pucminas.gerenciadorfavoritos.api.entities.BookReview;

public interface BookReviewService 
{

	/**
	 * Retorna as informações de uma critica de um livro conforme identificador passado como parâmetro.
	 * 
	 * @return Optional<BookReview>
	 */
	Optional<BookReview> findById(Long bookReviewId);
	
	/**
	 * Insere um novo registro de bookReview.
	 * 
	 * @return BookReview
	 */
	BookReview insert(BookReview bookReview);
	
	/**
	 * Atualiza um registro de bookReview.
	 * 
	 * @return BookReview
	 */
	BookReview update(BookReview bookReview);
	
	/**
	 * Remove da base de dados um registro de bookReview conforme identificador passado via parâmetro.
	 *
	 */
	void delete(Long bookReviewId);
}
