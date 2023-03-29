import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
/**
 * Class that implements an AVL tree which implements the MyMap interface.
 * @author Chimaobi Onwuka
 * @UNI ceo2134
 * @version 1.0.0 December 14, 2022
 */



public class CommonWordFinder
{
    private static int limit = 10;
    private static int uniqueWords = 0;
    private static MyMap<String, Integer> map;
    private static MyMap<String, Integer> tempMap;

    public static void main(String[] args)
    {
        //Checks if there are sufficient command line arguments
        if ((args.length != 2) && (args.length != 3)) {
            System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
            System.exit(1);
        }

        // Creates new text file and checks if the text file exists in broader file
        File textFile = new File(args[0]);
        if (!textFile.exists()) {
            System.err.println("Error: Cannot open file '" + args[0] + "' for input.");
            System.exit(1);
        }

        //Checks to see if the data structure command argument is valid
        if ((!args[1].equals("bst") && (!args[1].equals("avl")) && (!args[1].equals("hash")))) {
            System.err.println("Error: Invalid data structure '" + args[1] + "' received.");
            System.exit(1);

        }

        //Checks to see if the limit is within the valid range (>0)
        if ((args.length == 3) && (Math.abs(Integer.parseInt(args[2])) != Integer.parseInt(args[2]))) {
            System.err.println("Error: Invalid limit '" + args[2] + "' received.");
            System.exit(1);
        }

        //Checks if a limit is specified, and if so, the limit variable is set to this value.
        if (args.length == 3) {
            limit = Integer.parseInt(args[2]);
        }

        //Instantiation of specified Data Structure depending on command line argument
        if (args[1].equals("hash"))
        {
            map = new MyHashMap<>();
            tempMap = new MyHashMap<>();
        }
        else if (args[1].equals("avl"))
        {
            map = new AVLTreeMap<>();
            tempMap = new AVLTreeMap<>();
        }
        else
        {
            map = new BSTMap<>();
            tempMap = new BSTMap<>();
        }

        //Calls the method that reads the given file, parses the text and inputs the words and their counts into the data structure
        reading(textFile, map);

        //If the specified limit is more than the amount of items in the data structure then the limit becomes the amount of items.
        if (limit > map.size())
        {
            limit = map.size();
        }

        //Initializes two arrays that hold the most common words, and their associated counts respectively
        String[] wordsAnd = new String[limit];
        int[] valuesAnd = new int[limit];

        //Calls the method that populates the most common words array and their counts array
        sorting(wordsAnd, valuesAnd);

        //Calls the method that formats displays each of the most common words, and their counts
        output(wordsAnd, valuesAnd);
    }

    /**
     * Parses the given text file, and inserts each word key with number of
     * occurrences as its value into the map parameter.
     * @param textFile the text file in which the words the buffered reader is reading from
     * @param map the map data structure (bst,avl, or hash) in which the word and occurrences are entered.
     */
    public static void reading(File textFile, MyMap<String, Integer> map ) {

        StringBuilder word = new StringBuilder();
        boolean middle = false;

        //Creates a buffered reader to read file "character by character" and add the words to map
        try {
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            int eachCharacter;
            while ((eachCharacter = reader.read()) != -1)
            {
                    char current = Character.toLowerCase((char) eachCharacter);

                    //Determines if the current character is an EOW character and puts contents of string builder into map
                    if (current == ' '||current == '\n')
                    {

                        String item = word.toString().strip();
                        if (word.length() != 0)
                        {
                            if (map.get(item) == null)
                            {
                                map.put(item, 1);
                            }
                            else
                            {
                                map.put(item, map.get(item) + 1);
                            }
                        }
                        word.setLength(0);
                        map.remove("");
                        middle = false;
                    }
                    //Checking if the word being added to is at the beginning or not to see if "-" can be added
                    if (!word.isEmpty())
                    {
                        middle = true;
                    }
                    //Adds the character to the word if it meets all the initial criteria
                    if (Character.isAlphabetic(current) && !Character.isDigit(current) || current == (char) 39)
                    {
                        word.append(current);
                    }
                    //If the '-' is in not the first letter of the word THEN it gets added
                    if (current == '-' && middle)
                    {
                        word.append(current);
                    }
            }

            //Adds the last remaining word in the string builder and closes the reader
            String item = word.toString().strip();

            if (word.length() != 0)
            {
                if (map.get(item) == null)
                {
                    map.put(item, 1);
                }
                else
                {
                    map.put(item, map.get(item) + 1);
                }
            }
            word.setLength(0);
            map.remove("");
            reader.close();

            //Exception catching
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + textFile + "'.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the "limit" most frequent amount of words
     * and their associated occurrence values, as well as their associated place.
     * @param wordsAnd the array that hold the most common
     * words with a length of given limit or maximum amount of words (whichever is lesser)
     * @param valuesAnd the array that hold the highest amount of
     * occurrences associated with the strings in WordsAnd array
     * with a length of given limit
     */
    public static void output(String[] wordsAnd, int[] valuesAnd)
    {
        //Outputs the number of unique words
        System.out.println("Total unique words: " + uniqueWords);

        //Determines the format width based on limit
        int width = Integer.toString(limit).length();
        String actualwidth = "%" + width + "d";


        //Finds the longest word in the wordsAnd array and uses that to determine secondary format width
        int longestword = 0;
        for (String s : wordsAnd) {
            if (s.length() > longestword) {
                longestword = s.length();
            }
        }
        String numberWidth = "%-" + (longestword +3) + "s";

        //Prints the rank, word, and number of occurrences
        int count = 1;
        for (int i = 0; i<wordsAnd.length; i++)
        {

            System.out.printf(actualwidth, count);
            System.out.printf(numberWidth,". " + wordsAnd[i]);
            System.out.printf("%1d", valuesAnd[i]);
            System.out.print(System.lineSeparator());
            count+= 1;
        }

    }

    /**
     * Determines the most frequent words in the map, and adds the words into a sorted list from most frequent to least.
     * Additionally, takes each of the associated occurrences and places them in a separate sorted list, with each index of the
     * first list corresponding the index of the second.
     * @param wordsAnd the array that hold the most common
     * words with a length of given limit or maximum amount of words (whichever is lesser)
     * @param valuesAnd the array that hold the highest amount of
     *  occurrences associated with the strings in WordsAnd array
     * with a length of given limit
     */
    public static void sorting(String[] wordsAnd, int[] valuesAnd)
    {
        //Sorting the words and counts into two separate arrays
        int count3 = 0;
        tempMap = map;
        uniqueWords = map.size();
        for (int i = 0; i < limit; i++)
        {
            //Iterator goes through each entry, gets the string and occurence value
            Iterator<Entry<String, Integer>> itr2 = tempMap.iterator();
            int currentMax = 0;
            String maxWord = "";
            while (itr2.hasNext())
            {
                Entry ver = itr2.next();
                String word2 = (String) ver.key;
                int value2 = (int) ver.value;

                //Checks against the current "max occurrences" and if it is higher or equal it is updated
                if (value2 > currentMax)
                {
                    currentMax = value2;
                    maxWord = word2;
                }
                else if ((value2 == currentMax))
                {
                    if (word2.compareTo(maxWord) < 0)
                    {
                        currentMax = value2;
                        maxWord = word2;
                    }
                }
            }
            //The word is then removed from the map, and the word, and count are added to arrays. The process is repeated.
            tempMap.remove(maxWord);
            wordsAnd[count3] = maxWord;
            valuesAnd[count3] = currentMax;
            count3+= 1;
        }
    }

}
