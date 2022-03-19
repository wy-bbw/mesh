package de.xibix.mesh.kallies.io;

import de.xibix.mesh.kallies.model.NeighbourRegistry;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ModelCreator {
    private final JSONArray nodes;
    private final JSONArray elements;
    private final JSONArray values;

    public ModelCreator(final String filename) throws IOException {
        try (InputStream is = new FileInputStream(filename)) {
            final JSONTokener tokener = new JSONTokener(is);
            final JSONObject json = new JSONObject(tokener);
            nodes = json.getJSONArray("nodes");
            elements = json.getJSONArray("elements");
            values = json.getJSONArray("values");

        }
    }

    public NeighbourRegistry createNeighbourRegistry() {
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
        return new NeighbourRegistry(elementsByNode, nodesByElement);
    }

    private void insertIntoMap(int key, int value, Map<Integer, Set<Integer>> map) {
        if (map.containsKey(key)) {
            Set<Integer> existingEntries = map.get(key);
            if (!existingEntries.contains(value)) {
                existingEntries.add(value);
            }
        } else {
            Set<Integer> newList = new HashSet<>();
            newList.add(value);
            map.put(key, newList);
        }
    }
}
