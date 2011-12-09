package tddd36.grupp3.misc;

public class Journal {
	private String name;
	private String socialnumber;
	private String address;
	private String bloodtype;
	private String allergies;
	private String warning;

	public Journal(String name, String socialnumber, String address, String bloodtype, String allergies,
			String warning){
		this.name = name;
		this.socialnumber = socialnumber;
		this.address = address;
		this.bloodtype = bloodtype;
		this.allergies = allergies;
		this.warning = warning;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSocialnumber() {
		return socialnumber;
	}

	public void setSocialnumber(String socialnumber) {
		this.socialnumber = socialnumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

}
