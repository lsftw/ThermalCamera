package cmsc436.project.thermalcamera.temperature;

public class Temperature {
	private final String degrees;
	private final Scales scale;

	public Temperature(String degrees, Scales scale) {
		this.degrees = degrees;
		this.scale = scale;
	}

	// Pass in a String formatted like Temperature.toString()
	public Temperature(String degreesAndScale) {
		char lastChar = degreesAndScale.charAt(degreesAndScale.length() - 1);
		this.degrees = degreesAndScale.substring(0, degreesAndScale.length() - 1);
		this.scale = Scales.valueOf(lastChar + "");
	}

	public String getDegrees() {
		return degrees;
	}

	public Scales getScale() {
		return scale;
	}

	public String toString() {
		return degrees + scale.toSingleLetter();
	}
}
