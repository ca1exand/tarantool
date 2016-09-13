package de.uni_koeln.webapps.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Utils {

	public static List<String> txtToList(String string) throws FileNotFoundException {
		List<String> stringList = new ArrayList<>();
		Scanner scan = new Scanner(new File(string));
		while (scan.hasNext()) {
			stringList.add(scan.next());
		}
		scan.close();
		return stringList;
	}

	public static Map<String, Float> sortByValue(Map<String, Float> resultMap) {
		List<Map.Entry<String, Float>> list = new LinkedList<Map.Entry<String, Float>>(resultMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
			public int compare(Map.Entry<String, Float> o2, Map.Entry<String, Float> o1) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<String, Float> result = new LinkedHashMap<String, Float>();
		for (Map.Entry<String, Float> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
