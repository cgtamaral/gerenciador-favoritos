package br.pucminas.gerenciadorfavoritos.api.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import br.pucminas.gerenciadorfavoritos.api.Response;
import br.pucminas.gerenciadorfavoritos.api.dtos.BookDTO;
import br.pucminas.gerenciadorfavoritos.api.dtos.BookReviewDTO;
import br.pucminas.gerenciadorfavoritos.api.dtos.QuotationDTO;
import br.pucminas.gerenciadorfavoritos.api.dtos.SimpleBookReviewDTO;
import br.pucminas.gerenciadorfavoritos.api.entities.Book;
import br.pucminas.gerenciadorfavoritos.api.entities.BookReview;
import br.pucminas.gerenciadorfavoritos.api.entities.User;
import br.pucminas.gerenciadorfavoritos.api.services.BookReviewService;
import br.pucminas.gerenciadorfavoritos.api.services.BookService;
import br.pucminas.gerenciadorfavoritos.api.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/public")
@CrossOrigin(origins = "*")

@Api(value = "books", description = "Recurso para gerenciamento de criticas e reputação de livros realizadas por usuários da plataforma.", tags={ "books"})
public class BookController {
	
	private static final Logger log = LoggerFactory.getLogger(BookController.class);
	
	@Value("${gateway.search.books}")
	private String searchBooksURI;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookReviewService bookReviewService;
	
	@ApiOperation(value = "Consulta os livros existentes por titulo e isbn", nickname = "findAllBooksByFilter", notes = "", tags={ "books"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida!"),
		    @ApiResponse(code = 404, message = "Não foi encontrado nenhum livro para os parâmetros informados!"),
		    @ApiResponse(code = 400, message = "Ocorreu um erro ao pesquisar livros com os parâmetros informados!"),
		    @ApiResponse(code = 500, message = "Erro ao consultar API de livros!")})
	@GetMapping(value = "/books", produces = "application/json")
	public ResponseEntity<Object> findAllBooksByFilter(@ApiParam(value = "Titulo de um livro a ser pesquisado. Quando o parâmetro isbn for informado o titulo será desconsiderado na pesquisa.", required = false)  @RequestParam(value = "title", required= false) String title,
			@ApiParam(value = "isbn de um livro a ser pesquisado.", required = false)  @RequestParam(value = "isbn", required= false) String isbn)
	{
		Response<List<Object>> response = new Response<List<Object>>();
		String params ="?";
		if(isbn !=null && !isbn.isEmpty())
		{
			log.info("Consultando livro por isbn: {}" + isbn);
			params += "isbn=" + isbn;
		}
		else if(title != null && !title.isEmpty())
		{
			log.info("Consultando livros por title: {}" + title);
			params += params.length()>1 ? "&title=" + title : "title=" + title;
		}
		else
		{
			log.info("Deve ser informado pelo menos um dos parâmetros de pesquisa!");
			response.getErrors().add("Deve ser informado pelo menos um dos parâmetros de pesquisa!");
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    headers.add("Accept", "*/*");
	    
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		ResponseEntity<Object> responseEntity = null;
		try
		{
			responseEntity = rest.exchange(searchBooksURI+params, HttpMethod.GET, requestEntity,Object.class);
			
			return responseEntity;
		}
		catch (Exception e) {
			
			log.info(e.getMessage());
			response.getErrors().add(e.getMessage());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	@ApiOperation(value = "Consulta as criticas/ avaliações feitas por usuários para um livro", nickname = "findBookReviewsByIsbn_10", notes = "", tags={ "books"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida!"),
		    @ApiResponse(code = 404, message = "Não foi encontrado nenhum livro para o isbn informado!"),
		    @ApiResponse(code = 400, message = "Ocorreu um erro ao pesquisar criticas/ avaliações com os parâmetros informados!")})
	@GetMapping(value = "/books/{isbn}/bookReviews", produces = "application/json")
	public ResponseEntity<Response<List<BookReviewDTO>>> findBookReviewsByIsbn_10(@ApiParam(value = "isbn do livro a ser pesquisado.", required = true)  @PathVariable("isbn") String isbn)
	{
		Response<List<BookReviewDTO>> response = new Response<List<BookReviewDTO>>();
		if(isbn !=null && !isbn.isEmpty())
		{
			log.info("Consultando livro por isbn: {}" + isbn);
		}
		else
		{
			log.info("O parâmetro isbn deve ser informado!");
			response.getErrors().add("O parâmetro isbn deve ser informado!");
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		Optional<Book> book = bookService.findByIsbn(isbn);
		
		if (!book.isPresent() || (book.get().getBookReviews()==null || book.get().getBookReviews().size() == 0))
		{
			log.info("O livro informado não possui nenhuma critica/ avaliação realizada por usuários!");
			response.getErrors().add("O livro informado não possui nenhuma critica/ avaliação realizada por usuários!");
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		response.setData(new BookReview().convertToDTO(book.get().getBookReviews()));

		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation(value = "Permite a um usuário incluir uma nova critica/ avaliação para um livro", nickname = "insertBookReview", notes = "", tags={ "books"})
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Operação bem sucessida e a critica/ avaliação foi incluida para o livro!"),
		    @ApiResponse(code = 400, message = "O objeto de request possui informações inválidas para a inclusão da critica/ avaliação!")})
	@PostMapping(value = "/books/{isbn}/bookReviews", produces = "application/json")
	public ResponseEntity<Response<BookReviewDTO>> insertBookReview(@ApiParam(value = "isbn do livro para associação da critica/ avaliação.", required = true)  @PathVariable("isbn") String isbn,
			@ApiParam(value = "Objeto com informações da critica/ avaliação ao livro que será incluida na base dados.", required = true) @Valid @RequestBody SimpleBookReviewDTO simpleBookReviewDTO, 
			BindingResult result)
	{
		Response<BookReviewDTO> response = new Response<BookReviewDTO>();
		if(isbn==null || isbn.isEmpty())
		{
			log.info("O parâmetro isbn deve ser informado!");
			response.getErrors().add("O parâmetro isbn deve ser informado!");
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		log.info("Incluindo nova critica/ avaliação para o livro com isbn {} " + isbn);


		BookReview bookReview = convertBookReviewDTOToEntity(isbn, null, simpleBookReviewDTO, result);
		if (result.hasErrors()) 
		{
			log.error("Erro ao validar critica/ avaliação: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		bookReview = bookReviewService.insert(bookReview);
		Book book = bookReview.getBook();
		List<BookReview> bookReviews = book.getBookReviews();
		bookReviews.add(bookReview);	
		book.setBookReviews(bookReviews);
		insertBookReviewRating(book);
		
		response.setData(bookReview.convertToDTO(bookReview));
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation(value = "Permite a um usuário alterar uma critica/ avaliação para um livro", nickname = "updateBookReview", notes = "", tags={ "books"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida e a critica/ avaliação foi atualiada para o livro!"),
		    @ApiResponse(code = 400, message = "O objeto de request possui informações inválidas para a inclusão da critica/ avaliação!"),
		    @ApiResponse(code = 404, message = "O livro ou critica / avaliação informada não foi encontrado na base de dados!")})
	@PutMapping(value = "/books/{isbn}/bookReviews/{bookReviewId}", produces = "application/json")
	public ResponseEntity<Response<BookReviewDTO>> updateBookReview(@ApiParam(value = "isbn do livro associado a critica/ avaliação.", required = true) @PathVariable("isbn") String isbn,
			@ApiParam(value = "Identificador da critica / avaliação a ser atualizada", required = true) @PathVariable("bookReviewId") Long bookReviewId,
			@ApiParam(value = "Objeto com informações da critica/ avaliação ao livro que será atualizada na base dados.", required = true) @Valid @RequestBody SimpleBookReviewDTO simpleBookReviewDTO,  BindingResult result)
	{
    	Response<BookReviewDTO> response = new Response<BookReviewDTO>();
		log.info("Atualizando a critica / avaliação: {}" + bookReviewId);
		BookReview bookReview = convertBookReviewDTOToEntity(isbn, bookReviewId, simpleBookReviewDTO, result);
		if (result.hasErrors()) 
		{
			log.error("Erro ao validar favorito: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		bookReview = bookReviewService.update(bookReview);
		Book book = bookReview.getBook();
		insertBookReviewRating(book);
		
		response.setData(bookReview.convertToDTO(bookReview));
		
		return ResponseEntity.ok().body(response);
	}
	
    @ApiOperation(value = "Remove uma critica /avaliação de um livro especifico", nickname = "deleteBookReview", notes = "", tags={ "books"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida e a critica / avaliação foi removida com sucesso na base de dados!")})
	@DeleteMapping(value = "/books/{isbn}/bookReviews/{bookReviewId}", produces = "application/json")
	public ResponseEntity<Response<String>> deleteBookReview(@ApiParam(value = "isbn do livro associado a critica/ avaliação.", required = true) @PathVariable("isbn") String isbn,
			@ApiParam(value = "Identificador da critica / avaliação a ser removida", required = true) @PathVariable("bookReviewId") Long bookReviewId)
	{
		log.info("Removendo o critica / avaliação: {}" + bookReviewId);
		Response<String> response = new Response<String>();
		bookReviewService.delete(bookReviewId);
		
		updateBookReviewRating(isbn);
		
		return ResponseEntity.ok().body(response);
	}
	
	private void updateBookReviewRating(String isbn) 
	{
		Optional<Book> bookOp = bookService.findByIsbn(isbn);
		
		if (bookOp.isPresent())
		{
			Book book = bookOp.get();
			Integer numberOfReviews = book.getBookReviews() == null ? 0 :book.getBookReviews().size();
			Integer starRating = 0;
			if(numberOfReviews!=0)
			{
				starRating = book.countTotalStar() / numberOfReviews;			
			}
			book.setStarRating(starRating);
			
			bookService.update(book);
		}
	}
	
	private void insertBookReviewRating(Book book)
	{
		Integer numberOfReviews = book.getBookReviews() == null ? 0 :book.getBookReviews().size();
		Integer starRating = 0;
		if(numberOfReviews!=0)
		{
			starRating = book.countTotalStar() / numberOfReviews;			
		}
		book.setStarRating(starRating);
		bookService.update(book);
	}

	@SuppressWarnings("unchecked")
	private BookReview convertBookReviewDTOToEntity(String isbn, Long bookReviewId, SimpleBookReviewDTO simpleBookReviewDTO, BindingResult result)
	{
		Optional<Book> bookOp = bookService.findByIsbn(isbn);
		
		BookReview bookReview = null;
		Book book = null;
		if(!bookOp.isPresent())
		{
			ResponseEntity<Object> response = findAllBooksByFilter(null, isbn);	
			if(response.getStatusCode().equals(HttpStatus.OK))
			{
				List<BookDTO> books = convertResponseToBookDTO((List<Map<String,Object>>)response.getBody());
				if(books!=null && books.size() > 0)
				{
					BookDTO bookDTO = books.get(0);
					book = new Book();
					book.setTitle(bookDTO.getTitle());
					book.setIsbn(bookDTO.getIsbn_10());
					book.setStarRating(0);
					book = bookService.insert(book);
				}
			}
		}
		else
		{
			book = bookOp.get();
		}
		
		bookReview = new BookReview();
		if(bookReviewId!=null)
		{
			Optional<BookReview> bookReviewOp = bookReviewService.findById(bookReviewId);
			if(!bookReviewOp.isPresent())
			{
				result.addError(new ObjectError("bookReviewId", "A critica / avaliação informada não existe."));
			}
			else
			{
				bookReview = bookReviewOp.get();
			}
		}


		

		bookReview.setBook(book);

		
		Optional<User> user = userService.findById(simpleBookReviewDTO.getUserId());
		if(!user.isPresent())
		{
			result.addError(new ObjectError("userId", "O usuário informado não existe."));	
		}
		else if(bookReviewId==null)
		{
			bookReview.setUser(user.get());			
		}
		
		bookReview.setStarsNumber(simpleBookReviewDTO.getStarsNumber());
		bookReview.setTitleComment(simpleBookReviewDTO.getTitleComment());
		bookReview.setComment(simpleBookReviewDTO.getComment());
		bookReview.setCreationDate(Calendar.getInstance());
		
		return bookReview;
	}
	
	    @SuppressWarnings("unchecked")
		public List<BookDTO> convertResponseToBookDTO(List<Map<String,Object>> props)
	    {
	    	List<BookDTO> retorno = new ArrayList<BookDTO>();
	    	for (Map<String, Object> prop : props)
	    	{
	    		BookDTO bookDTO = new BookDTO();
		  		bookDTO.setTitle((String)prop.get("title"));
		  		bookDTO.setSubTitle((String)prop.get("subTitle"));
		  		bookDTO.setAuthors((String)prop.get("authors"));
		  		bookDTO.setDescription((String)prop.get("description"));
		  		bookDTO.setLanguage((String)prop.get("language"));
		  		bookDTO.setPageCount((Integer)prop.get("pageCount"));
		  		bookDTO.setPublishedDate((String)prop.get("publishedDate"));
		  		bookDTO.setPublisher((String)prop.get("publisher"));
		  		bookDTO.setIsbn_10((String)prop.get("isbn_10"));
		  		bookDTO.setIsbn_13((String)prop.get("isbn_13"));
		  		bookDTO.setWebRating((String)prop.get("webRating"));
		  		bookDTO.setWebNumberAvaliation((Integer)prop.get("webNumberAvaliation"));
		  		
		  		QuotationDTO quotationDTO = new QuotationDTO();
		  		Map<String, Object> quotationProp = (Map<String, Object>)prop.get("quotation");
		  		if(quotationProp!=null && quotationProp.size() > 0)
		  		{
		  			quotationDTO.setMarketPlacePrice((String)prop.get("marketPlacePrice"));
		  			quotationDTO.setGoogleEBookPrice((String)prop.get("googleEBookPrice"));
		  			quotationDTO.setYouSafeInGoogleEBook((String)prop.get("youSafeInGoogleEBook"));		  			
		  		}
		  	    bookDTO.setQuotation(quotationDTO);
		  		bookDTO.setPreviewLink((String)prop.get("previewLink"));
	    		
	    		retorno.add(bookDTO);
			}
	    	
	    	return retorno;
	    } 
}
