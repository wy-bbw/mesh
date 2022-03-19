package de.xibix.mesh.kallies.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Slope {
    public enum State {RISING, FALLING};
    final State state;
    final String nodeId;
    final double height;
}
