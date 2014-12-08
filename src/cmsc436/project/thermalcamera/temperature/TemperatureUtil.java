package cmsc436.project.thermalcamera.temperature;

public class TemperatureUtil {
	public static String storeTemperaturesInFileName(String fileName, Temperature photoTemp, Temperature homeTemp) {
		int extensionDot = fileName.lastIndexOf('.');
		String fileNameNoExtension = fileName.substring(0, extensionDot);
		String extension = fileName.substring(extensionDot + 1);
		String newFileName = fileNameNoExtension + "_P" + photoTemp + "-H" + homeTemp + "." + extension;
		return newFileName;
	}
	// TODO loadTemperaturesFromFileName <file_name>_P<temp+scale>-H<temp+scale>.<extension>
}
