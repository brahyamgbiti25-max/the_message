package by.Brahyam.Garcia.Blas.the.message.Dao;

import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String verificationToken);

    Optional<User> findByPasswordResetToken(String tokenResetPassword);

//methods to use in the userController

    //List<User> findByFullNameContainingIgnoreCase(String query);
}
