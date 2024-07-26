package dio.apirest.spring_boot_ao_deploy.service;

import dio.apirest.spring_boot_ao_deploy.model.User;

public interface UserService {
    User findById(Long id);

    User create (User userToCreate);
}
