package de.xibix.mesh.kallies.usecases;

import de.xibix.mesh.kallies.entities.Hill;
import de.xibix.mesh.kallies.entities.NeighbourRegister;
import de.xibix.mesh.kallies.entities.Step;
import lombok.AllArgsConstructor;

import java.util.*;

/**
 * groups mesh elements (triangles) into hills
 */
@AllArgsConstructor
public class Hillfinder {
    private NeighbourRegister neighbourRegister;
    private Map<Integer, Double> heightRegistry;

    private record StarterKit(List<Step> paths, Hill hill) {
    }

    ;

    /**
     * return hills. A hill is a collection of elements, where each neighbouring element not part of the hill is elevated.
     *
     * @return list of hills
     */
    List<Hill> findAll() {
        Set<Integer> ungroupedElements = getMeshElementIds();

        List<Hill> allHills = new ArrayList<>();

        while (!ungroupedElements.isEmpty()) {
            // find a start node for a hill
            StarterKit starterKit = initializeHill(ungroupedElements);

            List<Step> startSteps = starterKit.paths();
            Map<Integer, Step> pathPool = new HashMap<>();
            for (Step step : startSteps) {
                updatePathPool(pathPool, step);
            }

            Hill hill = starterKit.hill();

            while (!pathPool.isEmpty()) {
                Step currentPath = front(pathPool);
                double heightOfNeigbourElement = heightRegistry.get(currentPath.elementCandidateId());
                // always go down from the top
                if (heightOfNeigbourElement <= currentPath.previousHeight()) {
                    hill.addElement(currentPath.elementCandidateId());
                    ungroupedElements.remove(currentPath.elementCandidateId());

                    Set<Integer> newNeighbourCandidates = neighbourRegister.neighbourIds(currentPath.elementCandidateId());
                    newNeighbourCandidates.retainAll(ungroupedElements);
                    for (Integer nextNeighbourId : newNeighbourCandidates) {
                        Step newPath = new Step(nextNeighbourId, heightOfNeigbourElement);
                        updatePathPool(pathPool, newPath);
                    }
                }
            }
            allHills.add(hill);
        }
        return allHills;
    }


    // look for the highest element and go downhill
    private StarterKit initializeHill(Set<Integer> ungroupedElements) {
        Iterator<Integer> it = ungroupedElements.iterator();
        Integer current = it.next();
        Integer idOfMaxHeight = current;
        Double maxHeight = heightRegistry.get(current);

        Hill hill = new Hill(heightRegistry);
        ungroupedElements.remove(idOfMaxHeight);
        hill.addElement(idOfMaxHeight);

        Set<Integer> neighbours = neighbourRegister.neighbourIds(idOfMaxHeight);
        neighbours.retainAll(ungroupedElements);
        List<Step> paths = new LinkedList<>();
        for (Integer neighbourId : neighbours) {
            Step step = new Step(neighbourId, maxHeight);
            paths.add(step);
        }
        return new StarterKit(paths, hill);
    }

    /**
     * returns ids of all elements in mesh.
     * @return set with elements
     */
    private Set<Integer> getMeshElementIds() {
        /*
        treeset. Reasoning: all elements are added initially and subsequently removed.
        element with the highest value should be in front to make removal O(log n). With n elements
        in the mesh, total element removal cost in O(n log n).
         */
        Set<Integer> returnValue = new TreeSet<>((a, b) -> {
            return Double.compare(heightRegistry.get(b), heightRegistry.get(a));
        });
        heightRegistry.keySet().forEach(returnValue::add);
        return returnValue;
    }

    private void updatePathPool(Map<Integer, Step> queue, final Step step) {
        Integer id = step.elementCandidateId();
        /*
        enter element into queue if it is not there. If element can be reached from a higher point, use that one as reference.
        */
        if (!queue.containsKey(id)) {
            queue.put(id, step);
        } else {
            Step currentQueueEntry = queue.get(id);
            if (currentQueueEntry.previousHeight() < step.previousHeight()) {
                queue.put(id, step);
            }
        }
        return;
    }

    private Step front(Map<Integer, Step> queue) {
        Iterator<Integer> it = queue.keySet().iterator();
        Integer firstKey = it.next();
        Step step = queue.remove(firstKey);
        return step;
    }
}
