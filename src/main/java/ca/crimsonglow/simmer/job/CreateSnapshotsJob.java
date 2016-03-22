package ca.crimsonglow.simmer.job;

import java.util.Properties;

import ca.crimsonglow.simmer.Job;
import ca.crimsonglow.simmer.SnapshotRetentionServiceImpl;

/**
 * A job that creates volume snapshots for each volume that is attached to an
 * instance.
 */
public class CreateSnapshotsJob extends Job {
  /**
   * Creates a new CreateSnapshots job.
   *
   * @param args
   *          The job arguments.
   */
  public CreateSnapshotsJob(String[] args) {
    super(args);
  }

  @Override
  protected void run(Properties p) {
    new SnapshotRetentionServiceImpl().createSnapshots();
  }

  public static void main(String[] args) {
    new CreateSnapshotsJob(args);
  }
}
