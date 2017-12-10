package com.mobiquityinc.packer.test;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.InputReader;
import com.mobiquityinc.packer.Packer;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.PackagingScenario;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InputReaderTestcase extends AnyTestcase {

    @Test
    public void checkWhenFileArgIsNull() {
        try {
            Packer.pack(null);
        } catch (APIException ex1) {
            assertEquals(ex1.getMessage(), "File must exist.");
        } catch (Exception ex2) {
            ex2.printStackTrace();
            fail("Not expecting exceptions of type" + ex2.getClass());
        }
    }

    @Test
    public void checkWhenFileDoesNotExist() {
        try {
            Packer.pack("/file/doesnot/exist.txt");
        } catch (APIException ex1) {
            assertEquals(ex1.getMessage(), "File must exist.");
        } catch (Exception ex2) {
            ex2.printStackTrace();
            fail("Not expecting exceptions of type " + ex2.getClass());
        }
    }

    @Test
    public void loadSuccessfullyAOneItemRow() {
        try {
            Collection<PackagingScenario> scenarios = this.getRowReader().readFile("8 : (1,5.3,€34)");
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(8, singeScenario.getPackageMaxWeight());
            assertEquals(1, singeScenario.getItemsToPack().size());
            assertEquals(0, singeScenario.getOverweightItemsCount());
            Item singleItem = singeScenario.getItemsToPack().iterator().next();
            assertEquals(1, singleItem.getIndex());
            assertEquals(5.3, singleItem.getWeight(), 0.01);
            assertEquals(34, singleItem.getCost());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }

    @Test
    public void loadSuccessfullyAMultiItemRow() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52)");
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(75, singeScenario.getPackageMaxWeight());
            assertEquals(4, singeScenario.getItemsToPack().size());
            assertEquals(1, singeScenario.getOverweightItemsCount());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }

    @Test
    public void loadSuccessfullyARowWithTrailingWhitespaces() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("75 : (1,55.31,€29) (2,14.55,€74) ");
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(75, singeScenario.getPackageMaxWeight());
            assertEquals(2, singeScenario.getItemsToPack().size());
            assertEquals(0, singeScenario.getOverweightItemsCount());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }

}


