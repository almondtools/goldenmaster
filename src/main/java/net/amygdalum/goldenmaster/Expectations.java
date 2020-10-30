package net.amygdalum.goldenmaster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.opentest4j.AssertionFailedError;

public class Expectations {

	private TestLocation location;
	private String expected;

	public Expectations(TestLocation location) {
		this.location = location;
	}

	public void load() {
		Path path = location.expected();
		
		try {
			expected = new String(Files.readAllBytes(path));
		} catch (IOException e) {
			expected = null;
		}
	}

	public void validate(String found) {
		if (expected == null || !expected.equals(found)) {
			throw new AssertionFailedError("expected result differs from actual", expected, found);
		}
	}
}
