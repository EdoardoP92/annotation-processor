package annotation.processor;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

@SupportedAnnotationTypes({"annotation.processor.CustomAnnotation"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CompileTimeProcessor2 extends AbstractProcessor{
	
	private Set<Element> annotatedClasses = new HashSet<>();
	private Set<Element> annotatedClassesEnclosedElements = new HashSet<>();
	private Set<VariableElement> annotatedClassesVariables = new HashSet<>();
	private Set<ExecutableElement> annotatedClassesMethods = new HashSet<>();
	private Set<String> packages = new HashSet<>();

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		if(!roundEnv.processingOver()) {
			
			//GET CLASSESS ANNOTATED
			for(Element e : roundEnv.getElementsAnnotatedWith(CustomAnnotation.class)) {
				this.annotatedClasses.add(e);
			}
			
			//GET ANNOTATED CLASS TYPE AND PACKAGES
			for(TypeElement te : ElementFilter.typesIn(this.annotatedClasses)) {
//				System.out.println("class: "+te.toString());//fully qualified class name
//				System.out.println("class: "+te.asType());//fully qualified class name
//				System.out.println("class: "+te.getKind());//KIND (CLASS)
//				System.out.println("class: "+te.getNestingKind());// TOP LEVEL (package)
				
				this.packages.add(te.getEnclosingElement().toString());
				this.annotatedClassesEnclosedElements.addAll(te.getEnclosedElements());
				System.out.println("Type element: "+te);
			}
			
			//GET ANNOTATED CLASSES VARIABLES
			for(VariableElement var : ElementFilter.fieldsIn(this.annotatedClassesEnclosedElements)) {
				this.annotatedClassesVariables.add(var);
				System.out.println("Variable element: "+(var.asType().getKind().isPrimitive()));
			}
			
			//GET ANNOTATED CLASSES METHODS
			for(ExecutableElement method : ElementFilter.methodsIn(this.annotatedClassesEnclosedElements)) {
				this.annotatedClassesMethods.add(method);
				System.out.println("Method element: "+method.asType());
			}
			
			//GET CONSTRUCTOR OF ANNOTATED CLASSES VARIABLES
			for(TypeElement constructor : ElementFilter.typesIn(this.annotatedClassesVariables)) {
				System.out.println("Constructor of variables: "+constructor);
			}
		}
		return true;	
	}
}
