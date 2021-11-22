import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RandomKRoundRobinLoadBalancer {

    private final ReentrantReadWriteLock.WriteLock addNodeLock = new ReentrantReadWriteLock(true)
            .writeLock();
    private final Random randomizer = new Random();


    private final List<Server> activeServers = new ArrayList<>();
    private int randomChoices;


    public RandomKRoundRobinLoadBalancer(final int randomChoices) {
        this.randomChoices = randomChoices;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        final int randomChoices = 2;
        final RandomKRoundRobinLoadBalancer randomKRoundRobinLoadBalancer = new
                RandomKRoundRobinLoadBalancer(randomChoices);
        final int nodes = 12;
        final int requests = 12;

        List<Server> totalServers = new LinkedList<>();
        for(int i=0;i<nodes;i++)
            totalServers.add(new Server(UUID.randomUUID().toString()));

        final int rounds = 6;
        for (int iter = 0; iter < requests; iter+=rounds) {
            randomKRoundRobinLoadBalancer.addNode(totalServers);
            for(int i=iter;i<rounds;i++) {
                Server server = randomKRoundRobinLoadBalancer.selectNode();
                randomKRoundRobinLoadBalancer.ExecuteNode(server);
            }
        }
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }

    public Server selectNode() {
        Server server = null;
        if (addNodeLock.tryLock()) {
                final Server[] servers = new Server[randomChoices];
                final List<Integer> selectedIndexes = new ArrayList<>(randomChoices);
                for (int serverIndex = 0; serverIndex < randomChoices; serverIndex++) {
                    int index = randomizer.nextInt(activeServers.size());
                    while (selectedIndexes.contains(index)) {
                        index = randomizer.nextInt(activeServers.size());
                    }
                    selectedIndexes.add(index);
                    servers[serverIndex] = activeServers.get(index);
                }
                Arrays.sort(servers, Comparator.comparing(Server::getLoad));
                server = servers[0];
                addNodeLock.unlock();
        }
        return server;
    }

    public void addNode(final List<Server> server) {

        if (addNodeLock.tryLock()) {
              for(int i=0;i<6;i++)
                    activeServers.add(server.get(new Random().nextInt(12)));
            addNodeLock.unlock();
        }

    }

    public void ExecuteNode(Server server) {
        int times = Math.round(server.getLoad().getLoadValue());
        for(int i=0;i< times;i++)
        {
            try {
                File file = new File("D:/CMPE275/LoadBalancer/src/main/resources/Input/test.txt");
                Scanner scan = new Scanner(file);
                while (scan.hasNextLine()) {
                    scan.nextLine();
                }
                scan.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}
