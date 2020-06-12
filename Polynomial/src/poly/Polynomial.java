package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author mkl112-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		// (1) EMPTY
		if (poly1 == null && poly2 == null) { return null; } 
		
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node newNode = new Node(0, 0, null);
		Node temp = newNode;
		
		
		// ITERATION - through both Linked Lists (by each node)
		// 		(2) if ptr1.term.degree > ptr2.term.degree
		// 		(3) if ptr1.term.degree < ptr2.term.degree
		// 		(4)	if ptr1.term.degree = ptr2.term.degree
					
		while (ptr1 != null && ptr2 != null) {

			// ptr1.term.degree == ptr2.term.degree
			if (ptr1.term.degree == ptr2.term.degree && (ptr1.term.coeff + ptr2.term.coeff) != 0) {		// will not create node if = 0
				newNode.next = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, null);
				newNode = newNode.next; 	// ADD NEW NODE TO END
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			} else if(ptr1.term.degree > ptr2.term.degree) { // ptr1.term.degree > ptr2.term.degree
				newNode.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
				newNode = newNode.next;		// ADD NEW NODE TO END
				ptr2 = ptr2.next; 	// MOVING TO NEXT NODE
			}  else if (ptr1.term.degree < ptr2.term.degree) { // ptr1.term.degree < ptr2.term.degree
				newNode.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
				newNode = newNode.next;		// ADD NEW NODE TO END
				ptr1 = ptr1.next;		// MOVING TO NEXT NODE
			}  else { 
				// MOVING TO NEXT NODE
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
		}

		while (ptr1 != null) {
			newNode.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
			newNode = newNode.next;
			ptr1 = ptr1.next;
		}
		
		while (ptr2 != null) {
			newNode.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
			newNode = newNode.next;
			ptr2 = ptr2.next;
		}
		
		// RETURN FRONT
		return temp.next;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		if (poly1 == null || poly2 == null) { return null; } 
		
		// ITERATING linked lists
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node newNode = new Node(0, 0, null);
		Node temp = newNode;
		int maxDegree = 0;
		
		
		// MULTIPLYING nodes
		while (ptr1 != null) {
			while (ptr2 != null) {
				
				int prodDegree = ptr1.term.degree + ptr2.term.degree;
				newNode.next = new Node(ptr1.term.coeff * ptr2.term.coeff, prodDegree, null);
				newNode = newNode.next;
				ptr2 = ptr2.next;
				
				if (maxDegree < prodDegree) {
					maxDegree = prodDegree;			// save maxDegree - used in COMBINING
				}
				
			}
			
			ptr1 = ptr1.next;
			ptr2 = poly2;
			
		}
		
		// COMBINING LIKE DEGREES - ITERATING MAX DEGREE times
		Node finalNode = null;
		
		for (int i = maxDegree; i >= 0; i--) {
			Node ptr = temp.next;	
			float sum = 0;		// holds coefficient sum
			while (ptr != null) {		// iterating through newNode (unaccompanied LL)
				if (ptr.term.degree == i) {		// matching degrees
					sum += ptr.term.coeff;					
				}
				ptr = ptr.next;
			}
			// finalNode = new Node(sum, i, finalNode);
			if (sum != 0) {
				finalNode = new Node(sum, i, finalNode);
			}
		}
		
		
		return finalNode;
	}
	
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		if (poly == null) {
			return 0;
		}
		
		int sum = 0;
		Node ptr = poly;
		
		
		while (ptr != null) {
			
			// float temp = (float) Math.pow(x, ptr.term.degree);
			// sum += (ptr.term.coeff) * temp;
			
			sum += (ptr.term.coeff)*(Math.pow(x, ptr.term.degree));
			ptr = ptr.next;
			
		}
		return sum;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
