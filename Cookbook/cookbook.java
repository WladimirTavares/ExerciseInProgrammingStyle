import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
 * Procedural Style:
 * 
 * Constraints:
 * Complexity of control flow tamed by dividing the large
 * problem into smaller units using procedural abstraction. 
 * Procedures are pieces of functionality that may take input, 
 * but that don’t necessarily produce output that is relevant for the problem.
 * 
 * Procedures may share state in the form of static variables (class variables).
 * 
 * The larger problem is solved by applying the procedures, 
 * one after the other, that change, or add to, the shared state.
 * 
*/

public class cookbook {
    static char []data;
    static String [] words;
    static ArrayList<Pair<String,Integer>> word_freqs;

    public static void read_file(String path_to_file) throws IOException {
        Scanner sc = new Scanner(new FileReader(path_to_file));
        String content = "";
        while( sc.hasNext() ){
            content = content + "\n" + sc.nextLine();
        }

        data = content.toCharArray();
        

    }

    public static boolean isalnum(char ch) {
        return Character.isDigit(ch) || Character.isLetter(ch);
    }

    public static void filter_chars_and_normalize(){
        for(int i = 0; i < data.length; i++){
            if (! isalnum( data[i])){
                data[i] = ' ';
            }else{
                data[i] = Character.toLowerCase(data[i]);
            }
        }
        
    }

    public static void scan(){
        String data_str = new String(data);
        words = data_str.split("\\W+");
        
    }


    public static void remove_stop_words() throws IOException{
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
        


    }

    
    public static void frequencies(){
        
        Map<String,Integer> freq = new HashMap<String,Integer>();
        
        for(String word: words){
            if( freq.get(word) == null)
                freq.put(word, 1);
            else
                freq.put(word, freq.get(word) + 1);
        }

        word_freqs = new ArrayList<Pair<String,Integer>>();

        freq.forEach((k, v) -> {
            word_freqs.add( new Pair<String,Integer>(k,v) );
        });

        
        
        

    }

    public static void sort(){
        Collections.sort(word_freqs, new Comparator<Pair<String,Integer>>() {
            @Override
            public int compare(final Pair<String,Integer> lhs,final Pair<String,Integer> rhs){
                return rhs.getValue() - lhs.getValue();
            }
        });

    }

    public static void print(){
        for(int i = 0; i < 25; i++){
            System.out.println(word_freqs.get(i).getKey() + " : " + word_freqs.get(i).getValue() );
        }

    }

    public static void main(String[] args) throws IOException {
        read_file("../pride-and-prejudice.txt");
        filter_chars_and_normalize();
        scan();
        remove_stop_words();
        frequencies();
        sort();
        print();
    }
}
