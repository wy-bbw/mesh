package de.xibix.mesh.kallies.entities;

import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class NeighbourRegistry {
    private final Map<Integer, Set<Integer>> elementsByNode;
    private final Map<Integer, Set<Integer>> nodesByElement;

    private static final int VERTICES_OF_SQUARE_FROM_NEIGHBOURS = 4;

    public Set<Integer> neighbourIds(final Integer elementId) {
        final Set<Integer> verticesOfElement = nodesByElement.get(elementId);
        Set<Integer> neighbourIds = new HashSet<>();
        for (final Integer vertex : verticesOfElement) {
            Set<Integer> neighbourIdsAtVertex = elementsByNode.get(vertex);
            neighbourIds.addAll(neighbourIdsAtVertex);
        }
        neighbourIds.remove(elementId);
        return neighbourIds;
    };
}
