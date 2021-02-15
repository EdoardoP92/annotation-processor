package annotation.processor;

import java.util.HashSet;
import java.util.Set;

public class ByteCodeFactory {

	protected static Set<String> classesToInject = new HashSet<>();
	protected static final String builderName = "CustomBuilder";
	protected static String packageName;

	private ByteCodeFactory() {}

	public static String sourceFileTemplate() {

		int counter = 1;
		StringBuilder sb = new StringBuilder();
		
		sb.append("package "+packageName+";\r\npublic class "+builderName+"{\r\n");
		for(String classToInject : classesToInject) {
			sb.append("\tpublic static "+classToInject+" init"+counter+"(){\r\n"
					+ "\t\treturn new "+classToInject+"();\r\n\t}\r\n");
			counter++;
		}
		sb.append("\r\n}");

		return sb.toString();
	}

}
