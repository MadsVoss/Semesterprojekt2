/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
import data.EKGDTO;
import data.EKGListener;
import data.EKGObservable;
import java.util.LinkedList;

public class ProducerConsumer implements EKGObservable {
    Sensor sensor = new Sensor(0);
    LinkedList<EKGDTO> listGUI = new LinkedList<>();
    LinkedList<EKGDTO> listDB = new LinkedList<>();
    int capacity = 1000;
    private EKGListener listenerGUI;

    public void produce() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (listGUI.size() == capacity)
                    wait();
                LinkedList<EKGDTO> value = sensor.getData();
                if (value != null) {
                    for (EKGDTO i: value) {
                        listGUI.add(i);
                        listDB.add(i);
                        System.out.println("producer produce: "+ i);
                    }
                }
                notify();

            }
            Thread.sleep(1);
        }
    }
    public void consume() throws InterruptedException {
        while (true) {
            LinkedList<EKGDTO> consumeListGUI;
            synchronized (this) {
                while (listGUI.size() < 150)
                    wait();
                consumeListGUI = listGUI;
                if(listenerGUI != null) {
                    listenerGUI.notifyEKG(consumeListGUI);
                }
                listGUI = new LinkedList<>();

            }
        }
    }
    public void consume2() throws InterruptedException {
        while (true) {
            LinkedList<EKGDTO> consumeListDB;
            synchronized (this) {
                while (listDB.size() < 100)
                    wait();
                consumeListDB = listDB;
                if (listenerGUI != null) {
                    listenerGUI.notifyEKG(consumeListDB);
                    System.out.println("consumer consume: " + consumeListDB);
                }
                listDB = new LinkedList<>();
            }
        }
    }
    @Override
    public void registerGUI(EKGListener listenerGUI) {
        this.listenerGUI = listenerGUI;
    }



    public void runThreads () {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consume2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
