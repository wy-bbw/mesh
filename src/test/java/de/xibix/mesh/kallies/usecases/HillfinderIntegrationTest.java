package de.xibix.mesh.kallies.usecases;

import de.xibix.mesh.kallies.entities.Hill;
import de.xibix.mesh.kallies.entities.NeighbourRegister;
import de.xibix.mesh.kallies.io.ModelCreator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    // used for exporting plotting data in plot.py
//    @Test
//    public void exportHillsAsJson() throws  IOException {
//        String filename = Thread.currentThread().getContextClassLoader().getResource("mesh_x_sin_cos_20000.json").getFile();
//        ModelCreator modelCreator = new ModelCreator(filename);
//        NeighbourRegister neighbourRegister = modelCreator.createNeighbourRegister();
//        Map<Integer, Double> heightRegister = modelCreator.createHeightRegister();
//        Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegister);
//        List<Hill> hills = hillfinder.findAll();
//        hills.sort(Hill.HIGHEST_HILL_FIRST);
//        JSONArray exportHills = new JSONArray();
//        int id = hills.size();
//        for (Hill hill : hills) {
//            JSONObject obj = new JSONObject();
//            obj.put("hillId", id--);
//            obj.put("elementIds", hill.elementIds());
//            obj.put("peak", hill.getIdOfHighestElement());
//            exportHills.put(obj);
//        }
//        FileWriter out = new FileWriter("hills.json");
//        exportHills.write(out);
//        out.flush();
//    }

    @Test
    public void findHillsInSin() throws IOException {
        List<Long> elapsedTimesMsec = new ArrayList<>();
        for (int i = 0; i < 21; ++i) {
            long start = System.currentTimeMillis();
            String filename = Thread.currentThread().getContextClassLoader().getResource("mesh_x_sin_cos_10000.json").getFile();
            ModelCreator modelCreator = new ModelCreator(filename);
            NeighbourRegister neighbourRegister = modelCreator.createNeighbourRegister();
            Map<Integer, Double> heightRegister = modelCreator.createHeightRegister();
            Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegister);
            List<Hill> hills = hillfinder.findAll();
            hills.sort(Hill.HIGHEST_HILL_FIRST);
            long duration = System.currentTimeMillis() - start;
            elapsedTimesMsec.add(duration);
        }
        elapsedTimesMsec.sort((a, b) -> Long.compare(a, b));
        assertTrue(elapsedTimesMsec.get(11) < 500);
    }

    @Test
    public void findHillsInSinLarge() throws IOException {
        List<Long> elapsedTimesMsec = new ArrayList<>();
        for (int i = 0; i < 21; ++i) {
            long start = System.currentTimeMillis();
            String filename = Thread.currentThread().getContextClassLoader().getResource("mesh_x_sin_cos_20000.json").getFile();
            ModelCreator modelCreator = new ModelCreator(filename);
            NeighbourRegister neighbourRegister = modelCreator.createNeighbourRegister();
            Map<Integer, Double> heightRegister = modelCreator.createHeightRegister();
            Hillfinder hillfinder = new Hillfinder(neighbourRegister, heightRegister);
            List<Hill> hills = hillfinder.findAll();
            hills.sort(Hill.HIGHEST_HILL_FIRST);
            long duration = System.currentTimeMillis() - start;
            elapsedTimesMsec.add(duration);
        }
        elapsedTimesMsec.sort((a, b) -> Long.compare(a, b));
        assertTrue(elapsedTimesMsec.get(11) < 1500);
    }
}
