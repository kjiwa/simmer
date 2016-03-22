package ca.crimsonglow.simmer.model;

import com.amazonaws.services.ec2.model.Tag;

/**
 * A machine instance.
 */
public class Instance {
  private final String id;
  private final String name;

  /**
   * Creates a new machine instance.
   *
   * @param id
   *          The instance ID.
   * @param name
   *          The instance name.
   */
  public Instance(String id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Creates a new machine instance from an AWS instance object.
   *
   * @param instance
   *          The AWS instance.
   */
  public Instance(com.amazonaws.services.ec2.model.Instance instance) {
    this(instance.getInstanceId(), _getInstanceName(instance));
  }

  /**
   * Returns the instance ID.
   *
   * @return The instance ID.
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the instance name.
   *
   * @return The instance name.
   */
  public String getName() {
    return name;
  }

  private static String _getInstanceName(com.amazonaws.services.ec2.model.Instance instance) {
    for (Tag tag : instance.getTags()) {
      if (tag.getKey().equals("Name")) {
        return tag.getValue();
      }
    }

    return "";
  }
}
