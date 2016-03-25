package org.niobe.task;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: relex lawl
 * Date: 1/28/13
 * Time: 6:15 PM
 *
 * Used to manage and run multiple tasks.
 */
public final class TaskManager {

	/**
	 * The TaskManager constructor at which to
	 * initialize all variables.
	 */
    public TaskManager() {
    	tasks = new ArrayList<Task>();
    	pendingTasks = new ArrayDeque<Task>();
    }

	/**
	 * This list contains all current tasks to be
	 * performed by the pulse.
	 */
    private final List<Task> tasks;
    
    /**
     * This queue contains all tasks that 
     * need to be added to the {@link #tasks} list.
     */
    private final Queue<Task> pendingTasks;

	/**
	 * Creates a new task for the executor service to perform.
	 * @param task		The task to add onto the tasks list.
	 */
    public void submit(Task task) {
        pendingTasks.add(task);
    }

    /**
     * The tick that will be called
     * every {@link org.niobe.world.World#pulse()}.
     */
    public void pulse() {
    	Task task = null;
    	while ((task = pendingTasks.poll()) != null) {
			tasks.add(task);
		}
    	for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();) {
			task = iterator.next();
			if (task.isActive()) {
				task.tick();
			} else {
				iterator.remove();
			}
		}
    }
}
