import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        generateWord("",letters);
    }
    public void generateWord(String s, String letters){
        if(letters.length() == 0){
            words.add(s);
            return;
        }
        for(int i = 0; i < letters.length(); i++){
            String news = s + letters.charAt(i);
            words.add(news);
            generateWord(news, letters.substring(0,i) + letters.substring(i+1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        String[] so = mergeSort(words, 0, words.size()-1);
        ArrayList<String> temp = new ArrayList<String>();
        for(String s: so){
            temp.add(s);
        }
        words = temp;
    }

    public String[] mergeSort(ArrayList<String> w, int start, int end){
        if(start == end)
        {
            String[] s = new String[1];
            s[0] = w.get(start);
            return s;
        }

        int divide = (start+end) / 2;
        String[] merge1 = mergeSort(w,start, divide);
        String[] merge2 = mergeSort(w,divide+1, end);

        return merge(merge1, merge2);
    }

    public String[] merge(String[] arr1, String[] arr2){
        String[] merged = new String[arr1.length + arr2.length];
        int a = 0;
        int b = 0;
        int c = 0;
        while(a < arr1.length && b < arr2.length){
            if(arr1[a].compareTo(arr2[b]) < 0){
                merged[c++] = arr1[a++];
            }
            else{
                merged[c++] = arr2[b++];
            }
        }
        while(a < arr1.length){
            merged[c++] = arr1[a++];
        }
        while(b <arr2.length){
            merged[c++] = arr2[b++];
        }
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for(int i = 0; i < words.size(); i++){
            if(!searchDic(words.get(i),0, DICTIONARY_SIZE-1)){
                words.remove(i);
                i--;
            }
        }
    }

    public boolean searchDic(String s, int start, int end){
        int divide = start + (end-start)/2;
        String check = DICTIONARY[divide];
        if(s.equals(check)){
            return true;
        }
        if(start == end){
            return false;
        }
       if(s.compareTo(check) < 0){
            return searchDic(s,start, divide);
        }
       else{
            return searchDic(s,divide +1,end);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
