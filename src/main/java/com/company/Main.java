package com.company;

import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/*
** Testing of the Amanda class.
*/
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //Object the Amanda class.
        Amanda amanda = new Amanda();
        //String representing the audio gotten by the computer.
        String answer = Amanda.getAudio();
        System.out.println("You said: " + answer);
        //List of the tokens within the string.
        List<CoreLabel>tokenizeAnswer = Amanda.tokenizeAnswer(answer);
        for (CoreLabel coreLabels: tokenizeAnswer){
            System.out.println(coreLabels.originalText());
        }
        // Hashmap of the part of speech of each token found.
        HashMap<String,String> answerPOS = Amanda.answerPOS(tokenizeAnswer);
        System.out.println("Parts of speech within the string: " + answerPOS);
        // List of the position of each part of speech found in the string.
        ArrayList<String> posPosition = amanda.positionPOS(tokenizeAnswer);
        System.out.print("Parts of speech, and their position: ");
        StringBuilder stringBuilder = new StringBuilder("{");
        for (int i = 0; i < posPosition.size(); i++){
            stringBuilder.append(posPosition.get(i));
            if (i == posPosition.size() - 1){
                stringBuilder.append("}");
                System.out.println(stringBuilder);
            }
            else{
                stringBuilder.append(", ");
            }
        }
        // Hashmap of the amount of times a word is found in the string.
        HashMap<String,Integer> wordCount = Amanda.wordCount(tokenizeAnswer);
        System.out.println("Before sorting: " + wordCount);
        // Sorted hashmap of the amount of times a word is found in the string.
        HashMap<String, Integer> sortedValues = Amanda.wordCountSorted(wordCount);
        System.out.println("After sorting: " + sortedValues);

    }
}
