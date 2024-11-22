package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;

import java.util.*;

public class AVLTree<T> {
    private class Node {
        T key;
        Node left, right;
        int height;

        Node(T key) {
            this.key = key;
            height = 1;
        }
    }

    private Node root;
    private final Comparator<T> comparator;

    public AVLTree() {
        this.comparator = null;
    }

    public AVLTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    // Public method to insert a key
    public void insert(T key) {
        root = insertRec(root, key);
    }

    // Private recursive insertion
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
            return node; // Duplicate keys are not allowed
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return balance(node);
    }

    // Balancing logic
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

    // Rotations
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T = x.right;

        x.right = y;
        y.left = T;

        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T = y.left;

        y.left = x;
        x.right = T;

        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));

        return y;
    }

    // Helpers
    private int getHeight(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalanceFactor(Node node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    public void inOrderTraversal(Map<ProductionNode, Integer> nodeDependencyLevels) {
        inOrderRec(root, nodeDependencyLevels);
    }

    private void inOrderRec(Node node, Map<ProductionNode, Integer> nodeDependencyLevels) {
        if (node != null) {
            // Primeiro processa o lado esquerdo
            inOrderRec(node.left, nodeDependencyLevels);

            // Processa o nó atual
            ProductionNode productionNode = (ProductionNode) node.key;
            int dependencyLevel = nodeDependencyLevels.getOrDefault(productionNode, -1);

            // Simulação de execução
            System.out.println("Processing node in BOO order: " + productionNode.getName() +
                    " with dependency level " + dependencyLevel);

            // Depois processa o lado direito
            inOrderRec(node.right, nodeDependencyLevels);
        }
    }

}
