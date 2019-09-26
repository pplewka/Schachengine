import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SearchTest {
    @Test
    public void getFullGeneratedDepthTest(){
        Move root = new MoveImpl(0,0,'0',null,false);

        Move [] rootchildren = new Move []{new MoveImpl(0,0,'0',null,false),
                                            new MoveImpl(0,0,'0',null,false)};

        root.setChildren(rootchildren);

        Move [] childChildren = new Move []{new MoveImpl(0,0,'0',null,false),
                                            new MoveImpl(0,0,'0',null,false)};

        rootchildren[0].setChildren(childChildren);

        SearchImpl.getSearch().setRoot(root);

        assertEquals(SearchImpl.getSearch().getFullGeneratedDepth(),1);



        root = new MoveImpl(0,0,'0',null,false);

        rootchildren = new Move []{new MoveImpl(0,0,'0',null,false),
                new MoveImpl(0,0,'0',null,false)};

        root.setChildren(rootchildren);

        Move [] child1Children = new Move []{new MoveImpl(0,0,'0',null,false),
                new MoveImpl(0,0,'0',null,false)};

        rootchildren[0].setChildren(child1Children);

        Move [] child2Children = new Move []{new MoveImpl(0,0,'0',null,false),
                new MoveImpl(0,0,'0',null,false)};

        rootchildren[1].setChildren(child2Children);

        SearchImpl.getSearch().setRoot(root);

        assertEquals(SearchImpl.getSearch().getFullGeneratedDepth(),2);
    }

    @Test
    public void switchQueuesWhileBlockedTest(){
        PriorityBlockingQueue<Integer> first = new PriorityBlockingQueue<Integer>();
        PriorityBlockingQueue<Integer> second = new PriorityBlockingQueue<Integer>();

         MyThread [] threads = new MyThread[10];
         MyThread.setQueue(first);
         for(MyThread t: threads){
             t = new MyThread();
             t.start();
         }

        System.out.println("Threads running\nnow waiting");
         Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
         long now = System.currentTimeMillis();
        for(long i=10000000000L; i> -10000000000L;i--){
            i=i;
        }
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        System.out.println("waited "+(System.currentTimeMillis() - now)+" milliseconds");

        for(int i =0; i< 10; i++){
             second.add(i);
         }

         MyThread.setQueue(second);
        System.out.println("set new queue!");
    }
}


class MyThread extends Thread{
    private static PriorityBlockingQueue<Integer> queue;

    public static void setQueue(PriorityBlockingQueue<Integer> newqueue) {
        queue = newqueue;
    }

    @Override
    public void run(){
        try{
            System.out.println("subThread started");
            Integer i=null;
            while(i==null) {
                i = queue.poll(1, TimeUnit.NANOSECONDS);
            }
            System.out.println(i);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}