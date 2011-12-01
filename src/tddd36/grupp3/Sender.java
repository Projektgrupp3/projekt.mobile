package tddd36.grupp3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.resources.Event;

/**
 * KLIENT-SENDER-KLASS
 * @author Emil
 *
 */
public class Sender {
	public static final String REQ_ALL_UNITS = "REQ_ALL_UNITS";
	private static final String COM_IP = "130.236.226.22";
	private static final int COM_PORT = 1879;
	private static PrintWriter pw;
	private static JSONObject jsonobject;

	private static String messageToServer;
	private static String username;
	private static String password;

	private static Socket socket;

	public static void establishConnection() {
		try {
			socket = new Socket(COM_IP, COM_PORT);
			pw = new PrintWriter(socket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			pw.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void send(String str) {
		messageToServer = str;
		String[] splittedMessage = messageToServer.split(":", 3);

		jsonobject = new JSONObject();
		try {
			jsonobject.put("user", username);
			jsonobject.put("pass", password);

			if (messageToServer.startsWith("ackevent")) {
				jsonobject.put("ack", "event");
				jsonobject.put("event", splittedMessage[1]);
				jsonobject.put("eventID", splittedMessage[2]);
			}
			else if (messageToServer.startsWith("ackunit")) {
				jsonobject.put("ack", "unit");
				jsonobject.put("unit", splittedMessage[1]);
			}
			else {
				jsonobject.put("req", messageToServer);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String jsonString = jsonobject.toString();

		establishConnection();

		pw.println(jsonString);

		closeConnection();
	}

	public static void send(Event ev) throws JSONException {
		jsonobject = new JSONObject();

		jsonobject.put("user", username);
		jsonobject.put("pass", password);
		jsonobject.put("req", "MAP_OBJECTS");
		jsonobject.put("header", ev.getHeader());
		jsonobject.put("description", ev.getMessage());
		jsonobject.put("tempCoordX", ev.getLatE6());
		jsonobject.put("tempCoordY", ev.getLonE6());
		jsonobject.put("eventID", ev.getID());

		String jsonString = jsonobject.toString();

		establishConnection();

		pw.println(jsonString);

		closeConnection();
	}

	public static void send(String user, String pass, String message)
			throws JSONException {
		username = user;
		password = pass;
		messageToServer = message;

		jsonobject = new JSONObject();
		jsonobject.put("user", username);
		jsonobject.put("pass", password);
		jsonobject.put("req", messageToServer);

		String jsonString = jsonobject.toString();

		establishConnection();

		pw.println(jsonString);

		closeConnection();
	}
	public static void sendContact(String contactName, String contactAddress)
	throws JSONException {
		jsonobject = new JSONObject();
		jsonobject.put("user", username);
		jsonobject.put("pass", password);
		jsonobject.put("req", "contact");
		jsonobject.put("sipaddress", contactAddress);
		jsonobject.put("contactName", contactName);
		String jsonString = jsonobject.toString();
		establishConnection();
		pw.println(jsonString);
		closeConnection();
	}
}
