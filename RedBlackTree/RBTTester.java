package sjsu.ryan.cs146.project2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sjsu.ryan.cs146.project2.RedBlackTree.RBTreeNode;
import sjsu.ryan.cs146.project2.RedBlackTree.Visitor;

public class RBTTester {

	@Test
	public void test() {
		RedBlackTree<String> rbt = new RedBlackTree<String>();
        rbt.insert("D");
        rbt.insert("B");
        rbt.insert("A");
        rbt.insert("C");
        rbt.insert("F");
        rbt.insert("E");
        rbt.insert("H");
        rbt.insert("G");
        rbt.insert("I");
        rbt.insert("J");
        assertEquals("DBACFEHGIJ", makeString(rbt));
        String str= "Color: true, Key:D Parent: \n"+
					"Color: true, Key:B Parent: D\n"+
					"Color: true, Key:A Parent: B\n"+
					"Color: true, Key:C Parent: B\n"+
					"Color: true, Key:F Parent: D\n"+
					"Color: true, Key:E Parent: F\n"+
					"Color: false, Key:H Parent: F\n"+
					"Color: true, Key:G Parent: H\n"+
					"Color: true, Key:I Parent: H\n"+
					"Color: false, Key:J Parent: I\n";
		assertEquals(str, makeStringDetails(rbt));
    }
    
    public static String makeString(RedBlackTree<String> t) {
       class MyVisitor implements RedBlackTree.Visitor<String> {
          String result = "";
          
          public void visit(RedBlackTree.RBTreeNode<String> n)
          {
             result = result + n.getData();
          }
       };
       
       MyVisitor v = new MyVisitor();
       t.preOrderVisit(v);
       
       return v.result;
    }

    public static String makeStringDetails(RedBlackTree<String> t) {
    	class MyVisitor implements RedBlackTree.Visitor<String> {
    		String result = "";
    		
    	    public void visit(RedBlackTree.RBTreeNode<String> n) {
    	    	if(n != null) {
    	    		result = result +"Color: "+n.getColor()+", Key:"+n.getData();
    	    	}
    	        	  
    	    	if(n.getPrev() != null) {
    	    		result += " Parent: "+n.getPrev().getData()+"\n";
    	    	} else {
    	    		result += " Parent: \n";
    	    	}
    	    }
    	};
    	       
    	MyVisitor v = new MyVisitor();
    	t.preOrderVisit(v);
    	
    	return v.result;
    }
    
    /*
     * Test for rotateRight()
     */
    @Test
    public void testRotateTreeRight() {
    	String[] words = {"x", "a", "y", "b"};
    	RedBlackTree<String> tree = new RedBlackTree<String>();
    	
    	// Building tree
    	for(String word : words) {
    		tree.insert(word);
    	}

    	// Rotating tree about its root
    	tree.rotateRight(tree.getRoot());
    	
    	// Build actual output
    	StringBuilder actual = new StringBuilder();
    	
    	tree.preOrderVisit(new Visitor<String>() {

			@Override
			public void visit(RBTreeNode<String> n) {
				actual.append(n.getData() + " ");			
			}
    		
    	});
    	
    	// Expected output
    	String expected = "a x b y ";
    	
    	assertEquals(expected, actual.toString());
    }

    /*
     * Test for rotateLeft()
     */
    @Test
    public void testRotateTreeLeft() {
    	String[] words = {"b", "a", "d", "c"};
    	RedBlackTree<String> tree = new RedBlackTree<String>();
    	
    	// Building tree
    	for(String word : words) {
    		tree.insert(word);
    	}

    	// Rotating tree about its root
    	tree.rotateLeft(tree.getRoot());
    	
    	// Build actual output
    	StringBuilder actual = new StringBuilder();
    	
    	tree.preOrderVisit(new Visitor<String>() {

			@Override
			public void visit(RBTreeNode<String> n) {
				actual.append(n.getData() + " ");			
			}
    		
    	});
    	
    	// Expected output
    	String expected = "d b a c ";
    	
    	assertEquals(expected, actual.toString());
    }
    
    /*
     * Test tree search, also prints tree load/lookup times
     */
    @Test
    public void testDictionaryLookup() {
    	Dictionary dict = new Dictionary();
    	
    	// Insert time testing
    	long time = System.currentTimeMillis();
    	dict.loadFile("words10k.txt");
    	time = System.currentTimeMillis() - time;
    	System.out.println("Time to load words into RB tree: " + time + " ms");
    	
    	// Search time test
    	time = System.currentTimeMillis();
    	for(String word : dict.getWordList()) {
    		dict.lookUp(word);	
    	}
    	time = System.currentTimeMillis() - time;
    	System.out.println("Time to search for a word in RB tree: " + time + " ms");
    }
        
 }
  
