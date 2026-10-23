package Tree;

import java.util.ArrayList;

public class Tree {
    TreeNode root;
    ArrayList<TreeNode> children;
    Integer score;

    public Tree(TreeNode root, ArrayList<TreeNode> children, Integer score) {
        this.root = root;
        this.children = children;
        this.score = score;
    }

    public void setRoot(TreeNode root) {this.root = root;}
    public void setChildren(ArrayList<TreeNode> children) {this.children = children;}
    public void setScore(Integer score) {this.score = score;}
    public TreeNode getRoot() {return root;}
    public ArrayList<TreeNode> getChildren() {return children;}
    public Integer getScore() {return score;}

}
