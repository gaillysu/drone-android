package com.dayton.drone.view;

import com.dayton.drone.viewmodel.ChooseCityViewModel;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<ChooseCityViewModel> {

	public int compare(ChooseCityViewModel o1, ChooseCityViewModel o2) {
		if (o1.getSortLetter().equals("@")
				|| o2.getSortLetter().equals("#")) {
			return -1;
		} else if (o1.getSortLetter().equals("#")
				|| o2.getSortLetter().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetter().compareTo(o2.getSortLetter());
		}
	}

}
