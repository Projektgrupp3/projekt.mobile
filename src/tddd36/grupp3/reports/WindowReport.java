package tddd36.grupp3.reports;

public class WindowReport extends Report {

	String exactLocation;

	public WindowReport(String seriousEvent, String typeOfInjury,
			String threats, String numberOfInjuries, String extraResources, 
			String exactLocation, String typeOfReport) {
		super(seriousEvent, typeOfInjury, threats, numberOfInjuries, extraResources, typeOfReport);
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