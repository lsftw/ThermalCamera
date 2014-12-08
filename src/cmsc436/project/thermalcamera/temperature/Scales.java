package cmsc436.project.thermalcamera.temperature;

public enum Scales {
	F("°F", "F"), //Fahrenheit
	C("°C", "C"); //Celsius
	
	private String symbol;
	private String singleLetter;
	
	private Scales(String symbol, String singleLetter){
		this.symbol = symbol;
		this.singleLetter = singleLetter;
	}

	public String toSingleLetter() {
		return singleLetter;
	}
	
	public String toString(){
		return symbol;
	}
}
