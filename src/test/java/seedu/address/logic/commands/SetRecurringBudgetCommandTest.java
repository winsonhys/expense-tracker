package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalExpenses.getTypicalExpenseTracker;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.address.logic.CommandHistory;
import seedu.address.model.ExpenseTracker;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.testutil.ExpenseTrackerBuilder;


public class SetRecurringBudgetCommandTest {
    private Model model = new ModelManager(getTypicalExpenseTracker(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private long newRecurrenceFrequency = 123456;

    @BeforeEach
    public void setUp() {
        this.model = new ModelManager(getTypicalExpenseTracker(), new UserPrefs());
        this.commandHistory = new CommandHistory();
    }

    @Test
    public void execute_setRecurrence_successful() throws NoUserSelectedException {
        ExpenseTracker emptyBook = new ExpenseTrackerBuilder().build();
        model = new ModelManager(emptyBook, new UserPrefs());
        ModelManager expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs());
        expectedModel.setRecurrenceFrequency(this.newRecurrenceFrequency);
        expectedModel.commitExpenseTracker();
        SetRecurringBudgetCommand setRecurringBudgetCommand = new SetRecurringBudgetCommand(newRecurrenceFrequency);
        String expectedMessage = String.format(SetRecurringBudgetCommand.MESSAGE_SUCCESS, this.newRecurrenceFrequency);
        assertCommandSuccess(setRecurringBudgetCommand, model, commandHistory, expectedMessage, expectedModel);
        assertNotNull(expectedModel.getMaximumBudget().getNextRecurrence());
        assertTrue(this.newRecurrenceFrequency
            == expectedModel.getExpenseTracker().getMaximumBudget().getNumberOfSecondsToRecurAgain());

    }
    @Test
    public void execute_updateRecurrence_successful() throws NoUserSelectedException {
        ModelManager expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs());
        long initialRecurrenceFrequency =
            expectedModel.getExpenseTracker().getMaximumBudget().getNumberOfSecondsToRecurAgain();
        expectedModel.setRecurrenceFrequency(this.newRecurrenceFrequency);
        expectedModel.commitExpenseTracker();
        String expectedMessage = String.format(SetRecurringBudgetCommand.MESSAGE_SUCCESS, this.newRecurrenceFrequency);
        SetRecurringBudgetCommand setRecurringBudgetCommand = new SetRecurringBudgetCommand(newRecurrenceFrequency);
        assertCommandSuccess(setRecurringBudgetCommand, model, commandHistory, expectedMessage, expectedModel);
        assertFalse(initialRecurrenceFrequency
            == expectedModel.getExpenseTracker().getMaximumBudget().getNumberOfSecondsToRecurAgain());
    }

    @Test
    public void equals() {
        assertTrue(new SetRecurringBudgetCommand(1)
            .equals(new SetRecurringBudgetCommand(1)));
    }

}