package by.Brahyam.Garcia.Blas.the.message.Repositories;

import by.Brahyam.Garcia.Blas.the.message.Dao.UserRepository;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Enums.Role;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.EmailNotFoundException;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.TokenExpiredException;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.TokenVericationPasswordNotFoundExeption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.Instant;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Test
    public void exitsByEmailTest(){
        String email="brahyam@";
        User mockUser=new User();
        mockUser.setEmail("vkccfkvnf");
        mockUser.setFullName("brahyam");
        mockUser.setPassword("brahyam12345");
        mockUser.setRole(Role.USER);
        mockUser.setEmailVerified(true);
        entityManager.persistAndFlush(mockUser);
        boolean result=userRepository.existsByEmail(email);
        org.assertj.core.api.Assertions.assertThat(result).isFalse();
    }
    @Test
    public void findByEmailTest(){
        String email="brahyam@";
        User mockUser=new User();
        mockUser.setEmail(email);
        mockUser.setFullName("brahyam");
        mockUser.setPassword("brahyam12345");
        mockUser.setRole(Role.USER);
        mockUser.setEmailVerified(true);
        entityManager.persistAndFlush(mockUser);
        User result=userRepository.findByEmail(email).orElseThrow(()->new EmailNotFoundException("email bad"));
        org.assertj.core.api.Assertions.assertThat(result.getEmail()).isNotEmpty();

    }
    @Test
    public void findBYVerificationToken(){
        String token="njkvsddfbjvfjvbfj";
        User mockUser=new User();
        mockUser.setEmail("brahyam.t");
        mockUser.setFullName("brahyam");
        mockUser.setPassword("brahyam12345");
        mockUser.setRole(Role.USER);
        mockUser.setEmailVerified(true);
        mockUser.setVerificationToken(token);
        mockUser.setVerificationTokenExpiry(Instant.now().plusSeconds(3000));
        entityManager.persistAndFlush(mockUser);
        User result=userRepository.findByVerificationToken(token).orElseThrow(()->new RuntimeException("the token wasn't found"));
        org.assertj.core.api.Assertions.assertThat(result.getVerificationToken()).isNotEmpty();


    }
    @Test
    public void findBYPasswordResetToken(){
        String token="njkvsddfbjvfjvbfj";
        User mockUser=new User();
        mockUser.setEmail("brahyam.t");
        mockUser.setFullName("brahyam");
        mockUser.setPassword("brahyam12345");
        mockUser.setRole(Role.USER);
        mockUser.setEmailVerified(true);
        mockUser.setPasswordResetToken(token);
        mockUser.setPasswordResetTokenExpiry(Instant.now().plusSeconds(3000));
        entityManager.persistAndFlush(mockUser);
        User result=userRepository.findByPasswordResetToken(token).orElseThrow(()->new TokenVericationPasswordNotFoundExeption("the token wasn't found"));
        org.assertj.core.api.Assertions.assertThat(result.getPasswordResetToken()).isNotEmpty();


    }



}
