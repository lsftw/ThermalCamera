package cmsc436.project.thermalcamera;

public enum Scales {
	F("°F"), //Fahrenheit
	C("°C"); //Celsius
	
	private String symbol;
	
	private Scales(String symbol){
		this.symbol = symbol;
	}
	
	public String toString(){
		return symbol;
	}
}
