package rating;

public class Record {

	int user;
	int item;
	double preference;

	public Record(int u, int i, double p) {
		user = u;
		item = i;
		preference = p;
	}

	public int getUser() {
		return user;
	}

	public int getItem() {
		return item;
	}

	public double getPref() {
		return preference;
	}

}
