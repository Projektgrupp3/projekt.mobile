package tddd36.grupp3.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import tddd36.grupp3.models.LoginModel;
import android.os.AsyncTask;
import android.util.Log;

public class ConnectionController extends AsyncTask<Void, Void, Void> {
	public static final String REQ_ALL_UNITS = "REQ_ALL_UNITS";

	public static final int LISTEN_PORT = 1561;
	
	public String serverOutput;

	private SSLSocket s;
	private LoginModel cm;
	public static SSLServerSocket serversocket;
	private SSLSocket socket;

	public ConnectionController(LoginModel cm) throws IOException {
		this.cm = cm;
		serversocket =  (SSLServerSocket)SSLServerSocketFactory.getDefault().createServerSocket(LISTEN_PORT);
		serversocket.setEnabledCipherSuites(new String[] { "SSL_DH_anon_WITH_RC4_128_MD5" });
		//SSLSession session = serversocket.getSession();
				//new SSLServerSocket(LISTEN_PORT);
	}

	@Override
	protected Void doInBackground(Void... params) {
		while (true) {
			Log.d("Loop", "Lyssnar efter inkommande server connections");
			try {
				socket = (SSLSocket) serversocket.accept();
				serversocket.setEnabledCipherSuites(new String[] { "SSL_DH_anon_WITH_RC4_128_MD5" });
				new ConnectionTask((socket), this, cm).execute();
			} catch (IOException ioException) {
				ioException.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
