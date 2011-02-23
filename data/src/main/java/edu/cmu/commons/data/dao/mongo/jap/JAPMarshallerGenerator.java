package edu.cmu.commons.data.dao.mongo.jap;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.tools.Diagnostic.Kind;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * A {@link Processor} which generates {@link Marshaller} implementations for
 * classes annotated with {@link Entity}, {@link MappedSuperclass}, or
 * {@link Embeddable} JPA annotations.
 * @author hazen
 */
@SupportedAnnotationTypes({ "javax.persistence.Entity",
		"javax.persistence.MappedSuperclass", "javax.persistence.Embeddable" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JAPMarshallerGenerator extends AbstractProcessor {
	private class MonkeyElementVisitor extends
			SimpleElementVisitor6<Void, JCodeModel> {
		@Override
		protected Void defaultAction(Element e, JCodeModel p) {
			processingEnv.getMessager().printMessage(Kind.NOTE,
					"Visiting element '" + e + "'", e);
			return null;
		}
	}

	private class MonkeyCodeWriter extends CodeWriter {
		@Override
		public OutputStream openBinary(JPackage pkg, String fileName)
				throws IOException {
			String className = pkg.name() + "." + fileName.replaceAll("\\.java$", "");
			return processingEnv.getFiler().createSourceFile(className)
					.openOutputStream();
		}

		@Override
		public void close() throws IOException {}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		if (annotations.isEmpty()) return false;
		try {
			JCodeModel cm = new JCodeModel();

			// process MappedSuperclasses first, then Embeddables, then Entities.
			for (Class<? extends Annotation> cls : Arrays
					.<Class<? extends Annotation>> asList(MappedSuperclass.class,
							Embeddable.class, Entity.class)) {
				processingEnv.getMessager().printMessage(Kind.NOTE,
						"Processing classes annotated with '" + cls.getName() + "'");
				Set<? extends Element> elements =
						roundEnv.getElementsAnnotatedWith(cls);
				for (Element element : elements)
					element.accept(new MonkeyElementVisitor(), cm);
			}
			cm.build(new MonkeyCodeWriter());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return false;
	}
}
