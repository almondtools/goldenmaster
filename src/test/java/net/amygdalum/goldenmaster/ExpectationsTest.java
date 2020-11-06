package net.amygdalum.goldenmaster;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ExpectationsTest {

	@Nested
	class testLoadValidate {
		@Test
		void valid() throws Exception {
			Expectations expectations = new Expectations(new TestLocation("src/test/resources", "valid", "dummy"));
			expectations.load();

			assertDoesNotThrow(() -> expectations.validate("dummy"));
		}

		@Test
		void notVAlid() throws Exception {
			Expectations expectations = new Expectations(new TestLocation("src/test/resources", "notvalid", "dummy"));
			
			expectations.load();
			
			assertThrows(AssertionError.class, () -> expectations.validate("dummy"));
		}

		@Test
		void notExisting() throws Exception {
			Expectations expectations = new Expectations(new TestLocation("src/test/resources", "notexisting", "dummy"));
			
			expectations.load();
			
			assertThrows(AssertionError.class, () -> expectations.validate("dummy"));
		}
	}

}
