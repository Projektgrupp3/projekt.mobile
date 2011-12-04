package tddd36.grupp3.reports;

public class WindowReport extends Report {

	String exactLocation;

	public WindowReport(String seriousEvent, String typeOfInjury,
			String threats, String numberOfInjuries, String extraResources, String exactLocation) {
		super(seriousEvent, typeOfInjury, threats, numberOfInjuries, extraResources);
		// TODO Auto-generated constructor stub
		this.exactLocation = exactLocation;
	}
	@Override
	public String getExactLocation() {
		return exactLocation;
	}

	public void setExactLocation(String exactLocation) {
		this.exactLocation = exactLocation;
	}


}