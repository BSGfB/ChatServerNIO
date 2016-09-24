package ru.objects;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Sergei on 9/8/2016.
 */
public class User {
    private String userName = "Bob";
    private ByteBuffer buffer;
    
    private RSA rsa;
    private RC4 rc4;
    
    private BigInteger privateWord;
    
    public User(String userName, ByteBuffer buffer, RSA rsa, RC4 rc4) {
        this.userName = userName;
        this.buffer = buffer;
        this.rc4 = rc4;
        this.rsa = rsa;
        
        SecureRandom rnd = new SecureRandom();
        rnd.setSeed(System.currentTimeMillis());
        privateWord = new BigInteger(256, rnd);
    }

    @SuppressWarnings("unused")
	private User() {}

    public String getUserName() {
        return userName;
    }

    public BigInteger getPrivateWord() {
		return privateWord;
	}

	public void setPrivateWord(BigInteger privateWord) {
		this.privateWord = privateWord;
	}

	public void setUserName(String userName) {
        this.userName = userName;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public RSA getRsa() {
		return rsa;
	}

	public void setRsa(RSA rsa) {
		this.rsa = rsa;
	}

	public RC4 getRc4() {
		return rc4;
	}

	public void setRc4(RC4 rc4) {
		this.rc4 = rc4;
	}

	public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", buffer=" + buffer +
                '}';
    }
}
