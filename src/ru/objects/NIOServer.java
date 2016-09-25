package ru.objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sergei on 9/4/2016.
 */

public class NIOServer {
    private final int port;
    private final String address;

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;

    private final Map<SocketChannel, User> listUsers = new HashMap<SocketChannel, User>();

    private final Charset ch = Charset.forName("UTF-8");
    private final CharsetDecoder decoder = ch.newDecoder();
    
    private String password;

    public NIOServer(String address, int port, String password) throws IOException {
        this.port = port;
        this.address = address;

        this.selector = SelectorProvider.provider().openSelector();
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocketChannel.socket().bind(new InetSocketAddress(this.address, this.port));
        this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        
        this.password = password;
    }

    public void run() {
        System.out.println("Server is starting! " + address + ":" + port);

        try {
            while (this.selector.select() != -1) {
                Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        this.accept(key);
                    } else if (key.isReadable()) {
                        this.read(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(this.selector, SelectionKey.OP_READ);

        RSA rsa = new RSA();
    	rsa.init(1024);
    	RC4 rc4 = new RC4(password.getBytes());
        synchronized (listUsers) {
            this.listUsers.put(socketChannel, new User("Bob", ByteBuffer.allocate(8192), rsa, rc4));
        }
        
        BigInteger pow = rsa.getPublicKey();       
        BigInteger mod = rsa.getModulus();  
                
        /* EKE-1.1 модуль от RSA. 
         * EKE-1.2 показатель степени от RSA зашифрованный с помощью RC4(используя password).
         */
        sendMessage("EKE-1.1", "Server", mod.toString(), socketChannel);
        sendMessage("EKE-1.2", "Server", pow.toString(), socketChannel);
        System.out.println("New connection! " + socketChannel.getRemoteAddress());
        
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        User currentUser = (User)listUsers.get(socketChannel);
        
        try {
        	Message message = readMessage((SocketChannel) key.channel());
        	System.out.println(message);
        	
        	switch (message.getType()) {
            case "message":
                for(SocketChannel sc: listUsers.keySet()) {
                	User user = listUsers.get(sc);
                	if(!sc.equals(socketChannel)) {
                		sendMessage("message", currentUser.getUserName(), message.getAttachment(), sc);
                	}
                }
                break;
            case "Name":
                System.out.println(currentUser.getUserName() + " change name on " +  message.getAttachment());
                currentUser.setUserName( message.getAttachment());
                break;
        	case "EKE-1": {
        		RSA rsa = currentUser.getRsa();
            	RC4 rc4 = currentUser.getRc4();
            	
            	BigInteger code = new BigInteger(message.getAttachment());            	
            	code = rsa.decrypt(code);
            	
            	rc4 = new RC4(code.toByteArray());
            	currentUser.setRc4(rc4); 
            	
            	sendMessage("EKE-2", "Server", currentUser.getPrivateWord().toString(), socketChannel);
        		break;
        	}
        	case "EKE-2.1":
        		BigInteger word = new BigInteger(message.getAttachment());
        		if(!currentUser.getPrivateWord().equals(word)) {
        			//close(key);
        			System.out.println("Error: EKE-2.1. Server private str 1 != user private str 1");
        			break;
        		}
        		System.out.println("Server private str 1 == user private str 1");
        		break;
        	case "EKE-2.2":     		
            	sendMessage("EKE-3", "Server", message.getAttachment(), socketChannel);	
        		break;
        	default:
        		System.out.println("Error default: " + message);
        		break;
        	}
        } catch(IOException e) {
        	this.close(key);
        }
        
        
    }
    
    private void close(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        synchronized (listUsers) {
            listUsers.remove(socketChannel);
        }

        if(socketChannel.isConnected()) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        key.cancel();
    }
    
    private void sendMessage(String type, String name, String attachment, SocketChannel chanel) throws IOException {
    	JsonObject reply = new JsonObject();
        reply.addProperty("type", type);
        reply.addProperty("name", name);
        reply.addProperty("attachment", attachment);
        
        BigInteger code = new BigInteger(new Gson().toJson(reply).getBytes());
    	code = listUsers.get(chanel).getRc4().DoIt(code);
        chanel.write(ByteBuffer.wrap(code.toString().getBytes(ch)));
    }

    private Message readMessage(SocketChannel chanel) throws IOException {
    	User currentUser = (User)listUsers.get(chanel);
        ByteBuffer buffer = currentUser.getBuffer();
        buffer.clear();
            
        if(chanel.read(buffer) <= 0) {
        	throw new IOException("Read <= 0");
        }
        buffer.flip();
        CharBuffer buff = decoder.decode(buffer);
        
        BigInteger code = new BigInteger(String.valueOf(buff));
        code = currentUser.getRc4().DoIt(code);
        
        String str = new String(code.toByteArray(), ch);
        
        JsonReader reader = new JsonReader(new StringReader(str));
        reader.setLenient(true);      
        
        return ((Message)new Gson().fromJson(reader, Message.class));
    }
    
    class Message {
		String type;
		String attachment;
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getAttachment() {
			return attachment;
		}
		public void setAttachment(String attachment) {
			this.attachment = attachment;
		}
		
		@Override
		public String toString() {
			return "Message [type=" + type + ", attachment=" + attachment + "]";
		}
	}
}
