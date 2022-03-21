package de.xibix.mesh.kallies.usecases;

import de.xibix.mesh.kallies.entities.Hill;
import de.xibix.mesh.kallies.entities.NeighbourRegister;
import de.xibix.mesh.kallies.io.ModelCreator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HillfinderIntegrationTest {

    @Test
    public void findHillsInMesh() throws IOException {
        String filename = Thread.currentThread().getContextClassLoader().getResource("mesh.json").getFile();
        ModelCreator modelCreator = new ModelCreator(filename);
        NeighbourRegister neighbourRegister = modelCreator.createNeighbourRegister();
        Map<Integer, Double> heightRegister = modelCreator.createHeightRegister();
        Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegister);
        List<Hill> hills = hillfinder.findAll();
        hills.sort(Hill.HIGHEST_HILL_FIRST);
        assertEquals(153, hills.get(0).getIdOfHighestElement());
        assertEquals(141, hills.get(1).getIdOfHighestElement());
        assertEquals(99, hills.get(2).getIdOfHighestElement());
        assertEquals(87, hills.get(3).getIdOfHighestElement());
    }
    @Test
    public void exportHillsAsJson() throws  IOException {
        String filename = Thread.currentThread().getContextClassLoader().getResource("mesh.json").getFile();
        ModelCreator modelCreator = new ModelCreator(filename);
        NeighbourRegister neighbourRegister = modelCreator.createNeighbourRegister();
        Map<Integer, Double> heightRegister = modelCreator.createHeightRegister();
        Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegister);
        List<Hill> hills = hillfinder.findAll();
        JSONArray exportHills = new JSONArray();
        int id = 0;
        for (Hill hill : hills) {
            JSONObject obj = new JSONObject();
            obj.put("hillId", id++);
            obj.put("elementIds", hill.elementIds());
            exportHills.put(obj);
        }
        FileWriter out = new FileWriter("hills.json");
        exportHills.write(out);
        out.flush();
    }

    @Test
    public void findHillsInSin() throws IOException {
        String filename = Thread.currentThread().getContextClassLoader().getResource("mesh_x_sin_cos_10000.json").getFile();
        ModelCreator modelCreator = new ModelCreator(filename);
        NeighbourRegister neighbourRegister = modelCreator.createNeighbourRegister();
        System.out.println("creating neighbour register");
        Map<Integer, Double> heightRegister = modelCreator.createHeightRegister();
        System.out.println("creating height register");
        Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegister);
        List<Hill> hills = hillfinder.findAll();
        hills.sort(Hill.HIGHEST_HILL_FIRST);
    }

    @Test
    public void findHillsInSinLarge() throws IOException {
        String filename = Thread.currentThread().getContextClassLoader().getResource("mesh_x_sin_cos_20000.json").getFile();
        ModelCreator modelCreator = new ModelCreator(filename);
        NeighbourRegister neighbourRegister = modelCreator.createNeighbourRegister();
        System.out.println("creating neighbour register");
        Map<Integer, Double> heightRegister = modelCreator.createHeightRegister();
        System.out.println("creating height register");
        Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegister);
        List<Hill> hills = hillfinder.findAll();
        hills.sort(Hill.HIGHEST_HILL_FIRST);
    }
}
