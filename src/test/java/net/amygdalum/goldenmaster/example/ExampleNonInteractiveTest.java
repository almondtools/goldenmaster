package net.amygdalum.goldenmaster.example;

import org.junit.jupiter.api.Test;

import net.amygdalum.goldenmaster.Expectations;
import net.amygdalum.goldenmaster.GoldenMasterTest;

@GoldenMasterTest(store="src/test/resources", interactive=false)
public class ExampleNonInteractiveTest {

	@Test
	void testA(Expectations expectations) {
		Example next = new Example("astring", true, 1, -1).next();
		expectations.validate(next.getA() + ":" + next.isB() + ":" + next.getC() + ":" + next.getD());
	}
	
}
