package org.niobe.task;

/**
 * Created with IntelliJ IDEA.
 * User: relex lawl
 * Date: 1/28/13
 * Time: 6:12 PM
 *
 * Represents a single task which will then be stored
 * in the {@link TaskManager} executor service.
 */
public abstract class Task {

	/**
	 * The Ticks constructor.
	 * @param ticks		The amount of ticks (600 milliseconds/tick) for the task to be performed.
	 */
    public Task(int ticks) {
        if (ticks < 0)
            throw new IllegalArgumentException("Task ticks must not be negative!");
        this.ticks = ticks;
        this.startDelay = ticks;
    }
    
    /**
	 * The Ticks constructor.
	 */
    public Task() {
       this(1);
    }

	/**
	 * The amount of ticks to wait for the task to be performed.
	 */
    private int ticks;
    
    /**
     * The starting amount of ticks, to set back to once
     * the {@link Task} is executed.
     */
    private final int startDelay;
    
    /**
     * Checks if the task should stop from being executed.
     */
    private boolean stop;

	/**
	 * The action to be performed once {@param ticks} hits 0.
	 */
    public abstract void execute();

	/**
	 * This method is called every 600 ms in the
	 * scheduled executor service.
	 */
    public void tick() {
        if (ticks-- <= 0) {
            execute();
            ticks = startDelay;
        }
    }
    
    /**
     * Stops the task from executing.
     */
    public void stop() {
    	stop = true;
    }

	/**
	 * Checks if this task is active.
	 * @return 		If <code>true</code> the task will keep executing
	 * 				once tick hits 0.
	 */
    public boolean isActive() {
        return !stop;
    }
}
