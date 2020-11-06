package net.amygdalum.goldenmaster;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AcceptRejectDialogTest {

	private static Robot robot;

	@BeforeAll
	public static void beforeAll() throws Exception {
		robot = BasicRobot.robotWithCurrentAwtHierarchy();
	}

	@Nested
	class testOpen {
		@Test
		void accepting() throws Exception {
			AcceptRejectDialog dialog = new AcceptRejectDialog();
			CompletableFuture<List<Failure>> open = dialog.open(asList(new Failure("group", "test", "value")));
			FrameFixture fix = WindowFinder.findFrame(JFrame.class).using(robot);

			assertThat(fix.target().getTitle()).isEqualTo("Some expectations were not met");
			assertThat(fix.target().isVisible()).isTrue();
			assertThat(open.isDone()).isFalse();

			fix.button("Accept").click();

			assertThat(open.isDone()).isTrue();
			assertThat(open.get()).hasSize(1);
		}

		@Test
		void rejecting() throws Exception {
			AcceptRejectDialog dialog = new AcceptRejectDialog();
			CompletableFuture<List<Failure>> open = dialog.open(asList(new Failure("group", "test", "value")));
			FrameFixture fix = WindowFinder.findFrame(JFrame.class).using(robot);

			assertThat(fix.target().getTitle()).isEqualTo("Some expectations were not met");
			assertThat(fix.target().isVisible()).isTrue();
			assertThat(open.isDone()).isFalse();

			fix.button("Reject").click();

			assertThat(open.isDone()).isTrue();
			assertThat(open.get()).hasSize(0);
		}
	}

}
