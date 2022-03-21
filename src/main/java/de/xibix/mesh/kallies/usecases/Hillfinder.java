package de.xibix.mesh.kallies.usecases;

import de.xibix.mesh.kallies.entities.Hill;
import de.xibix.mesh.kallies.entities.NeighbourRegister;
import de.xibix.mesh.kallies.entities.Step;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;


@AllArgsConstructor
public class Hillfinder {
    private NeighbourRegister neighbourRegister;
    private Map<Integer, Double> heightRegistry;

    private record StarterKit(List<Step> paths, Hill hill) {};


    List<Hill> findAll() {
        Set<Integer> ungroupedElements = getElementIds();

        List<Hill> allHills = new ArrayList<>();

        while (!ungroupedElements.isEmpty()) {
            // find a start node for a hill
            StarterKit starterKit = initializeHill(ungroupedElements);

            List<Step> startSteps = starterKit.paths();
            Map<Integer, Step> queue = new HashMap<>();
            for (Step step : startSteps) {
                updateQueue(queue, step);
            }

            Hill hill = starterKit.hill();

            while (!queue.isEmpty()) {
                Step currentPath = front(queue);
                double heightOfNeigbourElement = heightRegistry.get(currentPath.elementCandidateId());
                    if (heightOfNeigbourElement <= currentPath.previousHeight()) {
                        hill.addElement(currentPath.elementCandidateId());
                        ungroupedElements.remove(currentPath.elementCandidateId());

                        Set<Integer> newNeighbourCandidates = neighbourRegister.neighbourIds(currentPath.elementCandidateId());
                        newNeighbourCandidates.retainAll(ungroupedElements);
                        for (Integer nextNeighbourId : newNeighbourCandidates) {
                            Step newPath = new Step(Step.State.FALLING, nextNeighbourId, heightOfNeigbourElement);
                            updateQueue(queue, newPath);
                        }
                    }
            }
            allHills.add(hill);
        }
        return allHills;
    }

    private StarterKit initializeHill(Set<Integer> ungroupedElements) {
        Iterator<Integer> it = ungroupedElements.iterator();
        Integer current = it.next();
        Integer idOfMaxHeight = current;
        Double maxHeight = heightRegistry.get(current);
        while (it.hasNext()) {
            current = it.next();
            final Double newHeight = heightRegistry.get(current);
            if (newHeight > maxHeight) {
                maxHeight = newHeight;
                idOfMaxHeight = current;
            }
        }

        Hill hill = new Hill(heightRegistry);
        ungroupedElements.remove(idOfMaxHeight);
        hill.addElement(idOfMaxHeight);

        Set<Integer> neighbours = neighbourRegister.neighbourIds(idOfMaxHeight);
        neighbours.retainAll(ungroupedElements);
        List<Step> paths = new LinkedList<>();
        for (Integer neighbourId : neighbours) {
            Step step = new Step(Step.State.FALLING, neighbourId, maxHeight);
            paths.add(step);
        }
        return new StarterKit(paths, hill);
    }

    private Set<Integer> getElementIds() {
        Set<Integer> returnValue = new HashSet<>();
        heightRegistry.keySet().forEach(returnValue::add);
        return returnValue;
    }

    private void updateQueue(Map<Integer, Step> queue, final Step step) {
        Integer id = step.elementCandidateId();
        if (!queue.containsKey(id)) {
            queue.put(id, step);
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
