package cmsc436.project.thermalcamera;

public enum Scales {
	FAHRENHEIT("°F"),
	CELSIUS("°C");
	
	private String symbol;
	
	private Scales(String symbol){
		this.symbol = symbol;
	}
	
	public String toString(){
		return symbol;
	}
}
