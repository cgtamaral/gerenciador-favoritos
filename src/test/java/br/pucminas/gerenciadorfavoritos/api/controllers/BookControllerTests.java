package br.pucminas.gerenciadorfavoritos.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.pucminas.gerenciadorfavoritos.api.dtos.SimpleBookReviewDTO;
import br.pucminas.gerenciadorfavoritos.api.entities.Book;
import br.pucminas.gerenciadorfavoritos.api.entities.BookReview;
import br.pucminas.gerenciadorfavoritos.api.entities.User;
import br.pucminas.gerenciadorfavoritos.api.enums.UserProfileEnum;
import br.pucminas.gerenciadorfavoritos.api.services.BookReviewService;
import br.pucminas.gerenciadorfavoritos.api.services.BookService;
import br.pucminas.gerenciadorfavoritos.api.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTests {
	
	
	
	private static final String ISBN = "0553897845";
	private static final Long USER_ID = 1L;
	private static final Long BOOK_ID = 1L;
	private static final Long BOOK_REVIEW_ID = 1L;

	
	private static final String BUSCAR_BOOK_URL = "/v1/public/books";
	private static final String REVIEWS_URL = "/bookReviews";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private BookService bookService;
	
	@MockBean
	private BookReviewService bookReviewService;
	
	@MockBean
	private UserService userService;
	
	
	@Test
	public void testFindAllBooksByFilter() throws Exception
	{
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_BOOK_URL + "?isbn=" + ISBN)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testFindBookReviewsByIsbn_10() throws Exception
	{
		Optional<Book> book = getFakeBook();
		
		BDDMockito.given(this.bookService.findByIsbn(ISBN)).willReturn(book);
		
		BDDMockito.given(this.bookReviewService.findById(BOOK_REVIEW_ID)).willReturn(this.getFakeBookReview(book.get()));
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_BOOK_URL + "/" + ISBN + REVIEWS_URL)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testInsertBookReview() throws Exception
	{
		Optional<Book> book = getFakeBook();
		
		BDDMockito.given(this.bookService.findByIsbn(ISBN)).willReturn(book);
		
		BDDMockito.given(this.bookReviewService.findById(BOOK_REVIEW_ID)).willReturn(Optional.of(book.get().getBookReviews().get(0)));
		
		BDDMockito.given(this.userService.findById(USER_ID)).willReturn(this.getFakeUser());
		
		BDDMockito.given(this.bookReviewService.insert(Mockito.any(BookReview.class))).willReturn(book.get().getBookReviews().get(0));
		 
		SimpleBookReviewDTO simpleBookReviewDTO = simpleBookFake();
		 
		mvc.perform(MockMvcRequestBuilders.post(BUSCAR_BOOK_URL + "/" + ISBN + REVIEWS_URL)
				.content(asJsonString(simpleBookReviewDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	

	@Test
	public void testUpdateBookReview() throws Exception
	{
		Optional<Book> book = getFakeBook();
		
		BDDMockito.given(this.bookService.findByIsbn(ISBN)).willReturn(book);
		
		BDDMockito.given(this.bookReviewService.findById(BOOK_REVIEW_ID)).willReturn(Optional.of(book.get().getBookReviews().get(0)));
		
		BDDMockito.given(this.userService.findById(USER_ID)).willReturn(this.getFakeUser());
		
		BDDMockito.given(this.bookReviewService.update(Mockito.any(BookReview.class))).willReturn(book.get().getBookReviews().get(0));
		 
		SimpleBookReviewDTO simpleBookReviewDTO = simpleBookFake();
		 
		mvc.perform(MockMvcRequestBuilders.put(BUSCAR_BOOK_URL + "/" + ISBN + REVIEWS_URL + "/" + BOOK_REVIEW_ID)
				.content(asJsonString(simpleBookReviewDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteBookReview() throws Exception
	{
		Optional<Book> book = getFakeBook();
		
		BDDMockito.given(this.bookService.findByIsbn(ISBN)).willReturn(book);
		
		mvc.perform(MockMvcRequestBuilders.delete(BUSCAR_BOOK_URL + "/" + ISBN + REVIEWS_URL + "/" + BOOK_REVIEW_ID)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	private static String asJsonString(Object obj) {
	    try 
	    {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    }
	    catch (Exception e) 
	    {
	        throw new RuntimeException(e);
	    }
	} 
	
	private SimpleBookReviewDTO simpleBookFake()
	{
		SimpleBookReviewDTO simpleBookReviewDTO = new SimpleBookReviewDTO();
		
		simpleBookReviewDTO.setUserId(USER_ID);
		simpleBookReviewDTO.setStarsNumber(4);
		simpleBookReviewDTO.setTitleComment("Primeira avaliação");
		simpleBookReviewDTO.setComment("Comentario primeira avaliação");
	
		return simpleBookReviewDTO;
		
	}
	
	private Optional<Book> getFakeBook() {

		Book book = new Book();
		book.setId(BOOK_ID);
		book.setTitle("A Game of Thrones");
		book.setIsbn(ISBN);
		book.setStarRating(5);
		
		List<BookReview> bookReviews = new ArrayList<BookReview>();
		bookReviews.add(getFakeBookReview(book).get());
		book.setBookReviews(bookReviews);
			
		return Optional.of(book);
	}
	

	private Optional<BookReview> getFakeBookReview(Book book) 
	{
		Optional<User>  user = this.getFakeUser();
		
		BookReview bookReview = new BookReview();
		bookReview.setId(BOOK_REVIEW_ID);
		bookReview.setBook(book);
		bookReview.setUser(user.get());
		bookReview.setStarsNumber(4);
		bookReview.setTitleComment("Teste");
		bookReview.setComment("Comentário Teste");
		bookReview.setCreationDate(Calendar.getInstance());
			
		return Optional.of(bookReview);
	}

	private Optional<User> getFakeUser() 
	{
		User user = new User();
		
		user.setId(USER_ID);
		user.setName("Usuario 01");
		user.setEmail("usuario_01@gmail.com");
		user.setPassword("1234");
		user.setUserProfile(UserProfileEnum.ROLE_USUARIO);
		user.setActive(true);
		user.setCreationDate(Calendar.getInstance());
		
		return Optional.of(user);
	}
}
