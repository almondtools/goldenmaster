package net.amygdalum.goldenmaster;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.engine.execution.ExtensionValuesStore;
import org.junit.jupiter.engine.execution.NamespaceAwareStore;
import org.opentest4j.AssertionFailedError;

import net.amygdalum.goldenmaster.example.ExampleClassTest;

public class GoldenMasterExtensionTest {

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	void testTestFailed() throws Exception {
		GoldenMasterExtension goldenMasterExtension = new GoldenMasterExtension(null);
		Namespace ns = Namespace.create(GoldenMasterExtension.class);
		Store store = new NamespaceAwareStore(new ExtensionValuesStore(null), ns);
		
		ExtensionContext context = mock(ExtensionContext.class, RETURNS_DEEP_STUBS);
		when(context.getRoot().getStore(ns)).thenReturn(store);
		when(context.getElement()).thenReturn(Optional.of(ExampleClassTest.class ));
		when(context.getRequiredTestClass()).thenReturn((Class) ExampleClassTest.class);
		when(context.getRequiredTestMethod()).thenReturn(ExampleClassTest.class.getDeclaredMethod("testA", Expectations.class));
		
		goldenMasterExtension.testFailed(context, new AssertionFailedError("msg", "expected","actual"));
		
		assertThat(store.get("src/test/resources", Failures.class).getFailures()).hasSize(1);
	}

}
