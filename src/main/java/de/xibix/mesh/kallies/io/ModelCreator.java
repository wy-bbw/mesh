package de.xibix.mesh.kallies.io;

import de.xibix.mesh.kallies.entities.NeighbourRegister;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * helper class for handling json file input.
 */
public class ModelCreator {
    private final JSONArray elements;
    private final JSONArray values;

    /**
     * Ctor.
     *
     * @param filename name of json file
     * @throws IOException io exception
     */
    public ModelCreator(final String filename) throws IOException {
        try (InputStream is = new FileInputStream(filename)) {
            final JSONTokener tokener = new JSONTokener(is);
            final JSONObject json = new JSONObject(tokener);
            elements = json.getJSONArray("elements");
            values = json.getJSONArray("values");
        }
    }

    /**
     * create neighbour registry for finding neighbouring elements by element id.
     *
     * @return neighbour registry.
     */
    public NeighbourRegister createNeighbourRegister() {
        Map<Integer, Set<Integer>> nodesByElement = new HashMap<>();
        Map<Integer, Set<Integer>> elementsByNode = new HashMap<>();
        for (int i = 0; i < elements.length(); ++i) {
            JSONObject nodeInfo = elements.getJSONObject(i);
            int elementId = nodeInfo.getInt("id");
            JSONArray nodeIds = nodeInfo.getJSONArray("nodes");
            for (int j = 0; j < nodeIds.length(); ++j) {
                int nodeId = nodeIds.getInt(j);
                insertIntoMap(elementId, nodeId, nodesByElement);
                insertIntoMap(nodeId, elementId, elementsByNode);
            }
        }
        return new NeighbourRegister(elementsByNode, nodesByElement);
    }

    /**
     * create a map element id -> value;
     *
     * @return map with element id as key and value as value.
     */
    public Map<Integer, Double> createHeightRegister() {
        Map<Integer, Double> register = new HashMap<>();
        for (int i = 0; i < values.length(); ++i) {
            JSONObject entry = values.getJSONObject(i);
            Integer elementId = entry.getInt("element_id");
            Double height = entry.getDouble("value");
            register.put(elementId, height);
        }
        return register;
    }

    private void insertIntoMap(int key, int value, Map<Integer, Set<Integer>> map) {
        if (map.containsKey(key)) {
            Set<Integer> existingEntries = map.get(key);
            existingEntries.add(value);
        } else {
            Set<Integer> newList = new HashSet<>();
            newList.add(value);
            map.put(key, newList);
        }
    }
}
