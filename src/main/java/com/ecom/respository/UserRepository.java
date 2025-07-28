package com.ecom.respository;
import com.ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByVerificationCode(String verificationCode);
    User findByResetPasswordToken(String token);
}