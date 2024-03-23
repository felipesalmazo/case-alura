package br.com.alura.techcase.core.service;

import br.com.alura.techcase.api.dto.user.CreateUserForm;
import br.com.alura.techcase.api.model.User;
import br.com.alura.techcase.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserByUserName(String username){
      return userRepository.findByUsername(username);
    }

    public User createUser(CreateUserForm form) {
        var user = new User(form);
        return userRepository.save(user);
    }
}
