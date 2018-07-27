package Data_Structures.Trie;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private TrieNode root = TrieNode.newNode();

    private Trie(){}

    public static Trie create(){ return new Trie(); }

    public void add(char[] word, Object obj){
        TrieNode node = root;
        for (char c : word){
            if (!node.getChildren().containsKey(c)) {
                node.addChild(Character.valueOf(c), TrieNode.newNode());
            }
            node = node.getChildren().get(c);
        }
        node.getObjects().add(obj);
    }

    public List<Object> get(char[] word){
        TrieNode node = root;
        for (char c : word){
            if (!node.getChildren().containsKey(c)){
                return null;
            }
            node = node.getChildren().get(c);
        }
        return node.getObjects();
    }

    public List<Object> getAll (char[] prefix){
        List<Object> results = new ArrayList<Object>();
        TrieNode node = root;
        for (char c : prefix){
            if (!node.getChildren().containsKey(c)){
                return null;
            }
            node = node.getChildren().get(c);
        }
        getAllFrom(node, results);
        return results;
    }

    public void getAllFrom(TrieNode node, List<Object> results){
        results.add(node.getObjects());
        for (TrieNode child : node.getChildren().values()){
            getAllFrom(child, results);
        }
    }
}
