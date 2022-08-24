import java.io.*;
import java.util.*;

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
 * Things Style:
 * 
 * Constraints:
 * The larger problem is decomposed into things that make sense for the 
 * problem domain.
 * Each thing is a capsule of data that exposes procedures to the rest of
 * the world.
 * Data is never accessed directly, only through these procedures.
 * Capsules can reappropriate procedures defined in other capsules.
 * 
*/



public class things {
    public static void main(String[] args) throws IOException {
        new WordFrequencyController("../pride-and-prejudice.txt").run();
    }
}

abstract class TFExercise {
    public String getInfo() {
        return this.getClass().getName();
    }
}

class WordFrequencyController  extends TFExercise {
    private DataStorageManager storageManager;
    private StopWordManager stopWordManager;
    private WordFrequencyManager wordFreqManager;
    
    public WordFrequencyController(String pathToFile) throws IOException {
        this.storageManager = new DataStorageManager(pathToFile);
        this.stopWordManager = new StopWordManager();
        this.wordFreqManager = new WordFrequencyManager();
    }
    
    public void run() {
        for (String word : this.storageManager.getWords()) {
            if (!this.stopWordManager.isStopWord(word)) {
                this.wordFreqManager.incrementCount(word);
            }
        }
        
        int numWordsPrinted = 0;
        for (WordFrequencyPair pair : this.wordFreqManager.sorted()) {
            System.out.println(pair.getWord() + " - " + pair.getFrequency());
            
            numWordsPrinted++;
            if (numWordsPrinted >= 25) {
                break;
            }
        }
    }
}


class DataStorageManager extends TFExercise {
    private List<String> words;
    
    public DataStorageManager(String pathToFile) throws IOException {
        this.words = new ArrayList<String>();
        
        Scanner f = new Scanner(new File(pathToFile), "UTF-8");
        try {
            f.useDelimiter("[\\W_]+");
            while (f.hasNext()) {
                this.words.add(f.next().toLowerCase());
            }
        } finally {
            f.close();
        }
    }
    
    public List<String> getWords() {
        return this.words;
    }
    
    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.words.getClass().getName();
    }
}

class StopWordManager extends TFExercise {
    private Set<String> stopWords;
    
    public StopWordManager() throws IOException {
        this.stopWords = new HashSet<String>();
        
        Scanner f = new Scanner(new File("../stop_words.txt"), "UTF-8");
        try {
            f.useDelimiter(",");
            while (f.hasNext()) {
                this.stopWords.add(f.next());
            }
        } finally {
            f.close();
        }
        
        // Add single-letter words
        for (char c = 'a'; c <= 'z'; c++) {
            this.stopWords.add("" + c);
        }
    }
    
    public boolean isStopWord(String word) {
        return this.stopWords.contains(word);
    }
    
    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.stopWords.getClass().getName();
    }
}

class MutableInteger {
    private int value;
    
    public MutableInteger(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
}

class WordFrequencyManager extends TFExercise {
    private Map<String, MutableInteger> wordFreqs;
    
    public WordFrequencyManager() {
        this.wordFreqs = new HashMap<String, MutableInteger>();
    }
    
    public void incrementCount(String word) {
        MutableInteger count = this.wordFreqs.get(word);
        if (count == null) {
            this.wordFreqs.put(word, new MutableInteger(1));
        } else {
            count.setValue(count.getValue() + 1);
        }
    }
    
    public List<WordFrequencyPair> sorted() {
        List<WordFrequencyPair> pairs = new ArrayList<WordFrequencyPair>();
        for (Map.Entry<String, MutableInteger> entry : wordFreqs.entrySet()) {
            pairs.add(new WordFrequencyPair(entry.getKey(), entry.getValue().getValue()));
        }
        Collections.sort(pairs);
        Collections.reverse(pairs);
        return pairs;
    }
    
    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.wordFreqs.getClass().getName();
    }
}

class WordFrequencyPair implements Comparable<WordFrequencyPair> {
    private String word;
    private int frequency;
    
    public WordFrequencyPair(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }
    
    public String getWord() {
        return word;
    }
    
    public int getFrequency() {
        return frequency;
    }
    
    public int compareTo(WordFrequencyPair other) {
        return this.frequency - other.frequency;
    }
}