package ca.crimsonglow.simmer;

import java.util.Properties;

public class SimmerProperties extends Properties
{
	public SimmerProperties(String[] args)
	{
		for (String arg : args) {
			if (!arg.startsWith("--") || !arg.contains("=")) {
				continue;
			}

			int radix = arg.indexOf('=');
			String key = arg.substring(2, radix);
			String value = arg.substring(radix + 1);
			setProperty(key, value);
		}
	}
}
