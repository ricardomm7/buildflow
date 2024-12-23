# Complexity Analysis per User Story - ESINF Sprint 3


---------------------------

## USEI17 - Build a PERT-CPM graph

### Methods Used

- `` - 
- `` - 

### Total Complexity

- **Overall Complexity:** 
---------------------------

## USEI18 - Detect Cycles

### Methods Used

- `detectCircularDependencies()` - O(n)
- `colorDFS(Activity activity, Map<Activity, Integer> color)` - O(n)

### Total Complexity

- **Overall Complexity:** O(n)

---------------------------

## USEI19 - Topological Sort of project activities

### Methods Used

- `performTopologicalSort()` - O(n²)
- `printSortedActivities(List<Activity> sortedActivities)` - O(n)
- `handleTopologicalSort()` - O(n²)

### Total Complexity

- **Overall Complexity:** O(n²)

## USEI20 - Calculate Earliest and Latest Start and Finish Times

### Methods Used

- `calculateTimes()` - O(n²)
- `performTopologicalSort()` - O(n²)

### Total Complexity

- **Overall Complexity:** O(n²)

---------------------------
## USEI23 - Identify Bottleneck Activities

### Methods Used

- `countDirectDependencies()` - O(n²)
- `analyzePathComplexity()` - O(n)
- `combineMetrics(dependencyCounts, pathComplexity)` - O(n)
- `sortBottlenecksTopologically(bottlenecks)` - O(nlog(n))

### Total Complexity

- **Overall Complexity:** O(n²)

---------------------------

## USEI24 - Simulate Project Delays and their Impact

### Methods Used

- `simulateProjectDelays(Map<String, Integer> delayMap)` - O(n²)
- `calculateOriginalMetrics()` - O(n²)
- `saveOriginalDurations()` - O(n)
- `applyDelays(delayMap)` - O(n)
- `calculateNewMetrics()` - O(n²)
- `displayImpactAnalysis(delayMap)` - O(n)
- `findCriticalPath()` - O(n²)

### Total Complexity

- **Overall Complexity:** O(n^2)
