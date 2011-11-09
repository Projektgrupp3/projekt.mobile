package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.Connection;
import tddd36.grupp3.models.ClientModel;

public class ConnectionController extends Thread implements Runnable, Observer {

	private static final String COM_IP = "130.236.227.149";
	private static final int COM_PORT = 4444;
	public static final int LISTEN_PORT = 4445;
	private InputStreamReader isr;
	private PrintWriter pw;
	private BufferedReader br;
	public String serverOutput;
	private Socket s;
	private ClientModel cm;
	private ServerSocket serverSocket;
	private Socket socket;
	private String input;
	private String messageFromServer;
	private boolean listen = true;

	public ConnectionController(ClientModel cm) throws IOException {
		this.cm = cm;
		serverSocket =  new ServerSocket(LISTEN_PORT);
	}

	//	public void run(String userName, String password) {
	//		if (!cm.isAuthenticated()) {
	//			try {
	//				login(userName, password);
	//			} catch (IOException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//		}	
	//	}
	public void run(String userName, String password) throws IOException{
		if(!cm.isAuthenticated()){
			login(userName, password);
		}
		else {
			while (listen) {
				try {				
					socket =  serverSocket.accept();
					new Connection((socket), this).start();

				} catch (IOException ioException) {
					ioException.printStackTrace();
					System.exit(-1);
				} 
			}
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
	}

	protected void setMessage(){
		
	}

	public void login(String userName, String password) throws IOException {
		try {
			s = new Socket(COM_IP, COM_PORT);
			isr = new InputStreamReader(s.getInputStream());
			pw = new PrintWriter(s.getOutputStream(), true);
			br = new BufferedReader(isr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!cm.isAuthenticated()) {
			try {
				pw.println(userName);
				pw.println(password);

				if ((serverOutput = br.readLine()) != "") {
					if (serverOutput.equals("Authenticated")) {
						cm.setAuthenticated(true);
					} else {
						cm.setAuthenticated(false);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pw.close();
		br.close();
		isr.close();
		s.close();
	}

}
