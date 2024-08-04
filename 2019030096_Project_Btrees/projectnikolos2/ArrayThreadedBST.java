/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectnikolos2;

import java.util.ArrayList;


/**
 * Class ArrayThreadedBST is an implementation of a Threaded BST,
 * based on the array implementation of the class ArrayBST
 * 
 * @author Kostas Nikolos
 */
public class ArrayThreadedBST extends ArrayBST{
    
    
    //Contains info about which is a thread
    // 0 - no threads
    // 1 - left thread
    // 2 - right thread
    // 3 - both threads
    //byte[] threadInfo;
    
    //Extra getters and setters to handle threads
    
    //Those 2 return anything on the right no matter thread or not
    
    /**
     * Returns the right child or right thread index of the node you specified.
     * 
     * 
     * @param node the node you want the child of
     * @return the index of the right child or thread
     */
    private int right_anything(int node){
        
        return NODES[node][2];
        
    }
    
    /**
     * Returns the left child or right thread index of the node you specified.
     * 
     * 
     * @param node the node you want the child of
     * @return the index of the left child or thread
     */
    private int left_anything(int node){
        
        return NODES[node][1];
        
    }
    
    //Those still only return true children (so that some methods of the original will still work)
    public int right(int node){
        
        if(!right_thread(node)) return right_anything(node);
        return -1;
        
    }
    
    public int left(int node){
        
        if(!left_thread(node)) return left_anything(node);
        return -1;
    
    }
    
    //These set and get the boolean values associated with the thread
    
    /**
     * Returns weather the specified node has a left thread
     * 
     * @param node the node you specify
     * @return 
     */
    private boolean left_thread(int node){
        
        return NODES[node][3] == 1;
        
    }
    
    /**
     * Returns weather the specified node has a right thread
     * 
     * @param node the node you specify
     * @return 
     */
    private boolean right_thread(int node){
        
        return NODES[node][4] == 1;
                
    }
    
    /**
     * Sets the left child of the node you specify to be a thread or not
     * 
     * @param node the node you specify
     * @param value weather the left child should be a thread
     */
    private void set_left_thread(int node, boolean value){
        
        NODES[node][3] = (value)?(1):(0);
        
    }
    
    /**
     * Sets the right child of the node you specify to be a thread or not
     * 
     * @param node the node you specify
     * @param value weather the right child should be a thread
     */
    private void set_right_thread(int node, boolean value){
        
        NODES[node][4] = (value)?(1):(0);
        
    }
    
    
    //When freeing a node we set the thread value to 0 as it begun
    protected boolean freeNode(int node){
        
        //Do the standard deletion steps
        boolean ret = super.freeNode(node);
        
        //Reset the value of threads
        if(ret){
            NODES[node][3] = 0;
            NODES[node][4] = 0;
        }
        
        return ret;
        
    }
    
    ArrayThreadedBST(int maxsize){
        
        super(maxsize,5);
        
        for(int i = 0; i < SIZE; i++){
            NODES[i][3] = 0;
            NODES[i][4] = 0;
            
        }
        
    }
    
    
    /**
     * Returns the inorder-predecessor node index of the
     * node you specified.
     * 
     * @param node The node you specify
     * @return the inorder-predecessor
     */
    protected int inorderPred(int node){
        incrCompares();
        
        if(left_thread(node) && incrCompares()){
            incrCompares();
            return left_anything(node);
            
        }else{
            incrCompares();
            return getRightmost(left(node),new int[1]);
            
        }
    }
    
    /**
     * Returns the inorder-successor node index of the
     * node you specified.
     * 
     * @param node The node you specify
     * @return the inorder-successor
     */
    protected int inorderSucc(int node){
        incrCompares();
        //Ditto of inorderPred
        //return getLeftmost(right_anything(node),new int[1]);
        
        if(right_thread(node) && incrCompares()){
            incrCompares();
            return right_anything(node);
            
        }else{
            incrCompares();
            return getLeftmost(right(node),new int[1]);
            
        }
        
        
    }
    
    //This links two nodes inorder UNUSED
    /*
    private void linkInorder(int Left, int Right){
        
        //(at least one has to be non NULL)
        if(Left == -1 && Right == -1) return;
        
        
        if(Left == -1){
            
            NODES[Right][1] = -1;
            set_left_thread(Right,true);
            
        }else if(Right == -1){
            
            NODES[Left][2] = -1;
            set_right_thread(Left,true);
            
        }else{
            
            if(NODES[Left][2] != Right){
                NODES[Left][2] = Right;
                set_right_thread(Left,true);
                
            }
            
            if(NODES[Right][1] != Left){
                NODES[Right][1] = Left;
                set_left_thread(Right,true);
            }
            
            
        }
        
        
        
    }
    */
    
    //We need to override add key and delete key for this motherfucker to work correctly!!
    //The code is mostly the same but a few things need to be set
    
    public boolean addKey(int key){
        incrCompares();
        //Adds a key to the tree while keeping the order
        
        //Returns false if there is no space
        if(avail_at == -1 && incrCompares()){
            incrCompares();
            return false;
        }
        
        //Create the new node
        incrCompares(6);
        int newnode = getNextNode();
        NODES[newnode][0] = key;
        NODES[newnode][1] = -1;
        NODES[newnode][2] = -1;
        NODES[newnode][3] = 1;
        NODES[newnode][4] = 1;
        
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
                    
                    //Connect the threads on the new node
                    incrCompares(4);
                    NODES[newnode][2] = right_anything(node_at);
                    NODES[newnode][1] = node_at;
                    
                    //Connect the parent to the new child
                    NODES[node_at][2] = newnode;
                    set_right_thread(node_at,false);
                    
                    return true;
                }
                
                //Go Right
                incrCompares();
                node_at = right(node_at);
                
            }
            
            if(info(node_at) > key && incrCompares()){
                
                
                //insert the node at the leaf
                if(left(node_at) == -1 && incrCompares()){
                    
                    //Connect the threads on the new node
                    incrCompares(4);
                    NODES[newnode][1] = left_anything(node_at);
                    NODES[newnode][2] = node_at;
                    
                    //Connect the parent to the child
                    NODES[node_at][1] = newnode;
                    set_left_thread(node_at, false);
                    
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
                if(left(parent[0]) == keynode && incrCompares()){
                    
                    //Turn the left parent pointer to a thread
                    NODES[parent[0]][1] = left_anything(keynode);
                    set_left_thread(parent[0],true);
                    
                }else{
                    
                    //Turn the right parent pointer to a thread
                    NODES[parent[0]][2] = right_anything(keynode);
                    set_right_thread(parent[0],true);
                    
                }
            }
            
            freeNode(keynode);
        
        //CASE 2: There is at least one child
        }else if(left(keynode) == -1 || right(keynode) == -1 && incrCompares() && incrCompares()){
            
            //Find which is the child
            int mychild = left(keynode);
            if(mychild == -1 && incrCompares()) mychild = right(keynode);
            
            //Find the inorder successor and the inorder predecessor
            int in_pred = inorderPred(keynode);
            int in_succ = inorderSucc(keynode);
            
            //Check if you have a parent
            if(parent[0] == -1 && incrCompares()){
                
                //You are the root so make your child root
                root = mychild;
                
            }else{
                
                //Make your father, father of your only child before you commit suicide
                
                if(left(parent[0]) == keynode && incrCompares()){

                    //Make the parent show the child of the deleted
                    NODES[parent[0]][1] = mychild;
                    
                    //Make this connection not to be a thread
                    set_left_thread(parent[0],false);
                    

                }else{
                    
                    //Make the parent show the child of the deleted
                    NODES[parent[0]][2] = mychild;
                    
                    //Make this connection not to be a thread
                    set_right_thread(parent[0],false);
                    
                }
            }
            
            //If the deleted node has a left subtree we need to connect this thread
            if(left(keynode) != -1 && incrCompares()){
                NODES[in_pred][2] = in_succ;
                set_right_thread(in_pred, true);
            }
            
            //If the deleted node has a right subtree we need to connect this thread
            if(right(keynode) != -1){
                NODES[in_succ][1] = in_pred;
                set_left_thread(in_succ, true);
            }
            
            //Connect the threads
            //linkInorder(in_pred,in_succ);
            
            
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
            //So we repeat the steps for CASE 1 or 2
            
            //Create a variable to store the child
            int mychild = left(leftmost);
            if(mychild == -1 && incrCompares()) mychild = right(leftmost);
            
            if(mychild == -1 && incrCompares()){
                
                //CASE 1 REVISITED
                
                if(left(leftparent[0]) == leftmost && incrCompares()){
                    
                    //Turn the left parent pointer to a thread
                    NODES[leftparent[0]][1] = left_anything(leftmost);
                    set_left_thread(leftparent[0],true);
                    
                }else{
                    
                    //Turn the right parent pointer to a thread
                    NODES[leftparent[0]][2] = right_anything(leftmost);
                    set_right_thread(leftparent[0],true);
                    
                }
                
                
            }else{
                
                //CASE 2 REVISITED
                
                //Find the inorder successor and the inorder predecessor
                int in_pred = inorderPred(leftmost);
                int in_succ = inorderSucc(leftmost);


                //Make your father, father of your only child before you commit suicide

                if(left(leftparent[0]) == leftmost && incrCompares()){

                    //Make the parent show the child of the deleted
                    NODES[leftparent[0]][1] = mychild;

                    //Make this connection not to be a thread
                    set_left_thread(leftparent[0],false);


                }else{

                    //Make the parent show the child of the deleted
                    NODES[leftparent[0]][2] = mychild;

                    //Make this connection not to be a thread
                    set_right_thread(leftparent[0],false);

                }

                //If the deleted node has a left subtree we need to connect this thread
                if(left(leftmost) != -1 && incrCompares()){
                    NODES[in_pred][2] = in_succ;
                    set_right_thread(in_pred, true);
                }

                //If the deleted node has a right subtree we need to connect this thread
                if(right(leftmost) != -1){
                    NODES[in_succ][1] = in_pred;
                    set_left_thread(in_succ, true);
                }
                
            }
            
            //Kill the node
            freeNode(leftmost);
             
        }
        
        
        return true;
        
    }
    
    public void printInorder(){
        
        
        //First find the leftmost node
        int at = getLeftmost(root,new int[1]);
        
        while(at != -1 && incrCompares()){
            
            System.out.print(" "+info(at)+" ");
            at = inorderSucc(at);
            
        }
        
        System.out.println("");
        
    }
    
    public ArrayList<Integer> searchInorderRange(int Min, int Max){
        
        
        //Switch min and max if they are opposite
        if(Min > Max && incrCompares()){
            
            incrCompares(3);
            int temp = Min;
            Min = Max;
            Max = temp;
            
        }
        
        incrCompares(2);
        ArrayList<Integer> al = new ArrayList<Integer>();
        //First find the node which is the first eligible
        int at = root;
        while(true){
            
            if(info(at) == Min && incrCompares()) break;
            if(info(at) > Min && incrCompares()){
                
                if(left(at) == -1) break;
                at = left(at);
                
            }else{
                
                if(right(at) == -1 && incrCompares()){
                    incrCompares();
                    at = right_anything(at);
                    break;
                }
                incrCompares();
                at = right(at);
                
            }
            
            
        }
        
        //After that at is the first node >= Min
        while(info(at) <= Max && incrCompares()){
            
            incrCompares();
            al.add(new Integer(info(at)));
            at = inorderSucc(at);
            if(at == -1 && incrCompares()) break;
            
        }
        
        incrCompares();
        return al;
        
        
        
    }
    
    
    
}
