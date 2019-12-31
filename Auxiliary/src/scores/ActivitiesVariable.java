package scores;

import java.util.Random;

public class ActivitiesVariable implements Activity {
	private double bumpAmount;
	private double decayFactor;
	private double[] activities;
	private double activityUpperBound = Math.pow(10, 20);

	public ActivitiesVariable(double bumpAmount, double decayFactor, int size) {
		//Catch illegal arguments
		if(size <= 0){
			throw new IllegalArgumentException("Size <= 0 activities not allowed.");
		}
		if(decayFactor <= 0) {
			throw new IllegalArgumentException("Decay factor negative or zero.");
		}
		if(bumpAmount <= 0){
			throw new IllegalArgumentException("Bump factor negative or zero.");
		}
		this.bumpAmount = bumpAmount;
		this.decayFactor = decayFactor;

		// Initialize score array and set initial random scores for each
		// variable between 0 and 10
		activities = new double[size];
		for (int i = 0; i < activities.length; i++) {
			Random rand = new Random();
			int x = rand.nextInt(10);
			activities[i] = x;
		}
	}

	public ActivitiesVariable(int size) {
		// Default bump constant and decay factor used in MiniSAT
		this(100,0.95,size);
	}

	@Override
	public int size() {
		return 0;
	}

	public void bump(int varIndex) {
		activities[varIndex] += bumpAmount;
		// Check upper bound condition for this variable
		if (activities[varIndex] > activityUpperBound) {
			rescaleActivities();
		}
	}

	public void decayAll() {
		bumpAmount *= 1.0 / decayFactor;
	}

	public double getActivity(int i) {
		return activities[i];
	}

	private void rescaleActivities() {
		// If variables do not decay, rescale will not apply.
		// Overflow may occur
		if (decayFactor == 1) {
			return;
		}

		// Rescale all activities
		for (int i = 0; i < activities.length; i++) {
			activities[i] *= 1.0 / activityUpperBound;
		}
		// Rescale bump amount
		bumpAmount *= 1.0 / activityUpperBound;
	}

	@Override
	public String toString(){
		String r = "";
		for (int i = 0; i < activities.length; i++) {
			r += i+" -> "+activities[i]+" || ";
		}

		return r.substring(0,r.length()-4);
	}
}
