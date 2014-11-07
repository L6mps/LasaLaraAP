package com.lasalara.lasalara.backend.structure;

import java.util.List;

/**
 * Class responsible for holding book or chapter progress data.
 * @author Ants-Oskar Mäesalu
 */
public class Progress {
	int current;
	int maximum;
	
	/**
	 * Constructor without any parameters. The current and maximum progress are set to zero.
	 */
	Progress() {
		current = 0;
		maximum = 0;
	}
	
	/**
	 * Constructor. Used when the current and maximum progresses are known.
	 * @param current		The current progress.
	 * @param maximum		The maximum progress possible.
	 */
	public Progress(int current, int maximum) {
		this();
		if (current >= 0) {
			if (maximum >= 0) {
				if (current <= maximum) {
					this.current = current;
					this.maximum = maximum;
				} else {
					// TODO: Throw exception - current progress can't be larger than the maximum progress.
				}
			} else {
				// TODO: Throw exception - maximum progress can't be negative.
			}
		} else {
			// TODO: Throw exception - current progress can't be negative.
		}
	}
	
	/**
	 * Constructor. Used when the current and maximum progresses could be calculated based
	 * on a list of Progress objects. The progress is weighted.
	 * @param progressList		The list of Progress objects.
	 */
	public Progress(List<Progress> progressList) {
		this();
		for (Progress progress: progressList) {
			current += progress.getCurrent();
			maximum += progress.getMaximum();
		}
	}
	
	/**
	 * @return the percentage of the progress based on the current and the maximum progress.
	 */
	public int getPercentage() {
		return 100 * current / maximum;
	}
	
	/**
	 * @return the current progress.
	 */
	public int getCurrent() {
		return current;
	}
	
	/**
	 * @return the maximum progress possible.
	 */
	public int getMaximum() {
		return maximum;
	}
}
