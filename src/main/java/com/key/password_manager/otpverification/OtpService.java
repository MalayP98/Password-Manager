package com.key.password_manager.otpverification;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.key.password_manager.email.Email;
import com.key.password_manager.email.EmailService;

@Service
public class OtpService {

    private final String OTP_MESSAGE =
            "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\"><div style=\"margin:50px auto;width:70vw;padding:20px 0\"><div style=\"border-bottom:1px solid #eee\"><a href=\"\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">Keys</a></div> <p style=\"font-size:1.1em\">Hi,</p> <p>Thank you for choosing Keys. Use the following OTP to complete your Sign Up procedures. OTP is valid for %d minutes</p> <h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\"> %s </h2> <p style=\"font-size:0.9em;\">Regards,<br />Keys</p> <hr style=\"border:none;border-top:1px solid #eee\" /> <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\"> <p>Keys Inc</p> <p>1600 Amphitheatre Parkway</p> <p>California</p> </div> </div> </div>";

    private final String SUBJECT = "Keys-OTP";

    @Value("${com.keys.otp.expiry}")
    private int EXPIRY_TIME;

    @Autowired
    private OtpFactory otpFactory;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisTemplate<String, Otp> otpStore;

    public String sentOTP(String recipientEmail) {
        String message = "";
        try {
            Otp otp = otpFactory.createOtp(recipientEmail);
            emailService.sendHTMLMail(new Email(recipientEmail, SUBJECT,
                    String.format(OTP_MESSAGE, EXPIRY_TIME, otp.getOtp())));
            storeOTP(otp);
            message = "Otp send successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            message = "Cannot send otp.";
        }
        return message;
    }

    private void storeOTP(Otp otp) {
        otpStore.opsForValue().set(otp.getOtp(), otp);
        otpStore.expire(otp.getOtp(), Duration.between(otp.getCreationDate().toInstant(),
                otp.getExpiryDate().toInstant()));
    }
}
