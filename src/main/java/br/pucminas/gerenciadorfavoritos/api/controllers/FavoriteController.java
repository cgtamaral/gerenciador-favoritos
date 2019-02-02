package br.pucminas.gerenciadorfavoritos.api.controllers;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import br.pucminas.gerenciadorfavoritos.api.Response;
import br.pucminas.gerenciadorfavoritos.api.dtos.BookDTO;
import br.pucminas.gerenciadorfavoritos.api.dtos.FavoriteDTO;
import br.pucminas.gerenciadorfavoritos.api.dtos.SimpleFavoriteDTO;
import br.pucminas.gerenciadorfavoritos.api.entities.Book;
import br.pucminas.gerenciadorfavoritos.api.entities.Favorite;
import br.pucminas.gerenciadorfavoritos.api.entities.User;
import br.pucminas.gerenciadorfavoritos.api.services.BookService;
import br.pucminas.gerenciadorfavoritos.api.services.FavoriteService;
import br.pucminas.gerenciadorfavoritos.api.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/public")
@CrossOrigin(origins = "*")

@Api(value = "favorites", description = "Recurso para gerenciamento de favoritos", tags={ "favorites"})
public class FavoriteController {
	
	private static final Logger log = LoggerFactory.getLogger(FavoriteController.class);
	
	@Value("${gateway.search.books}")
	private String searchBooksURI;
	
	@Autowired
	FavoriteService favoriteService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	BookService	bookService;

    @ApiOperation(value = "Recupera todos os favoritos cadastrados", nickname = "findAllFavorites", notes = "", tags={ "favorites"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida!"),
    					   	@ApiResponse(code = 404, message = "Nenhum favorito foi encontrado na base de dados!") })
    @GetMapping(value ="/favorites", produces = "application/json")
	public ResponseEntity<Response<List<SimpleFavoriteDTO>>> findAllFavorites()
	{
		log.info("Buscando todos os autores da base!");
		Response<List<SimpleFavoriteDTO>> response = new Response<List<SimpleFavoriteDTO>>();
		List<Favorite> favorites = favoriteService.findAll();

		if (favorites.isEmpty()) {
			log.info("Nenhum favorito foi encontrado na base de dados!");
			response.getErrors().add("Nenhum favorito foi encontrado na base de dados!");
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		response.setData(Favorite.convertToDTOWithoutBooks(favorites));
		
		return ResponseEntity.ok(response);
	}
    
    @SuppressWarnings("static-access")
	@ApiOperation(value = "Consulta os livros de um favorito especifico", nickname = "findAllBooksByFavorite", notes = "", tags={ "favorites"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida!"),
		    @ApiResponse(code = 404, message = "O favorito informado não foi encontrado na base de dados!")})
	@GetMapping(value = "/favorites/{favoriteId}/books", produces = "application/json")
	public ResponseEntity<Response<FavoriteDTO>> findAllBooksByFavorite(@ApiParam(value = "Identificador do favorito a ter os livros pesquisados", required = true) @PathVariable("favoriteId") Long favoriteId)
	{
		log.info("Consultando livros do favorite: {}" + favoriteId);
		Response<FavoriteDTO> response = new Response<FavoriteDTO>();
		Optional<Favorite> favorite = favoriteService.findById(favoriteId);
		if (!favorite.isPresent()) {
			log.info("Nenhum favorito foi encontrado para o favoriteId {} " + favoriteId);
			response.getErrors().add("Nenhum favorito foi encontrado para o favoriteId {} " + favoriteId);
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if(favorite.get().getBooks().isEmpty())
		{
			log.info("Nenhum livro foi encontrado para o favoriteId {} " + favoriteId);
			response.getErrors().add("Nenhum livro foi encontrado para o favoriteId {} " + favoriteId);
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		response.setData(favorite.get().convertToDTOWithBooks(favorite.get()));
		
		return ResponseEntity.ok().body(response);
	}
    
	@ApiOperation(value = "Consulta detalhes de um livro especifico de um favorito", nickname = "findBookById", notes = "", tags={ "favorites"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida!"),
		    @ApiResponse(code = 404, message = "O favorito informado não foi encontrado na base de dados!"),
		    @ApiResponse(code = 500, message = "Erro ao consultar API de livros!")})
	@GetMapping(value = "/favorites/{favoriteId}/books/{bookId}", produces = "application/json")
	public ResponseEntity<Object> findBookById(@ApiParam(value = "Identificador do favorito", required = true) @PathVariable("favoriteId") Long favoriteId,
			@ApiParam(value = "Identificador do livro a ser pesquisado", required = true) @PathVariable("bookId") Long bookId)
	{
		log.info("Consultando livro : {} " + bookId + " para o favorito {} " + favoriteId);
		Response<BookDTO> response = new Response<BookDTO>();
		Optional<Favorite> favorite = favoriteService.findById(favoriteId);
		if (!favorite.isPresent()) {
			log.info("Nenhum favorito foi encontrado para o favoriteId {} " + favoriteId);
			response.getErrors().add("Nenhum favorito foi encontrado para o favoriteId {} " + favoriteId);
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if(favorite.get().getBooks().isEmpty())
		{
			log.info("Nenhum livro foi encontrado para o favoriteId {} " + favoriteId);
			response.getErrors().add("Nenhum livro foi encontrado para o favoriteId {} " + favoriteId);
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		else
		{
			List<Book> books = favorite.get().getBooks();
			Book retorno = null;
			for (Book book : books) {
				if(book.getId().equals(bookId))
				{
					retorno = book;
					break;
				}
			}
			
			if(retorno == null)
			{
				log.info("Nenhum livro encontrado para o bookId {} " + bookId);
				response.getErrors().add("Nenhum livro encontrado para o bookId {} " + bookId);
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			
			String params ="?isbn=" + retorno.getIsbn();

			log.info("Consultando livro por isbn: {}" + retorno.getIsbn());
				
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
	}
    
    @SuppressWarnings("static-access")
	@ApiOperation(value = "Insere um novo favorito", nickname = "insertFavorite", notes = "", tags={ "favorites"})
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Operação bem sucessida e um novo favorito foi incluido a base de dados!"),
		    @ApiResponse(code = 400, message = "O objeto de request possui informações inválidas para a inclusão do favorito!")})
	@PostMapping(value = "/favorites", produces = "application/json")
	public ResponseEntity<Response<FavoriteDTO>> insertFavorite(@ApiParam(value = "Objeto de favorito que a ser incluido na base de dados", required = true) @Valid @RequestBody FavoriteDTO favoriteDTO, 
			BindingResult result)
	{
		log.info("Incluindo novo favorito!");
		Response<FavoriteDTO> response = new Response<FavoriteDTO>();

		Favorite favorite = convertFavoriteDTOToEntity(null, favoriteDTO, result);
		if (result.hasErrors()) 
		{
			log.error("Erro ao validar favorito: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		favorite = favoriteService.insert(favorite);
		
		response.setData(favorite.convertToDTOWithBooks(favorite));
		
		return ResponseEntity.ok().body(response);
	}
	
    @SuppressWarnings("static-access")
	@ApiOperation(value = "Atualiza os dados de um favorito especifico", nickname = "updateFavorite", notes = "", tags={ "favorites"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida e o favorito foi atualizado com sucesso na base de dados!"),
		    @ApiResponse(code = 400, message = "O objeto de request possui informações inválidas para a atualização do favorito!"),
		    @ApiResponse(code = 404, message = "O favorito informado não foi encontrado na base de dados!")})
	@PutMapping(value = "/favorites/{favoriteId}", produces = "application/json")
	public ResponseEntity<Response<FavoriteDTO>> updateFavorite(@ApiParam(value = "Identificador do favorito a ser atualizado", required = true) @PathVariable("favoriteId") Long favoriteId,
			@ApiParam(value = "Objeto de favorito que precisa ser atualizado na base de dados", required = true) @Valid @RequestBody FavoriteDTO favoriteDTO,  BindingResult result)
	{
		log.info("Atualizando o favorite: {}" + favoriteId);
		Response<FavoriteDTO> response = new Response<FavoriteDTO>();
		Favorite favorite = convertFavoriteDTOToEntity(favoriteId, favoriteDTO, result);
		if (result.hasErrors()) 
		{
			log.error("Erro ao validar favorito: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		favorite = favoriteService.update(favorite);
		
		response.setData(favorite.convertToDTOWithBooks(favorite));
		
		return ResponseEntity.ok().body(response);
	}
	
    @ApiOperation(value = "Remove um favorito especifico", nickname = "deleteFavorite", notes = "", tags={ "favorites"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida e o favorito foi removido com sucesso na base de dados!")})
	@DeleteMapping(value = "/favorites/{favoriteId}", produces = "application/json")
	public ResponseEntity<Response<String>> deleteFavorite(@ApiParam(value = "Identificador do favorito a ser removido", required = true) @PathVariable("favoriteId") Long favoriteId)
	{
		log.info("Removendo o favorite: {}" + favoriteId);
		Response<String> response = new Response<String>();
		favoriteService.delete(favoriteId);
		
		return ResponseEntity.ok().body(response);
	}

	private Favorite convertFavoriteDTOToEntity(Long favoriteId, FavoriteDTO favoriteDTO, BindingResult result)
	{
		Favorite favorite = null;
		if(favoriteId!=null)
		{
			Optional<Favorite> favoriteOp = favoriteService.findById(favoriteId);
		
			if (!favoriteOp.isPresent()) {
				result.addError(new ObjectError("favoriteId", "Não foi encontrado nenhum favorido para o favoriteId informado."));
			}
			else
			{
				Optional<Favorite> favoriteOp2 = favoriteService.findByTitleAndUser(favoriteDTO.getTitle(),favoriteOp.get().getUser());
				if(favoriteOp2.isPresent() && !favoriteOp2.get().getId().equals(favoriteId))
				{
					result.addError(new ObjectError("title", "O usuário já possui um outro favorito cadastrado com o titulo informado."));	
				}
				favorite = favoriteOp.get();
			}
		}
		else
		{
			favorite = new Favorite();
		}
		if(favorite!=null)
		{
			favorite.setTitle(favoriteDTO.getTitle());
			favorite.setDescription(favoriteDTO.getDescription());
			if(favoriteId == null && favoriteDTO.getUserId()!=null)
			{
				Optional<User> user = userService.findById(favoriteDTO.getUserId());
				if (!user.isPresent()) {
					result.addError(new ObjectError("UserId", "Não foi encontrado nenhum Usuário para o userId informado."));
				}
				else
				{
					Optional<Favorite> favoriteOp = favoriteService.findByTitleAndUser(favoriteDTO.getTitle(),user.get());
					if(favoriteOp.isPresent())
					{
						result.addError(new ObjectError("title", "O usuário já possui um favorito com o titulo informado."));	
					}
					favorite.setUser(user.get());
				}
			}
			
			List<Book> books = new ArrayList<Book>();
			if(favoriteDTO.getBooksDTO()!=null && favoriteDTO.getBooksDTO().size() > 0)
			{
				for (BookDTO bookDTO : favoriteDTO.getBooksDTO()) 
				{
					if(bookDTO.getIsbn_10()==null)
					{
						if(result.getFieldError("favoriteId") == null) 
						{
							result.addError(new ObjectError("favoriteId", "Existem livros na lista de favoritos sem a informação de isbn!"));
						}
					}
					Optional<Book> book = bookService.findByIsbn(bookDTO.getIsbn_10());
	
					if (!book.isPresent()) 
					{
						//result.addError(new ObjectError(bookDTO.getIsbn(), "Não foi encontrado nenhum livro para o isbn {}" + bookDTO.getIsbn()));
						Book bookEntity = new Book();
						bookEntity.setTitle(bookDTO.getTitle());
						bookEntity.setIsbn(bookDTO.getIsbn_10());
						
						books.add(bookEntity);	
					}
					else
					{
						books.add(book.get());	
					}
				}
			}
			
			if(books!=null && books.size()>0)
			{
				favorite.setBooks(books);
			}
		}
		return favorite;
	}
}
