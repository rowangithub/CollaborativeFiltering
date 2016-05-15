package svd.batched;

import svd.SVDModel;

public class Model extends SVDModel {

	public Model(int un, int in, int r, double minPref, double maxPref) {
		super(un, in, r, minPref, maxPref);
	}

	public double update(int user, int item, double pref) {
		double predicted = predict(user, item);
		double error = pref - predicted;

		double[] userVecotr = new double[rank];
		for (int r = 0; r < rank; r++) {
			userVecotr[r] = userVectors[user][r]
					+ (error * itemVectors[item][r] - k * userVectors[user][r])
					* alpha;
			itemVectors[item][r] = itemVectors[item][r]
					+ (error * userVectors[user][r] - k * itemVectors[item][r])
					* alpha;

			userVectors[user][r] = userVecotr[r];
		}

		return error * error;
	}

}
