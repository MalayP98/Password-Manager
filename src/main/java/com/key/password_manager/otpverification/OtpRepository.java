package com.key.password_manager.otpverification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

	public Otp findByOtp(String otp);

	public Otp findByUserEmail(String email);

}
