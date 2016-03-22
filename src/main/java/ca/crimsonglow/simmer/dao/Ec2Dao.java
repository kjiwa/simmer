package ca.crimsonglow.simmer.dao;

import java.util.Collection;

import ca.crimsonglow.simmer.exception.BadRequestException;
import ca.crimsonglow.simmer.exception.NotFoundException;
import ca.crimsonglow.simmer.model.Instance;
import ca.crimsonglow.simmer.model.Snapshot;
import ca.crimsonglow.simmer.model.Volume;

/**
 * An interface for interacting with AWS EC2.
 */
public interface Ec2Dao {
  /**
   * Returns an instance object, given its ID.
   *
   * @param id
   *          The instance ID.
   * @return An instance with a matching ID.
   * @throws NotFoundException
   *           If an instance with the given ID could not be found.
   */
  Instance getInstanceById(String id) throws NotFoundException;

  /**
   * Returns all disk volume objects.
   *
   * @return All disk volume objects.
   */
  Collection<Volume> getVolumes();

  /**
   * Creates a snapshot of a disk volume.
   *
   * @param volume
   *          The disk volume for which to take the snapshot.
   * @return The disk volume for which to take the snapshot.
   */
  Snapshot createSnapshot(Volume volume);

  /**
   * Returns all snapshots for a given disk volume.
   *
   * @param volume
   *          The disk volume for which to find snapshots.
   * @return All snapshots for a given disk volume.
   */
  Collection<Snapshot> getSnapshots(Volume volume);

  /**
   * Deletes a snapshot.
   * 
   * @param snapshot
   *          The snapshot to delete.
   * @throws BadRequestException
   *           If the request contains an error.
   * @throws NotFoundException
   *           If the snapshot does not exist.
   */
  void deleteSnapshot(Snapshot snapshot) throws BadRequestException, NotFoundException;
}
