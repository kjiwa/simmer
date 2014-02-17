package ca.crimsonglow.simmer.model;

import com.amazonaws.services.ec2.model.VolumeAttachment;

public class Volume
{
	private final String _id;
	private final String _instanceId;

	public Volume(String id, String instanceId)
	{
		_id = id;
		_instanceId = instanceId;
	}

	public Volume(com.amazonaws.services.ec2.model.Volume volume)
	{
		this(volume.getVolumeId(), _getInstanceId(volume));
	}

	public boolean hasInstanceId()
	{
		return _instanceId != null;
	}

	public String getId()
	{
		return _id;
	}

	public String getInstanceId()
	{
		return _instanceId;
	}

	private static String _getInstanceId(com.amazonaws.services.ec2.model.Volume volume)
	{
		String instanceId = null;
		for (VolumeAttachment attachment : volume.getAttachments()) {
			instanceId = attachment.getInstanceId();
			break;
		}

		return instanceId;
	}
}
