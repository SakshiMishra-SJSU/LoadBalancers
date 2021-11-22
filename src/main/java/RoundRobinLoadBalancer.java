import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RoundRobinLoadBalancer {

    private final ReentrantReadWriteLock.WriteLock addNodeLock = new ReentrantReadWriteLock(true)
            .writeLock();
    private static final int currLoad=500;
    private final List<Server> activeServers = new ArrayList<>();
    private int nodeIndex;

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        final RoundRobinLoadBalancer roundRobinLoadBalancer = new RoundRobinLoadBalancer();
        final int nodeCount = 6;
        Server[] servers = new Server[nodeCount];
        for (int iter = 0; iter < nodeCount; iter++) {
            Server server = new Server(UUID.randomUUID().toString());
            server.setLoad(new Load((float) currLoad));
            roundRobinLoadBalancer.addNode(server);
            servers[iter] = server;
        }
        int rounds = nodeCount * 2;
        for (int iter = 0; iter < rounds; iter++) {
            Server selectedServer = roundRobinLoadBalancer.selectNode();
            roundRobinLoadBalancer.ExecuteNode(selectedServer);
        }

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }

    public Server selectNode() {
        Server server = null;
        if (addNodeLock.tryLock()) {
                if (activeServers.size() == 1) {
                    return activeServers.get(0);
                }
                int nextNode = nodeIndex % activeServers.size();
                server = activeServers.get(nextNode);
                nodeIndex = nextNode + 1;
                addNodeLock.unlock();
        }
        return server;
    }

    public void addNode(Server server) {
        if (addNodeLock.tryLock()) {
                activeServers.add(server);
                addNodeLock.unlock();
        }
    }

    public void ExecuteNode(Server server) {
        int times = Math.round(server.getLoad().getLoadValue());
        for(int i=0;i< times;i++)
        {
            try {
                File myObj = new File("D:/CMPE275/test.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    myReader.nextLine();
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

}
