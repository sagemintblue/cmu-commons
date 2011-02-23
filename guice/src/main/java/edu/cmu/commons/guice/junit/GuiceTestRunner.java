package edu.cmu.commons.guice.junit;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Uses Guice to inject JUnit4 tests. Use the {@link RunWith} and
 * {@link Modules} annotations to enable Guice injection of your JUnit class:
 * 
 * <pre>
 * &#064;RunWith(GuiceTestRunner.class)
 * &#064;Modules({ MyModule.class, MyOtherModule.class })
 * public class MyJUnitTest {
 * 	&#064;Inject
 * 	private Thing thing;
 * 
 * 	&#064;Test
 * 	public void doSomething() {
 * 		Assert.assertNotNull(thing);
 * 	}
 * }
 * </pre>
 * 
 * @author Gili Tzabari
 * @author hazen
 */
public class GuiceTestRunner extends BlockJUnit4ClassRunner {
	private static final Logger log = LoggerFactory
			.getLogger(GuiceTestRunner.class);

	private final Injector injector;

	/**
	 * Creates a new GuiceTestRunner.
	 * 
	 * @param classToRun the test class to run.
	 * @param modules the Guice modules.
	 * @throws InitializationError if the test class is malformed.
	 */
	public GuiceTestRunner(final Class<?> classToRun, Module... modules)
			throws InitializationError {
		super(classToRun);
		Modules modulesAnnotation = null;
		Class<?> cls = classToRun;
		while (modulesAnnotation == null && cls != null) {
			modulesAnnotation = cls.getAnnotation(Modules.class);
			cls = cls.getSuperclass();
		}
		if (modulesAnnotation != null) {
			int i = modules.length;
			modules = Arrays.copyOf(modules, i + modulesAnnotation.value().length);
			for (Class<? extends Module> moduleClass : modulesAnnotation.value()) {
				try {
					modules[i++] = moduleClass.newInstance();
				} catch (Exception e) {
					throw new InitializationError(e);
				}
			}
		}
		this.injector = createInjector(modules);
	}

	/**
	 * Passes empty Module array to {@link #GuiceTestRunner(Class, Module...)}.
	 * @param classToRun
	 * @throws InitializationError
	 */
	public GuiceTestRunner(final Class<?> classToRun) throws InitializationError {
		this(classToRun, new Module[] {});
	}

	/**
	 * @param modules instances of all configured Modules.
	 * @return injector containing desired modules.
	 */
	protected Injector createInjector(Module... modules) {
		log.debug("Guice modules: " + Arrays.toString(modules));
		return Guice.createInjector(modules);
	}

	/**
	 * @return the Guice injector
	 */
	protected Injector getInjector() {
		return injector;
	}

	@Override
	public Object createTest() {
		TestClass testClass = getTestClass();
		return injector.getInstance(testClass.getJavaClass());
	}

	@Override
	protected void validateZeroArgConstructor(List<Throwable> errors) {
		/* Guice can inject constructors with parameters so we don't want this
		 * method to trigger an error */
	}
}
