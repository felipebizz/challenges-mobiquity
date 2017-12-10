package com.mobiquityinc.packer.test;

import com.mobiquityinc.packer.Packer;
import com.mobiquityinc.packer.model.Package;
import com.mobiquityinc.packer.model.PackagingScenario;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class FromChallengePDFPackerTestcase extends AnyTestcase {

    @Test
    public void testFirstLineOfPDF() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) " +
                                                           "(4,72.30,€76) (5,30.18,€9) (6,46.34,€48)");
            // sanity check
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(81, singeScenario.getPackageMaxWeight());
            assertEquals(5, singeScenario.getItemsToPack().size());
            assertEquals(1, singeScenario.getOverweightItemsCount());
            // packer test
            Packer packer = new Packer();
            Package packageInfo = packer.maximizeCostOfPackage(singeScenario);
            assertEquals("4", packageInfo.result2String());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }

    @Test
    public void testSecondLineOfPDF() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("8 : (1,15.3,€34)");
            // sanity check
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(8, singeScenario.getPackageMaxWeight());
            assertEquals(0, singeScenario.getItemsToPack().size());
            assertEquals(1, singeScenario.getOverweightItemsCount());
            // packer test
            Packer packer = new Packer();
            Package packageInfo = packer.maximizeCostOfPackage(singeScenario);
            assertEquals("-", packageInfo.result2String());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }

    @Test
    public void testThirdLineOfPDF() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) " +
                            "(4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)");
            // sanity check
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(75, singeScenario.getPackageMaxWeight());
            assertEquals(5, singeScenario.getItemsToPack().size());
            assertEquals(4, singeScenario.getOverweightItemsCount());
            // packer test
            Packer packer = new Packer();
            Package packageInfo = packer.maximizeCostOfPackage(singeScenario);
            String resultString = packageInfo.result2String();
            assertEquals(3, resultString.length());
            assertTrue( resultString.indexOf("2") >= 0);
            assertTrue( resultString.indexOf("7") >= 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }


    @Test
    public void testFourthLineOfPDF() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) "+
                            "(5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)");
            // sanity check
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(56, singeScenario.getPackageMaxWeight());
            assertEquals(7, singeScenario.getItemsToPack().size());
            assertEquals(2, singeScenario.getOverweightItemsCount());
            // packer test
            Packer packer = new Packer();
            Package packageInfo = packer.maximizeCostOfPackage(singeScenario);
            String resultString = packageInfo.result2String();
            assertEquals(3, resultString.length());
            assertTrue( resultString.indexOf("8") >= 0);
            assertTrue( resultString.indexOf("9") >= 0);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }
}
