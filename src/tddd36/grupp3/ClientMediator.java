package tddd36.grupp3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import android.os.AsyncTask;
public class ClientMediator extends AsyncTask<String, Integer, String>{

	private static final String COM_IP = "130.236.226.120";
	private static final int COM_PORT = 4040;
	
	private InputStreamReader isr;
	private PrintWriter pw;
	private BufferedReader br;
	
	private boolean auth;
	
	public String serverOutput;
	
	private Socket s;
	private ServerSocket serv;
	
	
	public void connect(String user, String pass) throws UnknownHostException, IOException{
		s = new Socket(COM_IP,COM_PORT);
		//Setting up streams
		isr = new InputStreamReader(s.getInputStream());
		pw = new PrintWriter(s.getOutputStream(),true);
		br = new BufferedReader(isr);
		//Sending username and password to server
		pw.println(user);
		pw.println(pass);
		
		if((serverOutput = br.readLine()) != ""){
			if(!serverOutput.equals("Authenticated")) {
				auth = true;
			}
			else{
				auth = false;
				}
		}
	}
	public void send(String str){
		if(auth){
			pw.println(str);
		}
	}
	public void listen() throws IOException{
		while(true){
			s = serv.accept();
		}
	}
	public boolean getAuth(){
		return auth;
	}
	public void disconnect() throws IOException{
		s.close();
	}
	@Override
	protected String doInBackground(String... params) {
		return null;
	}

}

