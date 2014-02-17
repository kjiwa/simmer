package ca.crimsonglow.simmer.model;

import com.amazonaws.services.ec2.model.Tag;

public class Instance
{
	private final String _id;
	private final String _name;

	public Instance(String id, String name)
	{
		_id = id;
		_name = name;
	}

	public Instance(com.amazonaws.services.ec2.model.Instance instance)
	{
		this(instance.getInstanceId(), _getInstanceName(instance));
	}

	public String getId()
	{
		return _id;
	}

	public String getName()
	{
		return _name;
	}

	private static String _getInstanceName(com.amazonaws.services.ec2.model.Instance instance)
	{
		for (Tag tag : instance.getTags()) {
			if (tag.getKey().equals("Name")) {
				return tag.getValue();
			}
		}

		return "";
	}
}
