package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;

import java.util.Comparator;
import java.util.Map;

/**
 * A generic implementation of an AVL tree, a self-balancing binary search tree.
 * The tree supports insertion of elements and ensures that the height difference
 * between the left and right subtrees of any node is at most 1.
 *
 * @param <T> the type of elements stored in the AVL tree. Elements must either
 *            implement {@link Comparable} or be provided with a {@link Comparator}.
 */
public class AVLTree<T> {
    /**
     * Represents a node in the AVL tree.
     */
    private class Node {
        T key;           // The value stored in the node.
        Node left;       // Reference to the left child.
        Node right;      // Reference to the right child.
        int height;      // The height of the node in the tree.

        /**
         * Constructs a new node with the given key.
         *
         * @param key the value to be stored in the node.
         */
        Node(T key) {
            this.key = key;
            height = 1;
        }
    }

    private Node root; // The root node of the AVL tree.
    private final Comparator<T> comparator; // Optional comparator for custom sorting.

    /**
     * Constructs an empty AVL tree that uses natural ordering for comparisons.
     */
    public AVLTree() {
        this.comparator = null;
    }

    /**
     * Constructs an empty AVL tree with a custom comparator for ordering elements.
     *
     * @param comparator the comparator to define the order of elements.
     */
    public AVLTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Inserts a key into the AVL tree.
     *
     * @param key the value to be inserted.
     */
    public void insert(T key) {
        root = insertRec(root, key);
    }

    /**
     * Recursively inserts a key into the subtree rooted at the given node.
     *
     * @param node the root of the subtree.
     * @param key  the value to be inserted.
     * @return the updated root of the subtree.
     */
    private Node insertRec(Node node, T key) {
        if (node == null) {
            return new Node(key);
        }

        int compare = (comparator == null) ? ((Comparable<T>) key).compareTo(node.key)
                : comparator.compare(key, node.key);

        if (compare < 0) {
            node.left = insertRec(node.left, key);
        } else if (compare > 0) {
            node.right = insertRec(node.right, key);
        } else {
            return node; // Duplicate keys are not allowed.
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return balance(node);
    }

    /**
     * Balances the subtree rooted at the given node to ensure AVL properties.
     *
     * @param node the root of the subtree.
     * @return the balanced root of the subtree.
     */
    private Node balance(Node node) {
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Performs a right rotation on the subtree rooted at the given node.
     *
     * @param y the root of the subtree.
     * @return the new root of the rotated subtree.
     */
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T = x.right;

        x.right = y;
        y.left = T;

        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));

        return x;
    }

    /**
     * Performs a left rotation on the subtree rooted at the given node.
     *
     * @param x the root of the subtree.
     * @return the new root of the rotated subtree.
     */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T = y.left;

        y.left = x;
        x.right = T;

        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));

        return y;
    }

    /**
     * Returns the height of the given node.
     *
     * @param node the node whose height is to be determined.
     * @return the height of the node, or 0 if the node is null.
     */
    private int getHeight(Node node) {
        return (node == null) ? 0 : node.height;
    }

    /**
     * Returns the balance factor of the given node.
     *
     * @param node the node whose balance factor is to be determined.
     * @return the difference between the heights of the left and right subtrees.
     */
    private int getBalanceFactor(Node node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    /**
     * Performs an in-order traversal of the AVL tree and processes each node
     * based on its dependency level from the provided map.
     *
     * @param nodeDependencyLevels a map containing the dependency levels of {@link ProductionNode}s.
     */
    public void inOrderTraversal(Map<ProductionNode, Integer> nodeDependencyLevels) {
        inOrderRec(root, nodeDependencyLevels);
    }

    /**
     * Recursively performs an in-order traversal of the subtree rooted at the given node.
     *
     * @param node                 the root of the subtree.
     * @param nodeDependencyLevels a map containing the dependency levels of {@link ProductionNode}s.
     */
    private void inOrderRec(Node node, Map<ProductionNode, Integer> nodeDependencyLevels) {
        if (node != null) {
            // Traverse the left subtree first
            inOrderRec(node.left, nodeDependencyLevels);

            // Process the current node
            ProductionNode productionNode = (ProductionNode) node.key;
            int dependencyLevel = nodeDependencyLevels.getOrDefault(productionNode, -1);

            // Simulate processing
            System.out.println("Processing node in BOO order: " + productionNode.getName() +
                    " with dependency level " + dependencyLevel);

            // Traverse the right subtree
            inOrderRec(node.right, nodeDependencyLevels);
        }
    }
}
