package org.routing.software.generators.multischeduling;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.routing.software.OperationType;
import org.routing.software.helper.Debug;
import org.routing.software.jpos.LocationNodeJpo;
import org.routing.software.model.*;
import org.routing.software.untils.RandomUtil;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//It will propably be the solver
public class MultiSchedulingGenerator {

    private PlanToSolve planToSolve;

    private Long initialPopulation = 10L;
    List<Assignment> assignmentPool = new ArrayList<>();
    List<List<OperationType>> chromosomePool = new ArrayList<>();
    List<Assignment> generatedSolution = new ArrayList<>();
    List<Assignment> remainingAssignmentPool = new ArrayList<>(assignmentPool); //two lists are independent but objects inside refer to the same
    List<Assignment> currentAssignmentPool = new ArrayList<>();
    Map<Integer, List<Integer>> sourceTimeslotsThatAreOccupatedDueToLoading = new HashMap<>(); //multiple unloading points for a single location node source
    Map<Integer, List<Integer>> locationNodesTimeslotsThatAreOccupatedDueToUnloading = new HashMap<>();
    private int NUMBER_OF_LOADING_SPOTS = 2;
    private Solution solution = new Solution();

    //Phase 2 from the current assignment pool we start by creating
    //Based on the number of the assignments (we suppose that from phase 1 the demand is
    //fullfilled) we create the allele number

    public void createInitialChromosomePool() {

        int alleles = assignmentPool.size();
        alleles = 8; //TODO REMOVE

        //Produce initial ChromosomePopulation
        for (int i = 0; i < initialPopulation; i++) {
            //Produce the alleles of each chromosome
            List<OperationType> chromosome = new ArrayList<>();
            for (int j = 0; j < alleles; j++) {
                int randomNumber = RandomUtil.produceRandomNumber(0, OperationType.values().length);
                OperationType selectedOperationType = OperationType.values()[randomNumber];
                chromosome.add(selectedOperationType);
            }
            chromosomePool.add(chromosome);
        }
        Debug.print(chromosomePool);

        //also solution should be class that contains assignments
        //also check the abstract plan in order to be decoupled and can print
        //how much work per tank remains the least
        //how much work per truck
    }

    //we want a function that we will put the chromosome and  will create the solution
    private void calculateSolution(Chromosome chromosome) {


    }

    //function that picks current solution, then choses the operation that is defined by the allele,
    //then for each of the remaining assignments calculate the best assignment based on the operation type
    //when it finds the best assignment it puts it on a solution list, then it adds the assignment in
    //currentAssignmentPool and removes it remainingAssignmentPool
    public void findBestAssignment(Chromosome chromosome) {

        for (int i = 0; i < chromosome.getChromosome().size(); i++) {

            OperationType operationType = chromosome.getChromosome().get(i);
            Assignment assignment = alleleHandler(operationType);
            currentAssignmentPool.add(assignment);
            remainingAssignmentPool.remove(assignment);

        }
    }

    public Assignment alleleHandler(OperationType operationType) {


        if (operationType.equals(OperationType.LOR)) {
            //execute LOR
        } else if (operationType.equals(OperationType.MOR)) {
            //execute MOR
        } else if (operationType.equals(OperationType.LWR)) {
            //execute LWR
        } else if (operationType.equals(OperationType.MWR)) {
            //execute MWR
        } else if (operationType.equals(OperationType.LPT)) {
            //execute LPT
        } else if (operationType.equals(OperationType.SPT)) {
            //execute SPT
        }

        return new Assignment(); //TODO REMOVE
    }


    public void routeCreationBasedOnAssignmentList(List<Assignment> assignments) {
        long freeTimeslotInSource = 0;
        long assignementCompleteTimeslot = 0;
        initializeLoadingSpotsOnSource();

        //for each assignment as ordered in list start placing
        for (int i = 0; i < remainingAssignmentPool.size(); i++) {

            Assignment assignment = assignments.get(0);
            Truck pickedTruck = assignment.getTruck();
            LocationNode locationNode = assignment.getLocationNode();
            Map<Long, Map<Long, List<Long>>> truckDistanceMatrices =
                    planToSolve.getTruckDistanceMatrices();
            Map<Long, List<Long>> truckDistanceMatrix = truckDistanceMatrices.get(pickedTruck.getId());
            //We want only the matrix for source to tanks
            List<Long> distanceOfLocationNodesFromSource = truckDistanceMatrix.get(0L);
            Long travelTimeToNode = distanceOfLocationNodesFromSource.get(locationNode.getId());

            //Check in which timeslots it will unload on location node
            List<Integer> timeslotThatCouldUnloadOnLocationNode = new ArrayList<>();
            long startUnloadingTimeslot = freeTimeslotInSource + travelTimeToNode + 1;
            long finishUnloadingTimeslot = startUnloadingTimeslot + pickedTruck.getConvertedUnloadingTime();

            for (int j = (int) startUnloadingTimeslot; j < (int) finishUnloadingTimeslot; j++) {
                timeslotThatCouldUnloadOnLocationNode.add(j);
            }

            List<Integer> occupiedTimeslotsForNode = locationNodesTimeslotsThatAreOccupatedDueToUnloading.get(locationNode.getId());
            long timeslotThatWillActuallyLoad = defineTimeslotThatWillActuallyUnload(timeslotThatCouldUnloadOnLocationNode, occupiedTimeslotsForNode);

            long timeslotThatItWillBeBackToSource = timeslotThatWillActuallyLoad + pickedTruck.getConvertedUnloadingTime() + travelTimeToNode;

            //We assume a single source location node. HOWEVER, one single source can have multiple unloading points.
            freeTimeslotInSource = findFreeTimeslotInSource(this.sourceTimeslotsThatAreOccupatedDueToLoading);
            //now we must check also if it could load or not in source location mode, due to loading of another truck
            //is a spot free in source
            assignementCompleteTimeslot = Math.max(freeTimeslotInSource, timeslotThatItWillBeBackToSource);


            Map<Integer, List<Integer>> truckLoadingTimeslot = new HashMap<>();
            truckLoadingTimeslot.computeIfAbsent(pickedTruck.getId(), k -> new ArrayList<>())
                    .add(assignementCompleteTimeslot);



            //TODO  check how is going to be implemented with operation type. maybe void return is not the best,
            // maybe its best to return when the truck is ready to load in depot(keep in mind delay in depot and also multiple depots)
            //and based on this return th timeslot based on the assignments provided.ie in the currently assigned assignments
            //each unsigned assignment from pool is selected and added and calculate and get the best result based on operation type
            //objective.
        }



    }

    /**
     * It will take the timeslots that the investigated truck could actually load on its n-th trip
     * and based on the availability of the locationNode assigned it will check overlapping and will return
     * the timeslot that actually will unload
     *
     * @param timeslotThatCouldUnloadOnLocationNode
     * @return the timeslot that will actually unload
     */
    public long defineTimeslotThatWillActuallyUnload(List<Integer> timeslotThatCouldUnloadOnLocationNode,
                                                     List<Integer> occupiedTimeslotsForNode) {

        //Get the first and the last unload timeslots of locationNode
        int lastUnloadTimeslotForLocationNode = occupiedTimeslotsForNode.get(occupiedTimeslotsForNode.size() - 1);
        int firstUnloadTimeslotForLocationNode = occupiedTimeslotsForNode.get(0);

        //Get the first and last unload timeslots of truck
        int firstUnloadTimeslotForTruck = timeslotThatCouldUnloadOnLocationNode.get(0);

        //Check the case, locationNode does not have any occupied timeslots
        if (occupiedTimeslotsForNode.isEmpty()) {
            return firstUnloadTimeslotForTruck;
        }

        //Check the case trucks starts loading after last unload timeslot of occupationList
        if (firstUnloadTimeslotForTruck > lastUnloadTimeslotForLocationNode) {
            return firstUnloadTimeslotForTruck;
        }

        //Check the case trucks starts loading just in the last unload timeslot of occupationList
        if (firstUnloadTimeslotForTruck == lastUnloadTimeslotForLocationNode) {
            return firstUnloadTimeslotForTruck + 1;
        }

        //Check the case it starts loading before first unload timeslot and CAN Unload
        if ((truckArrivesBeforeFirstUnloadTimeslotForLocationNode(firstUnloadTimeslotForTruck, firstUnloadTimeslotForLocationNode))
                && (truckUnloadingIsAllowed(firstUnloadTimeslotForTruck, timeslotThatCouldUnloadOnLocationNode.size(), firstUnloadTimeslotForLocationNode))) {
            return firstUnloadTimeslotForTruck;
        }

        //If the loop arrives at this point it means that the unloading will be probably
        //between int firstOccupiedTimeslotForLocationNode.get(0) and firstOccupiedTimeslotForLocationNode.get(firstOccupiedTimeslotForLocationNode.size() - 1)
        //and if all the previous space is occupied it will be placed firstOccupiedTimeslotForLocationNode.get(firstOccupiedTimeslotForLocationNode.size() - 1) + 1
        for (int i = firstUnloadTimeslotForLocationNode + 1; i <= lastUnloadTimeslotForLocationNode; i++) {

            //for each timeslot check if the timeslot exists in occupiedTimeslotsForNode
            //if it exists then we do not need to check and we continue to the next timeslot
            if (!truckArrivesBeforeFirstUnloadTimeslotForLocationNode(firstUnloadTimeslotForTruck, i)) {
                continue;
            }

            if (occupiedTimeslotsForNode.contains(i)) {
                continue;
            }

            //now we check if from the timeslot we currently investigate (i) can fit a solution
            //to fit a solution the next j timeslots in iteration with size equal to the unloading time
            //must not be in occupiedTimeslotsForNode
            boolean isOccupied = false;
            for (int j = i; j < i + timeslotThatCouldUnloadOnLocationNode.size(); j++) {
                if (occupiedTimeslotsForNode.contains(j)) {
                    isOccupied = true;
                    break;
                }
            }

            //if the timeslot we examine is occupied by locationNode then increase
            if (isOccupied) {
                continue;
            } else {
                return i;
            }
        }
        return lastUnloadTimeslotForLocationNode + 1;
    }

    private boolean truckArrivesBeforeFirstUnloadTimeslotForLocationNode(int truckFirstUnloadTimeslot,
                                                                         int firstOccupiedTimeslotForLocationNode) {
        return truckFirstUnloadTimeslot <= firstOccupiedTimeslotForLocationNode;
    }

    private boolean truckUnloadingIsAllowed(int truckUnloadTimeslot,
                                            int truckTotalUnloadingTimeInTimeslots,
                                            int occupiedTimeslotForLocationNode) {
        return truckUnloadTimeslot + truckTotalUnloadingTimeInTimeslots - 1 < occupiedTimeslotForLocationNode;
    }

    private Long getSourceNodeId() {
       return planToSolve
               .getLocationNodesList()
               .stream()
               .filter(LocationNodeJpo::isSource)
               .collect(Collectors.toList())
               .get(0)
               .getId();
    }

    /**
     * The source location node can have multiple unloading spots. ie multiple trucks CAN load at the same source (depot)
     * location node at the same time. The function checks and finds the most available (i.e nearest to 0) but also
     * larger than the current ALL occupied timeslots in list. i.e the UNLOADING_SPOTS refer to the SAME source location node.
     * KEEP IN MIND it is meant to be loading TILL each last timeslot. No intervals allowed (see relative test no3)
     */
    public int findFreeTimeslotInSource(Map<Integer, List<Integer>> sourceTimeslotsThatAreOccupatedDueToLoading) {
        //we iterate through all spots and get their last timeslot
        //then from the stuff we get we take the smaller
        int freeTimeslot = 0;
        for (int spot = 0; spot < NUMBER_OF_LOADING_SPOTS; spot++) {
            //check empty
            List<Integer> sourceTimeslotsForSpotList;
            spot++;
            Integer maxSourceTimeslotsForSpotList;
            int finalSpot = spot;
            sourceTimeslotsForSpotList =
                    sourceTimeslotsThatAreOccupatedDueToLoading
                            .entrySet()
                            .stream()
                            .filter(c -> c.getKey().equals(finalSpot))
                            .flatMap(p -> p.getValue().stream()).toList();

            maxSourceTimeslotsForSpotList = sourceTimeslotsForSpotList.get(sourceTimeslotsForSpotList.size() - 1);
            if (freeTimeslot < maxSourceTimeslotsForSpotList) {
                freeTimeslot = maxSourceTimeslotsForSpotList;

                //populate the relative set
                sourceTimeslotsThatAreOccupatedDueToLoading
                        .computeIfAbsent(spot, k -> new ArrayList<>())
                        .add(freeTimeslot);
            }
        }
        return freeTimeslot + 1;

    }

    /**
     *  Initialize the arraylists for each unloading spot
     */
    private void initializeLoadingSpotsOnSource() {
        for (int i = 0; i < NUMBER_OF_LOADING_SPOTS; i++) {
            int spotId = i + 1;
            List<Integer> spotList = new ArrayList<>();
            sourceTimeslotsThatAreOccupatedDueToLoading.put(spotId, spotList);
        }
    }

}





