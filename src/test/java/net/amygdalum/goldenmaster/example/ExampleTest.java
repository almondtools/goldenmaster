package net.amygdalum.goldenmaster.example;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import net.amygdalum.goldenmaster.Expectations;
import net.amygdalum.goldenmaster.GoldenMasterTest;

class ExampleTest {

	@GoldenMasterTest(store="src/test/resources")
	@Test
	void testA(Expectations expectations) {
		Example next = new Example("astring", true, 1, -1).next();
		expectations.validate(next.getA() + ":" + next.isB() + ":" + next.getC() + ":" + next.getD());
	}
	
	@GoldenMasterTest(store="src/test/resources")
	@Nested
	class testB {
		@Test
		void beingTrue(Expectations expectations) {
			Example next = new Example("a", true, 1, -1).next();
			expectations.validate(next.getA() + ":" + next.isB() + ":" + next.getC() + ":" + next.getD());
		}
		@Test
		void beingFalse(Expectations expectations) {
			Example next = new Example("a", false, 1, -1).next();
			expectations.validate(next.getA() + ":" + next.isB() + ":" + next.getC() + ":" + next.getD());
		}
	}
	
}
