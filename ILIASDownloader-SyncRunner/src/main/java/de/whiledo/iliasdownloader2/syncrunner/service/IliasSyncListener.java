package de.whiledo.iliasdownloader2.syncrunner.service;

import java.util.Collection;

public interface IliasSyncListener {

	void syncStarted();
	void syncFinished();
	void syncStopped();
	void syncCoursesFound(Collection<Long> activeCourseIds, Collection<Long> allCourseIds);
}
