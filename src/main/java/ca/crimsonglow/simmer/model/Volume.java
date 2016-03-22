package ca.crimsonglow.simmer.model;

import com.amazonaws.services.ec2.model.VolumeAttachment;

/**
 * A disk volume object.
 */
public class Volume {
  private final String id;
  private final String instanceId;

  /**
   * Creates a new disk volume object.
   *
   * @param id
   *          The volume ID.
   * @param instanceId
   *          The ID of the instance to which the volume is attached.
   */
  public Volume(String id, String instanceId) {
    this.id = id;
    this.instanceId = instanceId;
  }

  /**
   * Creates a new disk volume object from an AWS volume object.
   *
   * @param volume
   *          The AWS volume object.
   */
  public Volume(com.amazonaws.services.ec2.model.Volume volume) {
    this(volume.getVolumeId(), _getInstanceId(volume));
  }

  /**
   * Returns whether the volume is attached to an instance.
   *
   * @return Whether the volume is attached to an instance.
   */
  public boolean hasInstanceId() {
    return instanceId != null;
  }

  /**
   * Returns the ID of the volume.
   *
   * @return The ID of the volume.
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the ID of the instance to which the volume is attached. If the
   * volume is not attached to an instance, this value will be null.
   *
   * @return The ID of the instance to which the volume is attached.
   */
  public String getInstanceId() {
    return instanceId;
  }

  private static String _getInstanceId(com.amazonaws.services.ec2.model.Volume volume) {
    String instanceId = null;
    for (VolumeAttachment attachment : volume.getAttachments()) {
      instanceId = attachment.getInstanceId();
      break;
    }

    return instanceId;
  }
}
