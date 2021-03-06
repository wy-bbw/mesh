package de.xibix.mesh.kallies.io;

import de.xibix.mesh.kallies.entities.NeighbourRegister;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelCreatorTest {
    @Test
    public void testCreateNeighbourRegistryContainsNeighbours() throws IOException {
        String filename = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("mesh.json")).getFile();
        ModelCreator modelCreator = new ModelCreator(filename);
        NeighbourRegister registry = modelCreator.createNeighbourRegister();
        Set<Integer> neighbours = registry.neighbourIds(0);
        assertTrue(neighbours.contains(1));
        assertTrue(neighbours.contains(2));
        assertTrue(neighbours.contains(3));
        assertTrue(neighbours.contains(20));
        assertTrue(neighbours.contains(22));
        assertTrue(neighbours.contains(23));

    }

    @Test
    public void testDoesNotContainSelf() throws IOException {
        String filename = Thread.currentThread().getContextClassLoader().getResource("mesh.json").getFile();
        ModelCreator modelCreator = new ModelCreator(filename);
        NeighbourRegister registry = modelCreator.createNeighbourRegister();
        final int elementId = 0;
        Set<Integer> neighbours = registry.neighbourIds(elementId);
        assertFalse(neighbours.contains(elementId));
    }
}
