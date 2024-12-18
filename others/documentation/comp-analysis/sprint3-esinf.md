# Complexity Analysis per User Story - ESINF Sprint 3

---------------------------

## USEI18 - Detect Cycles

### Methods Used

- `detectCircularDependencies()` - O(n)
- `colorDFS(Activity activity, Map<Activity, Integer> color)` - O(n)

### Total Complexity

- **Overall Complexity:** O(n)

---------------------------

## USEI20 - Calculate Earliest and Latest Start and Finish Times

### Methods Used

- `calculateTimes()` - O(n^2)
- `performTopologicalSort()` - O(n^2)

### Total Complexity

- **Overall Complexity:** O(n^2)

---------------------------
## USEI23 - Identify Bottleneck Activities

### Methods Used

- `countDirectDependencies()` - O(n^2)
- `analyzePathComplexity()` - O(n)
- `combineMetrics(dependencyCounts, pathComplexity)` - O(n)
- `sortBottlenecksTopologically(bottlenecks)` - O(nlog(n))

### Total Complexity

- **Overall Complexity:** O(n^2)

---------------------------

## USEI24 - Simulate Project Delays and their Impact

### Methods Used

- `simulateProjectDelays(Map<String, Integer> delayMap)` - O(n^2)
- `calculateOriginalMetrics()` - O(n^2)
- `saveOriginalDurations()` - O(n)
- `applyDelays(delayMap)` - O(n)
- `calculateNewMetrics()` - O(n^2)
- `displayImpactAnalysis(delayMap)` - O(n)
- `findCriticalPath()` - O(n^2)

### Total Complexity

- **Overall Complexity:** O(n^2)
