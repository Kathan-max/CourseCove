package com.course_cove.autocorrect;

import java.util.List;
import java.util.ArrayList;

// AVL Tree Implementation
public class AVLTree {
    static class AVLTreeNode {
        CourseObject course;
        AVLTreeNode left, right;
        int height;

        public AVLTreeNode(CourseObject course) {
            this.course = course;
            this.height = 1;
        }
    }

    private AVLTreeNode root;

    // Get height of the node
    private int height(AVLTreeNode N) {
        if (N == null)
            return 0;
        return N.height;
    }

    // Get maximum of two integers
    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Right rotate
    private AVLTreeNode rightRotate(AVLTreeNode y) {
        AVLTreeNode x = y.left;
        AVLTreeNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        // Return new root
        return x;
    }

    // Left rotate
    private AVLTreeNode leftRotate(AVLTreeNode x) {
        AVLTreeNode y = x.right;
        AVLTreeNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }

    // Get Balance factor
    private int getBalance(AVLTreeNode N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    // Insert a course into the AVL tree
    public void insert(CourseObject course) {
        root = insertRec(root, course);
    }

    private AVLTreeNode insertRec(AVLTreeNode node, CourseObject course) {
        // 1. Perform the normal BST insertion
        if (node == null)
            return new AVLTreeNode(course);

        if (course.price < node.course.price)
            node.left = insertRec(node.left, course);
        else if (course.price > node.course.price)
            node.right = insertRec(node.right, course);
        else // Duplicate prices are not allowed
            return node;

        // 2. Update height of this ancestor node
        node.height = 1 + max(height(node.left), height(node.right));

        // 3. Get the balance factor to check if this node became unbalanced
        int balance = getBalance(node);

        // 4. If the node is unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && course.price < node.left.course.price)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && course.price > node.right.course.price)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && course.price > node.left.course.price) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && course.price < node.right.course.price) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // return the (unchanged) node pointer
        return node;
    }

    // Method to filter courses by max price
    public List<CourseObject> filterCoursesByMaxPrice(double maxPrice) {
        List<CourseObject> filteredCourses = new ArrayList<>();
        filterCoursesByMaxPriceRec(root, maxPrice, filteredCourses);
        return filteredCourses;
    }

    private void filterCoursesByMaxPriceRec(AVLTreeNode node, double maxPrice, List<CourseObject> filteredCourses) {
        if (node == null)
            return;

        // If current node's price is less than or equal to maxPrice, add to list
        if (node.course.price <= maxPrice)
            filteredCourses.add(node.course);

        // Traverse left subtree if max price is greater than current node
        if (maxPrice >= node.course.price)
            filterCoursesByMaxPriceRec(node.right, maxPrice, filteredCourses);

        // Always check left subtree for potentially cheaper courses
        filterCoursesByMaxPriceRec(node.left, maxPrice, filteredCourses);
    }
}