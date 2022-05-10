package by.incubator.carWash;

public class CarWashQueue<T> {
    Object[] queue = new Object[0];

    public CarWashQueue() {

    }

    public CarWashQueue(T[] vehicles) {
        queue = vehicles;
    }

    public void enqueue(T vehicle) {
        T[] newQueue = (T[]) new Object[queue.length + 1];
        fillInOldQueue(newQueue);
        newQueue[newQueue.length - 1] = vehicle;
        queue = newQueue;
    }

    private void fillInOldQueue(T[] newQueue) {
        System.arraycopy(queue, 0, newQueue, 0, queue.length);
    }

    public T dequeue() {
        T firstVehicle = peek();
        deleteFirstVehicle();
        return firstVehicle;
    }

    private void deleteFirstVehicle() {
        T[] newQueue = (T[]) new Object[queue.length - 1];

        System.arraycopy(queue, 1, newQueue, 0, queue.length - 1);

        queue = newQueue;
    }

    public T peek() {
        if (size() == 0) {
            throw new IllegalStateException("Queue is empty!!!");
        }
        return (T) queue[0];
    }

    public int size() {
        return queue.length;
    }
}
