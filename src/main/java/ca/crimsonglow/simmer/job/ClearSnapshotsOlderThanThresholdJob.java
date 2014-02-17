package ca.crimsonglow.simmer.job;

import ca.crimsonglow.simmer.SimmerJob;
import ca.crimsonglow.simmer.SimmerService;
import ca.crimsonglow.simmer.SimmerServiceImpl;

import java.util.Calendar;
import java.util.Properties;

public class ClearSnapshotsOlderThanThresholdJob extends SimmerJob
{
	public ClearSnapshotsOlderThanThresholdJob(String[] args)
	{
		super(args);
	}

	protected void run(Properties p)
	{
		int snapshotAgeInDays = Integer.parseInt(p.getProperty("snapshotAgeInDays"));

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -snapshotAgeInDays);

		SimmerService simmer = new SimmerServiceImpl();
		simmer.clearSnapshotsOlderThan(c.getTime());
	}

	public static void main(String[] args)
	{
		new ClearSnapshotsOlderThanThresholdJob(args);
	}
}
