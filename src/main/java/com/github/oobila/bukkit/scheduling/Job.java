package com.github.oobila.bukkit.scheduling;

import lombok.Getter;
import org.apache.commons.lang.time.StopWatch;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;

@SuppressWarnings("java:S1874")
public abstract class Job implements Runnable {

    @Getter
    Plugin plugin;
    boolean hasStarted;
    boolean isComplete;
    boolean canAccompany;
    protected Object lock = new Object();
    protected Set<Runnable> observers = new HashSet<>();
    protected Queue<Job> childJobs = new LinkedList<>();
    protected Job parentJob;

    protected void addChildJob(Job job){
        job.plugin = this.plugin;
        job.parentJob = this;
        synchronized (lock){
            childJobs.add(job);
        }
    }

    protected void addAccompanyingChildJob(Job job){
        job.canAccompany = true;
        addChildJob(job);
    }

    protected void informParent() {
        if (parentJob != null) {
            if (childJobs.isEmpty()) {
                synchronized (lock) {
                    parentJob.childJobs.remove(this);
                }
            }
            if(parentJob.childJobs.isEmpty()) {
                parentJob.informParent();
            }
        }
        if(childJobs.isEmpty()) {
            observers.forEach(Runnable::run);
        }
    }

    public void addObserver(Runnable runnable) {
        observers.add(runnable);
    }

    void runJob() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        hasStarted = true;
        run();
        informParent();
        isComplete = true;
        stopWatch.stop();

        if (stopWatch.getTime() > 20) {
            Bukkit.getLogger().log(Level.WARNING, "Long running job: " + this.getClass().getSimpleName() + " - " + stopWatch);
        }
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
