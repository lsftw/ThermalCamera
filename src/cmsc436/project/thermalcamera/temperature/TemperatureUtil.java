package cmsc436.project.thermalcamera.temperature;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// <file_name>_P<temp+scale>-H<temp+scale>.<extension>
public class TemperatureUtil {
	public static final String RGX_NUM = "^(-?\\d*\\.?\\d*)$"; // signed decimal number
	public static final Pattern REGEX_TEMPERATURE_FILE_NAME = Pattern.compile(".+_P(" + RGX_NUM + ")-H(" + RGX_NUM + ")\\..+");

	public static String storeTemperaturesInFileName(String fileName, Temperature photoTemp, Temperature homeTemp) {
		int extensionDot = fileName.lastIndexOf('.');
		String fileNameNoExtension = fileName.substring(0, extensionDot);
		String extension = fileName.substring(extensionDot + 1);
		String newFileName = fileNameNoExtension + "_P" + photoTemp + "-H" + homeTemp + "." + extension;
		return newFileName;
	}

	// Returns 2 temperatures: photo and home
	public static Temperature[] loadTemperaturesFromFileName(String fileName) {
		Matcher matcherTemp = REGEX_TEMPERATURE_FILE_NAME.matcher(fileName);
		if (matcherTemp.matches()) {
			String photoTempString = matcherTemp.group(1);
			String homeTempString = matcherTemp.group(2);
			Temperature photoTemp = new Temperature(photoTempString);
			Temperature homeTemp = new Temperature(homeTempString);
			return new Temperature[]{photoTemp, homeTemp};
		}
		return null; // no temps found
	}
}
