/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectnikolos2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class ArrayBST is an implementation of a BST, using an array and
 * its indexes instead of pointers and Node class
 * 
 * @author Kostas Nikolos
 */
public class ArrayBST {
    
    int SIZE; //Max node capacity of the tree
    int node_no; //Nodes present on the tree
    
    
    //This is a stack of available positions
    //Using ints we represent NULLs with -1
    int avail_at;
    
    
    //Format is DATA | LEFT | RIGHT
    int[][] NODES;
    
    int root; //What is the root node
    
    //Statistic values for the comparisons
    int compares;
    
    /**
     * Sets the compare counter back to zero,
     * for use between the statistic counts.
     * 
     */
    public void resetCompares(){
        
        compares = 0;

    }
    
    /**
     * Returns the counted 'comparisons' so far,
     * this includes comparisons, assignments and value returns.
     * 
     * 
     * @return The counted compares
     */
    public int getCompares(){
        
        return compares;
    }
    
    /**
     * Increases the comparison counter by one,
     * returns true, so it can be used directly on if() and while().
     * 
     * @return always true
     */
    protected boolean incrCompares(){
        
        compares++;
        return true;
        
    }
    
    /**
     * Increases the compares counter by a some number
     * returns true, so it can be used directly on if() and while().
     * 
     * @param times How many times to increase
     * @return 
     */
    protected boolean incrCompares(int times){
        
        compares+= times;
        return true;
        
    }
    
    
    
    
    /**
     * Checks and returns an empty space on the node array,
     * to be used for a new node.
     * If it fails, it returns -1.
     * 
     * @return the index on the array
     */
    protected int getNextNode(){
        
        //Returns the position of the next available node, making it unavailable
        
        //If there is no node available, just return NULL
        if(avail_at == -1) return -1;
        
        //If there's an available node return that and change the available to the next
        int ret = avail_at;
        avail_at = NODES[avail_at][2];
        return ret;
        
    }
    
    /**
     * Frees a space on the node array, previously used
     * for a node that is now deleted.
     * 
     * @param node which node to free
     * @return weather it was successful
     */
    protected boolean freeNode(int node){
        
        //Opposite of getNextNode, free's a node and puts the space in the stack
        //Returns false when something is wrong
        
        //If it's already free space then something is wrong, so return false
        if(NODES[node][1] == -2){
            return false;
        }
        
        //Make the node available
        NODES[node][1] = -2;
        NODES[node][2] = avail_at;
        avail_at = node;
        
        return true;
        
        
    }
    
    
    //Getters and setters for the array
    
    /**
     * Returns the key of the node you specified
     * 
     * 
     * @param node the node you want info on
     * @return the key
     */
    public int info(int node){
        
        return NODES[node][0];
        
    }
    
    /**
     * Returns the left child index of the node you specify,
     * if there's none it returns -1
     * 
     * @param node the node you need the child
     * @return the left child index of the node
     */
    public int left(int node){
        
        return NODES[node][1];
        
    }
    
    /**
     * Returns the right child index of the node you specify,
     * if there's none it returns -1
     * 
     * @param node the node you need the child
     * @return the right child index of the node
     */
    public int right(int node){
        
        return NODES[node][2];
        
    }
    
    /**
     * Returns the leftmost child index of the node you specify,
     * also returns it's parent by reference.
     * 
     * @param node the node you need the leftmost child of
     * @param parent an int[1] array, to pass the parent by reference
     * @return the index of the leftmost child
     */
    protected int getLeftmost(int node, int[] parent){
        incrCompares(2);
        //Gets the leftmost node starting from the node specified
        //I've made it recursive BC I can.
        incrCompares();
        int at = node;
        if(at == -1 && incrCompares()){
            incrCompares();
            return -1;
        }
        while(true){
            if(left(at) == -1 && incrCompares()){
                incrCompares();
                return at;
            }
            incrCompares(2);
            parent[0] = at;
            at = left(at);
        }
        
    }
    
    
    /**
     * Returns the rightmost child index of the node you specify,
     * also returns it's parent by reference.
     * 
     * @param node the node you need the rightmost child of
     * @param parent an int[1] array, to pass the parent by reference
     * @return the index of the rightmost child
     */
    protected int getRightmost(int node, int[] parent){
        incrCompares(2);
        //It's the twin brother of getLeftmost
        //Check that for information
        incrCompares();
        int at = node;
        if(at == -1 && incrCompares()){
            incrCompares();
            return -1;
        }
        
        while(true){
            if(right(at) == -1 && incrCompares()){
                incrCompares();
                return at;
            }
            incrCompares(2);
            parent[0] = at;
            at = right(at);
        }

    }
    
    /**
     * Constructor for the ArrayBST class,
     * allows you to give the maximum node number
     * 
     * 
     * @param maxsize maximum nodes in the tree
     */
    ArrayBST(int maxsize){
        
        this(maxsize,3);
        
    }
    
    /**
     * Constructor for the ArrayBST class,
     * allows you to give the maximum node number,
     * also allows you to give the maximum number of node information.
     * 
     * @param maxsize maximum nodes in the tree
     * @param NODE_SIZE maximum data on each node (ArrayBST needs 3)
     */
    ArrayBST(int maxsize, int NODE_SIZE){
        
        //Initializes the arrays and the size info
        SIZE = maxsize;
        node_no = 0;
        
        //The first available node is the next one
        avail_at = 0;
        
        //At first every node is empty
        NODES = new int[SIZE][];
        for(int i = 0; i < SIZE; i++){
            //No need to initialize nodes that aren't yet used
            NODES[i] = new int[NODE_SIZE];
            
            //Create the available stack
            NODES[i][2] = i+1;
            NODES[i][1] = -2; //Having -2 on position 1 signifies that its free space
            if(i == SIZE) NODES[i][2] = -1; //NULL
            
        }
        
        //At first there is no root
        root = -1;
        
        //Initialize comparisons
        compares = 0;
        
        
        
    }
    
    /**
     * Adds a new key to the BST,
     * may fail if you don't have size on the array
     * 
     * @param key the key you want to add
     * @return weather the key was added
     */
    public boolean addKey(int key){
        incrCompares();
        
        //Adds a key to the tree while keeping the order
        
        //Returns false if there is no space
        if(avail_at == -1 && incrCompares()){
            incrCompares();
            return false;
        }
        
        //Create the new node
        incrCompares(4);
        int newnode = getNextNode();
        NODES[newnode][0] = key;
        NODES[newnode][1] = -1;
        NODES[newnode][2] = -1;
        
        //Special case for nonexistent root
        if(root == -1 && incrCompares()){
            //Make the new node to be the root
            incrCompares(2);
            root = newnode;
            return true;
        }
        
        
        //Search for the correct position
        incrCompares();
        int node_at = root;
        while(true){
            
            
            if(info(node_at) < key && incrCompares()){
                
                
                
                //insert the node at the leaf
                if(right(node_at) == -1 && incrCompares()){
                    incrCompares(2);
                    NODES[node_at][2] = newnode;
                    return true;
                }
                
                //Go Right
                incrCompares();
                node_at = right(node_at);
                
            }
            
            if(info(node_at) > key  && incrCompares()){
                
                
                //insert the node at the leaf
                if(left(node_at) == -1 && incrCompares()){
                    incrCompares(2);
                    NODES[node_at][1] = newnode;
                    return true;
                }
                
                //Go left
                incrCompares();
                node_at = left(node_at);
                
                
            }
            
            
            
            if(info(node_at) == key && incrCompares()){
                
                //No adding because it is already there
                //Free the node you got
                incrCompares();
                freeNode(newnode);
                return false;
            }
        }
    }
    
    /**
     * Internal function for finding a specific key on the tree,
     * returns the node index, as well as its parent
     * 
     * @param key the key you are searching for
     * @param node the root of the subtree you are searching on
     * @param parent an int[1] array that will hold the return value of the index of the parent
     * @return the node index you searched for
     */
    protected int findKeyNode(int key, int node, int[] parent){
        incrCompares(3);
        //Searches a key starting from a specific node
        //Returns the position of the node on the array
        
        if(node == -1 && incrCompares()){
            incrCompares();
            return -1;
        }
            
        if(key == info(node) && incrCompares()){
            incrCompares();
            return node;
        }
        
        incrCompares();
        parent[0] = node;
        if(key > info(node) && incrCompares()){
            incrCompares();
            return findKeyNode(key,right(node),parent);
        }
        
        incrCompares();
        return findKeyNode(key, left(node),parent);
        
    }
    
    /**
     * Searches for a node holding the key you specify on the tree.
     * 
     * 
     * @param key the key you are searching for
     * @return the node index you searched, or -1 on failure
     */
    public int findKey(int key){
        incrCompares(2);
        //Returns weather the key you were asking for was found
        //Uses findKeyNode() at the root
        
        return findKeyNode(key,root,new int[1]);
        
    }
    
    public boolean deleteKey(int key){
        
        
        //First find the key that needs to be deleted
        int[] parent = new int[1];
        parent[0] = -1;
        
        int keynode = findKeyNode(key,root,parent);
        
        //If there is no such node then return false
        if(keynode == -1 && incrCompares()) return false;
        
        //Else you have found a node that needs to be deleted
        
        //CASE 1: No children [this case is covered by case 2 but left in for readability]
        if(left(keynode) == -1 && right(keynode) == -1 && incrCompares() && incrCompares()){
            
            //Just remove him from the parent and free the node
            if(parent[0] == -1 && incrCompares()){
                //You are the root so make the root -1
                root = -1;
                
            }else{
                
                //You are not the root so the parent needs have the pointer NULLified
                if(left(parent[0]) == keynode) NODES[parent[0]][1] = -1;
                else NODES[parent[0]][2] = -1;
            }
            
            freeNode(keynode);
        
        //CASE 2: There is at least one child
        }else if(left(keynode) == -1 || right(keynode) == -1 && incrCompares() && incrCompares()){
            
            //Find which is the child
            int mychild = left(keynode);
            if(mychild == -1 && incrCompares()) mychild = right(keynode);
            
            //Check if you have a parent
            if(parent[0] == -1 && incrCompares()){
                //You are the root so make your child root
                root = mychild;
            }else{
                
                //Make your father, father of your only child before you commit suicide
                if(left(parent[0]) == keynode && incrCompares()) NODES[parent[0]][1] = mychild;
                else NODES[parent[0]][2] = mychild;

            }
            
            //Commit SUDOKU
            freeNode(keynode);

        //CASE 3: There are two children
        }else{
            
            //Find the leftmost node of the right child
            int[] leftparent = new int[1];
            leftparent[0] = keynode;
            int leftmost = getLeftmost(right(keynode),leftparent);
            
            //After you find it, swap the keys
            int temp = info(keynode);
            NODES[keynode][0] = info(leftmost);
            NODES[leftmost][0] = keynode;
            
            //Then you just need to delete the leftmost node from the tree
            //The leftmost node is guarranteed to fall under CASE 1 or 2
            //So we repeat the steps for CASE 2
            
            //Create a variable to store the child
            int mychild = left(leftmost);
            if(mychild == -1 && incrCompares()) mychild = right(leftmost);

            //Make your father, father of your only child before you commit suicide
            //Note that the leftmost node is guarranteed to have a child
            if(left(leftparent[0]) == leftmost && incrCompares()) NODES[leftparent[0]][1] = mychild;
            else NODES[leftparent[0]][2] = mychild;

            //Kill the node
            freeNode(leftmost);
             
        }
        
        
        return true;
        
    }
    
    private void printInorderNode(int node){
        
        //Standard implementation of inorder traversal
        
        if(node == -1 && incrCompares()) return;
        printInorderNode(left(node));
        System.out.print(" "+info(node)+" ");
        printInorderNode(right(node));
        
    }
    
    public void printInorder(){
        
        //Prints the tree inorder = by the order of the keys
        printInorderNode(root);
        System.out.println("");
        
    }
    
    
    /**
     * Internal function that searches for key values within a range,
     * returns the keys in ascending order on the ArrayList given
     * 
     * @param node the root of the subtree you are searching on
     * @param Min the minimum value of the search range
     * @param Max the maximum value of the search range
     * @param al the ArrayList to save the found keys
     */
    private void printInorderNodeRange(int node, int Min, int Max, ArrayList<Integer> al){
        incrCompares(4);
        
        if(node == -1 && incrCompares()) return;
        
        if(info(node) < Min && incrCompares()){
            //If you are below the minimum just check your right child
            printInorderNodeRange(right(node),Min,Max,al);
            return;
        }
        
        if(info(node) > Max && incrCompares()){
            //If you are above the maximum just check your left child
            printInorderNodeRange(left(node),Min,Max,al);
            return;
        }
        
        //If non of the above applies then check normal inorder
        printInorderNodeRange(left(node),Min,Max,al);
        al.add(new Integer(info(node)));
        printInorderNodeRange(right(node),Min,Max,al);
        
        
        
    }
    
    /**
     * Function that searches for key values within a range,
     * returns the keys in ascending order on the ArrayList given
     * 
     * @param Min the minimum value of the range
     * @param Max the maximum value of the range
     * @return the ArrayList holding all the keys
     */
    public ArrayList<Integer> searchInorderRange(int Min, int Max){
        incrCompares(2);
        //Switch min and max if they are opposite
        if(Min > Max && incrCompares()){
            incrCompares(3);
            int temp = Min;
            Min = Max;
            Max = temp;
            
        }
        
        incrCompares(2);
        ArrayList al = new ArrayList<Integer>();
        printInorderNodeRange(root,Min,Max,al);
        return al;
        
    }
}
