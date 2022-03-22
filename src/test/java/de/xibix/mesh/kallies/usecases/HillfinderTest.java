package de.xibix.mesh.kallies.usecases;

import de.xibix.mesh.kallies.entities.Hill;
import de.xibix.mesh.kallies.entities.NeighbourRegister;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HillfinderTest {

    void enterIntoMap(Map<Integer, Set<Integer>> map, final Integer key, final Integer... values) {
        final Set<Integer> setOfKey = new HashSet<>();
        Arrays.stream(values).forEach(setOfKey::add);
        map.put(key, setOfKey);
    }
    /*
     * heights:
     *   ------
     *    \ 3/
     *     \/
     * |\  /|
     * |1--0|
     * |/  \|
     * nodes are ordered anticlockwise
     * elements:
     *   ------
     *    \ 1/
     *     \/
     * |\  /|
     * |2--0|
     * |/  \|
     */

    @Test
    public void testTwoPeaksOnePlateau() {
        Map<Integer, Set<Integer>> elementsByNode = new HashMap<>();
        enterIntoMap(elementsByNode, 1, 2);
        enterIntoMap(elementsByNode, 2, 2);
        enterIntoMap(elementsByNode, 3, 2, 0);
        enterIntoMap(elementsByNode, 4, 0);
        enterIntoMap(elementsByNode, 5, 0, 1);
        enterIntoMap(elementsByNode, 6, 1);
        enterIntoMap(elementsByNode, 7, 1);

        Map<Integer, Set<Integer>> nodesByElement = new HashMap<>();
        enterIntoMap(nodesByElement, 2, 1, 2, 3);
        enterIntoMap(nodesByElement, 0, 3, 4, 5);
        enterIntoMap(nodesByElement, 1, 5, 6, 7);

        NeighbourRegister neighbourRegister = new NeighbourRegister(elementsByNode, nodesByElement);
        Map<Integer, Double> heightRegistry = new HashMap<>();
        heightRegistry.put(2, 1.0);
        heightRegistry.put(0, 0.0);
        heightRegistry.put(1, 3.0);

        Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegistry);
        List<Hill> hills = hillfinder.findAll();
        assertEquals(2, hills.size());
        hills.sort(Hill.HIGHEST_HILL_FIRST);
        assertEquals(3, hills.get(0).getMaxHeight());
        assertEquals(1, hills.get(1).getMaxHeight());
    }

    /**
     * element connections
     * 0 <-17-> 1
     * 0 <-5-> 2
     * 0 <-4-> 5
     * 0 <-5-> 7
     * 1 <-15-> 4
     * 2 <-11-> 3
     * 2 <-5-> 7
     * 2 <-8-> 8
     * 3 <-13-> 4
     * 5 <-2,3-> 6
     * <p>
     * heights:
     * 0 -> 0
     * 1 -> 1
     * 2 -> 0
     * 3 -> 8
     * 4 -> 3
     * 5 -> 5
     * 6 -> 6
     * 7 -> -1
     * 8 -> 2
     */
    @Test
    public void testHillWithHoleInTheMiddle() {
        Map<Integer, Double> heightRegistry = new HashMap<>();
        heightRegistry.put(0, 0.0);
        heightRegistry.put(1, 1.0);
        heightRegistry.put(2, 0.0);
        heightRegistry.put(3, 8.0);
        heightRegistry.put(4, 3.0);
        heightRegistry.put(5, 5.0);
        heightRegistry.put(6, 6.0);
        heightRegistry.put(7, -1.0);
        heightRegistry.put(8, 2.0);

        Map<Integer, Set<Integer>> elementsByNode = new HashMap<>();
        enterIntoMap(elementsByNode, 1, 6);
        enterIntoMap(elementsByNode, 2, 5, 6);
        enterIntoMap(elementsByNode, 3, 5, 6);
        enterIntoMap(elementsByNode, 4, 0, 5);
        enterIntoMap(elementsByNode, 5, 0, 2, 7);
        enterIntoMap(elementsByNode, 6, 7);
        enterIntoMap(elementsByNode, 7, 7);
        enterIntoMap(elementsByNode, 8, 2, 8);
        enterIntoMap(elementsByNode, 9, 8);
        enterIntoMap(elementsByNode, 10, 8);
        enterIntoMap(elementsByNode, 11, 2, 3);
        enterIntoMap(elementsByNode, 12, 3);
        enterIntoMap(elementsByNode, 13, 3, 4);
        enterIntoMap(elementsByNode, 14, 4);
        enterIntoMap(elementsByNode, 15, 1, 4);
        enterIntoMap(elementsByNode, 16, 1);
        enterIntoMap(elementsByNode, 17, 0, 1);

        Map<Integer, Set<Integer>> nodesByElement = new HashMap<>();
        enterIntoMap(nodesByElement, 0, 4, 5, 17);
        enterIntoMap(nodesByElement, 1, 15, 16, 17);
        enterIntoMap(nodesByElement, 2, 5, 8, 11);
        enterIntoMap(nodesByElement, 3, 11, 12, 13);
        enterIntoMap(nodesByElement, 4, 13, 14, 15);
        enterIntoMap(nodesByElement, 5, 2, 3, 4);
        enterIntoMap(nodesByElement, 6, 1, 2, 3);
        enterIntoMap(nodesByElement, 7, 5, 6, 7);
        enterIntoMap(nodesByElement, 8, 9, 10, 8);

        NeighbourRegister neighbourRegister = new NeighbourRegister(elementsByNode, nodesByElement);
        Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegistry);
        List<Hill> hills = hillfinder.findAll();
        assertEquals(3, hills.size());
        hills.sort(Hill.HIGHEST_HILL_FIRST);
        assertEquals(8, hills.get(0).getMaxHeight());
        assertEquals(6, hills.get(1).getMaxHeight());
        assertEquals(2, hills.get(2).getMaxHeight());
    }


    /**
     * connections: terminology: elementId1 <- connecting node ids -> elementId2
     * 0 <-2-> 1
     * 1 <-6-> 2
     * 1 <-3-> 3
     * heights = ids
     */
    @Test
    public void testTwoHills() {
        Map<Integer, Double> heightRegistry = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            heightRegistry.put(i, (double) i);
        }

        Map<Integer, Set<Integer>> nodesByElement = new HashMap<>();
        enterIntoMap(nodesByElement, 0, 0, 1, 2);
        enterIntoMap(nodesByElement, 1, 2, 3, 6);
        enterIntoMap(nodesByElement, 2, 6, 7, 8);
        enterIntoMap(nodesByElement, 3, 3, 4, 5);

        Map<Integer, Set<Integer>> elementsByNode = new HashMap<>();
        enterIntoMap(elementsByNode, 0, 0);
        enterIntoMap(elementsByNode, 1, 0);
        enterIntoMap(elementsByNode, 2, 0, 1);
        enterIntoMap(elementsByNode, 3, 1, 3);
        enterIntoMap(elementsByNode, 4, 3);
        enterIntoMap(elementsByNode, 5, 3);
        enterIntoMap(elementsByNode, 6, 1, 2);
        enterIntoMap(elementsByNode, 7, 2);
        enterIntoMap(elementsByNode, 8, 2);

        NeighbourRegister neighbourRegister = new NeighbourRegister(elementsByNode, nodesByElement);
        Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegistry);
        List<Hill> hills = hillfinder.findAll();
        assertEquals(2, hills.size());
    }
}
