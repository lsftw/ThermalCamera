package cmsc436.project.thermalcamera.temperature;

public class Temperature implements Comparable<Temperature>{
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


	@Override
	public int compareTo(Temperature another) {
		//Convert everything to Fahrenheit, then compare.
		double d1 = Double.parseDouble(this.degrees);
		if (this.scale == Scales.C) {
			d1 = d1 * 9/5 + 32;
		}
		double d2 = Double.parseDouble(another.degrees);
		if (another.scale == Scales.C) {
			d2 = d2 * 9/5 + 32;
		}
		
		//maybe an easier way to do this; I was worried about rounding into 0
		//for just returning (int) d1 - d2
		if (d1 - d2 == 0) {
			return 0;
		} else if (d1 - d2 > 0){
			return 1;
		} else {
			return -1;
		}
	}
	
}
