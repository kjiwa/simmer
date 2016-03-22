package ca.crimsonglow.simmer;

import java.util.Date;

/**
 * An interface for snapshot retention operations.
 */
public interface SnapshotRetentionService {
  /**
   * Clears snapshots taken before a given date. The first snapshot of each
   * month is retained for snapshots taken prior to the threshold date.
   *
   * @param threshold
   *          The threshold date.
   */
  void clearSnapshotsOlderThan(Date threshold);

  /**
   * Creates snapshots for each disk volume.
   */
  void createSnapshots();
}
