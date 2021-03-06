package net.amygdalum.goldenmaster;

import java.awt.GraphicsEnvironment;
import java.util.Optional;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.platform.commons.support.AnnotationSupport;
import org.opentest4j.AssertionFailedError;

public class GoldenMasterExtension implements BeforeEachCallback, TestWatcher, ParameterResolver {
	private static final Namespace NAMESPACE = Namespace.create(GoldenMasterExtension.class);

	private Interaction interaction;

	public GoldenMasterExtension() {
		this(GraphicsEnvironment.isHeadless()
			? null
			: new AcceptRejectDialog());

	}

	public GoldenMasterExtension(Interaction interaction) {
		this.interaction = interaction;
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		fetchExpectations(context).load();
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == Expectations.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return fetchExpectations(extensionContext);
	}

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		if (cause instanceof AssertionFailedError) {
			AssertionFailedError e = (AssertionFailedError) cause;
			fetchFailed(context).add(new Failure(groupOf(context), testOf(context), e.getActual().getValue()));
		}
	}

	private Expectations fetchExpectations(ExtensionContext extensionContext) {
		Store store = extensionContext.getStore(NAMESPACE);
		return store.getOrComputeIfAbsent(pathOf(extensionContext), Expectations::new, Expectations.class);
	}

	private Failures fetchFailed(ExtensionContext extensionContext) {
		Store store = extensionContext.getRoot().getStore(NAMESPACE);
		String basePath = basePath(extensionContext);
		Interaction interaction = interaction(extensionContext);
		return store.getOrComputeIfAbsent(basePath, g -> new Failures(basePath, interaction), Failures.class);
	}

	private TestLocation pathOf(ExtensionContext extensionContext) {
		String basePath = basePath(extensionContext);
		String testGroup = groupOf(extensionContext);
		String testFile = testOf(extensionContext);
		return new TestLocation(basePath, testGroup, testFile);
	}

	private Optional<GoldenMasterTest> annotation(ExtensionContext extensionContext) {
		Optional<ExtensionContext> context = Optional.of(extensionContext);
		while (context.isPresent()) {
			Optional<GoldenMasterTest> goldenMasterTest = context
				.flatMap(ExtensionContext::getElement)
				.flatMap(annotatedElement -> AnnotationSupport.findAnnotation(annotatedElement, GoldenMasterTest.class));
			if (goldenMasterTest.isPresent()) {
				return goldenMasterTest;
			}
			context = context.flatMap(ExtensionContext::getParent);
		}
		return Optional.empty();
	}

	private Interaction interaction(ExtensionContext extensionContext) {
		return annotation(extensionContext)
			.filter(test -> test.interactive())
			.map(test -> interaction)
			.orElse(null);
	}

	private String basePath(ExtensionContext extensionContext) {
		return annotation(extensionContext)
			.map(test -> test.store())
			.orElseThrow(() -> new RuntimeException("cannot find annotation GoldenMasterTest"));
	}

	private String groupOf(ExtensionContext extensionContext) {
		return extensionContext.getRequiredTestClass().getCanonicalName();
	}

	private String testOf(ExtensionContext extensionContext) {
		return extensionContext.getRequiredTestMethod().getName();
	}

}
