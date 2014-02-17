package ca.crimsonglow.simmer;

import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

public abstract class SimmerJob
{
	public SimmerJob(String[] args)
	{
		Properties p = new SimmerProperties(args);
		PropertyConfigurator.configure(p);
		System.setProperty("aws.accessKeyId", p.getProperty("aws.accessKeyId"));
		System.setProperty("aws.secretKey", p.getProperty("aws.secretKey"));
		run(p);
	}

	protected abstract void run(Properties p);
}
