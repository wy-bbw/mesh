package de.xibix.mesh.kallies.model;

import de.xibix.mesh.kallies.model.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ElementTest {
    @Test
    void testCtorFailOnNullId() {
        Node[] nodes = new Node[]{new Node("foo", 1, 2), new Node("bar", 1, 2), new Node("foobar", 1, 2)};
        assertThrows(NullPointerException.class, () -> {new Element(null, nodes);});
    }

    @Test
    void testCtorFailOnNullNodes() {
        assertThrows(NullPointerException.class, () -> {new Element("foo", null);});
    }

    @Test
    void testCtorFailOnInvalidNumberOfNodes() {
        Node[] nodes = new Node[]{new Node("foo", 1, 2), new Node("bar", 1, 2)};
        assertThrows(IllegalArgumentException.class, () -> {new Element("spam", nodes);});
    }

    @Test
    void testCtorPass() {
        Node[] nodes = new Node[]{new Node("foo", 1, 2), new Node("bar", 1, 2), new Node("foobar", 1, 2)};
        assertDoesNotThrow(() -> {new Element("spam", nodes);});
    }
}
