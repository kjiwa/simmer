package ca.crimsonglow.simmer;

import java.util.Properties;

/**
 * A class that parses an argument array into a Properties list.
 */
public class JobProperties extends Properties {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new JobProperties object from an argument array. Each argument
   * must have the form --key=value, otherwise the argument is ignored.
   *
   * @param args
   *          The job arguments.
   */
  public JobProperties(String[] args) {
    for (String arg : args) {
      if (!arg.startsWith("--") || !arg.contains("=")) {
        continue;
      }

      int radix = arg.indexOf('=');
      String key = arg.substring(2, radix);
      String value = arg.substring(radix + 1);
      setProperty(key, value);
    }
  }
}
