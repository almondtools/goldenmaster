package net.amygdalum.goldenmaster;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.Test;

public class AcceptRejectDialogTest {

	@Test
	void testOpen() throws Exception {
		AcceptRejectDialog dialog = new AcceptRejectDialog();
		CompletableFuture<List<Failure>> open = dialog.open(Collections.emptyList());

		FrameFixture fix = WindowFinder.findFrame(JFrame.class).using(BasicRobot.robotWithCurrentAwtHierarchy());
		assertThat(fix.target().getTitle()).isEqualTo("Some expectations were not met");
	}

}
