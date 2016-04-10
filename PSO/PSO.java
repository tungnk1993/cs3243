import net.sourceforge.jswarm_pso.*;

import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PSO {

	public static void main(String[] args) {
		
		Swarm swarm = new Swarm(Config.NUMBER_OF_PARTICLES
							, new MyParticle()
							, new MyFitnessFunction());
		
		/*
		SwarmRepulsive swarm = new SwarmRepulsive(Config.NUMBER_OF_PARTICLES
									, new MyParticle()
									, new MyFitnessFunction());
		*/
		swarm.setInertia(Config.INERTIA);
		swarm.setParticleIncrement(Config.PARTICLE_INCREMENT);
		swarm.setGlobalIncrement(Config.GLOBAL_INCREMENT);
		
		swarm.setMaxPosition(Config.MAX_POSITION);
		swarm.setMinPosition(Config.MIN_POSITION);
		

		// Optimize a few times
		int countGeneration = 0;
		while (countGeneration < 100000) {
			countGeneration++;
			swarm.evolve();

			leaveHistory(countGeneration, swarm.toStringStats());
			if (Config.DEBUG) {
				System.out.println("Generation " + countGeneration + " stats");
				System.out.println(swarm.toStringStats());	
			}
			
		}
		
		// Print en results
		//System.out.println(swarm.toStringStats());
	}

	public static void leaveHistory(int countGeneration, String history) {
        File file = new File("result/" + Config.outputFile);

        try {
            PrintWriter writer = new PrintWriter(file);
            // Config details
            writer.println("PARTICLE COUNT = " + Config.NUMBER_OF_PARTICLES);
            writer.println("FEATURES = " + Config.NUM_OF_FEATURES);
            writer.println(Arrays.toString(Config.features));
            writer.println("GAMES = " + Config.GAMES);
            writer.println("REPULSIVE = " + Config.USE_REPULSIVE_ALGO);
            writer.println("MAX_POSITION = " + Config.MAX_POSITION);
            writer.println("MIN_POSITION = " + Config.MIN_POSITION);            

            // Generation
            writer.println("GENERATION = " + countGeneration);
            writer.println(history);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            writer.println("Last updated: " + dateFormat.format(date));
            writer.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
	}
}