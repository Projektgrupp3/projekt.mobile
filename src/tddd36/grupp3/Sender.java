package tddd36.grupp3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.reports.Report;
import tddd36.grupp3.resources.Event;

/**
 * KLIENT-SENDER-KLASS
 * @author Emil
 *
 */
public class Sender {
	public static final String REQ_ALL_UNITS = "REQ_ALL_UNITS";
	public static final String ACK_VERIFICATION_REPORT = "ACK_VERIFICATION_REPORT";
	public static final String ACK_WINDOW_REPORT = "ACK_WINDOW_REPORT";

	public static final String REQ_MAP_OBJECTS = "REQ_MAP_OBJECTS";
	public static final String REQ_ALL_CONTACTS ="REQ_ALL_CONTACTS";
	public static final String REQ_CONTACT = "REQ_CONTACT";
	public static final String UPDATE_MAP_OBJECT = "UPDATE_MAP_OBJECT";
	public static final String ACK_RECIEVED_EVENT = "ACK_RECIEVED_EVENT";
	public static final String ACK_ACCEPTED_EVENT = "ACK_ACCEPTED_EVENT";
	public static final String ACK_REJECTED_EVENT = "ACK_REJECTED_EVENT";
	public static final String ACK_STATUS = "ACK_STATUS";
	public static final String ACK_CHOSEN_UNIT = "ACK_CHOSEN_UNIT";
	public static final String LOG_OUT = "LOG_OUT";
	
	private static final String COM_IP = "130.236.226.203";
	private static final int COM_PORT = 1560;
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
		String[] splittedMessage = messageToServer.split(":", 2);

		jsonobject = new JSONObject();
		try {
			jsonobject.put("user", username);
			jsonobject.put("pass", password);

			if (messageToServer.startsWith(ACK_ACCEPTED_EVENT) 
					|| messageToServer.startsWith(ACK_RECIEVED_EVENT)
					|| messageToServer.startsWith(ACK_REJECTED_EVENT)) {
				jsonobject.put("ack", "event");
				jsonobject.put("event", splittedMessage[0]);
				jsonobject.put("eventID", splittedMessage[1]);
			}
			else if (messageToServer.startsWith(ACK_CHOSEN_UNIT)) {
				jsonobject.put("ack", "unit");
				jsonobject.put("unit", splittedMessage[1]);
			}
			else if(messageToServer.startsWith(ACK_STATUS)){
				jsonobject.put("ack", "status");
				jsonobject.put("status", splittedMessage[1]);
			}
//			else if(messageToServer.startsWith(ACK_VERIFICATION_REPORT)){
//				jsonobject.put("ack", ACK_VERIFICATION_REPORT);
//				jsonobject.put("ACK_VERIFICATION_REPORT)", splittedMessage[1]);
//			}
//			else if(messageToServer.startsWith(ACK_WINDOW_REPORT)){
//				jsonobject.put("ack", "ACK_WINDOW_REPORT)");
//				jsonobject.put("ACK_WINDOW_REPORT)", splittedMessage[1]);
//			}
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
		jsonobject.put("req", UPDATE_MAP_OBJECT);
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
		jsonobject.put("req", REQ_CONTACT);
		jsonobject.put("sipaddress", contactAddress);
		jsonobject.put("contactName", contactName);
		String jsonString = jsonobject.toString();
		establishConnection();
		pw.println(jsonString);
		closeConnection();
	}
	public static void sendReport(Report report) throws JSONException{
		jsonobject = new JSONObject();
		jsonobject.put("ack", "report");
		jsonobject.put("seriousEvent", report.getSeriousEvent());
		jsonobject.put("typeOfInjury", report.getTypeOfInjury());
		jsonobject.put("threats", report.getThreats());
		jsonobject.put("numberOfInjuries",report.getNumberOfInjuries());
		jsonobject.put("setExtraResources",report.getSeriousEvent());
		if(report.getClass().getName().equals("WindowReport")){
			jsonobject.put("exactLocation", report.getExactLocation());
		}
		else if(report.getClass().getName().equals("VerificationReport")){
			jsonobject.put("areaSearched", report.getAreaSearched());
			jsonobject.put("timeOfDeparture", report.getTimeOfDeparture());
		}
		String jsonString = jsonobject.toString();
		establishConnection();
		pw.println(jsonString);
		closeConnection();
	}
}
