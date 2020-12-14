package com.example.roguishfinal;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Stasher {

    Context context;
    int[] stashedValues = {0, 0, 0};
    String filename = "stash";

    public Stasher(Context context) {
        this.context = context;
        initializeStash();
    }

    public void initializeStash() {
        try {
            fetchData();
        }
        catch (FileNotFoundException e) {
            this.clear();
        }
    }

    public void clear() {
        Arrays.fill(this.stashedValues, 0);
        String contents = "0\n0\n0";

        try (FileOutputStream outStream =
                     this.context
                             .openFileOutput(
                                     filename,
                                     Context.MODE_PRIVATE)) {
            // Actually trying this
            outStream.write(contents.getBytes());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void put(int position, int value) {
        this.stashedValues[position] = value;
        pushData();
    }

    public int findSpace() {
        try {
            this.fetchData();
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for(int i = 0; i < this.stashedValues.length; i++) {
            if (this.stashedValues[i] == 0) {
                return i;
            }
        }

        return -1;
    }

    public void pushData() {
        StringBuilder valueBuilder = new StringBuilder();
        for (int stashedValue : this.stashedValues)
            valueBuilder.append(stashedValue).append("\n");
        String contents = valueBuilder.toString();
        Log.d("hr", "contents: " + contents);

        try(FileOutputStream outStream =
                    this.context
                            .openFileOutput(
                                    filename,
                                    Context.MODE_PRIVATE)) {
            // Actually trying this
            outStream.write(contents.getBytes());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchData() throws FileNotFoundException {
        FileInputStream inputStream = this.context.openFileInput(filename);
        InputStreamReader inputStreamReader =
                new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        try(BufferedReader reader = new BufferedReader(inputStreamReader)) {
            for(int i = 0; i < this.stashedValues.length; i++) {
                String line = reader.readLine();
                this.stashedValues[i] = Integer.parseInt(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
