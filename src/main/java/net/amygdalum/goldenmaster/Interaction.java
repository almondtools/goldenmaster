package net.amygdalum.goldenmaster;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Interaction {

	CompletableFuture<List<Failure>> open(List<Failure> failures);

}
