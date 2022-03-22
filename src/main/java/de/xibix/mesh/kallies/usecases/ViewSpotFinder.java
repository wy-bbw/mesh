package de.xibix.mesh.kallies.usecases;

import de.xibix.mesh.kallies.entities.Hill;
import de.xibix.mesh.kallies.entities.NeighbourRegister;
import de.xibix.mesh.kallies.io.ModelCreator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * finds viewspots
 */
public class ViewSpotFinder {
    /**
     * finds viewspots on a mesh.
     *
     * @param filename name of json file
     * @param N        number of viewspots to find
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
        for (int i = 0; i < N && i < listOfHills.size(); ++i) {
            JSONObject hillObject = new JSONObject();
            hillObject.put("element_id", listOfHills.get(i).getIdOfHighestElement());
            hillObject.put("value", listOfHills.get(i).getMaxHeight());
            viewSpots.put(hillObject);
        }
        return viewSpots;
    }

    public static void main(String... args) {
        if (args.length != 2) {
            System.err.println("require 2 arguments, got " + args.length);
            return;
        }

        String filename = args[0];

        if (!(new File(filename).exists())) {
            System.err.println("file '" + filename + "' does not exist");
            return;
        }

        int N = 0;
        try {
            N = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("second argument must be an Integer. Could not be parsed");
            return;
        }

        if (N < 0) {
            System.err.println("supplied number must be positive");
            return;
        }

        ViewSpotFinder instance = new ViewSpotFinder();

        try {
            JSONArray viewspots = instance.findViewspots(filename, N);
            String output = viewspots.toString(4);
            System.out.println(output);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
