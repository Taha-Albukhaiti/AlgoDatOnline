package de.vfh.algodat;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Taha Al-Bukhaiti
 */
public class AnalyzeWords1 {
    DownloadPage page;

    public AnalyzeWords1(DownloadPage downloadPage) {
        this.page = downloadPage;
    }

    public AnalyzeWords1(String str) throws IOException {
        this(new DownloadPage(str));
    }

    /**
     * liefert die Anzahl von Vorkommen des Wortes word.
     *
     * @param word
     * @return
     */
    public int frequency(String word) {
        int j = 0;
        for (String stringsWord : page.getWords()) {
            if (stringsWord.equals(word)) {
                j++;
            }
        }
        return j;
        //return Arrays.stream(stringsWords).sequential().mapToInt((strings) -> strings.equals(word) ? 1 : 0).sum();
    }

    /**
     * liefert das häufigste Wort in der Seite.
     *
     * @return
     */
    public String mostFrequent() {
        Map<String, Integer> map = new HashMap<>();
        for (String str : page.getWords()) {
            if (!map.containsKey(str)) {
                map.put(str, 1);
            } else {
                map.put(str, map.get(str) + 1);
            }
        }

        int number = 0;
        for (int i : map.values()) {
            if (i > number) {
                number = i;
            }
        }

        String s = "";
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        Map.Entry<String, Integer> key;
        while (iterator.hasNext()) {
            key = iterator.next();
            if (key.getValue().equals(number)) {
                s = key.getKey();
            }
        }
        return s;
    }

    /**
     * liefert ein Array, das alle Wörter enthält, die nur genau einmal vorkommen
     *
     * @return
     */
    public String[] unique() {
        int count = 0;
        for (String test : page.getWords()) {
            int freq = frequency(test);
            if (freq == 1) {
                count++;
            }
        }
        String[] result = new String[count];
        count = 0;
        for (String test : page.getWords()) {
            int freq = frequency(test);
            if (freq == 1) {
                result[count++] = test;
            }
        }
        return result;
    }

    /**
     * berechnet die kleinste Distanz zwischen einem Vorkommen von string1 und
     * einem Vorkommen von string2. Dabei soll das Vorzeichen negativ sein, wenn
     * die kürzeste Distanz erreicht ist, wenn string2 vor string1 vorkommt. Sollte
     * eine der beiden Zeichenketten gar nicht vorkommen, dann soll distance den
     * Wert Integer.MAX_VALUE liefern. Falls string2 gleich string1 ist, dann soll
     * distance nicht 0 liefern, sondern die kleinste Distanz zwischen zwei verschiedenen
     * Vorkommen von string1, bzw. Integer.MAX_VALUE, wenn das Wort nur einmal
     * vorkommt.
     *
     * @param string1
     * @param string2
     * @return sum
     */
    public int distance(String string1, String string2) {

        int index1 = -1;
        int index2 = -1;
        int minDistance = Integer.MAX_VALUE;
        int tempDistance = 0;

        for (int x = 0; x < page.getWords().length - 1; x++) {
            if (!string1.equals(string2)) {
                if (page.getWords()[x].equals(string1)) {
                    index1 = x;
                }
                if (page.getWords()[x].equals(string2)) {
                    index2 = x;
                }
                if (index1 != -1 && index2 != -1 && index1 < index2) {
                    tempDistance = (int) Math.abs(index2 - index1);
                    if (tempDistance < minDistance) {
                        minDistance = tempDistance;
                    }
                } else if (index1 != -1 && index2 != -1) {
                    tempDistance = (int) Math.abs(index1 - index2);
                    if (tempDistance < minDistance) {
                        minDistance = -tempDistance;
                    }
                }
            } else {
                if (x != 0) {
                    if (page.getWords()[x - 1].equals(string1)) {
                        index1 = x - 1;
                    }
                    if (page.getWords()[x].equals(string2)) {
                        index2 = x;
                    }
                    if (index1 != -1 && index2 != -1 && index1 < index2) {
                        tempDistance = index2 - index1;
                        if (tempDistance < minDistance) {
                            minDistance = tempDistance;
                        }
                    }
                }
            }
        }
        return minDistance;

    }

    /**
     * liefert ein Array aller Wörter, die in einer Distanz von maximal dist zu dem Wort
     * stehen (davor oder danach). Das Array kann doppelte Einträge enthalten, wenn
     * Wörter mehrfach vorkommen. Das Array ist nicht sortiert
     *
     * @param string
     * @param dist
     * @return
     */
    public String[] wordsNear(String string, int dist) {
        String[] arr = new String[page.getWords().length + 1];
        int n = page.getWords().length;
        int m = 0;
        for (int i = 0; i < n; i++) {
            if (page.getWords()[i].equals(string)) {
                int l = 0;
                if (i == n - 1) {
                    l = i;
                }
                if (i + dist < n) {
                    l = i + dist;
                }
                for (int k = i + 1; k <= l; k++) {
                    arr[m++] = page.getWords()[k];
                }

                if (i > 0) {
                    for (int j = i - dist; j < i; j++) {
                        arr[m++] = page.getWords()[j];
                    }
                }
            }
        }
        int nulls = 0;
        for (String s : arr) if (s == null) nulls++;
        String[] neuesArray = new String[arr.length - nulls];
        for (int i = 0, j = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                neuesArray[j] = arr[i];
                j++;
            }
        }
        return neuesArray;
    }

    public static void main(String[] args) throws IOException {
        AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
        String[] wordsNear = aw.wordsNear("bla", 1);
        Arrays.stream(wordsNear).forEach(System.out::println);


    }
}
/*
 for (int i = 0; i < page.getWords().length - 1; i++) {
            for (int j = i + 1; j < page.getWords().length; j++) {
                sum = j - i;
                tempo = i - j;
                if (page.getWords()[i].equals(string1) && page.getWords()[j].equals(string2) && !page.getWords()[j].equals(string1)) {
                    System.out.println(sum);
                    if ((j - i) <= sum) {
                        sum = j - i;
                        System.out.println(sum);
                        if (sum == 1) {
                            return sum;
                        }
                    }
                } else if (page.getWords()[i].equals(string2) && page.getWords()[j].equals(string1)
                        && !string2.equals(string1)) {
                    if ((i - j) >= tempo) {
                        tempo = i - j;
                        sum = tempo;
                        if (tempo >= -2) return sum;
                        if (i == j) return sum;
                    }
                }
            }
        }



        // Sollte eine der beiden Zeichenketten gar nicht vorkommen, dann soll distance den
        // Wert Integer.MAX_VALUE liefern.
        // String1 == String2 und kommt nur einmal vor!
        if (frequency(string1) == 0 || frequency(string2) == 0 || (string1.equals(string2) && frequency(string1) == 1))
            return Integer.MAX_VALUE;
        int tempo;
        int sum = 0;
        for (int i = 0; i < page.getWords().length - 1; i++) {
            for (int j = i + 1; j < page.getWords().length; j++) {
                sum = j - i;
                if (page.getWords()[i].equals(string1) && page.getWords()[j].equals(string2) && !page.getWords()[j].equals(string1)) {
                    if ((j - i) <= sum) {
                        sum = j - i;
                        if (sum == 1) {
                            return sum;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < page.getWords().length - 1; i++) {
            for (int j = i + 1; j < page.getWords().length; j++) {
                tempo = i - j;
                if (page.getWords()[i].equals(string2) && page.getWords()[j].equals(string1)
                        && !string2.equals(string1)) {
                    if ((i - j) >= tempo) {
                        tempo = i - j;
                        sum = tempo;
                    }
                }
            }
        }

        return sum;
 */