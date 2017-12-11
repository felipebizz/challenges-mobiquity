package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Package;
import com.mobiquityinc.packer.model.PackagingScenario;

import java.util.LinkedList;
import java.util.List;

public class Packer {

    public static String pack(String _filename) throws APIException {
        InputReader reader = new InputReader();

        StringBuffer results = new StringBuffer();
        Packer packer = new Packer();
        reader.readFile(_filename).forEach( scenario -> {
            try {
                Package bestPackage = packer.maximizeCostOfPackage(scenario);
                results.append(bestPackage.result2String()).append(System.getProperty("line.separator"));
            } catch (APIException e) {
                results.append("Error: " + e.getMessage()).append(System.getProperty("line.separator"));
            }
        });
        return results.toString();
    }

    /**
     * Searches for the highest total cost combining one or more items that fit on the package.
     */
    public Package maximizeCostOfPackage(PackagingScenario _scenario) throws APIException {
        List<Package> finalCandidatePackages = this.calculateBestPackages(_scenario);
        return this.chooseTheBestPackage(finalCandidatePackages);
    }

    private List<Package> calculateBestPackages(PackagingScenario _scenario) throws APIException {
        List<Package> previousIteration = new LinkedList<>();
        List<Package> currentIteration = new LinkedList<>();

        for (Item currentItem : _scenario.getItemsToPack()) {
            currentIteration.clear();
            for (int weight = 1; weight <= _scenario.getPackageMaxWeight(); weight++) {
                Package currentPackage = (previousIteration.size() < weight ?
                        new Package(weight) :
                        previousIteration.get(weight - 1).clone());
                // first we make sure the item fits in this package (considering it as being empty)
                if (currentItem.getWeight() <= currentPackage.getTotalWeight()) {
                    if (currentItem.getWeight() <= currentPackage.getFreeWeight()) {
                        // the item fits in the free weight of the package!
                        currentPackage.addItem(currentItem);
                    } else {
                        currentPackage = this.verifyAndSwitchItemsInCurrentPackage(currentItem, currentPackage,
                                                                                   previousIteration);
                    }
                }
                currentIteration.add(currentPackage);
            }
            // last iteration becames the previous one, and we start over again
            previousIteration.clear();
            previousIteration.addAll(currentIteration);
        }
        return currentIteration;
    }

    /**
     * When an item fits the package but the current free weight is not enough, we must check if removing one or more
     * items to fit the current one can result in a higher cost. If so, we proceed switching the items in the current
     * package disposition.
     */
    private Package verifyAndSwitchItemsInCurrentPackage(Item _currentItem, Package _currentPackage,
                                                         List<Package> _previousIteration) throws APIException {
        // check if removing the item's weight to fit it is better then ignoring the item
        int minPackageWeight =  (int)Math.floor(_currentPackage.getTotalWeight()-_currentItem.getWeight());
        boolean updateCurrentPackage = false;
        Package previousPackageWithRoomForItem = null;
        if (minPackageWeight == 0) {
            // if element must be put alone, then its cost must be greater than the current package cost
            updateCurrentPackage = _currentItem.getCost() > _currentPackage.getTotalCost();
        } else {
            // if element might be combined with another, we need to check previous packages. Since weights
            // are floating point numbers, we must test the "floor" & "ceiling" of package.maxWeight - item.weight,
            // because in the "ceiling" there might be room for the item.
            for (int j = 0; j <= 1; j++) {
                previousPackageWithRoomForItem = _previousIteration.get(minPackageWeight - j);
                // we can update the current package using the previous + this item if:
                //  1) the previous package has room (=free weight) for this item AND
                //  2) the previous package cost + this item cost is greater then the current package cost
                updateCurrentPackage = (
                        ((previousPackageWithRoomForItem.getUsedWeight() + _currentItem.getWeight()) <= _currentPackage.getTotalWeight()) &&
                                ((previousPackageWithRoomForItem.getTotalCost() + _currentItem.getCost()) > _currentPackage.getTotalCost())
                );
                // stop if we found a combination where the current package can be updated
                if (updateCurrentPackage) { break; }
            }
        }
        // update the current package, if we reached a higher cost with the current item
        if (updateCurrentPackage) {
            _currentPackage.resetPackage();
            _currentPackage.addItem(_currentItem);
            if (previousPackageWithRoomForItem != null) {
                _currentPackage.addAllItems(previousPackageWithRoomForItem.getItensInside());
            }
        }
        return _currentPackage;
    }

    /**
     * After all calculations, we end up with a set of "best solutions". Now we must choose which one weights less.
     */
    private Package chooseTheBestPackage(List<Package> _finalPackages) {
        // now we need to find the best option with the highest cost inside
        Package bestPackage = new Package(0);
        for (int index = _finalPackages.size()-1; index > 0; index--) {
            if (_finalPackages.get(index).getTotalCost() >= bestPackage.getTotalCost()) {
                bestPackage = _finalPackages.get(index);
            }
        }
        return bestPackage;
    }
}
