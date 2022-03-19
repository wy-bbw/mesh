package de.xibix.mesh.kallies.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Element {
    private final String id;
    private final Node[] vertices;

    public Element(String id, Node... nodes) {
        if (id == null || nodes == null) {
            throw new NullPointerException("Neither id nor nodes must be null");
        }
        if (nodes.length != 3) {
            throw new IllegalArgumentException("an element has 3 limiting vertices. passed " + nodes.length + " vertices");
        }
        this.id = id;
        vertices = new Node[]{nodes[0], nodes[1], nodes[2]};
    }
}
