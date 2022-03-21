package de.xibix.mesh.kallies.entities;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * class representing a hill.
 * consists of multiple elements that form a hill.
 */
public class Hill {
    private Set<Integer> elements;
    private double maxHeight;
    private Map<Integer, Double> heightsByElement;

    /**
     * Ctor.
     * @param heightMapping Map elementId -> value
     */
    public Hill(Map<Integer, Double> heightMapping) {
        elements = new HashSet<>();
        heightsByElement = heightMapping;
        maxHeight = -1.0;
    }

    /**
     * obtain maximum height of all elements.
     * @return height.
     */
    public double getMaxHeight() {
        return maxHeight;
    }

    /**
     * add an element to the hill.
     * @param elementId id of element.
     */
    public void addElement(final Integer elementId) {
        elements.add(elementId);
        final double newHeight = heightsByElement.get(elementId);
        if (newHeight > maxHeight || elements.size() == 1) {
            maxHeight = newHeight;
        }
    }

    /**
     * return number of elements a hill is made of.
     * @return number of elements.
     */
    public int numberOfElements() {
        return elements.size();
    }
}
