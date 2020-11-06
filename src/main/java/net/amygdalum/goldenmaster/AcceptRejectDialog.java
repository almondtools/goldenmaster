package net.amygdalum.goldenmaster;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class AcceptRejectDialog implements Interaction {

	private CompletableFuture<List<Failure>> future;

	public AcceptRejectDialog() {
		this.future = new CompletableFuture<>();
	}

	@Override
	public CompletableFuture<List<Failure>> open(List<Failure> failures) {
		JFrame f = new JFrame("Some expectations were not met");
		Point start = leftUpperStart();

		f.setLocation(start);
		f.setAlwaysOnTop(true);
		f.setLayout(new FlowLayout());
		f.getContentPane()
			.add(createContent(failures));
		f.getContentPane()
			.add(createAcceptRejectButtons(event -> {
				f.dispose();
				future.complete(failures);
			}, event -> {
				f.dispose();
				future.complete(emptyList());
			}));
		f.pack();
		f.setVisible(true);
		return future;
	}

	private Point leftUpperStart() {
		GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point start = graphicsEnv.getCenterPoint();
		start.translate(-200, -100);
		return start;
	}

	private Component createContent(List<Failure> failures) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		panel.add(createDescription());
		panel.add(createListOfFailed(failures));
		return panel;
	}

	private Component createDescription() {
		JTextArea text = new JTextArea(""
			+ "Following tests are started the first time"
			+ " or do not meet the previously stored expectations."
			+ "\n\n"
			+ "* Reject and the current result will be serialized aside the current expectations as `<testname>.failed`."
			+ "\n"
			+ "* Accept to override the current expectations in `<testname>.failed`."
			+ "\n\n"
			+ "Some IDEs will let you preview the differences if you inspect the test results.");
		text.setMargin(new Insets(10,10,10,10));
		text.setEditable(false);
		return text;
	}

	private Component createListOfFailed(List<Failure> failures) {
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10,10,10,10));
		panel.setLayout(new GridLayout(0,1));
		Map<String, List<Failure>> groupedFailures = failures.stream()
			.collect(groupingBy(Failure::getGroup));
		
		for (Map.Entry<String, List<Failure>> entry : groupedFailures.entrySet()) {
			panel.add(group(entry.getKey()));
			for (Failure failure : entry.getValue()) {
				panel.add(test(failure.getTest()));
			}
		}
		return panel;
	}

	private JLabel group(String group) {
		JLabel label = new JLabel(group);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		return label;
	}

	private JLabel test(String test) {
		JLabel label = new JLabel(test);
		label.setBorder(new EmptyBorder(0, 20, 0, 0));
		return label;
	}

	private Component createAcceptRejectButtons(ActionListener onAccept, ActionListener onReject) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		panel.add(createButton("Accept", onAccept));
		panel.add(createButton("Reject", onReject));
		return panel;
	}

	private JButton createButton(String label, ActionListener listener) {
		JButton button = new JButton(label);
		button.addActionListener(listener);

		return button;
	}

}
