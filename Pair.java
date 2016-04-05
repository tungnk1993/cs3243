public class Pair implements Comparable<Pair> {
	int index;
	double value;

	public Pair(int _index, double _value) {
		index = _index;
		value = _value;
	}

	public int getIndex() {
		return index;
	}

	public double getValue() {
		return value;
	}

	@Override
	public int compareTo(Pair anotherPair) {
		double c = anotherPair.getValue() - this.value;
		if (c > 0.0) return 1;
		else return -1;
	}
}