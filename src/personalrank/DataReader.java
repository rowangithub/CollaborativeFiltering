package personalrank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import rating.Reader;
import rating.Record;

public class DataReader extends Reader {

	LinkedList<Record> records;
	Iterator<Record> iter;
	BufferedReader br;

	public DataReader(String filePath) throws IOException {
		records = new LinkedList<Record>();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(
				new File(filePath))));
		String line;
		while (true) {
			line = br.readLine();
			if (line == null)
				break;

			String[] fields = line.split("\t");
			int u = Integer.parseInt(fields[0]) - 1;
			int i = Integer.parseInt(fields[1]) - 1;

			int score = Integer.parseInt(fields[1]);
			if (score >= 3)
				records.push(new Record(u, i, 1));
		}
		br.close();

	}

	@Override
	public double getMinPref() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double getMaxPref() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return iter.hasNext();
	}

	@Override
	public Record getNext() {
		// TODO Auto-generated method stub
		return iter.next();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		iter = records.iterator();
	}

	// 以下两个方法是无效方法
	@Override
	public int getUserNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemNum() {
		// TODO Auto-generated method stub
		return 0;
	}

}
