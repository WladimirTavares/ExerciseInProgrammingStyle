import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/*
 * Problem:
 * Given a text file, we want to diplay the N (e.g. 25)
 * most frequen words and corresponding frequencies ordered
 * by deacreasing value of frequency. We should make sure
 * to normalize for capitalization and to ignore stop words
 * like "the","for", etc. To keep things simple, we don't care
 * about of words that have equal frequencies. This computacional task
 * is known as term frequency.
 * 
 * 
 * Pipeline Style:
 * 
 * Constraints:
 * Larger problem is decomposed using functionl abstraction. Functions
 * take input, and produce output.
 * No shared state between functions.
 * The larger problem is solved by composing functions one after the other,
 * in pipeline, as a faithful reproduction of mathematical 
 * function composition f ◦ g.
 * 
*/


public class pipeline {
    
    public static char[] read_file(String path_to_file) throws IOException {
        Scanner sc = new Scanner(new FileReader(path_to_file));
        String content = "";
        while( sc.hasNext() ){
            content = content + "\n" + sc.nextLine();
        }

        char []data;
        data = content.toCharArray();
        
        return data;
    }

    public static boolean isalnum(char ch) {
        return Character.isDigit(ch) || Character.isLetter(ch);
    }

    public static char[] filter_chars_and_normalize(char[] data){
        for(int i = 0; i < data.length; i++){
            if (! isalnum( data[i])){
                data[i] = ' ';
            }else{
                data[i] = Character.toLowerCase(data[i]);
            }
        }

        return data;
        
    }

    public static String[] scan(char[] data){
        String data_str = new String(data);
        String [] words;
        words = data_str.split("\\W+");
        return words;
    }

    public static String[] remove_stop_words(String [] words) throws IOException{
        Scanner sc = new Scanner(new FileReader("../stop_words.txt"));
        String [] stopWords = sc.nextLine().split(","); 
        ArrayList <String> stopWordsList = new ArrayList<String>();
        for(String word : stopWords){
            stopWordsList.add(word);
        }

        for(char c = 'a'; c <= 'z'; c++){
            stopWordsList.add( String.valueOf(c) );
        }

        ArrayList <String> temp = new ArrayList<String>();

        for(String word : words){
            if( !stopWordsList.contains(word) ){
                temp.add(word);
            }
        }

       
        words = new String[temp.size()];
        for(int i = 0; i < temp.size(); i++){
            words[i] = temp.get(i);
        }
        
        return words;

    }

    public static ArrayList<Pair<String,Integer>>  frequencies(String[] words){
        Map<String,Integer> freq = new HashMap<String,Integer>();
        for(String word: words){
            if( freq.get(word) == null)
                freq.put(word, 1);
            else
                freq.put(word, freq.get(word) + 1);
        }
        ArrayList<Pair<String,Integer>> word_freqs = new ArrayList<Pair<String,Integer>>();
        freq.forEach((k, v) -> {
            word_freqs.add( new Pair<String,Integer>(k,v) );
        });

        return word_freqs;
    }

    public static ArrayList<Pair<String,Integer>> sort(ArrayList<Pair<String,Integer>> word_freqs){
        Collections.sort(word_freqs, new Comparator<Pair<String,Integer>>() {
            @Override
            public int compare(final Pair<String,Integer> lhs,final Pair<String,Integer> rhs){
                return rhs.getValue() - lhs.getValue();
            }
        });
        return word_freqs;
    }

    public static void print(ArrayList<Pair<String,Integer>> word_freqs){
        for(int i = 0; i < 25; i++){
            System.out.println(word_freqs.get(i).getKey() + " : " + word_freqs.get(i).getValue() );
        }

    }



    public static void main(String[] args) throws IOException {
        print(
            sort(
                frequencies(
                    remove_stop_words(
                        scan(
                            filter_chars_and_normalize( 
                                read_file("../pride-and-prejudice.txt")
                            )
                        )
                    )
                )
            )
        );

    }

}
