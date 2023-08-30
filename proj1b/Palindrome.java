public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque list = new LinkedListDeque();
        for (int i = 0; i < word.length(); i++) {
            list.addLast(word.charAt(i));
        }
        return list;
    }
    public boolean isPalindrome(String word) {
        Deque w = wordToDeque(word);
        equals eq = new equals();
        return helpPal(w, eq);
        /*
        Implementation not using <Deque>.
         */
//        int length = word.length();
//        int l = Math.floorDiv(length, 2);
//        for (int i = 0; i < l; i++) {
//            if (word.charAt(i) != word.charAt(length - 1 - i)) {
//                return false;
//            }
//        }
//        return true;
    }
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque w = wordToDeque(word);
        OffByOne obo = new OffByOne();
        return helpPal(w, obo);
    }
    /*
    Helper functions:
     */
    private boolean helpPal(Deque w, CharacterComparator cc) {
        if (w.size() <= 1) {
            return true;
        } else if (cc.equalChars((char)w.removeFirst(), (char)w.removeLast())){
            return helpPal(w, cc);
        } else {
            return false;
        }
    }
    private class equals implements CharacterComparator {
        @Override
        public boolean equalChars(char x, char y) {
            return x == y;
        }
    }
}
