package svd.inclearn;

import java.io.IOException;

import rating.Reader;
import rating.Record;
import svd.DataReader;

public class Recommender {

	Model model;
	Reader trainData;
	String trainFile;

	int userNum;
	int itemNum;

	public Recommender(String trainFilePath) throws IOException {
		trainFile = trainFilePath;
		trainData = new DataReader(trainFile);
		userNum = trainData.getUserNum();
		itemNum = trainData.getItemNum();
		double minPref = trainData.getMinPref();
		double maxPref = trainData.getMaxPref();

		int rank = 150;

		double[][] ratingMatrix = new double[userNum][itemNum];

		for (int u = 0; u < userNum; u++) {
			for (int i = 0; i < itemNum; i++) {
				ratingMatrix[u][i] = 0;
			}
		}

		while (trainData.hasNext()) {
			Record rating = trainData.getNext();
			int u = rating.getUser();
			int i = rating.getItem();
			double p = rating.getPref();
			ratingMatrix[u][i] = p;
		}

		model = new Model(userNum, itemNum, rank, minPref, maxPref,
				ratingMatrix);

	}

	public void train(int maxIter) {
		double lastRmse = Double.MAX_VALUE;
		for (int iter = 0; iter < maxIter; iter++) {

			double rmse = model.update();
			if (lastRmse < rmse)
				break;
			lastRmse = rmse;
		}

	}

	public void test(String testFile) throws IOException {
		Reader testData = new DataReader(testFile);
		double sum = 0.0;
		int n = 0;
		while (testData.hasNext()) {
			Record rating = testData.getNext();
			int u = rating.getUser();
			int i = rating.getItem();
			double p = rating.getPref();
			try {
				double predicted = model.predict(u, i);
				double error = (p - predicted) * (p - predicted);
				sum += error;
				n++;
			} catch (Exception e) {
				continue;
			}
		}
		double rmse = Math.sqrt(sum / n);
		System.out.println("test rmse:" + rmse);

	}

	public static void main(String[] args) throws IOException {
		System.out.println("Running 5-fold cross-validtion ...");

		System.out.println("---------------------------------------");
		long starttime = System.currentTimeMillis();
		Recommender trainer1 = new Recommender("dataset/u1.base");
		trainer1.train(1000);
		trainer1.test("dataset/u1.test");

		System.out.println("---------------------------------------");

		Recommender trainer2 = new Recommender("dataset/u2.base");
		trainer2.train(1000);
		trainer2.test("dataset/u2.test");

		System.out.println("---------------------------------------");

		Recommender trainer3 = new Recommender("dataset/u3.base");
		trainer3.train(1000);
		trainer3.test("dataset/u3.test");

		System.out.println("---------------------------------------");

		Recommender trainer4 = new Recommender("dataset/u4.base");
		trainer4.train(1000);
		trainer4.test("dataset/u4.test");

		System.out.println("---------------------------------------");

		Recommender trainer5 = new Recommender("dataset/u5.base");
		trainer5.train(1000);
		trainer5.test("dataset/u5.test");

		System.out.println("---------------------------------------");
		System.out
				.printf("Average execution time \t= %.3fs\n",
						((double) (System.currentTimeMillis() - starttime) / (double) (1000 * 5)));
	}

}
