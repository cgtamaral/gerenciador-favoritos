package br.pucminas.gerenciadorfavoritos.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.pucminas.gerenciadorfavoritos.api.entities.Book;
import br.pucminas.gerenciadorfavoritos.api.repositories.BookRepository;
import br.pucminas.gerenciadorfavoritos.api.services.BookService;

@Service
public class BookServiceImpl implements BookService{
	
private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
	
	@Autowired
	BookRepository bookRepository;
	
	@Override
	public List<Book> findAll() {
		log.info("Buscando todos os livros da base! {}");
		return bookRepository.findAll();
	}

	@Override
	public Optional<Book> findByIsbn(String isbn) {
		log.info("Buscando livro pelo isbn: {}" + isbn);
		return bookRepository.findByIsbn(isbn);
	}
	
	@Override
	public Book insert(Book book) {
		log.info("Inserido o book: {}"+ book.getTitle());
		return bookRepository.save(book);
	}
	
	@Override
	public Book update(Book book) {
		log.info("Atualizando o book: {}"+ book.getId());
		return bookRepository.save(book);
	}
}
