import net.sourceforge.jswarm_pso.*;

public class testPSO {
	public static void main(String[] args) {
		Swarm swarm = new Swarm(10
		, new MyParticle()
		, new MyFitnessFunction());

		swarm.setMaxPosition(1);
		swarm.setMinPosition(0);
		// Optimize a few times
		for( int i = 0; i < 200; i++ ) swarm.evolve();
		// Print en results
		System.out.println(swarm.toStringStats());

		for (Particle p : swarm) {
			System.out.println(p.toString());
		}
	}
}