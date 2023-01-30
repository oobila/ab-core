package com.github.oobila.bukkit.scheduling;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

@SuppressWarnings("java:S1874")
public abstract class Job extends Observable implements Runnable {

    @Getter
    Plugin plugin;
    boolean hasStarted;
    boolean isComplete;
    boolean canAccompany;
    private Object lock = new Object();
    protected Queue<Job> childJobs = new LinkedList<>();

    protected void addChildJob(Job job){
        job.plugin = this.plugin;
        synchronized (lock){
            childJobs.add(job);
        }
    }

    protected void addAccompanyingChildJob(Job job){
        job.canAccompany = true;
        addChildJob(job);
    }

    void runJob() {
        hasStarted = true;
        run();
        notifyObservers();
        isComplete = true;
    }

    Job getNextJob(){
        synchronized (lock) {

            //remove
            if (childJobs.isEmpty()) {
                return null;
            } else if (childJobs.peek().isComplete && childJobs.peek().childJobs.isEmpty()) {
                childJobs.remove();
            }

            boolean isFirst = true;
            for (Job child : childJobs) {
                if (!child.hasStarted && (isFirst || canAccompany)) {
                    return child;
                } else {
                    Job grandChild = child.getNextJob();
                    if (grandChild != null) {
                        return grandChild;
                    }
                }
                isFirst = false;
            }
            return new WaitJob();
        }
    }

}
