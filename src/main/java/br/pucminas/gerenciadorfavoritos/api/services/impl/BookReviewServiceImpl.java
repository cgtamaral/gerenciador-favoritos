package br.pucminas.gerenciadorfavoritos.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.pucminas.gerenciadorfavoritos.api.entities.BookReview;
import br.pucminas.gerenciadorfavoritos.api.repositories.BookReviewRepository;
import br.pucminas.gerenciadorfavoritos.api.services.BookReviewService;

@Service
public class BookReviewServiceImpl implements BookReviewService{

	private static final Logger log = LoggerFactory.getLogger(BookReviewServiceImpl.class);
	
	@Autowired
	BookReviewRepository bookReviewRepository;
	
	@Override
	public Optional<BookReview> findById(Long bookReviewId) {
		log.info("Buscando critica/ avaliação: {}" + bookReviewId);
		return bookReviewRepository.findById(bookReviewId);
	}
	
	@Override
	public BookReview insert(BookReview bookReview) {
		log.info("Inserindo uma nova critica/ avaliação");
		return bookReviewRepository.save(bookReview);
	}

	@Override
	public BookReview update(BookReview bookReview) {
		log.info("Atualizando o bookReview: {}"+ bookReview.getId());
		return bookReviewRepository.save(bookReview);
	}

	@Override
	public void delete(Long bookReviewId) {
		log.info("Revomendo o bookReview: {}"+ bookReviewId);
		bookReviewRepository.deleteById(bookReviewId);
		
	}



}
