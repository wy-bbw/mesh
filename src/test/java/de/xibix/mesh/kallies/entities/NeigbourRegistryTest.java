package de.xibix.mesh.kallies.entities;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NeigbourRegistryTest {
    @Test
    public void testNotConnected() {
        Set<Integer> nodesOfFirst = new HashSet<>();
        nodesOfFirst.add(0);
        nodesOfFirst.add(1);
        Set<Integer> nodesOfSecond = new HashSet<>();
        nodesOfSecond.add(2);
        nodesOfSecond.add(3);

        Set<Integer> elementsOf0 = new HashSet<>();
        elementsOf0.add(0);
        Set<Integer> elementsOf1 = new HashSet<>();
        elementsOf1.add(0);
        Set<Integer> elementsOf2 = new HashSet<>();
        elementsOf2.add(1);
        Set<Integer> elementsOf3 = new HashSet<>();
        elementsOf3.add(1);

        Map<Integer, Set<Integer>> nodesByElement = new HashMap<>();
        nodesByElement.put(0, nodesOfFirst);
        nodesByElement.put(1, nodesOfSecond);
        Map<Integer, Set<Integer>> elementsByNode = new HashMap<>();
        elementsByNode.put(0, elementsOf0);
        elementsByNode.put(1, elementsOf1);
        elementsByNode.put(2, elementsOf2);
        elementsByNode.put(3, elementsOf3);

        NeighbourRegistry registry = new NeighbourRegistry(elementsByNode, nodesByElement);

        Set<Integer> neighboursOf0 = registry.neighbourIds(0);
        assertEquals(0, neighboursOf0.size());

        Set<Integer> neighboursOf1 = registry.neighbourIds(1);
        assertEquals(0, neighboursOf1.size());
    }

    @Test
    public void testConnected() {
        Set<Integer> nodesOfFirst = new HashSet<>();
        nodesOfFirst.add(0);
        nodesOfFirst.add(1);
        Set<Integer> nodesOfSecond = new HashSet<>();
        nodesOfSecond.add(1);
        nodesOfSecond.add(2);

        Set<Integer> elementsOf0 = new HashSet<>();
        elementsOf0.add(0);
        Set<Integer> elementsOf1 = new HashSet<>();
        elementsOf1.add(0);
        elementsOf0.add(1);
        Set<Integer> elementsOf2 = new HashSet<>();
        elementsOf2.add(1);

        Map<Integer, Set<Integer>> nodesByElement = new HashMap<>();
        nodesByElement.put(0, nodesOfFirst);
        nodesByElement.put(1, nodesOfSecond);
        Map<Integer, Set<Integer>> elementsByNode = new HashMap<>();
        elementsByNode.put(0, elementsOf0);
        elementsByNode.put(1, elementsOf1);
        elementsByNode.put(2, elementsOf2);

        NeighbourRegistry registry = new NeighbourRegistry(elementsByNode, nodesByElement);

        Set<Integer> neighboursOf0 = registry.neighbourIds(0);
        assertEquals(1, neighboursOf0.size());
        assertTrue(neighboursOf0.contains(1));

        Set<Integer> neighboursOf1 = registry.neighbourIds(1);
        assertEquals(1, neighboursOf1.size());
        assertTrue(neighboursOf1.contains(0));
    }
}
