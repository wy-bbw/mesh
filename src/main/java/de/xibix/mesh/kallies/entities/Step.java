package de.xibix.mesh.kallies.entities;

/**
 * record to facilitate hillfinding
 * @param elementCandidateId element id of next candidate
 * @param previousHeight height of previous element
 */
public record Step(int elementCandidateId, double previousHeight) {
}
