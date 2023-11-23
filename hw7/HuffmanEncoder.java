import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> table = new HashMap<>();
        for (char c: inputSymbols) {
            if (table.containsKey(c)) {
                table.put(c, table.get(c) + 1);
            } else {
                table.put(c, 1);
            }
        }
        return table;
    }
    public static void main(String[] args) {
        if (args.length == 0) throw new IllegalArgumentException("File name required");
        // Read file as 8bit symbols into an array;
        char[] inputSymbols = FileUtils.readFile(args[0]);
        // Construct frequency table and decoding trie;
        Map<Character, Integer> freqTable = buildFrequencyTable(inputSymbols);
        BinaryTrie bt = new BinaryTrie(freqTable);
        // Write trie to new file;
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(bt);
        // Write number of symbols;
        ow.writeObject(inputSymbols.length);
        // Use trie to create lookUpTable;
        Map<Character, BitSequence> lookUpTable = bt.buildLookupTable();
        // Create list of bitSequences;
        List<BitSequence> list = new ArrayList<>();
        for (char c: inputSymbols)
            list.add(lookUpTable.get(c));
        // Assemble, write to file;
        ow.writeObject(BitSequence.assemble(list));
    }
}
