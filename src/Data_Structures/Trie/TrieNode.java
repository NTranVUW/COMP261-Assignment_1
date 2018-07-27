package Data_Structures.Trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrieNode {
    private final List<Object> objects = new ArrayList<Object>();
    private final HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();

    private TrieNode(){}

    public static TrieNode newNode(){
        return new TrieNode();
    }

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void addChild(Character value, TrieNode node){
        children.put(value, node);
    }
}
