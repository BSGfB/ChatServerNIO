package ru.objects;

public class RC4 {
	private int numlen = 128;
	private final byte[] S = new byte[numlen];
	private final byte[] Z = new byte[numlen];
	
	
	public RC4(final byte[] key) {		
		for(byte i = 0; i < numlen && i >= 0; ++i)
			S[i] = i;
		
		for(int i = 0, j = 0; i < numlen; i++) {			
			j = (j + S[i] + key[i % key.length]) % numlen;			
			RC4.swap(S, i, j);
		}
			
		for(byte m = 0, i = 0, j = 0; m < numlen && m >= 0; ++m) {
			i = (byte) ((i + 1) % numlen);
			j = (byte) ((j + S[i]) % numlen);
			RC4.swap(S, i, j);
			Z[m] = S[(S[i] + S[j]) % numlen];
		}
	}
	
	private static void swap(byte[] array, int i, int j) {
		byte temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public byte[] DoIt(final byte[] inArray) {			
		final byte[] outArray = new byte[inArray.length];
		
		for(byte i = 0; i < inArray.length; ++i) {						
			outArray[i] = (byte) (inArray[i] ^ Z[i % numlen]);
		}
		
		return outArray;
	}  
}

/*
public class RC4 {
	private final byte[] S = new byte[256];
	private int x = 0;
	private int y = 0;
	
	private RC4() {}
	
	public RC4(final byte[] key) {
		init(key);
	}
	
	private void init(final byte[] key) {
		for(int i = 0; i < S.length; i++)
			S[i] = (byte)i;
		
		for(int i = 0, j = 0; i < S.length; i++) {			
			j = (j + S[i] + key[i % key.length]) % 256;			
			RC4.swap(S, i, j);
		}
	}
	
	public byte[] DoIt(final byte[] inArray) {		
		final byte[] outArray = new byte[inArray.length];
		
		for(int i = 0; i < inArray.length; ++i) {
			x = (x + 1) % 256;
			y = (y + S[x]) % 256;
			RC4.swap(S, x, y);
						
			outArray[i] = (byte) (inArray[i] ^ ((S[(S[x] + S[y]) % 256])));
		}
		return outArray;
	}
		
	private static void swap(byte[] array, int i, int j) {
		byte temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}
*/
