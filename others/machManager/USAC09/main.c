#include <stdio.h>
#include "asm.h"  

void print_array(int* arr, int len, const char* message) {
	printf("%s: ", message);
	for(int i = 0; i < len; i++) {
		printf("%d ", arr[i]);
	}
	printf("\n");
}

int main() {
    //Ascending order
	int arr1[] = {64, 34, 25, 12, 22, 11, 90};
	int len1 = sizeof(arr1) / sizeof(arr1[0]);
	print_array(arr1, len1, "Original array");
    
	int result1 = sort_array(arr1, len1, 1);  // 1 for ascending
	if(result1) {
		print_array(arr1, len1, "Sorted ascending");
	} else {
		printf("Sort failed for ascending order\n");
	}
    
    //Descending order
	int arr2[] = {11, 22, 33, 44, 55, 66, 77};
	int len2 = sizeof(arr2) / sizeof(arr2[0]);
	print_array(arr2, len2, "\nOriginal array");
    
	int result2 = sort_array(arr2, len2, 0);  // 0 for descending
	if(result2) {
		print_array(arr2, len2, "Sorted descending");
	} else {
		printf("Sort failed for descending order\n");
	}
    
    //Array with duplicate elements
	int arr3[] = {5, 2, 8, 5, 1, 9, 2, 8};
	int len3 = sizeof(arr3) / sizeof(arr3[0]);
	print_array(arr3, len3, "\nOriginal array with duplicates");
    
	int result3 = sort_array(arr3, len3, 1);  // 1 for ascending
	if(result3) {
		print_array(arr3, len3, "Sorted ascending with duplicates");
	} else {
		printf("Sort failed for array with duplicates\n");
	}
    
    //Invalid length
	int arr4[] = {1};
	int result4 = sort_array(arr4, 0, 1);
	printf("\nTest with invalid length (0): %s\n", result4 ? "Incorrectly succeeded" : "Correctly failed");
    
    //Single element
	int arr5[] = {42};
	int len5 = 1;
	print_array(arr5, len5, "\nSingle element array");
    
	int result5 = sort_array(arr5, len5, 1);
	if(result5) {
		print_array(arr5, len5, "After sort");
	} else {
		printf("Sort failed for single element\n");
	}

	return 0;
}
