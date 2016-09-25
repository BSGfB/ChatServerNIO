package ru.start;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import com.google.gson.JsonObject;

import ru.objects.NIOServer;
import ru.objects.RC4;
import ru.objects.RSA;
// import ru.objects.RC4;

/**
 * Created by Sergei on 9/4/2016.
 */
public class Start {
    public static void main(String[] args) throws IOException {
    	new NIOServer("localhost", 2020, "99538574").run();
    	
    	/*
    	RC4 rc4 = new RC4("99538574".getBytes());
    	JsonObject reply = new JsonObject();
        reply.addProperty("type", "123");
        reply.addProperty("name", "123");
        reply.addProperty("attachment", "123");
        
    	BigInteger code = new BigInteger(reply.toString().getBytes());
    	code = rc4.DoIt(code);
    	
    	
    	*/
    	
    	/* Server */
    	/**
    	SecureRandom random = new SecureRandom();
    	random.setSeed(System.currentTimeMillis());
    	
    	RC4 ServerRC4 = new RC4("99538574".getBytes());
    	RSA ServerRSA = new RSA();
    	ServerRSA.init(1024);
    	
    	byte[] pow = ServerRSA.getPublicKey().toByteArray();
    	byte[] mod = ServerRSA.getModulus().toByteArray();

    	pow = ServerRC4.DoIt(pow);
    	
    	byte[] privateServerString = new BigInteger(256, random).toByteArray();
    	////////////////////////////////////////////
    	
    	/* Client */
    	/*
    	RC4 ClientRC4 = new RC4("99538574".getBytes());
    	RSA ClientRSA = new RSA();
    	byte[] K = new BigInteger(512, random).toByteArray();
    	byte[] privateClientString = new BigInteger(256, random).toByteArray();
    	////////////////////////////////////////////
    	*/
    	// 1 ///////////////////////////
    	
    	/* Client */
    	/*
    	pow = ClientRC4.DoIt(pow);
    	
    	ClientRSA.setPublicKey(new BigInteger(pow));
    	ClientRSA.setModulus(new BigInteger(mod));
    	
    	byte[] array_1 = ClientRSA.encrypt(new BigInteger(K)).toByteArray();
    	array_1 = ClientRC4.DoIt(array_1);
    	*/
    	/* Server */
    	/*
    	array_1 = ServerRC4.DoIt(array_1);
    	array_1 = ServerRSA.decrypt(new BigInteger(array_1)).toByteArray();
    	
    	System.out.println("K == K " + Arrays.equals(K, array_1));
    	
    	ServerRC4 = new RC4(array_1);
    	ClientRC4 = new RC4(K);
    	*/
    	// 2 ////////////////////////////
    	
    	/* Server */
    	/*
    	print(privateServerString);
    	byte[] array_2_1 = ServerRC4.DoIt(privateServerString);
*/
    	/* Client */
/*
    	array_2_1 = ClientRC4.DoIt(array_2_1);
    	array_2_1 = ClientRC4.DoIt(array_2_1);

    	byte[] array_2_2 = ClientRC4.DoIt(privateClientString);
*/
    	/* Server */
    	/*
    	array_2_1 = ServerRC4.DoIt(array_2_1);

    	array_2_2 = ServerRC4.DoIt(array_2_2);
    	array_2_2 = ServerRC4.DoIt(array_2_2);
    	System.out.println("privateServerString == KprivateServerString " + Arrays.equals(array_2_1, privateServerString));
*/
    	/* Client */
/*
    	array_2_2 = ClientRC4.DoIt(array_2_2);
    	System.out.println("privateServerString == KprivateServerString " + Arrays.equals(array_2_2, privateClientString));
*/
    }
    
    public static void print(int[] array) {
    	for(int i = 0; i < array.length; ++i)
    		System.out.print((byte)array[i] + " ");
    	System.out.println("\n");
    }
    
    public static void print(byte[] array) {
    	for(int i = 0; i < array.length; ++i)
    		System.out.print(array[i] + " ");
    	System.out.println("\n");
    }
}
