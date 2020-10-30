package net.amygdalum.goldenmaster;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AcceptRejectDialog {

	private CompletableFuture<List<Failure>> future;
	private Failures failures;

	public AcceptRejectDialog(Failures failures) {
		this.future = new CompletableFuture<>();
		this.failures = failures;
	}

	public CompletableFuture<List<Failure>> open() {
		JFrame f = new JFrame();
		Point start = leftUpperStart();

		f.setLocation(start);
		f.setAlwaysOnTop(true);
		f.setLayout(new FlowLayout());
		f.getContentPane()
			.add(createListOfFailed());
		f.getContentPane()
			.add(createAcceptRejectButtons(event -> {
				f.dispose();
				future.complete(failures.getFailures());
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

	private Component createListOfFailed() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		Map<String, List<Failure>> groupedFailures = failures.getFailures().stream()
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
		return label;
	}

	private Component createAcceptRejectButtons(ActionListener onAccept, ActionListener onReject) {
		JPanel panel = new JPanel();
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
