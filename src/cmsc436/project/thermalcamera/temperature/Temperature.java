package cmsc436.project.thermalcamera.temperature;

public class Temperature {
	private final String degrees;
	private final Scales scale;

	public Temperature(String degrees, Scales scale) {
		this.degrees = degrees;
		this.scale = scale;
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
