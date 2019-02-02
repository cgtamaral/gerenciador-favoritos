package br.pucminas.gerenciadorfavoritos.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.pucminas.gerenciadorfavoritos.api.entities.User;
import br.pucminas.gerenciadorfavoritos.api.repositories.UserRepository;
import br.pucminas.gerenciadorfavoritos.api.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public List<User> findAll() {
		log.info("Buscando todos os usuários da base! {}");
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findById(Long userId) {
		log.info("Buscando usuário: {}" + userId);
		return userRepository.findById(userId);
	}
}
