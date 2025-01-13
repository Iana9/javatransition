package TaskTracker;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    static class Node {
        DefaultTask value;
        Node next = null;
        Node previous = null;

        Node(DefaultTask task) {
            this.value = task;
        }
    }

    private class SingleLinkedList {

        private Node head = null;

        SingleLinkedList() {}

        public void linkLast(Node task) {
            if (this.head == null) {
                this.head = task;
            }
            else {
                Node lastNode = this.head;
                while (lastNode.next != null) {
                    lastNode = lastNode.next;
                }
                lastNode.next = task;
                task.previous = lastNode;
            }
        }

        public ArrayList<DefaultTask> getTasks() {
            ArrayList<DefaultTask> tasks = new ArrayList<>();
            Node currNode = this.head;
            while (currNode != null) {
                tasks.addLast(currNode.value);
                currNode = currNode.next;
            }
            return tasks;
        }
    }

    SingleLinkedList tasks = new SingleLinkedList();
    HashMap<Integer,Node> tasksMap = new HashMap<>();

    
    @Override
    public List<DefaultTask> getHistory() {
        return tasks.getTasks();
    }

    @Override
    public void add(DefaultTask task) {
        if (!tasksMap.containsKey(task.identification)) {
            Node currNode = new Node(task);
            tasks.linkLast(currNode);
            tasksMap.put(task.identification, currNode);
        }
    }

    @Override
    public void remove(int id) {
        if (tasksMap.containsKey(id)) {
            Node remNode = tasksMap.get(id);
            Node previous = remNode.previous;
            Node next = remNode.next;
            if (previous != null) {
                previous.next = next;
            }
            else {
                this.tasks.head = next;
            }
            if (next != null) {
                next.previous = previous;
            }
            tasksMap.remove(id);
        }
    }
}
