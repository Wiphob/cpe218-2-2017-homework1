public class Homework1 {

	public static void main(String[] args) {
		String input = "";
		// Begin of arguments input sample
		if (args.length > 0) {
			input = args[0];
			if (input.equalsIgnoreCase("251-*32*+")) {
				System.out.println("(2*(5-1))+(3*2)=14");


			}

		}
		// End of arguments input sample
		
		// TODO: Implement your project here
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


	}

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

	public static String inorder(Node inorderNode) {
		String left = "";
		String right = "";
		if (inorderNode.nodeLeft != null) {
			left = "(" + inorder(inorderNode.nodeLeft);
		}
		if (inorderNode.nodeRight != null) {
			right = inorder(inorderNode.nodeRight) + ")";
		}

		return (left + inorderNode.number + right);
	}

	public static int calculate(Node calculateNode) {

		if (calculateNode.number.matches("[0-9]"))
			return Integer.valueOf(calculateNode.number);

		int result = 0;
		int left = calculate(calculateNode.nodeLeft);
		int right = calculate(calculateNode.nodeRight);

		switch (calculateNode.number){
			case "+":result = left + right;break;
			case "-":result = left - right;break;
			case "*":result = left * right;break;
			case "/":result = left / right;break;
		}

		return result;
	}














}
