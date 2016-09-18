package ru.objects;

import java.nio.ByteBuffer;

/**
 * Created by Sergei on 9/8/2016.
 */
public class User {
    private String userName = "Bob";
    private ByteBuffer buffer;
    
    private RSA rsa;
    private RC4 rc4;

    public User(String userName, ByteBuffer buffer, RSA rsa, RC4 rc4) {
        this.userName = userName;
        this.buffer = buffer;
        this.rc4 = rc4;
        this.rsa = rsa;
    }

    @SuppressWarnings("unused")
	private User() {}

    public String getUserName() {
        return userName;
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
