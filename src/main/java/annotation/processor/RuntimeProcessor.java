package annotation.processor;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.Executors;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class RuntimeProcessor {
	
	public static void process() {

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forJavaClassPath())
				.setScanners(new FieldAnnotationsScanner())
				.setExecutorService(Executors.newFixedThreadPool(3)));

		Set<Field> annotatedFields = reflections.getFieldsAnnotatedWith(CustomAnnotation.class);

		for(Field field : annotatedFields) {

			try {
				Class<?> classToSet = field.getDeclaringClass();
				Object obj = classToSet.getDeclaredConstructor(new Class[] {}).newInstance(new Object[] {});

				field.setAccessible(true);
				Object value = field.getType().getDeclaredConstructor(new Class[] {}).newInstance(new Object[] {});
				field.set(obj, value);
			}catch(Exception e) {
				System.out.println(e);
			}
		}
	}

}
