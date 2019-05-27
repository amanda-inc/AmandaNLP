package com.company;

import com.microsoft.cognitiveservices.speech.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Amanda {

    private static String answer;
    private static Object PosPosition;

    /**
     * Returns a String representation of the audio gotten by the user.
     * @return String of the audio.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String getAudio() throws ExecutionException, InterruptedException {
        String speechSubscriptionKey = "a601516cf4b441179e01b135c3e07624";
        String serviceRegion = "eastus";
        SpeechConfig configure = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
        assert(configure != null);
        SpeechRecognizer recognizer = new SpeechRecognizer(configure);
        assert(recognizer != null);
        System.out.println("Say something...");
        Future<SpeechRecognitionResult> task = recognizer.recognizeOnceAsync();
        assert(task != null);
        SpeechRecognitionResult result = task.get();
        assert(result != null);
        if (result.getReason() == ResultReason.RecognizedSpeech) {
            answer = result.getText();
        }
        else if (result.getReason() == ResultReason.NoMatch) {
            System.out.println("NOMATCH: Speech could not be recognized.");
        }
        else if (result.getReason() == ResultReason.Canceled) {
            CancellationDetails cancellation = CancellationDetails.fromResult(result);
            System.out.println("CANCELED: Reason=" + cancellation.getReason());

            if (cancellation.getReason() == CancellationReason.Error) {
                System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                System.out.println("CANCELED: Did you update the subscription info?");
            }
        }
        recognizer.close();
        return answer;
    }
    /**
     * Return a List of tokens of a String
     * @param answer String representation of the audio.
     * @return List of tokens.
     */
    public static List<CoreLabel> tokenizeAnswer(String answer){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(answer);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabelList = coreDocument.tokens();
        return coreLabelList;
    }
    /**
     * Returns s hashmap representing the part of speech within a string.
     * @param coreLabelList List of tokens found within a string.
     * @return Hashmap representing the part of speech within a string.
     */
    public static HashMap<String , String> answerPOS(List<CoreLabel> coreLabelList){
        HashMap<String , String> map = new HashMap<>();
          for (CoreLabel coreLabel: coreLabelList){
              String pos = coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class);
              map.put(coreLabel.originalText(),pos);
          }
          return map;
    }
    /**
     * Returns a hashmap representing the amount of times a word is within a string.
     * @param list List of tokens found within a string
     * @return Hashmap of words, and their appearance within a string.
     */
    public static HashMap<String, Integer> wordCount(List<CoreLabel> list){
        HashMap<String,Integer> dictionary = new HashMap<>();
        for (CoreLabel coreLabel: list){
            String key = coreLabel.originalText();
            if (!dictionary.containsKey(key)){
                dictionary.put(key,1);
            }
            else {
                int keyValue = dictionary.get(key);
                keyValue++;
                dictionary.remove(key);
                dictionary.put(key,keyValue);
            }
        }
       return dictionary;
    }
    /**
     * Returns a sorted hashmap representing the amount of times a word is within a string.
     * @param dictionary Hashmap of the words, and their appearance within a string.
     * @return Sorted hashmap of words, and their appearance within a string.
     */
    public static HashMap<String, Integer> wordCountSorted(HashMap<String,Integer> dictionary){
        HashMap<String, Integer> sort;
        List<Map.Entry<String, Integer>> toSort = new ArrayList<>();
        for (Map.Entry<String, Integer> stringIntegerEntry : dictionary.entrySet()) {
            toSort.add(stringIntegerEntry);
        }
        toSort.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> stringIntegerEntry : toSort) {
            map.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
        }
        sort = map;
        return sort;
    }
    /**
     * Class to be use in a method.
     */
    public class PosPosition{

        int position;
        String pos;

        public PosPosition(String pos, int position){
            setPos(pos);
            setPosition(position);
        }
        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }
        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String toString(){
            String string = getPos() + "=" + getPosition();
            return string;
        }
    }
    /**
     * Returns the position of each part of speech in a string.
     * @param coreLabelList List of tokens found within a string.
     * @return List with the position of each part of speech within a string.
     */
    public ArrayList<String> positionPOS(List<CoreLabel> coreLabelList){
        PosPosition posPosition;
       ArrayList<String> list = new ArrayList<>();
        int i = 0;
        for (CoreLabel coreLabel: coreLabelList){
            String pos = coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            posPosition = new PosPosition(pos,i);
            list.add(posPosition.toString());
            i++;
        }
        return list;
    }

}
