package cmsc436.project.thermalcamera.temperature;

public enum Scales {
	F("°F"), //Fahrenheit
	C("°C"); //Celsius
	
	private String symbol;
	
	private Scales(String symbol) {
		this.symbol = symbol;
	}

	public String toSingleLetter() {
		return this.name();
	}
	
	public String toString() {
		return symbol;
	}
}
