package ca.crimsonglow.simmer.job;

import ca.crimsonglow.simmer.SimmerJob;

import java.util.Properties;

public class EchoJob extends SimmerJob
{
	public EchoJob(String[] args)
	{
		super(args);
	}

	@Override
	protected void run(Properties p)
	{
		System.out.println(p);
	}

	public static void main(String[] args)
	{
		new EchoJob(args);
	}
}
