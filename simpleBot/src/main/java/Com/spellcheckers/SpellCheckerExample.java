package Com.spellcheckers;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.TeXWordFinder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SpellCheckerExample implements SpellCheckListener {
    private static SpellDictionaryHashMap dictionaryHashMap;

    static{
        File dictionary = new File("/Users/dhwanildesai/Desktop/simpleBot/src/main/resources/dictionary.txt");

        try{

            dictionaryHashMap = new SpellDictionaryHashMap(dictionary);

        }catch (IOException e){

            e.printStackTrace();

        }
    }

    private SpellChecker spellChecker;
    private List<String> misspelledWords;

    public SpellCheckerExample(){


        misspelledWords = new ArrayList<String>();
        spellChecker = new SpellChecker(dictionaryHashMap);
        spellChecker.addSpellCheckListener(this);
    }

    public List<String> getMisspelledWords(String text) {
        StringWordTokenizer texTok = new StringWordTokenizer(text,
                new TeXWordFinder());
        spellChecker.checkSpelling(texTok);
        return misspelledWords;
    }

    public String getCorrectedLine(String line) {
        List<String> misSpelledWords = getMisspelledWords(line);

        for (String misSpelledWord : misSpelledWords) {
            List<String> suggestions = getSuggestions(misSpelledWord);
            if (suggestions.size() == 0) {
                continue;
            }
            String bestSuggestion = suggestions.get(0);
            line = line.replace(misSpelledWord, bestSuggestion);
        }

        return line;
    }

    public String getCorrectedText(String line) {
        StringBuilder builder = new StringBuilder();
        String[] tempWords = line.split(" ");

        for (String tempWord : tempWords) {
            if (!spellChecker.isCorrect(tempWord)) {
                List suggestions = spellChecker.getSuggestions(tempWord, 0);
                if (suggestions.size() > 0) {
                    builder.append(spellChecker.getSuggestions(tempWord, 0).get(0).toString());
                } else {
                    builder.append(tempWord);
                }
            } else {
                builder.append(tempWord);
            }
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    public List<String> getSuggestions(String misspelledWord) {

        @SuppressWarnings("unchecked")
        List<Word> suggestedWords = spellChecker.getSuggestions(misspelledWord, 0);
        List<String> suggestions = new ArrayList<>();

        suggestedWords.forEach(i -> suggestions.add(i.getWord()));

        return suggestions;
    }

    public void spellingError(SpellCheckEvent event) {
        event.ignoreWord(true);
        misspelledWords.add(event.getInvalidWord());
    }



}
