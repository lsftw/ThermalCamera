package cmsc436.project.thermalcamera.temperature;

public class TemperatureUtil {
	public static String storeTemperaturesInFileName(String fileName, String photoDegrees, Scales photoScale, String homeDegrees, Scales homeScale) {
		int extensionDot = fileName.lastIndexOf('.');
		String fileNameNoExtension = fileName.substring(0, extensionDot);
		String extension = fileName.substring(extensionDot + 1);
		String newFileName = fileNameNoExtension + "_P" + photoDegrees + photoScale.toSingleLetter() + "-H" + homeDegrees + homeScale.toSingleLetter() + "." + extension;
		return newFileName;
	}
	// TODO loadTemperaturesFromFileName <file_name>_P<temp+scale>-H<temp+scale>.<extension>
}
