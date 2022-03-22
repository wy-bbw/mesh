package de.xibix.mesh.kallies.entities;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * class representing a hill.
 * consists of multiple elements that form a hill.
 */
public class Hill {
    private final Set<Integer> elements;
    private double maxHeight;
    private int idOfHighestElement;
    private final Map<Integer, Double> heightsByElement;

    /**
     * comparator to sort hills by height.
     */
    public static final Comparator<Hill> HIGHEST_HILL_FIRST = (a, b) -> Double.compare(b.getMaxHeight(), a.getMaxHeight());

    /**
     * Ctor.
     *
     * @param heightMapping Map elementId -> value
     */
    public Hill(Map<Integer, Double> heightMapping) {
        elements = new HashSet<>();
        heightsByElement = heightMapping;
        maxHeight = -1.0;
    }

    /**
     * obtain maximum height of all elements.
     *
     * @return height
     */
    public double getMaxHeight() {
        return maxHeight;
    }

    public Set<Integer> elementIds() {
        return elements;
    }

    /**
     * obtain element id of highest element in hill.
     *
     * @return element id
     */
    public int getIdOfHighestElement() {
        return idOfHighestElement;
    }

    /**
     * add an element to the hill.
     *
     * @param elementId id of element.
     */
    public void addElement(final Integer elementId) {
        elements.add(elementId);
        final double newHeight = heightsByElement.get(elementId);
        if (newHeight > maxHeight || elements.size() == 1) {
            idOfHighestElement = elementId;
            maxHeight = newHeight;
        }
    }

    /**
     * return number of elements a hill is made of.
     *
     * @return number of elements
     */
    public int numberOfElements() {
        return elements.size();
    }
}
