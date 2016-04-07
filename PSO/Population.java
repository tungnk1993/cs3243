import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Population {

    Individual[] individuals;
    int currentPopulation;
    double fittestValue = 0.0;
    int fittestIndex = -1;

    public Population(int populationSize, boolean initialise) {
        individuals = new Individual[populationSize];

        // Initialise population
        currentPopulation = 0;
        if (initialise) {
            for (int i = 0; i < Config.POPULATION; i++) {
                Individual newIndividual = new Individual();
                newIndividual.generateIndividual();
                addIndividual(newIndividual);
            }
        }
    }

    public void calculateAllFitness() {
        for (int i = 0; i < currentPopulation; i++)
            individuals[i].evaluateFitness();
        findFittest();
    }

    public void calculateAllFitnessParallel() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < currentPopulation; i++) {
            executor.execute(individuals[i]);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {}
        
        
        // need all parallel tasks to finish before this
        findFittest();
        
    }

    public void findFittest() {
        double bestValue = 0.0;
        int bestIndex = -1;
        
        for (int i = 0; i < currentPopulation; i++) {
            if (individuals[i].getFitness() >= bestValue) {
                bestValue = individuals[i].getFitness();
                bestIndex = i;
            }
        }
        fittestValue = bestValue;
        fittestIndex = bestIndex;
    }

    public Individual getFittest() {
        return individuals[fittestIndex];
    }

    public int size() {
        return currentPopulation;
    }

    public void addIndividual(Individual newGuy) {
        individuals[currentPopulation] = newGuy;
        currentPopulation++;
    }

    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public void setIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }

    public void leaveHistory(int generationCount) {
        System.out.println("Generation = " + generationCount + " | Size = " + currentPopulation + " | Fittest = " + fittestValue);
        File file = new File("result/" + Config.outputFile);

        try {
            PrintWriter writer = new PrintWriter(file);
            // Config details
            writer.println("POPULATION = " + Config.POPULATION);
            writer.println("GAMES = " + Config.GAMES);
            writer.println("Crossover = " + Config.crossoverRate);
            writer.println("Mutation = " + Config.mutationRate);
            writer.println("TournamentSize = " + Config.tournamentSize);
            writer.println("tournamentChance = " + Config.tournamentChance);
            writer.println("elitism = " + Config.elitism);

            // Generation
            writer.println("GENERATION = " + generationCount + " | Fittest = " + fittestValue);
            for (int i = 0; i < currentPopulation; i++)
                if (individuals[i].getFitness() >= fittestValue * Config.LEADER_CUTOFF)
                {
                    writer.print(individuals[i].getFitness() + " ");
                    for (int j = 0; j < Config.NUM_OF_FEATURES; j++)
                        writer.print(individuals[i].getGene(j) + " ");
                    writer.println();
                }                
            writer.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}