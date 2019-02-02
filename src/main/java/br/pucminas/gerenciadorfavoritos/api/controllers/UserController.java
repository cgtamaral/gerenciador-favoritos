package br.pucminas.gerenciadorfavoritos.api.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucminas.gerenciadorfavoritos.api.Response;
import br.pucminas.gerenciadorfavoritos.api.dtos.UserDTO;
import br.pucminas.gerenciadorfavoritos.api.entities.User;
import br.pucminas.gerenciadorfavoritos.api.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/public")
@CrossOrigin(origins = "*")

@Api(value = "users", description = "Recurso para consulta de usuários existentes.", tags={ "users"})
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "Consulta dos os usuários existentes.", nickname = "findAllUsers", notes = "", tags={ "users"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida!"),
		    @ApiResponse(code = 404, message = "Não foi encontrado nenhum usuário")})
	@GetMapping(value = "/users", produces = "application/json")
	public ResponseEntity<Response<List<UserDTO>>> findAllUsers()
	{
		Response<List<UserDTO>> response = new Response<List<UserDTO>>();
		
		log.info("Consultando usuários existentes na base de dados!");
		List<User> users = userService.findAll();

		if (users.isEmpty()) {
			log.info("Nenhum usuário foi encontrado na base de dados!");
			response.getErrors().add("Nenhum usuário foi encontrado na base de dados!");
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		response.setData(User.convertToDTO(users));
		
		return ResponseEntity.ok(response);
	}
}
