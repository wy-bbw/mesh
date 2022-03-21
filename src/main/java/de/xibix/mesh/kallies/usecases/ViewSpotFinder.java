package de.xibix.mesh.kallies.usecases;

import de.xibix.mesh.kallies.entities.Hill;
import de.xibix.mesh.kallies.entities.NeighbourRegister;
import de.xibix.mesh.kallies.io.ModelCreator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * finds viewspots
 */
public class ViewSpotFinder {
    /**
     * finds viewspots on a mesh.
     * @param filename name of json file
     * @param N number of viewspots to find
     * @return json object with all viewspots
     * @throws IOException
     */
    public JSONArray findViewspots(final String filename, final int N) throws IOException {
        ModelCreator creator = new ModelCreator(filename);
        Map<Integer, Double> heightRegister = creator.createHeightRegister();
        NeighbourRegister neighbourRegister = creator.createNeighbourRegister();
        Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegister);
        List<Hill> listOfHills = hillfinder.findAll();
        listOfHills.sort(Hill.HIGHEST_HILL_FIRST);
        if (listOfHills.size() < N) {
            System.err.println("specified " + N + " viewspots, only found " + listOfHills.size() + ". Returning all found viewspots.");
        }
        JSONArray viewSpots = new JSONArray();
        for (Hill hill : listOfHills) {
            JSONObject hillObject = new JSONObject();
            hillObject.put("element_id", hill.getIdOfHighestElement());
            hillObject.put("value", hill.getMaxHeight());
            viewSpots.put(hillObject);
        }
        return viewSpots;
    }
}
