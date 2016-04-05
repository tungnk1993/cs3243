import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Evolution {
    
    // Evolve a population
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.size(), false);

        // Keep our best individual
        if (Config.elitism) {
            newPopulation.addIndividual(pop.getFittest());
        }

        while (newPopulation.size() < Config.POPULATION) {

            Individual parent1 = tournamentSelection(pop);
            Individual parent2 = tournamentSelection(pop);

            double pCross = Math.random();

            if (pCross <= Config.crossoverRate) {    
                if (parent1.getFitness() > parent2.getFitness())
                    newPopulation.addIndividual(parent1);
                else newPopulation.addIndividual(parent2);

                if (newPopulation.size() < Config.POPULATION) {
                    Individual child = crossover(parent1, parent2);    
                    newPopulation.addIndividual(child);
                }
                
            }
            else {
                parent1.mutateOperation(); parent2.mutateOperation();
                if (parent1.getFitness() > parent2.getFitness())
                    newPopulation.addIndividual(parent1);
                else newPopulation.addIndividual(parent2);

                if (newPopulation.size() < Config.POPULATION) {
                    if (parent1.getFitness() > parent2.getFitness())
                        newPopulation.addIndividual(parent2);
                    else newPopulation.addIndividual(parent1);
                }
                    
            }
        }

        return newPopulation;
    }

    // Crossover individuals
    private static Individual crossover(Individual indiv1, Individual indiv2) {
        Individual child = new Individual();
        for (int i = 0; i < Config.NUM_OF_FEATURES; i++) {
            if (indiv1.getFitness() > indiv2.getFitness()) {
                child.setGene(i, Config.eliteFavor * indiv1.getGene(i) + (1.0 - Config.eliteFavor) * indiv2.getGene(i));
            }  
            else {
                child.setGene(i, Config.eliteFavor * indiv2.getGene(i) + (1.0 - Config.eliteFavor) * indiv1.getGene(i));
            }
        }
        //child.normalizeGenes();
        return child;
    }

    // Mutate an individual


    // Select individuals for crossover
    /*
    private static Individual selection(Population pop) {
        
    }

    private static Individual rouletteSelection(Population pop) {

    }
    */

    private static Individual tournamentSelection(Population pop) {
        ArrayList<Pair> members = new ArrayList<Pair>();
        Random r = new Random();
        while (members.size() < Config.tournamentSize) {
            int chosenIndex = r.nextInt(pop.size());
            Pair newPair = new Pair(chosenIndex, pop.getIndividual(chosenIndex).getFitness());
            members.add(newPair);
        }
        Collections.sort(members);

        while (true) {
            double chance = Config.tournamentChance;
            double modifier = 1.0 - chance;
            for (int i = 0; i < members.size(); i++) {
                double p = Math.random();
                if (p <= chance) {
                    int index = members.get(i).getIndex();
                    return pop.getIndividual(i);
                }
                chance *= modifier;
            }
        }

    }
}