package de.xibix.mesh.kallies.entities;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HillTest {
    @Test
    public void testGetMaxHeightSingleEntry() {
        Map<Integer, Double> heightRegister = new HashMap<>();
        heightRegister.put(0, 1.0);
        heightRegister.put(1, 2.0);
        Hill hill = new Hill(heightRegister);
        hill.addElement(0);
        assertEquals(1.0, hill.getMaxHeight());
    }

    @Test
    public void testGetMaxHeightDoubleEntryLowestFirst() {
        Map<Integer, Double> heightRegister = new HashMap<>();
        heightRegister.put(0, 1.0);
        heightRegister.put(1, 2.0);
        Hill hill = new Hill(heightRegister);
        hill.addElement(0);
        hill.addElement(1);
        assertEquals(2.0, hill.getMaxHeight());
    }

    @Test
    public void testGetMaxHeightDoubleEntryHighestFirst() {
        Map<Integer, Double> heightRegister = new HashMap<>();
        heightRegister.put(0, 1.0);
        heightRegister.put(1, 2.0);
        Hill hill = new Hill(heightRegister);
        hill.addElement(1);
        hill.addElement(0);
        assertEquals(2.0, hill.getMaxHeight());
    }

    @Test
    public void testNumberOfElements() {
        Map<Integer, Double> heightRegister = new HashMap<>();
        heightRegister.put(0, 1.0);
        heightRegister.put(1, 2.0);
        Hill hill = new Hill(heightRegister);
        hill.addElement(1);
        hill.addElement(0);
        assertEquals(2, hill.numberOfElements());
    }
}
