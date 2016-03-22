package ca.crimsonglow.simmer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ca.crimsonglow.simmer.dao.Ec2Dao;
import ca.crimsonglow.simmer.dao.Ec2DaoImpl;
import ca.crimsonglow.simmer.exception.BadRequestException;
import ca.crimsonglow.simmer.exception.NotFoundException;
import ca.crimsonglow.simmer.model.Snapshot;
import ca.crimsonglow.simmer.model.Volume;

/**
 * An implementation of the snapshot retention operations for EC2.
 */
public class SnapshotRetentionServiceImpl implements SnapshotRetentionService {
  private static final Logger LOGGER = Logger.getLogger(SnapshotRetentionServiceImpl.class);

  private final Ec2Dao _ec2;

  /**
   * Creates a new SnapshotRetentionServiceImpl object.
   *
   * @param ec2
   *          A handle to the EC2 data access object.
   */
  public SnapshotRetentionServiceImpl(Ec2Dao ec2) {
    _ec2 = ec2;
  }

  /**
   * Creates a new SnapshotRetentionServiceImpl object.
   */
  public SnapshotRetentionServiceImpl() {
    this(new Ec2DaoImpl());
  }

  /**
   * Clears snapshots that were created before a given date. The first snapshot
   * of the month is retained for snapshots created before the threshold date.
   * 
   * @param threshold
   *          The threshold date.
   */
  public void clearSnapshotsOlderThan(Date threshold) {
    // Get a list of all disk volumes.
    Collection<Volume> volumes = _ec2.getVolumes();
    LOGGER.info(String.format("Found %d volumes", volumes.size()));

    for (Volume volume : volumes) {
      // Get a list of all snapshots for the disk volume.
      Collection<Snapshot> snapshots = _ec2.getSnapshots(volume);
      LOGGER.info(String.format("Found %d snapshots for volume %s", snapshots.size(), volume.getId()));

      // Group snapshots by the year and month in which they were created.
      Map<Date, Collection<Snapshot>> snapshotsByMonth = _groupSnapshotsByMonth(snapshots);
      for (Map.Entry<Date, Collection<Snapshot>> i : snapshotsByMonth.entrySet()) {
        // Skip the first snapshot and delete the rest if they were taken prior
        // to the threshold date.
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

  /**
   * Creates snapshots for each disk volume.
   */
  public void createSnapshots() {
    Collection<Volume> volumes = _ec2.getVolumes();
    LOGGER.info(String.format("Found %d volumes", volumes.size()));

    for (Volume volume : volumes) {
      Snapshot snapshot = _ec2.createSnapshot(volume);
      LOGGER.info(String.format("Created snapshot %s (%s)", snapshot.getId(), snapshot.getDescription()));
    }
  }

  private Map<Date, Collection<Snapshot>> _groupSnapshotsByMonth(Collection<Snapshot> snapshots) {
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

  private List<Snapshot> _sortSnapshotsByStartTime(Collection<Snapshot> snapshots) {
    List<Snapshot> result = new ArrayList<Snapshot>(snapshots);
    Collections.sort(result, new Comparator<Snapshot>() {
      public int compare(Snapshot o1, Snapshot o2) {
        return o1.getStartTime().compareTo(o2.getStartTime());
      }
    });

    return result;
  }
}
