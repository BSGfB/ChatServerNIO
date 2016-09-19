package ru.start;

import java.io.IOException;

import ru.objects.NIOServer;
// import ru.objects.RC4;

/**
 * Created by Sergei on 9/4/2016.
 */
public class Start {
    public static void main(String[] args) throws IOException {
    	new NIOServer("localhost", 2020, "99538574").run();
    	/*
    	String password = "123";
    	String text = "Hello, World!";
    	byte[] byteText = text.getBytes();
    	byte[] byteText2 = {1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4};
    	
    	System.out.println("byteText:");
    	print(byteText);
    	
    	
    	RC4 rc4Server = new RC4(byteText);
    	byte[] code = rc4Server.DoIt(text.getBytes());
    	
    	System.out.println("code:");
    	print(code);
    
    	byte[] decode = rc4Server.DoIt(code);
    	System.out.println("decode:");
    	print(decode);
    	
    	String str = new String(decode);
    	System.out.println(str);
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
