package de.xibix.mesh.kallies.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record Path(de.xibix.mesh.kallies.entities.Path.State state, int elementCandidateId, double previousHeight) {
    public enum State {RISING, FALLING};
}
