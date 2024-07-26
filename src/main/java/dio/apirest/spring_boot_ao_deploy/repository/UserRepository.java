package dio.apirest.spring_boot_ao_deploy.repository;

import dio.apirest.spring_boot_ao_deploy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
