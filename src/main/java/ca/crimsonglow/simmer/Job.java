package ca.crimsonglow.simmer;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * A job object.
 */
public abstract class Job {
  /**
   * Creates a new job and collects arguments into a properties object.
   * 
   * @param args
   *          The job arguments.
   */
  public Job(String[] args) {
    Properties p = new JobProperties(args);
    PropertyConfigurator.configure(p);
    System.setProperty("aws.accessKeyId", p.getProperty("aws.accessKeyId"));
    System.setProperty("aws.secretKey", p.getProperty("aws.secretKey"));
    run(p);
  }

  /**
   * The entry point for the job.
   * 
   * @param p
   *          The job arguments.
   */
  protected abstract void run(Properties p);
}
