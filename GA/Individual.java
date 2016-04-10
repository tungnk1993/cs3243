public class Individual implements Runnable {
    private double[] genes = new double[Config.NUM_OF_FEATURES];
    private AgentSkeleton agent;
    private double averageFitness = 0.0;
    
    public Individual() {}

    public Individual(Individual duplicate) {
        for (int i = 0; i < Config.NUM_OF_FEATURES; i++)
            genes[i] = duplicate.getGene(i);
        agent = new AgentSkeleton();
    }

    public int size(){
    	return Config.NUM_OF_FEATURES;
    }
    
    public void printGene() {
        System.out.print("Feature: ");
        for (int i = 0; i < Config.NUM_OF_FEATURES; i++) {
            System.out.print(genes[i]);
            System.out.print(" ");
        }
        System.out.println();
    }

    public double[] getGenes() {
        return genes;
    }
    
    public double getGene(int i){
    	return genes[i];
    }
    
    public void setGene(int index, double val){
    	genes[index] = val;
    }
    
    // Create a random individual
    public void generateIndividual() {
        for (int i = 0; i < Config.NUM_OF_FEATURES; i++) {
            double randomVal = randomInitialValue();
            genes[i] = randomVal;
        }
        //normalizeGenes();
    }
    
    public double getFitness() {
        return averageFitness;
    }

    public void evaluateFitness() {
        if (agent == null) agent = new AgentSkeleton();
        agent.setWeight(genes);
        double sumFitness = 0.0;
        for (int i = 0; i < Config.GAMES; i++) {
            sumFitness += (double)agent.runSimulation();
        }
        averageFitness = sumFitness / (double)Config.GAMES;
    }

    @Override
    public void run() {
        if (agent == null) agent = new AgentSkeleton();
        agent.setWeight(genes);
        double sumFitness = 0.0;
        for (int i = 0; i < Config.GAMES; i++) {
            sumFitness += (double)agent.runSimulation();
        }
        averageFitness = sumFitness / (double)Config.GAMES;
    }

    public double randomInitialValue() {
        return Math.random() * (Config.VALUE_RANGE * 2) - Config.VALUE_RANGE;
    }

    public void normalizeGenes() {
        double s = 0.0;
        for (int i = 0; i < Config.NUM_OF_FEATURES; i++)
            s += (genes[i]*genes[i]);
        s = Math.sqrt(s);
        for (int i = 0; i < Config.NUM_OF_FEATURES; i++) genes[i] /= s;
    }

    public void mutateOperation(){
        mutate();
    }

    public void mutate() {
        double pMutate = Math.random();
        if (pMutate <= Config.mutationRate) {
            for (int i = 0; i < Config.NUM_OF_FEATURES; i++) {
                genes[i] += randomMutation();
            }
        }
        //normalizeGenes();
    }

    public void pointMutate() {
        double pMutate = Math.random();
        if (pMutate <= Config.mutationRate) {
            int chosenIndex = (int)(Math.random() * Config.NUM_OF_FEATURES);
            genes[chosenIndex] += randomMutation();
        }
        //normalizeGenes();
    }

    private double randomMutation() {
        return Math.random() * (Config.VALUE_RANGE * 0.1) - Config.VALUE_RANGE * 0.05; // -0.2 -> 0.2
    }
}