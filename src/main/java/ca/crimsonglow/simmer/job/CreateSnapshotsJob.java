package ca.crimsonglow.simmer.job;

import ca.crimsonglow.simmer.SimmerJob;
import ca.crimsonglow.simmer.SimmerServiceImpl;

import java.util.Properties;

public class CreateSnapshotsJob extends SimmerJob
{
	public CreateSnapshotsJob(String[] args)
	{
		super(args);
	}

	protected void run(Properties p)
	{
		new SimmerServiceImpl().createSnapshots();
	}

	public static void main(String[] args)
	{
		new CreateSnapshotsJob(args);
	}
}
