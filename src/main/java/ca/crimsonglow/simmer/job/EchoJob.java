package ca.crimsonglow.simmer.job;

import java.util.Properties;

import ca.crimsonglow.simmer.Job;

/**
 * A sample job that prints the job arguments to the console.
 */
public class EchoJob extends Job {
  /**
   * Creates a new Echo job.
   *
   * @param args
   *          The job arguments.
   */
  public EchoJob(String[] args) {
    super(args);
  }

  @Override
  protected void run(Properties p) {
    System.out.println(p);
  }

  public static void main(String[] args) {
    new EchoJob(args);
  }
}
