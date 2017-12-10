package com.mobiquityinc.packer.test;

import com.mobiquityinc.packer.Packer;
import com.mobiquityinc.packer.model.Package;
import com.mobiquityinc.packer.model.PackagingScenario;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class PackerTestcase extends AnyTestcase {

    @Test
    public void testSingleItemToFit() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("75 : (1,14.55,€74) ");
            // sanity check
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(75, singeScenario.getPackageMaxWeight());
            assertEquals(1, singeScenario.getItemsToPack().size());
            assertEquals(0, singeScenario.getOverweightItemsCount());
            // packer test
            Packer packer = new Packer();
            Package packageInfo = packer.maximizeCostOfPackage(singeScenario);
            assertEquals("1", packageInfo.result2String());

        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }

    @Test
    public void testNoItemFitOnPackage() {
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
    public void testAllItemsFitOnPackage() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("7 : (1,5.31,€29) (2,1.45,€74) (3,0.21,€24)");
            // sanity check
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(7, singeScenario.getPackageMaxWeight());
            assertEquals(3, singeScenario.getItemsToPack().size());
            assertEquals(0, singeScenario.getOverweightItemsCount());
            // packer test
            Packer packer = new Packer();
            Package packageInfo = packer.maximizeCostOfPackage(singeScenario);
            String resultString = packageInfo.result2String();
            assertEquals(5, resultString.length());
            assertTrue( resultString.indexOf("1") >= 0);
            assertTrue( resultString.indexOf("2") >= 0);
            assertTrue( resultString.indexOf("3") >= 0);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }

    @Test
    public void testItemsWithIntWeightOnly() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("5 : (1,4,€50) (2,2,€40) (3,1,€30) (4,3,€45)");
            // sanity check
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(5, singeScenario.getPackageMaxWeight());
            assertEquals(4, singeScenario.getItemsToPack().size());
            assertEquals(0, singeScenario.getOverweightItemsCount());
            // packer test
            Packer packer = new Packer();
            Package packageInfo = packer.maximizeCostOfPackage(singeScenario);
            String resultString = packageInfo.result2String();
            assertEquals(3, resultString.length());
            assertTrue( resultString.indexOf("2") >= 0);
            assertTrue( resultString.indexOf("4") >= 0);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }

    @Test
    public void testNotAllItemsFitOnPackage() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("8 : (1,8.51,€29) (2,1.45,€74) (3,0.39,€16) (4,2.64,€55) (5,6.36,€52) " +
                            "(6,9.62,€75) (7,6.0,€74) (8,9.38,€35) (9,8.95,€78)");
            // sanity check
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(8, singeScenario.getPackageMaxWeight());
            assertEquals(5, singeScenario.getItemsToPack().size());
            assertEquals(4, singeScenario.getOverweightItemsCount());
            // packer test
            Packer packer = new Packer();
            Package packageInfo = packer.maximizeCostOfPackage(singeScenario);
            String resultString = packageInfo.result2String();
            assertEquals(5, resultString.length());
            assertTrue( resultString.indexOf("2") >= 0);
            assertTrue( resultString.indexOf("7") >= 0);
            assertTrue( resultString.indexOf("3") >= 0);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expecting exceptions of type " + e.getClass());
        }
    }

    @Test
    public void testAVeryLargeDataSet() {
        try {
            Collection<PackagingScenario> scenarios =
                    this.getRowReader().readFile("6 : (1,9.72,€13) (2,3.38,€40) (3,4.35,€10) (4,3.79,€16) " +
                            " (5,4.68,€36) (6,4.87,€79) (7,8.10,€45) (8,1.96,€79) (9,0.67,€64)");
            // sanity check
            assertEquals(1, scenarios.size());
            PackagingScenario singeScenario = scenarios.iterator().next();
            assertEquals(6, singeScenario.getPackageMaxWeight());
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
