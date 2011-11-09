package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import android.os.AsyncTask;

import tddd36.grupp3.models.ClientModel;

public class ConnectionController extends AsyncTask<String, Integer, String> implements Runnable, Observer {

	private static final String COM_IP = "130.236.226.212";
	private static final int COM_PORT = 4444;
	private InputStreamReader isr;
	private PrintWriter pw;
	private BufferedReader br;
	public String serverOutput;
	private Socket s;
	private ClientModel cm;

	public ConnectionController (ClientModel cm){
		this.cm = cm;
	}

	public void run(String userName, String password) {
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
		try {
			pw.println(userName);
			pw.println(password);

			if ((serverOutput = br.readLine()) != "") {
				if (!serverOutput.equals("Authenticated")) {
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

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
	}

	protected void disconnect() throws IOException {
		s.close();
	}

	public void run() {
		// TODO Auto-generated method stub
	}

	@Override
	protected String doInBackground(String... params) {
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
		if(!cm.isAuthenticated()){
			try {
				pw.println(params[0]);
				pw.println(params[1]);

				if ((serverOutput = br.readLine()) != "") {
					if (serverOutput.equals("Authenticated")) {
						cm.setAuthenticated(true);
					} 
					else {
						cm.setAuthenticated(false);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			for(String str : params){
				pw.println(str);
			}
		}
		return null;
	}

}
