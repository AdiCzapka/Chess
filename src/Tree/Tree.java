package Tree;

import java.util.ArrayList;

public class Tree {
    TreeNode root;
    ArrayList<TreeNode> children;

    public Tree(TreeNode root, ArrayList<TreeNode> children) {
        this.root = root;
        this.children = children;
    }

    public void setRoot(TreeNode root) {this.root = root;}
    public void setChildren(ArrayList<TreeNode> children) {this.children = children;}
    public TreeNode getRoot() {return root;}
    public ArrayList<TreeNode> getChildren() {return children;}

}
