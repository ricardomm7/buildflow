# Complexity Analysis per User Story - ESINF Sprint 3

---------------------------

## USEI18 - Detect Cycles

### Methods Used

- `detectCircularDependencies()` - O(n)
- `colorDFS(Activity activity, Map<Activity, Integer> color)` - O(n)

### Total Complexity

- **Overall Complexity:** O(n)

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

