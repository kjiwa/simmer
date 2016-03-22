package ca.crimsonglow.simmer.model;

import java.util.Date;

/**
 * A volume snapshot.
 */
public class Snapshot {
  private final String id;
  private final String description;
  private final Date startTime;

  /**
   * Creates a new volume snapshot object.
   *
   * @param id
   *          The snapshot ID.
   * @param description
   *          The snapshot description.
   * @param startTime
   *          The time at which the snapshot was taken.
   */
  public Snapshot(String id, String description, Date startTime) {
    this.id = id;
    this.description = description;
    this.startTime = startTime;
  }

  /**
   * Creates a new volume snapshot from an AWS snapshot object.
   *
   * @param snapshot
   *          The AWS snapshot.
   */
  public Snapshot(com.amazonaws.services.ec2.model.Snapshot snapshot) {
    this(snapshot.getSnapshotId(), snapshot.getDescription(), snapshot.getStartTime());
  }

  /**
   * Returns the snapshot ID.
   * 
   * @return The snapshot ID.
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the snapshot description.
   * 
   * @return The snapshot description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the time at which the snapshot was taken.
   * 
   * @return The time at which the snapshot was taken.
   */
  public Date getStartTime() {
    return startTime;
  }
}
