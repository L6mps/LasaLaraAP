package com.lasalara.lasalara.backend.structure;

import java.util.List;

import com.lasalara.lasalara.backend.exceptions.NumericException;
import com.lasalara.lasalara.backend.exceptions.NumericExceptionMessage;

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
	 * @throws NumericException 
	 */
	public Progress(int current, int maximum) throws NumericException {
		this();
		if (current >= 0) {
			if (maximum >= 0) {
				if (current <= maximum) {
					this.current = current;
					this.maximum = maximum;
				} else {
					throw new NumericException(NumericExceptionMessage.PROGRESS_CURRENT_TOO_BIG);
				}
			} else {
				throw new NumericException(NumericExceptionMessage.PROGRESS_MAXIMUM_NEGATIVE);
			}
		} else {
			throw new NumericException(NumericExceptionMessage.PROGRESS_CURRENT_NEGATIVE);
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
		if (maximum != 0) {
			return 100 * current / maximum;
		} else {
			return 0;
		}
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
