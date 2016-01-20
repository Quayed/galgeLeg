package com.example.jonas.galgelegaflevering;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Galgelogik{
    private ArrayList<String> muligeOrd = new ArrayList<String>();
    private String ordet;
    private ArrayList<String> brugteBogstaver = new ArrayList<String>();
    private String synligtOrd;
    private int antalForkerteBogstaver;
    private boolean sidsteBogstavVarKorrekt;
    private boolean spilletErVundet;
    private boolean spilletErTabt;
    private String DB_FULL_PATH;
    private ArrayList<Integer> possibleLengths;
    private Context context;
    private WordsDB dbHandler;
    private SQLiteDatabase db;
    private boolean wordsUpdated;
    private int wordLength;
    private int timeLeft;
    private static Galgelogik instance;
    private List<ParseObject> highscore;
    private List<HighscoreSubscriber> highscoreSubscribers;
    private int minHighscore;

    public ArrayList<String> getBrugteBogstaver() {
        return brugteBogstaver;
    }

    public String getSynligtOrd() {
        return synligtOrd;
    }

    public String getOrdet() {
        return ordet;
    }

    public int getAntalForkerteBogstaver() {
        return antalForkerteBogstaver;
    }

    public boolean erSidsteBogstavKorrekt() {
        return sidsteBogstavVarKorrekt;
    }

    public boolean erSpilletVundet() {
        return spilletErVundet;
    }

    public boolean erSpilletTabt() {
        return spilletErTabt;
    }

    public boolean erSpilletSlut() {
        return spilletErTabt || spilletErVundet;
    }

    public String[] getMuligeOrd() {
        String[] newStrings;
        newStrings = new String[muligeOrd.size()];
        newStrings = muligeOrd.toArray(newStrings);
        if (checkDataBase()) {
            muligeOrd = new ArrayList<>();
            SQLiteDatabase db = dbHandler.getReadableDatabase();
            Cursor cursor;
            cursor = db.rawQuery("SELECT * FROM words", null);
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    muligeOrd.add(cursor.getString(WordsDB.WORD));
                    newStrings = new String[muligeOrd.size()];
                    newStrings = muligeOrd.toArray(newStrings);
                } while (cursor.moveToNext());
            } else
                return newStrings;
        }

        return newStrings;
    }


    public static Galgelogik getInstance() {
        if(instance != null)
            return instance;
        else{
            instance = new Galgelogik();
            return instance;
        }
    }

    private Galgelogik() {
        muligeOrd.add("bil");
        muligeOrd.add("computer");
        muligeOrd.add("programmering");
        muligeOrd.add("motorvej");
        muligeOrd.add("busrute");
        muligeOrd.add("gangsti");
        muligeOrd.add("skovsnegl");
        muligeOrd.add("solsort");
        highscoreSubscribers = new ArrayList<>();
    }

    public void initDB(Context context){
        dbHandler = new WordsDB(context);
    }

    public String hentNytOrd() {
        if (!checkDataBase() || !wordsUpdated) {
            return muligeOrd.get(new Random().nextInt(muligeOrd.size()));
        } else {
            SQLiteDatabase db = dbHandler.getReadableDatabase();
            Cursor cursor;
            if (wordLength == 0) {
                cursor = db.rawQuery("SELECT * FROM words ORDER BY RANDOM() LIMIT 1", null);
            } else {
                cursor = db.rawQuery("SELECT * FROM words WHERE wordLength =? ORDER BY RANDOM() LIMIT 1", new String[]{String.valueOf(wordLength)});
            }
            String word = null;
            if (cursor != null) {
                if (cursor.moveToFirst())
                    word = cursor.getString(1);
                cursor.close();
            }
            db.close();
            if (word != null) {
                ordet = word;
                opdaterSynligtOrd();
                return word;
            } else
                return muligeOrd.get((int) (Math.random() * muligeOrd.size()));
        }
    }

    public void nulstil() {
        brugteBogstaver.clear();
        antalForkerteBogstaver = 0;
        spilletErVundet = false;
        spilletErTabt = false;
        ordet = hentNytOrd();
        opdaterSynligtOrd();
    }


    private void opdaterSynligtOrd() {
        synligtOrd = "";
        spilletErVundet = true;
        for (int n = 0; n < ordet.length(); n++) {
            String bogstav = ordet.substring(n, n + 1);
            if (brugteBogstaver.contains(bogstav)) {
                synligtOrd = synligtOrd + bogstav;
            } else {
                synligtOrd = synligtOrd + "*";
                spilletErVundet = false;
            }
        }
    }

    public void gætBogstav(String bogstav) {
        if (bogstav.length() != 1) return;
        System.out.println("Der gættes på bogstavet: " + bogstav);
        if (brugteBogstaver.contains(bogstav)) return;
        if (spilletErVundet || spilletErTabt) return;

        brugteBogstaver.add(bogstav);

        if (ordet.contains(bogstav)) {
            sidsteBogstavVarKorrekt = true;
            System.out.println("Bogstavet var korrekt: " + bogstav);
        } else {
            // Vi gættede på et bogstav der ikke var i ordet.
            sidsteBogstavVarKorrekt = false;
            System.out.println("Bogstavet var IKKE korrekt: " + bogstav);
            antalForkerteBogstaver = antalForkerteBogstaver + 1;
            if (antalForkerteBogstaver > 6) {
                spilletErTabt = true;
            }
        }
        opdaterSynligtOrd();
    }

    public void logStatus() {
        System.out.println("---------- ");
        System.out.println("- ordet (skult) = " + ordet);
        System.out.println("- synligtOrd = " + synligtOrd);
        System.out.println("- forkerteBogstaver = " + antalForkerteBogstaver);
        System.out.println("- brugeBogstaver = " + brugteBogstaver);
        if (spilletErTabt) System.out.println("- SPILLET ER TABT");
        if (spilletErVundet) System.out.println("- SPILLET ER VUNDET");
        System.out.println("---------- ");
    }


    private static String hentUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }

    public void hentNyeOrd() throws IOException {
        if (!checkDataBase()) {
            // the database does not exist
            String data = hentUrl("http://msondrup.dk/ord.txt");

            data = data.replaceAll("<.+?>", " ").toLowerCase().replaceAll("[^a-zæøå]", " ");
            muligeOrd.clear();
            muligeOrd.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));

            nulstil();
            int[] counter = new int[12];

            db = dbHandler.getWritableDatabase();

            for (String ord : muligeOrd) {
                if (ord.length() < 3 || ord.length() > 11) {
                    continue;
                }
                ContentValues values = new ContentValues();
                values.put("word", ord);
                values.put("timesUsed", 0);
                values.put("wordLength", ord.length());
                db.insert("words", null, values);
                counter[ord.length()]++;
            }
            possibleLengths = new ArrayList<>();
            for (int i = 3; i < counter.length; i++) {
                if (counter[i] > 5) {
                    possibleLengths.add(i);
                }
            }
            db.close();
        }
        wordsUpdated = true;
    }

    private boolean checkDataBase() {
        Cursor cursor = dbHandler.getReadableDatabase().rawQuery("SELECT * FROM words LIMIT 1", null);
        boolean returnValue = false;
        if (cursor != null) {
            returnValue = !(cursor.getCount() == 0);
            cursor.close();
        }
        return returnValue;
    }

    public ArrayList<Integer> getPossibleLengths() {
        if (possibleLengths == null) {
            if (checkDataBase()) {
                db = dbHandler.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM words", null);


                int[] counter = new int[12];

                if (cursor == null)
                    return null;

                if (cursor.getCount() == 0)
                    return null;

                cursor.moveToFirst();
                do {
                    counter[cursor.getInt(WordsDB.WORDLENGTH)]++;
                } while (cursor.moveToNext());
                cursor.close();
                possibleLengths = new ArrayList<>();
                for (int i = 3; i < counter.length; i++) {
                    if (counter[i] > 5) {
                        possibleLengths.add(i);
                    }
                }
                db.close();
                return possibleLengths;
            }
            return null;
        } else
            return possibleLengths;
    }

    public int getWordLength(){return this.wordLength;}

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public boolean isWordsUpdated() {
        return wordsUpdated;
    }

    public void setWordsUpdated(boolean wordsUpdated) {
        this.wordsUpdated = wordsUpdated;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public List<ParseObject> getHighscore() {
        return highscore;
    }

    public void setHighscore(List<ParseObject> highscore) {
        this.highscore = highscore;
    }

    public void updateHighscore(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("highscore");
        query.addDescendingOrder("score");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    highscore = objects;
                    minHighscore = objects.get(objects.size() - 1).getInt("score");
                    for (HighscoreSubscriber sub : highscoreSubscribers) {
                        sub.onHighscoreUpdate(highscore);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void subscribeToHighscore(HighscoreSubscriber sub){
        highscoreSubscribers.add(sub);
    }

    public int getMinHighscore() {
        return minHighscore;
    }

    public void setMinHighscore(int minHighscore) {
        this.minHighscore = minHighscore;
    }

    public int getNumberOnHighscore(){
        if(highscore == null)
            return 0;
        int score = getScore();
        for(int i = 0; i < highscore.size(); i++){
            if(score > highscore.get(i).getInt("score")){
                return i + 1;
            }
        }
        return 0;
    }

    public void uploadToHighscore(String name, int score, int wordLength){
        ParseObject object = new ParseObject("highscore");
        object.put("name", name);
        object.put("score", score);
        object.put("wordLength", wordLength);
        object.saveInBackground();
    }

    public int getScore(){
        return 1000 + timeLeft * wordLength - (antalForkerteBogstaver*35);
    }
}
