package com.key.password_manager.key;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.key.password_manager.key.types.AESKeyType;
import com.key.password_manager.key.types.KeyType;

@Entity
@Table(name = "key")
public class AESKey extends AbstractKey {

	@Column(updatable = false)
	private String salt;

	@Column(updatable = false)
	private String iv;

	@Enumerated(EnumType.STRING)
	private AESKeyType type;

	public AESKey() {
		super(KeyType.AES);
	}

	public AESKey(String key, String salt, String iv, AESKeyType type) {
		super(key, KeyType.AES);
		setSalt(salt);
		setIv(iv);
		setSubKeyType(type);
	}

	public AESKey(AESKey copyObject) {
		this(copyObject.getKey(), copyObject.getSalt(), copyObject.getIv(),
				copyObject.getSubKeyType());
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		if (Objects.isNull(salt) || salt.isBlank())
			throw new NullPointerException("Salt is not supplied while creating AES Key!");
		this.salt = salt;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		if (Objects.isNull(iv) || iv.isBlank())
			throw new NullPointerException("Iv is not supplied while creating AES Key!");
		this.iv = iv;
	}

	public boolean isPasswordKey(AESKeyType subType) {
		return AESKeyType.PASSWORD.equals(subType);
	}

	public boolean isEncryptionKey(AESKeyType subType) {
		return AESKeyType.ENCYPTION_KEY.equals(subType);
	}

	public AESKeyType getSubKeyType() {
		return type;
	}

	public void setSubKeyType(AESKeyType subKeyType) {
		if (Objects.isNull(subKeyType))
			throw new NullPointerException("Key type is not supplied while creating AES Key");
		this.type = subKeyType;
	}
}
