package tddd36.grupp3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import tddd36.grupp3.controllers.ConnectionController;

public class Connection extends Thread {
	private Socket socket = null;
	private boolean listening = true;
	private BufferedReader in;
	private String msg;
	private ConnectionController cc;

	public Connection(Socket socket, ConnectionController cc) {
		super("Connection");
		this.socket = socket;
		this.cc = cc;
	}

	public void run() {

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));

			while(listening){
				listen();
			}

			out.close();
			in.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void listen() throws IOException {
		String input;
		try{
			while(true){
				if((input = in.readLine()) != ""){
					msg = input;
					listening = false;
					break;
				}
			}
		} catch(NullPointerException e){
		}
	}
}
