package de.jungblut.bsp;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.NullWritable;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.BSP;
import org.apache.hama.bsp.BSPJob;
import org.apache.hama.bsp.BSPPeer;
import org.apache.hama.bsp.sync.SyncException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Shows off some real time usage of Hama.
 * 
 * @author thomas.jungblut
 * 
 */
public class DataStreamProcessing extends
    BSP<NullWritable, NullWritable, NullWritable, NullWritable, LongMessage> {

  private static final Log LOG = LogFactory.getLog(DataStreamProcessing.class);

  private Twitter twitter;
  private String userName;
  private final HashSet<Status> alreadyProcessedStatusses = new HashSet<>();
  private boolean isMaster;
  private String[] otherPeers;

  @Override
  public void setup(
      BSPPeer<NullWritable, NullWritable, NullWritable, NullWritable, LongMessage> peer)
      throws IOException, InterruptedException {
    // gets the default twitter
    twitter = new TwitterFactory().getInstance();
    userName = peer.getConfiguration().get("twitter.user.name");
    // our master is always the task which is the first in our array
    isMaster = peer.getPeerName().equals(peer.getAllPeerNames()[0]);
    if (isMaster) {
      // get the other peer names in an array
      String[] allPeerNames = peer.getAllPeerNames();
      int numPeers = allPeerNames.length - 1;
      HashSet<String> peers = new HashSet<>();
      peers.addAll(Arrays.asList(allPeerNames));
      peers.remove(peer.getPeerName());
      otherPeers = peers.toArray(new String[numPeers]);
    }
  }

  @Override
  public void bsp(
      BSPPeer<NullWritable, NullWritable, NullWritable, NullWritable, LongMessage> bspPeer)
      throws IOException, SyncException, InterruptedException {

    if (isMaster) {
      while (true) {
        try {
          int numMessagesSend = 0;
          // this should get us the least 20 tweets of this user
          List<Status> statuses = twitter.getUserTimeline(userName);
          for (Status s : statuses) {
            if (alreadyProcessedStatusses.add(s)) {
              LOG.info("Got new status from: " + s.getUser().getName()
                  + " with message " + s.getText());
              // we distribute messages to the other peers for
              // processing via user id partitioning
              // so a task gets all messages for a user
              bspPeer.send(
                  otherPeers[(int) (s.getUser().getId() % otherPeers.length)],
                  new LongMessage(s.getUser().getId(), s.getText()));
              numMessagesSend++;
            }
          }
          // if we send no messages, wait for a few seconds and go
          // further without syncing
          if (numMessagesSend == 0) {
            Thread.sleep(5000l);
          } else {
            // sync before we get new statusses again.
            bspPeer.sync();
          }
        } catch (TwitterException e) {
          e.printStackTrace();
        }
      }
    } else {
      while (true) {
        // wait for some work...
        bspPeer.sync();
        LongMessage message;
        while ((message = bspPeer.getCurrentMessage()) != null) {
          LOG.info("Got work in form of text: " + message.getData()
              + " for the userid: " + message.getTag());
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {

    // BSP job configuration
    HamaConfiguration conf = new HamaConfiguration();

    conf.set("twitter.user.name", "tjungblut");
    // I'm always testing in localmode so I use 2 tasks.
    conf.set("bsp.local.tasks.maximum", "2");

    BSPJob bsp = new BSPJob(conf);
    // Set the job name
    bsp.setJobName("Twitter stream processing");
    bsp.setBspClass(DataStreamProcessing.class);

    bsp.waitForCompletion(true);

  }

}
