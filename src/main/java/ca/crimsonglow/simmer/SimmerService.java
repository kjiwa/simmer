package ca.crimsonglow.simmer;

import java.util.Date;

public interface SimmerService
{
	void clearSnapshotsOlderThan(Date threshold);
	void createSnapshots();
}
