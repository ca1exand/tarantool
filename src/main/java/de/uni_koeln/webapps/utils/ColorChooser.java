package de.uni_koeln.webapps.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ColorChooser {

	private Set<Integer> usedColors = new HashSet<>();

	public ColorChooser() {
		usedColors = new HashSet<>();
	}

	private static int[][] intArray = { { 0, 255, 255 }, { 127, 255, 212 }, { 240, 255, 255 }, { 245, 245, 220 },
			{ 255, 228, 196 }, { 255, 235, 205 }, { 0, 0, 255 }, { 138, 43, 226 }, { 165, 42, 42 }, { 222, 184, 135 },
			{ 95, 158, 160 }, { 127, 255, 0 }, { 210, 105, 30 }, { 255, 127, 80 }, { 100, 149, 237 }, { 255, 248, 220 },
			{ 220, 20, 60 }, { 0, 255, 255 }, { 0, 0, 139 }, { 0, 139, 139 }, { 184, 134, 11 }, { 169, 169, 169 },
			{ 0, 100, 0 }, { 189, 183, 107 }, { 139, 0, 139 }, { 85, 107, 47 }, { 255, 140, 0 }, { 153, 50, 204 },
			{ 139, 0, 0 }, { 233, 150, 122 }, { 143, 188, 143 }, { 72, 61, 139 }, { 47, 79, 79 },
			{ 0, 206, 209 }, { 148, 0, 211 }, { 255, 20, 147 }, { 0, 191, 255 }, { 105, 105, 105 }, { 105, 105, 105 },
			{ 30, 144, 255 }, { 178, 34, 34 }, { 255, 250, 240 }, { 34, 139, 34 }, { 255, 0, 255 }, { 220, 220, 220 },
			{ 248, 248, 255 }, { 255, 215, 0 }, { 218, 165, 32 }, { 128, 128, 128 }, { 128, 128, 128 }, { 0, 128, 0 },
			{ 173, 255, 47 }, { 240, 255, 240 }, { 255, 105, 180 }, { 205, 92, 92 }, { 75, 0, 130 }, { 255, 255, 240 },
			{ 240, 230, 140 }, { 230, 230, 250 }, { 255, 240, 245 }, { 124, 252, 0 }, { 255, 250, 205 },
			{ 173, 216, 230 }, { 240, 128, 128 }, { 224, 255, 255 }, { 250, 250, 210 }, { 211, 211, 211 },
			{ 144, 238, 144 }, { 255, 182, 193 }, { 255, 160, 122 }, { 32, 178, 170 }, { 135, 206, 250 },
			{ 119, 136, 153 }, { 119, 136, 153 }, { 176, 196, 222 }, { 255, 255, 224 }, { 0, 255, 0 }, { 50, 205, 50 },
			{ 250, 240, 230 }, { 255, 0, 255 }, { 128, 0, 0 }, { 102, 205, 170 }, { 0, 0, 205 }, { 186, 85, 211 },
			{ 147, 112, 219 }, { 60, 179, 113 }, { 123, 104, 238 }, { 0, 250, 154 }, { 72, 209, 204 }, { 199, 21, 133 },
			{ 25, 25, 112 }, { 245, 255, 250 }, { 255, 228, 225 }, { 255, 228, 181 }, { 255, 222, 173 }, { 0, 0, 128 },
			{ 253, 245, 230 }, { 128, 128, 0 }, { 107, 142, 35 }, { 255, 165, 0 }, { 255, 69, 0 }, { 218, 112, 214 } };

	public int[] getColor() {
		Integer index = new Random().nextInt(intArray.length);
		return getColor(index);
	}

	private int[] getColor(Integer index) {
		if (usedColors.contains(index)) {
			index = new Random().nextInt(intArray.length);
			return getColor(index);
		} else {
			usedColors.add(index);
			return intArray[index];
		}
	}

}