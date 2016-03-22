package ca.crimsonglow.simmer.job;

import java.util.Calendar;
import java.util.Properties;

import ca.crimsonglow.simmer.Job;
import ca.crimsonglow.simmer.SnapshotRetentionService;
import ca.crimsonglow.simmer.SnapshotRetentionServiceImpl;

/**
 * A job that clears snapshots that have aged beyond a retention period.
 */
public class ClearSnapshotsOlderThanThresholdJob extends Job {
  /**
   * Creates a new ClearSnapshotsOlderThanThreshold job.
   *
   * @param args
   *          The job arguments.
   */
  public ClearSnapshotsOlderThanThresholdJob(String[] args) {
    super(args);
  }

  @Override
  protected void run(Properties p) {
    int snapshotAgeInDays = Integer.parseInt(p.getProperty("snapshotAgeInDays"));

    Calendar c = Calendar.getInstance();
    c.add(Calendar.DAY_OF_YEAR, -snapshotAgeInDays);

    SnapshotRetentionService simmer = new SnapshotRetentionServiceImpl();
    simmer.clearSnapshotsOlderThan(c.getTime());
  }

  public static void main(String[] args) {
    new ClearSnapshotsOlderThanThresholdJob(args);
  }
}
