package net.amygdalum.goldenmaster;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

public class Failures implements CloseableResource {
	private String basePath;
	private List<Failure> failures;
	private Interaction interaction;

	public Failures(String basePath) {
		this(basePath, null);
	}
	
	public Failures(String basePath, Interaction interaction) {
		this.basePath = basePath;
		this.interaction = interaction;
		this.failures = new ArrayList<>();
	}
	
	public List<Failure> getFailures() {
		return failures;
	}

	public void add(Failure failure) {
		failures.add(failure);
	}

	@Override
	public void close() throws Exception {
		if (interaction == null) {
			failures.forEach(this::reject);
			return;
		}
		interaction.open(failures)
			.thenAccept(accepted -> failures.forEach(failure -> {
				if (accepted.contains(failure)) {
					accept(failure);
				} else {
					reject(failure);
				}
			}))
			.get();
	}

	private void reject(Failure failure) {
		try {
			Path path = new TestLocation(basePath, failure.getGroup(), failure.getTest()).failed();
			Files.createDirectories(path.getParent());
			Files.write(path, failure.getValue().toString().getBytes(UTF_8), CREATE, WRITE, TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void accept(Failure failure) {
		try {
			Path path = new TestLocation(basePath, failure.getGroup(), failure.getTest()).expected();
			Files.createDirectories(path.getParent());
			Files.write(path, failure.getValue().toString().getBytes(UTF_8), CREATE, WRITE, TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
