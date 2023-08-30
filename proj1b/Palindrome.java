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
        return helpPal(w);
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
    private boolean helpPal(Deque w) {
        if (w.size() <= 1) {
            return true;
        } else if (w.removeFirst() == w.removeLast()){
            return helpPal(w);
        } else {
            return false;
        }
    }
}
