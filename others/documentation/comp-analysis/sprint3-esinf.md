# Complexity Analysis per User Story - ESINF Sprint 3


---------------------------

## USEI17 - Build a PERT-CPM graph

### Methods Used

- `vertices()` - O(n)
- `key(Activity vert)` - O(n)
- `vertex(int key)` - O(n)
- `vertex(Predicate<Activity> p)` - O(n)
- `adjVertices(Activity vert)` - O(n)
- `edges()` - O(n)
- `edge(Activity vOrig, Activity vDest)` - O(n)
- `edge(int vOrigKey, int vDestKey)` - O(n)
- `inDegree(Activity vert)` - O(n)
- `outgoingEdges(Activity vert)` - O(n)
- `incomingEdges(Activity vert)` - O(n)
- `addEdge(Activity vOrig, Activity vDest)` - O(n)
- `removeVertex(Activity vert)` - O(n)
- `removeEdge(Activity vOrig, Activity vDest)` - O(n)
- `clone()` - O(n)
- `getIncomingEdges(Activity activity)` - O(n)
- `getOutgoingEdges(Activity activity)` - O(n)
- `addDependency(Activity virtualStart, Activity start)` - O(n)
- `addDependency(Activity src, Activity dst)` - O(n)
- `getNeighbors(Activity activity)` - O(n)
- `getInDegrees()` - O(n)
- `detectCircularDependencies()` - O(n)
- `colorDFS(Activity activity, Map<Activity, Integer> color)` - O(n)
- `findActivityById(String a)` - O(n)
- `getIncomingEdges(Activity activity)` - O(n)
- `getOutgoingEdges(Activity activity)` - O(n)
- `getStartVertices()` - O(n)
- `getEndVertices()` - O(n)
- `getStartActivities()` - O(n)
- `getEndActivities()` - O(n)

### Total Complexity

- **Overall Complexity:** O(n)
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
- `applyDelays(delayMap)` - O(n²)
- `calculateNewMetrics()` - O(n²)
- `displayImpactAnalysis(delayMap)` - O(n²)
- `findCriticalPath()` - O(n²)

### Total Complexity

- **Overall Complexity:** O(n²)
