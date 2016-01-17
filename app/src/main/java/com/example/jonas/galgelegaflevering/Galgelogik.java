package com.example.jonas.galgelegaflevering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Galgelogik {
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

    public Galgelogik(Context context) {
        dbHandler = new WordsDB(context);
        muligeOrd.add("bil");
        muligeOrd.add("computer");
        muligeOrd.add("programmering");
        muligeOrd.add("motorvej");
        muligeOrd.add("busrute");
        muligeOrd.add("gangsti");
        muligeOrd.add("skovsnegl");
        muligeOrd.add("solsort");
        nulstil();
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
            System.out.println("BlaBla");
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

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public boolean isWordsUpdated() {
        return wordsUpdated;
    }

    public void setWordsUpdated(boolean wordsUpdated) {
        this.wordsUpdated = wordsUpdated;
    }
}
