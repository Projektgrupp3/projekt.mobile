package tddd36.grupp3.resources;

public class Contact {
	private String name;
	private String sipaddress;
	
	public Contact(String name, String sipaddress){
		this.name = name;
		this.sipaddress = sipaddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSipaddress(String sipaddress) {
		this.sipaddress = sipaddress;
	}

	public String getSipaddress() {
		return sipaddress;
	}
}
