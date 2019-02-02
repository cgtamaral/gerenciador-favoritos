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

import br.pucminas.gerenciadorfavoritos.api.dtos.BookDTO;
import br.pucminas.gerenciadorfavoritos.api.dtos.FavoriteDTO;
import br.pucminas.gerenciadorfavoritos.api.entities.Book;
import br.pucminas.gerenciadorfavoritos.api.entities.BookReview;
import br.pucminas.gerenciadorfavoritos.api.entities.Favorite;
import br.pucminas.gerenciadorfavoritos.api.entities.User;
import br.pucminas.gerenciadorfavoritos.api.enums.UserProfileEnum;
import br.pucminas.gerenciadorfavoritos.api.services.BookService;
import br.pucminas.gerenciadorfavoritos.api.services.FavoriteService;
import br.pucminas.gerenciadorfavoritos.api.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FavoriteControllerTests {
	
	private static final String ISBN = "0553897845";
	private static final Long FAVORITE_ID = 1L;
	private static final Long USER_ID = 1L;
	private static final Long BOOK_ID = 1L;
	private static final Long BOOK_REVIEW_ID = 1L;
	
	private static final String BUSCAR_FAVORITO_URL = "/v1/public/favorites";
	
	private static final String BUSCAR_BOOKS_URL = "/books";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FavoriteService favoriteService;
	
	@MockBean
	private BookService bookService;
	
	@MockBean
	private UserService userService;
	
	@Test
	public void testFindAllFavorites() throws Exception {
		
		BDDMockito.given(this.favoriteService.findAll()).willReturn(this.getFakeFavorites());
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_FAVORITO_URL)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
	}
	
	@Test
	public void testFindAllBooksByFavorite() throws Exception
	{
		BDDMockito.given(this.favoriteService.findById(FAVORITE_ID)).willReturn(Optional.of(this.getFakeFavorites().get(0)));
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_FAVORITO_URL + "/" + FAVORITE_ID + BUSCAR_BOOKS_URL)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testFindBookById() throws Exception
	{
		BDDMockito.given(this.favoriteService.findById(FAVORITE_ID)).willReturn(Optional.of(this.getFakeFavorites().get(0)));
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_FAVORITO_URL + "/" + FAVORITE_ID + BUSCAR_BOOKS_URL + "/" + BOOK_ID)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testInsertFavorite() throws Exception
	{
		List<Favorite> favoritos = this.getFakeFavorites();
		BDDMockito.given(this.favoriteService.findById(FAVORITE_ID)).willReturn(Optional.of(favoritos.get(0)));
		BDDMockito.given(this.userService.findById(USER_ID)).willReturn(Optional.of(favoritos.get(0).getUser()));
		BDDMockito.given(this.bookService.findByIsbn(ISBN)).willReturn(Optional.of(favoritos.get(0).getBooks().get(0)));
		
		BDDMockito.given(this.favoriteService.insert(Mockito.any(Favorite.class))).willReturn(favoritos.get(0));
			
		FavoriteDTO favoriteDTO = favoriteDTOFake();
		mvc.perform(MockMvcRequestBuilders.post(BUSCAR_FAVORITO_URL)
				.content(asJsonString(favoriteDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	


	@Test
	public void testUpdateFavorite() throws Exception
	{
		List<Favorite> favoritos = this.getFakeFavorites();
		BDDMockito.given(this.favoriteService.findById(FAVORITE_ID)).willReturn(Optional.of(favoritos.get(0)));
		BDDMockito.given(this.userService.findById(USER_ID)).willReturn(Optional.of(favoritos.get(0).getUser()));
		BDDMockito.given(this.bookService.findByIsbn(ISBN)).willReturn(Optional.of(favoritos.get(0).getBooks().get(0)));
		
		BDDMockito.given(this.favoriteService.update(Mockito.any(Favorite.class))).willReturn(favoritos.get(0));
			
		FavoriteDTO favoriteDTO = favoriteDTOFake();
		mvc.perform(MockMvcRequestBuilders.put(BUSCAR_FAVORITO_URL + "/" + FAVORITE_ID)
				.content(asJsonString(favoriteDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteFavorite() throws Exception
	{
		mvc.perform(MockMvcRequestBuilders.delete(BUSCAR_FAVORITO_URL + "/" + FAVORITE_ID)
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
	
	private List<Favorite> getFakeFavorites() {
		
		List<Favorite> favorites = new ArrayList<Favorite>();
		Favorite favorite = new Favorite();
		favorite.setId(FAVORITE_ID);
		favorite.setUser(this.getFakeUser().get());
		favorite.setTitle("Favorito Fake");
		favorite.setDescription("Descrição Favorito Fake");
		
		List<Book> books = new ArrayList<>();
		books.add(this.getFakeBook().get());
		favorite.setBooks(books);
		
		favorites.add(favorite);
		
		return favorites;
		
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
	
	private FavoriteDTO favoriteDTOFake() {
		
		FavoriteDTO favoriteDTO = new FavoriteDTO();
		
		favoriteDTO.setUserId(USER_ID);
		favoriteDTO.setTitle("Favorito Fake");
		favoriteDTO.setDescription("Descrição Favorito Fake");

		List<BookDTO> books = new ArrayList<BookDTO>();
	
		favoriteDTO.setBooksDTO(books);
		
		return favoriteDTO;
	}
}
