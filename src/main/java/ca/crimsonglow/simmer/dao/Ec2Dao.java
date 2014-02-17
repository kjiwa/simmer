package ca.crimsonglow.simmer.dao;

import ca.crimsonglow.simmer.exception.BadRequestException;
import ca.crimsonglow.simmer.exception.NotFoundException;
import ca.crimsonglow.simmer.model.Instance;
import ca.crimsonglow.simmer.model.Snapshot;
import ca.crimsonglow.simmer.model.Volume;

import java.util.Collection;

public interface Ec2Dao
{
	Instance getInstanceById(String id) throws NotFoundException;

	Collection<Volume> getVolumes();

	Snapshot createSnapshot(Volume volume);
	Collection<Snapshot> getSnapshots(Volume volume);
	void deleteSnapshot(Snapshot snapshot) throws BadRequestException, NotFoundException;
}
