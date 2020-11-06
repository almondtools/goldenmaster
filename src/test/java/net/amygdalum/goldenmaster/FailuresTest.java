package net.amygdalum.goldenmaster;

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

class FailuresTest {

	@TempDir
	public Path dir;

	@Nested
	class testAdd {
		@Test
		void none() throws Throwable {
			Failures failures = new Failures(dir.toString());

			failures.close();

			assertThat(Files.list(dir)).isEmpty();
		}

		@Test
		void one() throws Throwable {
			Failures failures = new Failures(dir.toString());

			failures.add(new Failure("group", "test", "found"));
			failures.close();

			assertThat(Files.isRegularFile(dir.resolve("group").resolve("test.failed")));
		}

		@Test
		void multiple() throws Throwable {
			Failures failures = new Failures(dir.toString());

			failures.add(new Failure("group", "test1", "found"));
			failures.add(new Failure("group", "test2", "found"));
			failures.close();

			assertThat(Files.isRegularFile(dir.resolve("group").resolve("test1.failed")));
			assertThat(Files.isRegularFile(dir.resolve("group").resolve("test2.failed")));
		}
	}

	@Nested
	class testCloseInteractively {
		@Test
		void acceptingFailures() throws Throwable {
			Interaction interaction = Mockito.mock(Interaction.class);
			when(interaction.open(Mockito.anyList())).thenAnswer(args -> {
				List<Failure> failed = args.getArgument(0);
				return completedFuture(failed);
			});
			Failures failures = new Failures(dir.toString(), interaction);

			failures.add(new Failure("group", "test", "found"));
			failures.close();

			assertThat(Files.isRegularFile(dir.resolve("group").resolve("test.expected")));
		}

		@Test
		void rejectingFailures() throws Throwable {
			Interaction interaction = Mockito.mock(Interaction.class);
			when(interaction.open(Mockito.anyList())).thenAnswer(args -> completedFuture(emptyList()));
			Failures failures = new Failures(dir.toString(), interaction);

			failures.add(new Failure("group", "test", "found"));
			failures.close();

			assertThat(Files.isRegularFile(dir.resolve("group").resolve("test.failed")));
		}
	}
}
