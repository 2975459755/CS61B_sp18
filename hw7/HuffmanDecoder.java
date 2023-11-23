
public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length < 2) throw new IllegalArgumentException("File name required");
        // Read the trie;
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie bt = (BinaryTrie) or.readObject();
        // Read number of symbols;
        int num = (int) or.readObject();
        // Read massive bit sequence;
        BitSequence bs = (BitSequence) or.readObject();
        // Read symbols;
        char[] chars = new char[num];
        int parsedBits;
        for (int i = 0; i < num; i ++) {
            Match oneSeq = bt.longestPrefixMatch(bs);
            chars[i] = oneSeq.getSymbol();
            parsedBits = oneSeq.getSequence().length();
            bs = bs.allButFirstNBits(parsedBits);
        }
        // Write symbols into file;
        FileUtils.writeCharArray(args[1], chars);
    }
}
