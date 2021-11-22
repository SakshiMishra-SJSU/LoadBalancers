import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WeightedRoundRobinLoadBalancer {

    private final ReentrantReadWriteLock.WriteLock addNodeLock = new ReentrantReadWriteLock(true)
            .writeLock();

    private final List<Server> activeServers = new ArrayList<>();
    private final Map<Server, Integer> nodeCapacity = new HashMap<>();
    private int nodeIndex;
    private static final int currLoad=500;

    public static void main(String[] args) {
        runWeightedRoundRobin();
    }

    private static void runWeightedRoundRobin() {

        long startTime = System.nanoTime();
        final WeightedRoundRobinLoadBalancer weightedRoundRobinLoadBalancer = new WeightedRoundRobinLoadBalancer();

        Server server1 = new Server(UUID.randomUUID().toString());
        server1.setCapacity(new Capacity(3));
        weightedRoundRobinLoadBalancer.addNode(server1);

        Server server2 = new Server(UUID.randomUUID().toString());
        server2.setCapacity(new Capacity(5));
        weightedRoundRobinLoadBalancer.addNode(server2);

        Server server3 = new Server(UUID.randomUUID().toString());
        server3.setCapacity(new Capacity(7));
        weightedRoundRobinLoadBalancer.addNode(server3);

        Server server4 = new Server(UUID.randomUUID().toString());
        server4.setCapacity(new Capacity(3));
        weightedRoundRobinLoadBalancer.addNode(server4);

        Server server5 = new Server(UUID.randomUUID().toString());
        server5.setCapacity(new Capacity(5));
        weightedRoundRobinLoadBalancer.addNode(server5);

        Server server6 = new Server(UUID.randomUUID().toString());
        server6.setCapacity(new Capacity(7));
        weightedRoundRobinLoadBalancer.addNode(server6);

        List<Server> removedServer= new ArrayList<>();
        int requests = 6;
        for (int request = 0; request < requests; request++) {
            Server server = weightedRoundRobinLoadBalancer.selectNode();
            if(server.getCapacity().getCapacity()<=0)
            {   weightedRoundRobinLoadBalancer.removeNode(server);
                removedServer.add(server);
                continue;
            }

            server.setLoad(new Load((float) currLoad));

            weightedRoundRobinLoadBalancer.ExecuteNode(server);

            for(Server servers: removedServer)
            {
                if(servers.getCapacity().getCapacity()>0)
                    weightedRoundRobinLoadBalancer.addNode(servers);
            }

        }
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }

    public Server selectNode() {
        Server server = null;
        if (addNodeLock.tryLock()) {
            try {

                boolean activeNodes = false;
                for (final Map.Entry<Server, Integer> entry : nodeCapacity.entrySet()) {
                    if (entry.getValue() > 0) {
                        activeNodes = true;
                        break;
                    }
                }
                if (!activeNodes) {
                    nodeCapacity.clear();
                    for (final Server serverCapacity : activeServers) {
                        nodeCapacity.put(serverCapacity, serverCapacity.getCapacity().getCapacity());
                    }
                }

                int remainingCapacity = 0, nextNode = 0;
                while (remainingCapacity == 0) {
                    nextNode = nodeIndex % activeServers.size();
                    server = activeServers.get(nextNode);

                    remainingCapacity = nodeCapacity.get(server);

                    if (remainingCapacity == 0) {
                        nodeIndex = nextNode + 1;
                    }
                }

                nodeCapacity.put(server, remainingCapacity - 1);

                nodeIndex = nextNode + 1;
            } finally {
                addNodeLock.unlock();
            }
        }
        return server;
    }



    public void addNode(Server server) {
        if (addNodeLock.tryLock()) {
                activeServers.add(server);
                nodeCapacity.put(server, server.getCapacity().getCapacity());
                addNodeLock.unlock();
        }

    }


    public void removeNode(Server server) {
        if (addNodeLock.tryLock()) {
                activeServers.remove(server);
                nodeCapacity.remove(server);
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
                    String data = myReader.nextLine();

                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}
