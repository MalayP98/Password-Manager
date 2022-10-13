package com.key.password_manager.key;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "key")
public class AESKey extends AbstarctKey {

    private String salt;

    private String iv;

    @Enumerated(EnumType.STRING)
    private AESKeyTypes type;

    public AESKey() {
        super();
    }

    public AESKey(String key, String salt, String iv, AESKeyTypes type) {
        super(key);
        setSalt(salt);
        setIv(iv);
        setType(type);
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

    public AESKeyTypes getType() {
        return type;
    }

    public void setType(AESKeyTypes type) {
        if (Objects.isNull(type))
            throw new NullPointerException("Key type is not supplied while creating AES Key");
        this.type = type;
    }
}
