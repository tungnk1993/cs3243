import java.util.Arrays;

public class GeneticAlgorithmMain {

    public static void main(String[] args) {

        // Create an initial population
        Population currentPopulation = new Population(Config.POPULATION, true);
        
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        double currentBest = 0.0;
        double bestGenes[] = new double[Config.NUM_OF_FEATURES];

        while (generationCount < Config.TEST_THRESHOLD) {
            generationCount++;
            //currentPopulation.calculateAllFitness();
            currentPopulation.calculateAllFitnessParallel();

            // Keep track of best dudes from this population
            if (currentPopulation.getFittest().getFitness() > currentBest) {
                currentBest = currentPopulation.getFittest().getFitness();
                bestGenes = currentPopulation.getFittest().getGenes();
            }
            currentPopulation.leaveHistory(currentBest, bestGenes, generationCount);
            
            if (Config.DEBUG) {
                System.out.println("All time best = " + currentBest);
                System.out.println(Arrays.toString(bestGenes));
            }
            

            // Perform evolutionary stuff
            currentPopulation = Evolution.evolvePopulation(currentPopulation);

        }
    }
}