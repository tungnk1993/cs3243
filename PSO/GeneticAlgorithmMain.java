public class GeneticAlgorithmMain {

    public static void main(String[] args) {

        // Create an initial population
        Population currentPopulation = new Population(Config.POPULATION, true);
        
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        double currentBest = 0.0;
        Individual legendaryHero = new Individual();

        while (currentBest < Config.TEST_THRESHOLD) {
            generationCount++;
            //currentPopulation.calculateAllFitness();
            currentPopulation.calculateAllFitnessParallel();

            // Keep track of best dudes from this population
            currentPopulation.leaveHistory(generationCount);
            
            if (currentPopulation.getFittest().getFitness() > currentBest) {
                legendaryHero = currentPopulation.getFittest();
                currentBest = legendaryHero.getFitness();
            }
            System.out.println("All time best = " + currentBest);
            legendaryHero.printGene();

            // Perform evolutionary stuff
            currentPopulation = Evolution.evolvePopulation(currentPopulation);

        }
    }
}