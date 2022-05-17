package application.carGarage;

public class CarGarageStack<T> {
    Object[] queue = new Object[0];

    public CarGarageStack() {

    }

    public CarGarageStack(T[] vehicles) {
        queue = vehicles;
    }

    public void push(T vehicle) {
        T[] newQueue = (T[]) new Object[queue.length + 1];
        fillInOldQueue(newQueue);
        newQueue[newQueue.length - 1] = vehicle;
        queue = newQueue;
    }

    private void fillInOldQueue(T[] newQueue) {
        System.arraycopy(queue, 0, newQueue, 0, queue.length);
    }

    public T pop() {
        T lastVehicle = peek();
        deleteLastVehicle();
        return lastVehicle;
    }

    private void deleteLastVehicle() {
        T[] newQueue = (T[]) new Object[queue.length - 1];

        System.arraycopy(queue, 0, newQueue, 0, queue.length - 1);

        queue = newQueue;
    }

    public T peek() {
        if (size() == 0) {
            throw new IllegalStateException("Queue is empty!!!");
        }
        return (T) queue[queue.length - 1];
    }

    public int size() {
        return queue.length;
    }
}
