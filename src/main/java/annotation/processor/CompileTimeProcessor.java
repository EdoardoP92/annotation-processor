package annotation.processor;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({"annotation.processor.CustomAnnotation"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CompileTimeProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		for(TypeElement annotation : annotations) {
			
			for(Element el: roundEnv.getElementsAnnotatedWith(annotation)) {
				
				String message = "\r\n*** Annotation "+annotation.getSimpleName()+" for "+el.getSimpleName()+" ***";
				processingEnv.getMessager().printMessage(Kind.WARNING, message, el);
				
				//START BUILDER
				String className = ((TypeElement)el.getEnclosingElement()).getQualifiedName().toString();
				String classToInject = el.asType().toString();
				String packageName = null;
		        int lastDot = className.lastIndexOf('.');
		        if (lastDot > 0) {
		            packageName = className.substring(0, lastDot);
		        }
		        ByteCodeFactory.packageName = packageName;
		        ByteCodeFactory.classesToInject.add(classToInject);
			}
		}
		if(roundEnv.processingOver()) {//execute only on final round
			String compileTimeClass = ByteCodeFactory.sourceFileTemplate();
			System.out.println("********** Source file created **********\r\n"+compileTimeClass);
			JavaFileObject jfo;
			try {
				jfo = processingEnv.getFiler().createSourceFile(ByteCodeFactory.packageName+"."+ByteCodeFactory.builderName);
				try (PrintWriter out = new PrintWriter(jfo.openWriter())){
					out.print(compileTimeClass);
				}
	        	
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
		}
		return true;
	}
}