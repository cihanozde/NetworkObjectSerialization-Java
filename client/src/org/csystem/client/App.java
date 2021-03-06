package org.csystem.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import org.csystem.utils.Keyboard;
import org.csystem.utils.Utils;

import common.Person;

public class App {

	public static void main(String[] args) 
	{
		Keyboard kb = new Keyboard();
		
		InetAddress ipv4 = null;
		int code = kb.getInt("Se�ene�i giriniz");		
		
		try {
			ipv4 = Utils.getLocalAddress();
			System.out.println(ipv4);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		
		
		try (Socket socket = new Socket(ipv4, 50500)) {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());			
			
			oos.writeInt(code);
			oos.flush();
			
			
			boolean success = ois.readBoolean();
			
			if (success) {
				if (code == 1) {					
					String name = kb.getLine("Isim giriniz");
					int number = kb.getInt("Numarayi giriniz", "Geçersiz format");
					char op = kb.getLine("Evli/Bekar?[e/b]").charAt(0);
				
					Person p = new Person(name, number, op == 'e' ? true : false);
					oos.writeObject(p);
					oos.flush();				
				}
				else if (code == 2) {
					int size = ois.readInt();
					
					for (int i = 0; i < size; ++i) 
						System.out.println(ois.readObject());					
				}
			}
			else
				System.out.println("Gecersiz kod");		
			
		}	
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
	}

}
