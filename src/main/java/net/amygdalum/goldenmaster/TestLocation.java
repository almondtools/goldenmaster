package net.amygdalum.goldenmaster;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class TestLocation {

	private String basePath;
	private String testGroup;
	private String testFile;

	public TestLocation(String basePath, String testGroup, String testFile) {
		this.basePath = basePath;
		this.testGroup = testGroup;
		this.testFile = testFile;
	}

	public Path expected() {
		return Paths.get(basePath).resolve(testGroup).resolve(testFile + ".expected");
	}

	public Path failed() {
		return Paths.get(basePath).resolve(testGroup).resolve(testFile + ".failed");
	}

	@Override
	public int hashCode() {
		return basePath.hashCode() * 13 
			+ testGroup.hashCode() * 7
			+ testFile.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TestLocation that = (TestLocation) obj;
		return Objects.equals(this.basePath, that.basePath)
			&& Objects.equals(this.testGroup, that.testGroup)
			&& Objects.equals(this.testFile, that.testFile);
	}
	
	

}
