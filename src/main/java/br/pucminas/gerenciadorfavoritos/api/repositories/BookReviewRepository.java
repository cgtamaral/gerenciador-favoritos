package br.pucminas.gerenciadorfavoritos.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.gerenciadorfavoritos.api.entities.BookReview;

public interface BookReviewRepository extends JpaRepository<BookReview, Long>{

}
