package net.amygdalum.goldenmaster;

import static net.amygdalum.extensions.assertj.conventions.DefaultEquality.defaultEquality;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class TestLocationTest {

	@Test
	void testEquals() {
		assertThat(new TestLocation("base", "group", "file")).satisfies(defaultEquality()
			.andEqualTo(new TestLocation("base", "group", "file"))
			.andNotEqualTo(new TestLocation("otherbase", "group", "file"))
			.andNotEqualTo(new TestLocation("base", "othergroup", "file"))
			.andNotEqualTo(new TestLocation("base", "group", "otherfile"))
			.conventions());
	}

	@Test
	void testExpected() {
		assertThat(new TestLocation("base", "group", "file").expected()).isEqualTo(Paths.get("base/group/file.expected"));
	}

	@Test
	void testfailed() {
		assertThat(new TestLocation("base", "group", "file").failed()).isEqualTo(Paths.get("base/group/file.failed"));
	}

}
