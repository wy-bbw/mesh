package de.xibix.mesh.kallies.usecases;

import de.xibix.mesh.kallies.entities.Hill;
import de.xibix.mesh.kallies.entities.NeighbourRegistry;
import de.xibix.mesh.kallies.entities.Path;
import lombok.AllArgsConstructor;

import java.util.*;



@AllArgsConstructor
public class Hillfinder {
    private NeighbourRegistry neighbourRegistry;
    private Map<Integer, Double> heightRegistry;

    private record StarterKit(List<Path> paths, Hill hill) {};


    List<Hill> findAll() {
        Set<Integer> ungroupedElements = getElementIds();

        List<Hill> allHills = new ArrayList<>();

        while (!ungroupedElements.isEmpty()) {
            // find a start node for a hill
            StarterKit starterKit = initializeHill(ungroupedElements);
            List<Path> queue = starterKit.paths();
            Hill hill = starterKit.hill();
            while (!queue.isEmpty()) {
                Path currentPath = queue.remove(0);
                double heightOfNeigbourElement = heightRegistry.get(currentPath.elementCandidateId());

                // if we were going up before, add the new element to the hill. Add paths for all neighbours to the queue.
                if (currentPath.state() == Path.State.RISING) {

                    hill.addElement(currentPath.elementCandidateId());
                    ungroupedElements.remove(currentPath.elementCandidateId());

                    Set<Integer> newNeighbourCandidates = neighbourRegistry.neighbourIds(currentPath.elementCandidateId());
                    Path.State nextState = currentPath.previousHeight() > heightOfNeigbourElement ? Path.State.FALLING : Path.State.RISING;
                    for (Integer nextNeighbourId : newNeighbourCandidates) {
                        // only address those that are in the unvisited set
                        if (ungroupedElements.contains(nextNeighbourId)) {
                            Path newPath = new Path(nextState, nextNeighbourId, heightOfNeigbourElement);
                            queue.add(newPath);
                        }
                    }
                } else {
                    // if we were going down before, we may not go up again as that would mean climbing another hill. Plateaus will be added
                    if (heightOfNeigbourElement <= currentPath.previousHeight()) {
                        Set<Integer> newNeighbourCandidates = neighbourRegistry.neighbourIds(currentPath.elementCandidateId());
                        for (Integer nextNeighbourId : newNeighbourCandidates) {
                            // only address those that are in the unvisited set
                            if (ungroupedElements.contains(nextNeighbourId)) {
                                Path newPath = new Path(Path.State.FALLING, nextNeighbourId, heightOfNeigbourElement);
                                queue.add(newPath);
                            }
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
            Set<Integer> allNeighbourIds = neighbourRegistry.neighbourIds(currentId);
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

        List<Path> paths = new LinkedList<>();
        if (!ascendingPlateauNeighbours.isEmpty()) {
            Iterator<Integer> firstAscending = ascendingPlateauNeighbours.iterator();
            Integer neighbourId = firstAscending.next();
            paths.add(new Path(Path.State.RISING, neighbourId, currentHeight));
        }
        return new StarterKit(paths, hill);
    }


    private Set<Integer> getElementIds() {
        Set<Integer> returnValue = new HashSet<>();
        heightRegistry.keySet().forEach(returnValue::add);
        return returnValue;
    }
}
