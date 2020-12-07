package de.hft.objects;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

	private Point _point;
	private ArrayList<TreeNode> _childrens;
	private TreeNode _parent;
	
    public TreeNode(Point data) {
    	_point = data;
    	_childrens = new ArrayList<>();
    }
    
    public void addChild(TreeNode treeNode) {
    	treeNode.setParent(this);
    	_childrens.add(treeNode);
    }
    
    public List<TreeNode> getChildrens(){
    	return _childrens;
    }
    
    public Point getData() {
    	return _point;
    }
    
    public TreeNode getParent() {
    	return _parent;
    }
    
    private void setParent(TreeNode treeNode) {
    	_parent = treeNode;
    }
    
}