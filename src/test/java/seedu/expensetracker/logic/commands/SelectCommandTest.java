package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.logic.commands.CommandTestUtil.showExpenseAtIndex;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_THIRD_EXPENSE;

import org.junit.Rule;
import org.junit.Test;

import seedu.expensetracker.commons.core.Messages;
import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.commons.events.ui.JumpToListRequestEvent;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.ui.testutil.EventsCollectorRule;

/**
 * Contains integration tests (interaction with the Model) for {@code SelectCommand}.
 */
public class SelectCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = getTypicalModel();
    private Model expectedModel = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() throws NoUserSelectedException {
        Index lastExpenseIndex = Index.fromOneBased(model.getFilteredExpenseList().size());
        assertExecutionSuccess(INDEX_FIRST_EXPENSE);
        assertExecutionSuccess(INDEX_THIRD_EXPENSE);
        assertExecutionSuccess(lastExpenseIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() throws NoUserSelectedException {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredExpenseList().size() + 1);
        SelectCommand selectCommand = new SelectCommand(outOfBoundsIndex);
        assertCommandFailure(selectCommand, model, commandHistory, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws NoUserSelectedException {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);
        showExpenseAtIndex(expectedModel, INDEX_FIRST_EXPENSE);

        assertExecutionSuccess(INDEX_FIRST_EXPENSE);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() throws NoUserSelectedException {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);
        showExpenseAtIndex(expectedModel, INDEX_FIRST_EXPENSE);

        Index outOfBoundsIndex = INDEX_SECOND_EXPENSE;
        // ensures that outOfBoundIndex is still in bounds of expense tracker list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getExpenseTracker().getExpenseList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectCommand selectFirstCommand = new SelectCommand(INDEX_FIRST_EXPENSE);
        SelectCommand selectSecondCommand = new SelectCommand(INDEX_SECOND_EXPENSE);

        // same object -> returns true
        assertTrue(selectFirstCommand.equals(selectFirstCommand));

        // same values -> returns true
        SelectCommand selectFirstCommandCopy = new SelectCommand(INDEX_FIRST_EXPENSE);
        assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstCommand.equals(null));

        // different expense -> returns false
        assertFalse(selectFirstCommand.equals(selectSecondCommand));
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        SelectCommand selectCommand = new SelectCommand(index);
        String expectedMessage = String.format(SelectCommand.MESSAGE_SELECT_EXPENSE_SUCCESS, index.getOneBased());

        assertCommandSuccess(selectCommand, model, commandHistory, expectedMessage, expectedModel);

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SelectCommand selectCommand = new SelectCommand(index);
        assertCommandFailure(selectCommand, model, commandHistory, expectedMessage);
    }
}
