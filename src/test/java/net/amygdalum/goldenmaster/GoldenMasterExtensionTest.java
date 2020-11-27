package net.amygdalum.goldenmaster;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.engine.execution.ExtensionValuesStore;
import org.junit.jupiter.engine.execution.NamespaceAwareStore;
import org.mockito.Mockito;
import org.opentest4j.AssertionFailedError;

import net.amygdalum.goldenmaster.example.ExampleClassTest;
import net.amygdalum.goldenmaster.example.ExampleNonInteractiveTest;
import net.amygdalum.xrayinterface.XRayInterface;

public class GoldenMasterExtensionTest {

	@TempDir
	Path tempDir;

	@Nested
	class testFailed {
		@SuppressWarnings({"unchecked", "rawtypes"})
		@Test
		void withNoInteractionSpecified() throws Exception {
			GoldenMasterExtension goldenMasterExtension = new GoldenMasterExtension(null);
			Namespace ns = Namespace.create(GoldenMasterExtension.class);
			Store store = new NamespaceAwareStore(new ExtensionValuesStore(null), ns);

			ExtensionContext context = mock(ExtensionContext.class, RETURNS_DEEP_STUBS);
			when(context.getRoot().getStore(ns)).thenReturn(store);
			when(context.getElement()).thenReturn(Optional.of(ExampleClassTest.class));
			when(context.getRequiredTestClass()).thenReturn((Class) ExampleClassTest.class);
			when(context.getRequiredTestMethod()).thenReturn(ExampleClassTest.class.getDeclaredMethod("testA", Expectations.class));

			goldenMasterExtension.testFailed(context, new AssertionFailedError("msg", "expected", "actual"));

			Failures failures = store.get("src/test/resources", Failures.class);
			assertThat(failures.getFailures()).hasSize(1);

			XRayInterface.xray(failures).to(OpenFailures.class).setBasePath(tempDir.toString());
			failures.close();

			assertThat(Files.isRegularFile(tempDir.resolve("net.amygdalum.goldenmaster.example.ExampleClassTest\\testA.failed")));
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		@Test
		void withInteractionSpecified() throws Exception {
			Interaction interaction = mock(Interaction.class);
			GoldenMasterExtension goldenMasterExtension = new GoldenMasterExtension(interaction);
			Namespace ns = Namespace.create(GoldenMasterExtension.class);
			Store store = new NamespaceAwareStore(new ExtensionValuesStore(null), ns);

			ExtensionContext context = mock(ExtensionContext.class, RETURNS_DEEP_STUBS);
			when(context.getRoot().getStore(ns)).thenReturn(store);
			when(context.getElement()).thenReturn(Optional.of(ExampleClassTest.class));
			when(context.getRequiredTestClass()).thenReturn((Class) ExampleClassTest.class);
			when(context.getRequiredTestMethod()).thenReturn(ExampleClassTest.class.getDeclaredMethod("testA", Expectations.class));

			goldenMasterExtension.testFailed(context, new AssertionFailedError("msg", "expected", "actual"));

			Failures failures = store.get("src/test/resources", Failures.class);
			assertThat(failures.getFailures()).hasSize(1);

			XRayInterface.xray(failures).to(OpenFailures.class).setBasePath(tempDir.toString());
			when(interaction.open(Mockito.anyList())).thenReturn(CompletableFuture.completedFuture(failures.getFailures()));
			failures.close();

			assertThat(Files.isRegularFile(tempDir.resolve("net.amygdalum.goldenmaster.example.ExampleClassTest\\testA.expected")));
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		@Test
		void withConfiguredNoInteraction() throws Exception {
			Interaction interaction = mock(Interaction.class);
			GoldenMasterExtension goldenMasterExtension = new GoldenMasterExtension(interaction);
			Namespace ns = Namespace.create(GoldenMasterExtension.class);
			Store store = new NamespaceAwareStore(new ExtensionValuesStore(null), ns);

			ExtensionContext context = mock(ExtensionContext.class, RETURNS_DEEP_STUBS);
			when(context.getRoot().getStore(ns)).thenReturn(store);
			when(context.getElement()).thenReturn(Optional.of(ExampleNonInteractiveTest.class));
			when(context.getRequiredTestClass()).thenReturn((Class) ExampleNonInteractiveTest.class);
			when(context.getRequiredTestMethod()).thenReturn(ExampleNonInteractiveTest.class.getDeclaredMethod("testA", Expectations.class));

			goldenMasterExtension.testFailed(context, new AssertionFailedError("msg", "expected", "actual"));

			Failures failures = store.get("src/test/resources", Failures.class);
			assertThat(failures.getFailures()).hasSize(1);

			XRayInterface.xray(failures).to(OpenFailures.class).setBasePath(tempDir.toString());
			failures.close();

			assertThat(Files.isRegularFile(tempDir.resolve("net.amygdalum.goldenmaster.example.ExampleNonInteractiveTest\\testA.failed")));
		}
	}

	interface OpenFailures {
		void setBasePath(String basePath);
	}
}
