package ca.crimsonglow.simmer.model;

import java.util.Date;

public class Snapshot
{
	private final String _id;
	private final String _description;
	private final Date _startTime;

	public Snapshot(String id, String description, Date startTime)
	{
		_id = id;
		_description = description;
		_startTime = startTime;
	}

	public Snapshot(com.amazonaws.services.ec2.model.Snapshot snapshot)
	{
		this(snapshot.getSnapshotId(), snapshot.getDescription(), snapshot.getStartTime());
	}

	public String getId()
	{
		return _id;
	}

	public String getDescription()
	{
		return _description;
	}

	public Date getStartTime()
	{
		return _startTime;
	}
}
