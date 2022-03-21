package de.xibix.mesh.kallies.entities;

/**
 * record to facilitate hillfinding
 * @param state either rising or falling, depending on whether height previously in- or decreased
 * @param elementCandidateId element id of next candidate
 * @param previousHeight height of previous element
 */
public record Step(Step.State state, int elementCandidateId, double previousHeight) {
    public enum State {RISING, FALLING};
}
