// use SDK Version 9

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;

public class Homework1 extends JPanel
		implements TreeSelectionListener {

	private JEditorPane htmlPane;
	private JTree tree;
    public String test = "";
    public String check = "251-*32*+";

    Homework1(Node node) {
		super(new GridLayout(1,0));

		//Create the nodes.
		DefaultMutableTreeNode top =
				new DefaultMutableTreeNode(node);
		createNodes(top);

		//Create a tree that allows one selection at a time.
		tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode
				(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Set the icon for leaf nodes.
        ImageIcon leafIcon = createImageIcon("middle.gif");
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setOpenIcon(leafIcon);
            renderer.setClosedIcon(leafIcon);
            tree.setCellRenderer(renderer);

		//Listen for when the selection changes.
		tree.addTreeSelectionListener(this);
		tree.putClientProperty("JTree.lineStyle", "None");

		//Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(tree);

		//Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		JScrollPane htmlView = new JScrollPane(htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 300));

		//Add the split pane to this panel.
		add(splitPane);
	}

	public static void main(String[] args) {
		String input = "251-*32*+";

		Node mainNode = new Node();
		Node tempNode = mainNode;
		for (int i = 0; i < input.length(); i++) {
			tempNode.nodeRight = new Node();
			tempNode = tempNode.nodeRight;
			tempNode.number = String.valueOf(input.charAt(i));
		}

		mainNode = infix(mainNode.nodeRight);
		String infix = inorder(mainNode);
		System.out.println(infix.substring(1,infix.length()-1)+ "=" + calculate(mainNode));



		//dont know why IntelliJ need to create this method

		Node finalMainNode = mainNode;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(finalMainNode);
			}
		});

	}

	// infix function
	public static Node infix(Node infixNode){

		Node tempNode = infixNode;

		while (tempNode != null){

			infixNode = tempNode;
			tempNode = infixNode.nodeRight;


			if(infixNode.number.matches("[0-9]")) {
				infixNode.nodeRight = null;
			}
			else{
				if(infixNode.nodeLeft.number.matches("[0-9]")) {
					infixNode.nodeRight = infixNode.nodeLeft;
					infixNode.nodeLeft = infixNode.nodeRight.nodeLeft;
					infixNode.nodeRight.nodeLeft = null;
				}
				else {
					infixNode.nodeRight = infixNode.nodeLeft;
					infixNode.nodeLeft = infixNode.nodeRight.nodeLeft.nodeLeft;
					infixNode.nodeRight.nodeLeft.nodeLeft = null;
				}
			}
			if (tempNode != null)
				tempNode.nodeLeft = infixNode;
		}
		return infixNode;
	}

	//inorder function
	public static String inorder(Node inorderNode) {
		String left = "";
		String right = "";

		//if inordernode is operand (NaN) then add "(" and ")"
		if (inorderNode.nodeLeft != null) {
			left = "(" + inorder(inorderNode.nodeLeft);
		}
		if (inorderNode.nodeRight != null) {
			right = inorder(inorderNode.nodeRight) + ")";
		}

		return (left + inorderNode.number + right);

		//example inordernode is + then result is (nodeleft+noderight)
	}

	//calculate function
	public static int calculate(Node calculateNode) {

		if (calculateNode.number.matches("[0-9]"))
			return Integer.valueOf(calculateNode.number);

		int result = 0;
		int left = calculate(calculateNode.nodeLeft);
		int right = calculate(calculateNode.nodeRight);


		//switch case to decide what operand executed
		switch (calculateNode.number){
			case "+":result = left + right;break;
			case "-":result = left - right;break;
			case "*":result = left * right;break;
			case "/":result = left / right;break;
		}

		return result;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				tree.getLastSelectedPathComponent();

		Node num = (Node)node.getUserObject();
		String text = inorder(num);
		if(num.nodeLeft == null){
			//if num is leaf node (only number and nodeleft/right is null)
			//then we dont need to cut "(" and ")" just show number
		}

		else text=(text.substring(1,text.length()-1)+ "=" + calculate(num));

		htmlPane.setText(text);
	}

	private void createNodes(DefaultMutableTreeNode top) {
		Node node = (Node)top.getUserObject();
		DefaultMutableTreeNode category;

		if(node.nodeLeft != null) {

		category = new DefaultMutableTreeNode(node.nodeLeft);
		top.add(category);
		createNodes(category);
		}

		if(node.nodeRight != null) {

		category = new DefaultMutableTreeNode(node.nodeRight);
		top.add(category);
		createNodes(category);
		}





	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Homework1.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private static void createAndShowGUI(Node finalMainNode) {
		//Create and set up the window.
		JFrame frame = new JFrame("Binary Calculator Tree Homework");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		frame.add(new Homework1(finalMainNode));

		//Display the window.
		frame.pack();
		OLO
		frame.setVisible(true);
	}
}
