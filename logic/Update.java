package logic;

import java.util.List;

import storage.Storage;
import parser.ParsedCommand;

public class Update implements Command {

	private static final int EVENT = 3;
	private static final int DEADLINETASK = 2;
	private static final int TASK = 1;
	private ParsedCommand specifications;
	private Task toUpdate;
	private Task updated;
	private Storage storage;

	public Update(Storage storage) {
		this.storage = storage;
	}
	
	public Update(ParsedCommand specifications,Storage storage) {
		this.specifications = specifications;
		this.storage = storage;
	}

	public void execute() {
		List<Task> taskList = storage.getAllTasks();
		toUpdate = Logic.searchList(taskList,specifications.getTaskId());
		// Clone Task
		updated = new Task(toUpdate);
		updated = updateTask(specifications, updated);
		storage.delete(toUpdate.getId());
		storage.add(updated);
	}

	public void undo() {
		storage.delete(updated.getId());
		storage.add(toUpdate);
		storage.sort();
	}

	/*
	 * Returns a Task Object with all the specified fields updated. Does not add
	 * anything to database.
	 */
	public Task updateTask(ParsedCommand parsedInput, Task toUpdate) {

		int taskType = toUpdate.getTaskType();
		if (taskType == TASK) {
			if (parsedInput.getTitle() != null) {
				toUpdate.setName(parsedInput.getTitle());
			}
			if (parsedInput.getDescription() != null) {
				toUpdate.setDetails(parsedInput.getDescription());
			}
			if (parsedInput.getTags().size() != 0) {
				toUpdate.setTags(parsedInput.getTags());
			}
		} else if (taskType == DEADLINETASK) {
			if (parsedInput.getTitle() != null) {
				toUpdate.setName(parsedInput.getTitle());
			}
			if (parsedInput.getDescription() != null) {
				toUpdate.setDetails(parsedInput.getDescription());
			}
			if (parsedInput.getTags().size() != 0) {
				toUpdate.setTags(parsedInput.getTags());
			} 
			if (parsedInput.getFirstDate() != null) {
				((DeadlineTask) toUpdate).setEnd(parsedInput.getFirstDate());
			}
		} else if (taskType == EVENT) {
			if (parsedInput.getTitle() != null) {
				toUpdate.setName(parsedInput.getTitle());
			} 
			if (parsedInput.getDescription() != null) {
				toUpdate.setDetails(parsedInput.getDescription());
			} 
			if (parsedInput.getTags().size() != 0) {
				toUpdate.setTags(parsedInput.getTags());
			}
			if (parsedInput.getSecondDate() != null) {
				((DeadlineTask) toUpdate).setEnd(parsedInput.getSecondDate());
			}
			if (parsedInput.getFirstDate() != null) {
				((Event) toUpdate).setStart(parsedInput.getFirstDate());
			}
		}

		return toUpdate;
	}

	public static boolean checkValid(ParsedCommand parsedInput, View view) {
		int taskType = parsedInput.getTaskType();

		if (parsedInput.getTaskId() >= Logic.getNewId()) {
			view.setConsoleMessage("Error: Invalid TaskID");
			return false;
		}
		if (taskType != 0 || taskType != 1 || taskType != 2) {
			view.setConsoleMessage("Logic Error: task type missing");
		}
		if (taskType == TASK) {
			if (parsedInput.getSecondDate() != null) { // Error: Task Should have no
												// End Field
				view.setConsoleMessage("Error: Task Should have no end field");
				return false;
			} else if (parsedInput.getFirstDate() != null) { // Error: Task Should
															// have no Start
															// Field
				view.setConsoleMessage("Error: Task Should have no start field");
				return false;
			}
		} else if (taskType == DEADLINETASK) {
			if (parsedInput.getSecondDate() != null) { // Error: DeadlineTask Should
													// have no Start Field
				view.setConsoleMessage("Error: DeadlineTask Should have no start field");
				return false;
			}
		} else if (taskType == EVENT) {

		}

		return true;
	}

}
