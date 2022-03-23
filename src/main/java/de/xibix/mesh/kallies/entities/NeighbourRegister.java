package de.xibix.mesh.kallies.entities;

import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@AllArgsConstructor
/**
 * helper class for neighbouring nodes.
 */
public class NeighbourRegister {
    private final Map<Integer, Set<Integer>> elementsByNode;
    private final Map<Integer, Set<Integer>> nodesByElement;

    /**
     * find neighbours of element.
     *
     * @param elementId elemend id.
     * @return set with ids of neighbours.
     */
    public Set<Integer> neighbourIds(final Integer elementId) {
        final Set<Integer> verticesOfElement = nodesByElement.get(elementId);
        Set<Integer> neighbourIds = new HashSet<>();
        for (final Integer vertex : verticesOfElement) {
            Set<Integer> neighbourIdsAtVertex = elementsByNode.get(vertex);
            neighbourIds.addAll(neighbourIdsAtVertex);
        }
        neighbourIds.remove(elementId);
        return neighbourIds;
    }
}
