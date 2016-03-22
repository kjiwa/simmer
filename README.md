# Snapshot Tools for EC2

Tools that create snapshots and manage their retention in EC2. There are two
binaries in this package:

* CreateSnapshotsJob
* ClearSnapshotsOlderThanThresholdJob

## CreateSnapshotsJob

This job will create a snapshot for every disk volume found in an EC2 account.
The job takes the following arguments:

* aws.accessKeyId
* aws.secretKey

View the
[source](src/main/java/ca/crimsonglow/simmer/job/CreateSnapshotsJob.java).

## ClearSnapshotsOlderThanThresholdJob

This job will delete snapshots taken before a given threshold date. One snapshot
from each month will be retained for snapshots taken prior to the threshold
date. The job takes the following arguments:

* aws.accessKeyId
* aws.secretKey
* snapshotAgeInDays

View the
[source](src/main/java/ca/crimsonglow/simmer/job/ClearSnapshotsOlderThanThresholdJob.java).
