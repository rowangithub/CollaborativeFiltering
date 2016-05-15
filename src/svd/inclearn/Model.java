package svd.inclearn;

import svd.SVDModel;

public class Model extends SVDModel {

	double[][] userdiffs;
	double[][] itemdiffs;
	double[][] ratingMatrix;

	public Model(int un, int in, int r, double minPref, double maxPref,
			double[][] ratings) {
		super(un, in, r, minPref, maxPref);

		userdiffs = new double[userNum][rank];
		itemdiffs = new double[itemNum][rank];
		ratingMatrix = ratings;

	}

	public void clearDiffs() {
		for (int u = 0; u < userNum; u++) {
			for (int r = 0; r < rank; r++) {
				userdiffs[u][r] = 0;
			}
		}

		for (int i = 0; i < itemNum; i++) {
			for (int r = 0; r < rank; r++) {
				itemdiffs[i][r] = 0;
			}
		}

	}

	public double update() {
		clearDiffs();
		double sum = 0;
		int count = 0;
		for (int u = 0; u < userNum; u++) {
			for (int i = 0; i < itemNum; i++) {

				if (ratingMatrix[u][i] == 0) {
				} else {
					double predicted = predict(u, i);
					double error = ratingMatrix[u][i] - predicted;
					sum = sum + error * error;
					count++;
					for (int r = 0; r < rank; r++) {
						userdiffs[u][r] += (error * itemVectors[i][r] - k
								* userVectors[u][r]);//
						itemdiffs[i][r] += (error * userVectors[u][r] - k
								* itemVectors[i][r]);//
					}
				}
			}
		}

		for (int u = 0; u < userNum; u++) {
			for (int r = 0; r < rank; r++) {
				userVectors[u][r] = userVectors[u][r] + userdiffs[u][r] * alpha;
			}
		}

		for (int i = 0; i < itemNum; i++) {
			for (int r = 0; r < rank; r++) {
				itemVectors[i][r] = itemVectors[i][r] + itemdiffs[i][r] * alpha;
			}
		}

		double rmse = Math.sqrt(sum / count);
		return rmse;
	}

}
