/**
 * CSCI 2110
 * Assignment 5
 * By Mihir Patel
 * I have used Binary trees,OOPs and other basic java concepts in this class
 * I have created five methods in this class that will help in taking input from user and converting them to huffman codes
 * I have also added two methods provided by Dr. Srini, that will help find the encoding
 */


import java.awt.font.FontRenderContext;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Huffman {

    public static void main(String[] args) throws IOException {

        // initialize a scanner class
        Scanner input = new Scanner(System.in);

        // creating Queues that will help in building huffman tree
        Queue<BinaryTree<Pair>> S = new LinkedList<BinaryTree<Pair>>();
        Queue<BinaryTree<Pair>> T = new LinkedList<BinaryTree<Pair>>();

        // taking fileName as an input from the user and putting it in the file object to read the file data
        System.out.print("Enter the first filename to read from: ");
        String fileName = input.nextLine();
        File file = new File(fileName);
        Scanner kb = new Scanner(file);
        Scanner input2 =new Scanner(System.in);

        // taking text input from user to convert that text to huffman codes
        System.out.print("\n" + "Enter a line of text (uppercase letters only): ");
        String inputLine = input2.nextLine();



        // while loop to read each line data of the file
        BinaryTree<Pair> data = new BinaryTree<>();
        Pair p1 = new Pair();
        while (kb.hasNext()) {
            data = new BinaryTree<>();
            String letter = kb.next();
            double prob = kb.nextDouble();
            p1 = new Pair(letter.charAt(0), prob);
            data.setData(p1);
            S.add(data);
        }


        kb.close();
        huffmanAlg(S, T);

        // finding all the encoding codes by calling findEncoding method
        String[] codes =new String[26];
        codes = findEncoding(T.peek());

        // creating hashmap to store codes value at the Letter index
        HashMap<Character,String> map = new HashMap<>();

        // storing all the letters to an arraylist
        ArrayList<Character> allLetters= getAllLetters();

        // for loop to add letters and their codes
        for(int i=0;i<allLetters.size();i++){
            map.put(allLetters.get(i),codes[i]);
        }

        // creating boolean, initializing it to true,will change it to false if the user enters invalid input
        boolean inputCheck = inputCheck(inputChar(inputLine));

        // if input entered is valid,then it will call other method and will print encoded and decoded line, else will print error message
        if(inputCheck) {
            System.out.print("Here’s the encoded line:\t");
            String[] outEnc = (encodedText(inputLine, map, inputChar(inputLine)));
            for(String outputData:outEnc){
                System.out.print(outputData);
            }
            System.out.print("\nThe decoded Line is:\t");
            for(int i =0;i<decodedText(outEnc,map).length;i++){
                System.out.print(decodedText(outEnc,map)[i]);
            }
        }
        else{
            System.out.println("Please enter UPPERCASE Letter");
        }
    }

    // Empty constructor for the class
    public Huffman() {
    }


    /**
     * huffmanAlg method will make the huffman tree of all the letters according to the probability provided
     * I have used if-else condition to find the minimum two probability value and will store that in A and b respectively
     * Then have added A and B as a left and right child in a binary tree P
     * Lastly, I have added all the values remaining in T to P by following the same steps of adding as have followed before while adding the minimum two values
     * This method is a void method
     * @param S : queue S that will store all the letters and their probability
     * @param T : queue T is the helper queue that will store all the parent value
     */
    public static void huffmanAlg(Queue<BinaryTree<Pair>> S,Queue<BinaryTree<Pair>> T) {
        BinaryTree<Pair> A = new BinaryTree<>();
        BinaryTree<Pair> B = new BinaryTree<>();
        double addition = 0;
        Pair dataTemp = new Pair();
        while (!S.isEmpty()){

            if (T.isEmpty()) {
                A = S.remove();
                B = S.remove();
            }
            else {
                if (T.peek().getData().getProb() < S.peek().getData().getProb()) {
                    A = T.remove();
                } else if(S.peek().getData().getProb() < T.peek().getData().getProb()){
                    A = S.remove();
                }
                if (!T.isEmpty()) {
                    if (T.peek().getData().getProb() < S.peek().getData().getProb() ) {
                        B = T.remove();
                    } else if(S.peek().getData().getProb() < T.peek().getData().getProb()) {
                        B = S.remove();
                    }
                } else {
                    B = S.remove();
                }
            }
            BinaryTree<Pair> P = new BinaryTree<>();
            addition = A.getData().getProb() + B.getData().getProb();
            dataTemp = new Pair('0', addition);
            P.attachLeft(A);
            P.attachRight(B);
            P.makeRoot(dataTemp);
            T.add(P);
        }
        while (T.size()>1){
            BinaryTree<Pair> P = new BinaryTree<>();
            A=T.remove();
            B=T.remove();
            addition = A.getData().getProb() + B.getData().getProb();
            dataTemp = new Pair('0', addition);
            P.makeRoot(dataTemp);
            P.attachLeft(A);
            P.attachRight(B);
            T.add(P);

        }
    }

    // This two method is provided by Dr. Srini
    // It will find the encoding codes for all the letters, by using recursion
    // Also, it will store all the codes in an Array of String
    private static String[] findEncoding(BinaryTree<Pair> bt){
        String[] result = new String[26];
        findEncoding(bt, result, "");
        return result;
    }

    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix) {
        // test is node/tree is a leaf
        if (bt.getLeft() == null && bt.getRight() == null) {
            a[bt.getData().getValue() - 65] = prefix;
        }

        // recursive calls
        else {
            findEncoding(bt.getLeft(), a, prefix + "0");
            findEncoding(bt.getRight(), a, prefix + "1");
        }
    }



    /**
     * This method will add all the Alphabet Letters to an Arraylist of Character
     * I have created for loop that will help in adding the letters
     * @return an ArrayList of character
     */
    private static ArrayList<Character> getAllLetters(){
        char ch;
        ArrayList<Character> allLetters = new ArrayList<>();

        for (ch = 'A'; ch <= 'Z'; ch++) {
            allLetters.add(ch);
        }
        return allLetters;
    }


    /**
     * This method will add all the input line characters to an array of character
     * @param inputLine : line inputted by user
     * @return an array of character
     */
    private static char[] inputChar(String inputLine){
        char[] chr = new char[inputLine.length()];

        for (int i = 0; i < inputLine.length(); i++) {
            chr[i] = inputLine.charAt(i);
        }
        return chr;
    }

    /**
     * This method will check if the inputted value is Uppercase or not
     * If not, then it will return false else true
     * @param allInpLetters : characters of inputted line
     * @return boolean
     */
    private static boolean inputCheck(char[] allInpLetters){
        boolean inpCheck=true;
        for (int ascii : allInpLetters) {
            // ascii value 32 for empty space(' ')
            if (!((ascii >= 65 && ascii <= 90) || ascii==32)) {
                inpCheck = false;
                break;
            }
        }
        return inpCheck;
    }

    /**
     * In this method I have used for loop and if-else condition
     * It will help in adding all the encoded codes of the inputted letters to an array of string
     * @param inputLine : Line inputted by user
     * @param map : hashmap containing alphabets index and their ascii value
     * @param inputChar : Array containing all the inputted line character
     * @return an array of string containing encoded codes
     */
    private static String[] encodedText(String inputLine,HashMap<Character,String> map,char[] inputChar) {

        String[] outputEnc = new String[inputLine.length()];

        for (int i=0;i< inputChar.length;i++) {
            for (char j = 'A'; j <= 'Z'; j++) {
                if (inputChar[i] == ' ') {
                    outputEnc[i]= (" ");
                    break;
                }
                else if (inputChar[i] == j) {
                    outputEnc[i]=(map.get(j));
                }
            }
        }
        return outputEnc;
    }

    /**
     * In this method I have used for loop and if-else condition
     * It will help in adding all the decoded codes of the encoded letters of the inputted value to an array of string
     * @param outputEnc : encoded output of the inputted lines
     * @param map : hashmap containing alphabets index and their ascii value
     * @return an array of string containing decoded codes
     */
    private static String[] decodedText(String[] outputEnc,HashMap<Character,String> map) {
        String[] outputDec = new String[outputEnc.length];

        for(int i=0;i< outputEnc.length;i++) {
            for (char j = 'A'; j <= 'Z'; j++) {
                if (outputEnc[i].equals(" ")) {
                    outputDec[i] = " ";
                }
                else if (outputEnc[i].equals(map.get(j))) {
                    outputDec[i] = j + "";
                }
            }
        }
        return outputDec;
    }
}

