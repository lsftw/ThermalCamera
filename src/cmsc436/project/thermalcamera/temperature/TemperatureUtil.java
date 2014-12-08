package cmsc436.project.thermalcamera.temperature;

public class TemperatureUtil {
	public static String storeTemperaturesInFileName(String fileName, String photoDegrees, Scales photoScale, String homeDegrees, Scales homeScale) {
		int extensionDot = fileName.lastIndexOf('.');
		String fileNameNoExtension = fileName.substring(0, extensionDot);
		String extension = fileName.substring(extensionDot + 1);
		String newFileName = String.format("%1-P%2%3-H%4%5.%6", fileNameNoExtension, photoDegrees, photoScale.toSingleLetter(), homeDegrees, homeScale.toSingleLetter(), extension);
		return newFileName;
	}
	// TODO loadTemperaturesFromFileName
}
