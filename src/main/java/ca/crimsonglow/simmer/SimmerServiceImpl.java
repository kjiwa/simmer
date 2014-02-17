package ca.crimsonglow.simmer;

import ca.crimsonglow.simmer.dao.Ec2Dao;
import ca.crimsonglow.simmer.dao.Ec2DaoImpl;
import ca.crimsonglow.simmer.exception.BadRequestException;
import ca.crimsonglow.simmer.exception.NotFoundException;
import ca.crimsonglow.simmer.model.Snapshot;
import ca.crimsonglow.simmer.model.Volume;
import org.apache.log4j.Logger;

import java.util.*;

public class SimmerServiceImpl implements SimmerService
{
	private static final Logger LOGGER = Logger.getLogger(SimmerServiceImpl.class);

	private final Ec2Dao _ec2;

	public SimmerServiceImpl(Ec2Dao ec2)
	{
		_ec2 = ec2;
	}

	public SimmerServiceImpl()
	{
		this(new Ec2DaoImpl());
	}

	@Override
	public void clearSnapshotsOlderThan(Date threshold)
	{
		Collection<Volume> volumes = _ec2.getVolumes();
		LOGGER.info(String.format("Found %d volumes", volumes.size()));

		for (Volume volume : volumes) {
			Collection<Snapshot> snapshots = _ec2.getSnapshots(volume);
			LOGGER.info(String.format("Found %d snapshots for volume %s", snapshots.size(), volume.getId()));

			Map<Date, Collection<Snapshot>> snapshotsByMonth = _groupSnapshotsByMonth(snapshots);
			for (Map.Entry<Date, Collection<Snapshot>> i : snapshotsByMonth.entrySet()) {
				List<Snapshot> sortedSnapshots = _sortSnapshotsByStartTime(i.getValue());
				for (int j = 1; j < sortedSnapshots.size(); ++j) {
					Snapshot snapshot = sortedSnapshots.get(j);
					if (snapshot.getStartTime().before(threshold)) {
						try {
							_ec2.deleteSnapshot(snapshot);
							LOGGER.info(String.format("Deleted snapshot %s", snapshot.getId()));
						} catch (BadRequestException e) {
							LOGGER.error(String.format("Unable to delete snapshot %s", snapshot.getId()), e);
						} catch (NotFoundException e) {
							LOGGER.warn(String.format("Snapshot %s was not found", snapshot.getId()), e);
						}
					}
				}
			}
		}
	}

	@Override
	public void createSnapshots()
	{
		Collection<Volume> volumes = _ec2.getVolumes();
		LOGGER.info(String.format("Found %d volumes", volumes.size()));

		for (Volume volume : volumes) {
			Snapshot snapshot = _ec2.createSnapshot(volume);
			LOGGER.info(String.format("Created snapshot %s (%s)", snapshot.getId(), snapshot.getDescription()));
		}
	}

	private Map<Date, Collection<Snapshot>> _groupSnapshotsByMonth(Collection<Snapshot> snapshots)
	{
		Map<Date, Collection<Snapshot>> result = new HashMap<Date, Collection<Snapshot>>(snapshots.size());

		Calendar c = Calendar.getInstance();
		for (Snapshot i : snapshots) {
			c.setTime(i.getStartTime());
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);

			Date key = c.getTime();
			if (!result.containsKey(key)) {
				result.put(key, new ArrayList<Snapshot>());
			}

			result.get(key).add(i);
		}

		return result;
	}

	private List<Snapshot> _sortSnapshotsByStartTime(Collection<Snapshot> snapshots)
	{
		List<Snapshot> result = new ArrayList<Snapshot>(snapshots);
		Collections.sort(result, new Comparator<Snapshot>()
		{
			@Override
			public int compare(Snapshot o1, Snapshot o2)
			{
				return o1.getStartTime().compareTo(o2.getStartTime());
			}
		});

		return result;
	}
}
