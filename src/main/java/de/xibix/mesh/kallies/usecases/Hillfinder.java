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

            // TODO: add only to queue if id is not in queue: use a map as queue
            while (!queue.isEmpty()) {
                // TODO: add step
                Step currentPath = front(queue);
                double heightOfNeigbourElement = heightRegistry.get(currentPath.elementCandidateId());

                // if we were going up before, add the new element to the hill. Add paths for all neighbours to the queue.
                if (currentPath.state() == Step.State.RISING) {

                    hill.addElement(currentPath.elementCandidateId());
                    ungroupedElements.remove(currentPath.elementCandidateId());

                    Set<Integer> nextNewNeighbourCandidates = neighbourRegister.neighbourIds(currentPath.elementCandidateId());
                    nextNewNeighbourCandidates.retainAll(ungroupedElements);
                    // TODO: only add one neighbour for ascension, but all for down
                    Set<Integer> sureCandidates = nextNewNeighbourCandidates
                            .stream()
                            .filter(x -> heightRegistry.get(x) <= heightRegistry.get(currentPath.elementCandidateId()))
                            .collect(Collectors.toSet());
                    Optional<Integer> neigbourUp = nextNewNeighbourCandidates
                            .stream()
                            .filter(x -> heightRegistry.get(x) > heightRegistry.get(currentPath.elementCandidateId()))
                            .findFirst();
                    neigbourUp.ifPresent(sureCandidates::add);
                    Step.State nextState = currentPath.previousHeight() > heightOfNeigbourElement ? Step.State.FALLING : Step.State.RISING;
                    for (Integer nextNeighbourId : sureCandidates) {
                        // only address those that are in the unvisited set
                        Step newPath = new Step(nextState, nextNeighbourId, heightOfNeigbourElement);
                        updateQueue(queue, newPath);
                    }
                } else {
                    // if we were going down before, we may not go up again as that would mean climbing another hill. Plateaus will be added
                    if (heightOfNeigbourElement <= currentPath.previousHeight()) {
                        Set<Integer> newNeighbourCandidates = neighbourRegister.neighbourIds(currentPath.elementCandidateId());
                        newNeighbourCandidates.retainAll(ungroupedElements);
                        for (Integer nextNeighbourId : newNeighbourCandidates) {
                            Step newPath = new Step(Step.State.FALLING, nextNeighbourId, heightOfNeigbourElement);
                            updateQueue(queue, newPath);
                        }
                    }
                }
            }
            allHills.add(hill);
        }
        return allHills;
    }

    private StarterKit initializeHill(Set<Integer> ungroupedElements) {
        Iterator<Integer> it = ungroupedElements.iterator();
        final int startId = it.next();
        final double currentHeight = heightRegistry.get(startId);
        ungroupedElements.remove(startId);

        Set <Integer> descendingPlateauNeighbours = new HashSet<>();
        Set<Integer> ascendingPlateauNeighbours = new HashSet<>();
        Set<Integer> plateauMembers = new HashSet<>();
        plateauMembers.add(startId);
        List<Integer> queue = new LinkedList<>();

        queue.add(startId);

        while (!queue.isEmpty()) {
            Integer currentId = queue.remove(0);
            Set<Integer> allNeighbourIds = neighbourRegister.neighbourIds(currentId);
            allNeighbourIds.retainAll(ungroupedElements);
            for (Integer neighbourId : allNeighbourIds) {
                final double neighbourHeight = heightRegistry.get(neighbourId);
                // descending nodes are always added. remove from ungrouped elements here to remove search time.
                if (neighbourHeight < currentHeight) {
                    descendingPlateauNeighbours.add(neighbourId);
                    ungroupedElements.remove(neighbourId);
                    // only one ascending node is added to hill
                } else if (neighbourHeight > currentHeight) {
                    ascendingPlateauNeighbours.add(neighbourId);
                } else {
                    plateauMembers.add(neighbourId);
                    ungroupedElements.remove(neighbourId);
                    queue.add(neighbourId);
                }
            }
        }

        Hill hill = new Hill(heightRegistry);
        plateauMembers.forEach(hill::addElement);
        descendingPlateauNeighbours.forEach(hill::addElement);

        // we started at a valley. choose the first ascending neighbour

        List<Step> paths = new LinkedList<>();
        if (!ascendingPlateauNeighbours.isEmpty()) {
            Iterator<Integer> firstAscending = ascendingPlateauNeighbours.iterator();
            Integer neighbourId = firstAscending.next();
            paths.add(new Step(Step.State.RISING, neighbourId, currentHeight));
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
        if (queue.containsKey(id)) {
            Step currentEntry = queue.get((id));
            boolean slopeCriterion = step.state() == Step.State.RISING && currentEntry.state() == Step.State.FALLING;
            boolean heightCriterion = step.state() == Step.State.FALLING && currentEntry.state() == Step.State.FALLING && step.previousHeight() > currentEntry.previousHeight();
            if (slopeCriterion || heightCriterion) {
                queue.put(id, currentEntry);
            }
        } else {
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
