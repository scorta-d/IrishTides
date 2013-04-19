package dz.tide;

public class TideInfo {
	String place;
	String firstLowTime;
	String firstHighTime;
	String firstLowDepth;
	String firstHighDepth;

	String secondLowTime;
	String secondHighTime;
	String secondLowDepth;
	String secondHighDepth;

	public String toString() {
		return (""//
				+ v(place) //
				+ v(firstLowTime) //
				+ v(firstHighTime) //
				+ v(secondLowTime) //
				+ v(secondHighTime) //
				+ v(firstLowDepth) //
				+ v(firstHighDepth) //
				+ v(secondLowDepth) //
		+ v(secondHighDepth) //
		);
	}

	private String v(String s) {
		String result = " ";
		if (s != null) {
			result = s + result;
		}
		return result;
	}
}
