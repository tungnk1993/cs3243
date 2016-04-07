import net.sourceforge.jswarm_pso.FitnessFunction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.CloneNotSupportedException;

public class MyFitnessFunction extends FitnessFunction {
	public double evaluate(double position[]) {
		
		AgentSkeleton[] agentList = new AgentSkeleton[Config.GAMES];
		for (int i = 0; i < Config.GAMES; i++)
			agentList[i] = new AgentSkeleton(position);

        double sumFitness = 0.0;
        ExecutorService executor = Executors.newFixedThreadPool(Config.THREADS);
        for (int i = 0; i < Config.GAMES; i++) {
            //sumFitness += (double)agent.runSimulation();
            executor.execute(agentList[i]);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {}
		
		for (int i = 0; i < Config.GAMES; i++) {
			sumFitness += (double)agentList[i].getSimulationFitness();
		}
		
		/*
		AgentSkeleton agent = new AgentSkeleton(position);
		double sumFitness = 0.0;
		for (int i = 0; i < Config.GAMES; i++) {
			sumFitness += (double)agent.runSimulation();
		}
		*/	
        double averageFitness = sumFitness / (double)Config.GAMES;
        return averageFitness;
	}
}