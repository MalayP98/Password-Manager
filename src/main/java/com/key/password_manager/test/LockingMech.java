package com.key.password_manager.test;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.key.password_manager.encryption.RSAKeyPairStore;
import com.key.password_manager.keypair.PrivatePublicKeyPair;
import com.key.password_manager.locks.Lock;
import com.key.password_manager.user.User;
import com.key.password_manager.user.UserService;

@RestController
@RequestMapping("/public/lock")
public class LockingMech {

	@Autowired
	private Lock lock;

	@Autowired
	private RSAKeyPairStore rsaKeyPairStore;

	@Autowired
	private UserService userService;

	@PostMapping("/rsa")
	public String rsalock(@RequestParam String str, @RequestParam Long id) throws Exception {
		PrivatePublicKeyPair ppkp = rsaKeyPairStore.getRSAKeyPair(id);
		return lock.lock(ppkp.getPublicKey(), str).replace("+", "%2B");
	}

	@PostMapping("/aes")
	public String aesUnlock(@RequestParam String str, @RequestParam Long id,
			@RequestParam String pass) throws Exception {
		User user = userService.getUser(id);
		user.getPassword().setKey(pass);
		System.out.println(user.getPassword().getKey() + " , " + str + "\n\n");
		return lock.unlock(user.getPassword(), str);
	}
}
