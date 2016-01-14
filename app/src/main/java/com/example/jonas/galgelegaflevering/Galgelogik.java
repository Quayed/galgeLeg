package com.example.jonas.galgelegaflevering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.BufferedReader;
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

        String[] newStrings = new String[muligeOrd.size()];
        newStrings = muligeOrd.toArray(newStrings);

        return newStrings;
    }

    public Galgelogik(Context context) {
        DB_FULL_PATH = context.getFilesDir() + "/database.db";
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
        if (!checkDataBase()) {
            return muligeOrd.get(new Random().nextInt(muligeOrd.size()));
        } else {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_FULL_PATH, null);
            Cursor cursor = db.rawQuery("SELECT * FROM words ORDER BY RANDOM() LIMIT 1", null);
            String word = null;
            if (cursor != null){
                cursor.moveToFirst();
                word = cursor.getString(1);
                cursor.close();
            }
            db.close();
            if(word != null)
                return word;
            else
                return muligeOrd.get((int)(Math.random()*muligeOrd.size()));
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


    public static String hentUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }

    public void hentOrdFraDr() throws Exception {
        if (!checkDataBase()) {
            // the database does not exist
            String data = hentUrl("http://dr.dk");
            System.out.println("data = " + data);

            data = data.replaceAll("<.+?>", " ").toLowerCase().replaceAll("[^a-zæøå]", " ");
            System.out.println("data = " + data);
            muligeOrd.clear();
            muligeOrd.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));

            System.out.println("muligeOrd = " + muligeOrd);
            nulstil();
            int[] counter = new int[12];
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_FULL_PATH, null);
            db.execSQL("CREATE TABLE words (id INTEGER PRIMARY KEY, word TEXT NOT NULL, timesUsed INTEGER, wordLength INTEGER);");
            for (String ord : muligeOrd) {
                if(ord.length() < 3){
                    continue;
                }
                ContentValues values = new ContentValues();
                values.put("word", ord);
                values.put("timesUsed", 0);
                values.put("wordLength", ord.length());
                db.insert("words", null, values);
                counter[ord.length()]++;
            }
            for(int i = 3; i < counter.length; i++){
                if(counter[i] > 5){
                    possibleLengths.add(i);
                }
            }
            db.close();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    public ArrayList<Integer> getPossibleLengths(){
        if(possibleLengths == null){
            if (checkDataBase()) {
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_FULL_PATH, null);
                Cursor cursor = db.rawQuery("SELECT * FROM words ORDER BY RANDOM() LIMIT 1", null);

                int[] counter = new int[12];

                if (cursor == null)
                    return null;

                while (cursor.moveToFirst()){
                    counter[cursor.getInt(4)]++;
                }
                for(int i = 3; i < counter.length; i++){
                    if(counter[i] > 5){
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
}
