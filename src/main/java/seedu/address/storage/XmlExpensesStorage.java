package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * A class to access AddressBook data stored as an xml file on the hard disk.
 */
public class XmlExpensesStorage implements ExpensesStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlExpensesStorage.class);

    private Path filePath;

    public XmlExpensesStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getExpensesFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyAddressBook> readExpenses() throws DataConversionException, IOException {
        return readExpenses(filePath);
    }

    /**
     * Similar to {@link #readExpenses()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyAddressBook> readExpenses(Path filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("AddressBook file " + filePath + " not found");
            return Optional.empty();
        }

        XmlSerializableAddressBook xmlAddressBook = XmlFileStorage.loadDataFromSaveFile(filePath);
        try {
            return Optional.of(xmlAddressBook.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveExpenses(ReadOnlyAddressBook addressBook) throws IOException {
        saveExpenses(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveExpenses(ReadOnlyAddressBook)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveExpenses(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlSerializableAddressBook(addressBook));
    }

}
