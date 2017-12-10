package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.PackagingScenario;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads the lines from the input file and translates each one of them into an instance of {@link PackagingScenario}. If
 * the file does not exist, is not a real file or has on line that do not match the predefined format, then an
 * {@link APIException} will be thrown.
 * <br/>
 * The format of each line should be:
 * <p>
 *     <code>{maxPackageWeight}: ({itemIndex},{itemWeight},{itemCost}), ...</code>
 * </p>
 */
public class InputReader {

    private Pattern linePattern = Pattern.compile("\\((\\d),(\\d+\\.?\\d*?),€?(\\d+)\\)");


    /**
     * Entry point for this class. Returns the list of test scenarios if all the lines in the file match the predefined
     * format or an exception otherwise.
     */
    public Collection<PackagingScenario> readFile(String _filename) throws APIException {
        // making sure the file exists!
        if (_filename == null) {
            throw new APIException("File must exist.");
        }
        File inputFile = new File(_filename);
        if (!inputFile.exists() || !inputFile.isFile()) {
            throw new APIException("File must exist.");
        }

        // reading & translating each line
        Collection<PackagingScenario> scenarios = new LinkedList<>();
        try {
            int lineNumber = 1;
            for (String testScenario : FileUtils.readLines(inputFile, Charset.forName("UTF-8"))) {
                scenarios.add(this.parseLine(testScenario.trim(), lineNumber++));
            }
        } catch(IOException ioe) {
            throw new APIException("Error while reading the input file", ioe);
        }
        return scenarios;
    }

    protected PackagingScenario parseLine(String _line, int _lineNumber) throws APIException {
        int idx = _line.indexOf(":");
        if (idx < 0) {
            throw new APIException("Line #" + _lineNumber + " does not conform to the predefined format. Cannot read package max weight!");
        }

        try {
            int pkgMaxWeight = Integer.valueOf(_line.substring(0, idx).trim());
            // going through all items
            Matcher matcher = this.linePattern.matcher(_line.substring(idx+1));
            PackagingScenario scenario = new PackagingScenario(pkgMaxWeight);
            while (matcher.find()) {
                int index = Integer.valueOf(matcher.group(1));
                double weight = Double.parseDouble(matcher.group(2));
                int cost = Integer.valueOf(matcher.group(3));
                Item item = new Item(index, weight, cost);
                scenario.addItem(item);
            }
            if (scenario.getItemsToPack().size() == 0 && scenario.getOverweightItemsCount() == 0) {
                throw new APIException("Line #" + _lineNumber + " does not conform to the predefined format. No item found!");
            }
            return scenario;
        } catch(RuntimeException re) {
            throw new APIException("Error while reading line #" + _lineNumber, re);
        }
    }
}
