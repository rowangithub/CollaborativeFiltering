package personalrank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import rating.Record;

public class LinkGraph {

	ArrayList<HashMap<Integer, Float>> graph;

	Manager userManager;

	public LinkGraph(DataReader reader) {
		userManager = new Manager(reader);

		int userItemNum = userManager.totalUserItem();

		graph = new ArrayList<HashMap<Integer, Float>>();
		for (int i = 0; i < userItemNum; i++) {
			graph.add(new HashMap<Integer, Float>());
		}

		reader.reset();
		Record record;
		int u;
		int i;
		float p;
		while (reader.hasNext()) {
			record = reader.getNext();
			u = userManager.getUserInternalID(record.getUser());
			i = userManager.getItemInternalID(record.getItem());
			p = (float) record.getPref();
			graph.get(u).put(i, p);
			graph.get(i).put(u, p);
		}

	}

	public void personalRank(int userOriginID) {
		int userInternalID = userManager.getUserInternalID(--userOriginID);

		float alpha = (float) 0.8;
		ArrayList<Item> sorted = personalRank(userInternalID, alpha, 20);

		int k = 100;
		for (int i = 0; i < k; i++) {
			Item item = sorted.get(i);
			int itemID = userManager.getOriginID(item.id);
			float w = item.weight;
			System.out.println((itemID + 1) + ":" + w);
		}

	}

	public ArrayList<Item> personalRank(int userInternalID, float alpha,
			int maxIters) {
		int userItemNum = userManager.totalUserItem();
		float[] rank = new float[userItemNum];
		for (int i = 0; i < userItemNum; i++) {
			rank[i] = 0;
		}
		rank[userInternalID] = 1;

		float[] rankCache;
		HashMap<Integer, Float> edges;
		Iterator<Entry<Integer, Float>> edgeIter;
		Entry<Integer, Float> edge;
		int j;
		for (int iter = 0; iter < maxIters; iter++) {
			rankCache = new float[userItemNum];
			for (int i = 0; i < userItemNum; i++) {
				rankCache[i] = 0;
			}

			for (int i = 0; i < graph.size(); i++) {
				edges = graph.get(i);
				edgeIter = edges.entrySet().iterator();
				while (edgeIter.hasNext()) {
					edge = edgeIter.next();
					j = edge.getKey();
					rankCache[j] += alpha * rank[i] / (edges.size() * 1.0);
					if (j == userInternalID) {
						rankCache[j] += 1.0 - alpha;
					}

				}

			}

			rank = rankCache;

		}

		ArrayList<Item> candidates = new ArrayList<Item>();
		for (int i = userManager.getUserMaxIternalID() + 1; i < rank.length; i++) {
			candidates.add(new Item(i, rank[i]));
		}

		Collections.sort(candidates);

		return candidates;

	}

	public static void main(String[] args) throws IOException {
		DataReader reader = new DataReader("dataset/ua.base");
		LinkGraph graph = new LinkGraph(reader);
		long startMili = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			System.out.println("------------------------------");
			graph.personalRank(1);
		}

		long endMili = System.currentTimeMillis();
		System.out.println("recommendation runs in "
				+ ((endMili - startMili) / (double) 1000) + "s");

	}

}
