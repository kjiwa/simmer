package ca.crimsonglow.simmer.dao;

import java.util.ArrayList;
import java.util.Collection;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateSnapshotRequest;
import com.amazonaws.services.ec2.model.CreateSnapshotResult;
import com.amazonaws.services.ec2.model.DeleteSnapshotRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeSnapshotsRequest;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Reservation;

import ca.crimsonglow.simmer.exception.BadRequestException;
import ca.crimsonglow.simmer.exception.InternalException;
import ca.crimsonglow.simmer.exception.NotFoundException;
import ca.crimsonglow.simmer.model.Instance;
import ca.crimsonglow.simmer.model.Snapshot;
import ca.crimsonglow.simmer.model.Volume;

/**
 * An implementation of the EC2 data access interface.
 */
public class Ec2DaoImpl implements Ec2Dao {
  private final AmazonEC2 _ec2;

  /**
   * Creates a new EC2DaoImpl object.
   * 
   * @param ec2
   *          A handle to an authenticated AmazonEC2 object.
   */
  public Ec2DaoImpl(AmazonEC2 ec2) {
    _ec2 = ec2;
  }

  /**
   * Creates a new EC2DaoImpl object. System properties (aws.accessKeyId and
   * aws.secretKey) are used to create an authenticated AmazonEC2 object.
   */
  public Ec2DaoImpl() {
    this(new AmazonEC2Client(new SystemPropertiesCredentialsProvider()));
  }

  /**
   * Returns an instance object, given its ID.
   *
   * @param id
   *          The instance ID.
   * @return An instance with a matching ID.
   * @throws NotFoundException
   *           If an instance with the given ID could not be found.
   */
  public Instance getInstanceById(String id) throws NotFoundException {
    DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(id);

    Collection<Reservation> reservations = _ec2.describeInstances(request).getReservations();
    for (Reservation reservation : reservations) {
      for (com.amazonaws.services.ec2.model.Instance instance : reservation.getInstances()) {
        return new Instance(instance);
      }
    }

    throw new NotFoundException(String.format("Instance %s was not found", id));
  }

  /**
   * Returns all disk volume objects.
   *
   * @return All disk volume objects.
   */
  public Collection<Volume> getVolumes() {
    Collection<com.amazonaws.services.ec2.model.Volume> response = _ec2.describeVolumes().getVolumes();
    Collection<Volume> result = new ArrayList<Volume>(response.size());
    for (com.amazonaws.services.ec2.model.Volume volume : response) {
      if (volume.getSnapshotId().isEmpty()) {
        result.add(new Volume(volume));
      }
    }

    return result;
  }

  /**
   * Creates a snapshot of a disk volume.
   *
   * @param volume
   *          The disk volume for which to take the snapshot.
   * @return The disk volume for which to take the snapshot.
   */
  public Snapshot createSnapshot(Volume volume) {
    CreateSnapshotRequest request = new CreateSnapshotRequest().withVolumeId(volume.getId());

    if (volume.hasInstanceId()) {
      try {
        request.setDescription(getInstanceById(volume.getInstanceId()).getName());
      } catch (NotFoundException e) {
        throw new InternalException(String.format("Instance %s was not found", volume.getInstanceId()), e);
      }
    }

    CreateSnapshotResult response = _ec2.createSnapshot(request);
    return new Snapshot(response.getSnapshot());
  }

  /**
   * Returns all snapshots for a given disk volume.
   *
   * @param volume
   *          The disk volume for which to find snapshots.
   * @return All snapshots for a given disk volume.
   */
  public Collection<Snapshot> getSnapshots(Volume volume) {
    DescribeSnapshotsRequest request = new DescribeSnapshotsRequest()
        .withFilters(new Filter().withName("volume-id").withValues(volume.getId()));

    Collection<com.amazonaws.services.ec2.model.Snapshot> response = _ec2.describeSnapshots(request).getSnapshots();

    Collection<Snapshot> result = new ArrayList<Snapshot>(response.size());
    for (com.amazonaws.services.ec2.model.Snapshot snapshot : response) {
      result.add(new Snapshot(snapshot));
    }

    return result;
  }

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
  public void deleteSnapshot(Snapshot snapshot) throws BadRequestException, NotFoundException {
    try {
      _ec2.deleteSnapshot(new DeleteSnapshotRequest().withSnapshotId(snapshot.getId()));
    } catch (AmazonServiceException e) {
      if (e.getErrorCode().equals("InvalidSnapshot.NotFound")) {
        throw new NotFoundException(String.format("Snapshot %s was not found", snapshot.getId()), e);
      } else if (e.getErrorCode().equals("InvalidSnapshot.InUse")) {
        throw new BadRequestException(String.format("Snapshot %s is in use", snapshot.getId()), e);
      }

      throw e;
    }
  }
}
