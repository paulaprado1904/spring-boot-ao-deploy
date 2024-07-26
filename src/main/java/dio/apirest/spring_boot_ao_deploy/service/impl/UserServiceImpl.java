package dio.apirest.spring_boot_ao_deploy.service.impl;

import dio.apirest.spring_boot_ao_deploy.model.User;
import dio.apirest.spring_boot_ao_deploy.repository.UserRepository;
import dio.apirest.spring_boot_ao_deploy.service.UserService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User create(User userToCreate) {
        if(userRepository.existsByAccountNumber(userToCreate.getAccount().getNumber())){
            throw new IllegalArgumentException("This Account ID already exists.");
        }
        return userRepository.save(userToCreate);
    }
}
