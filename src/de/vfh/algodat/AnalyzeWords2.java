package de.vfh.algodat;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Taha Al-Bukhaiti
 */
public class AnalyzeWords2 {
    DownloadPage page;

    private String[] stringsLinks;
    private String stringsUrl;
    private String stringsContent;
    private Map<String, Integer> map;


    public AnalyzeWords2(DownloadPage downloadPage) {
        this.page = downloadPage;
    }

    public AnalyzeWords2(String str) throws IOException {
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
        //return Arrays.stream(page.getWords()).sequential().mapToInt((strings) -> strings.equals(word) ? 1 : 0).sum();
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
                    if (j > i) {
                        if ((j - i) <= sum) {
                            sum = j - i;
                            if (sum == 1) {
                                return sum;
                            }
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
        int countNulls = 0;
        for (String s : arr) if (s == null) countNulls++;

        String[] neuesArray = new String[arr.length - countNulls];
        for (int i = 0, j = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                neuesArray[j] = arr[i];
                j++;
            }
        }
        return neuesArray;
    }

    public static void quickSort(String[] arr, int p, int r) {
        if (p < r) {
            int q = partition(arr, p, r);
            quickSort(arr, p, q - 1);
            quickSort(arr, q + 1, r);
        }
    }

    public static int partition(String[] arr, int p, int r) {
        String pivot = arr[r];
        int i = p;
        for (int j = p; j < r; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                String tempo = arr[i];
                arr[i] = arr[j];
                arr[j] = tempo;
                i++;
            }
        }
        String tempo2 = arr[r];
        arr[r] = arr[i];
        arr[i] = tempo2;
        return i;
    }

    public void merge(String[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        String[] liftArray = new String[n1];
        String[] rechtArray = new String[n2];
        int i, j, k;

        for (i = 0; i < liftArray.length; i++) {
            liftArray[i] = arr[l + i];
        }
        for (j = 0; j < rechtArray.length; j++) {
            rechtArray[j] = arr[m + 1 + j];
        }
        i = j = 0;
        k = l;
        while (i < n1 && j < n2) {
            if (liftArray[i].compareTo(rechtArray[j]) < 0) {
                arr[k] = liftArray[i];
                i++;
            } else {
                arr[k] = rechtArray[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = liftArray[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = rechtArray[j];
            j++;
            k++;
        }
    }

    public void mergesort(String[] arr, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergesort(arr, l, m);
            mergesort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }


    /**
     * liefert eine Position, an der s gespeichert ist.
     *
     * @param word
     * @return
     */
    public int isStoredAtPosition(String word) {
        quickSort(page.getWords(), 0, page.getWords().length - 1);
       /* int i;
        for (i = 0; i < page.getWords().length; i++) {
            if (word.equals(page.getWords()[i])) break;
        }
        */
        int l = 0;
        int r = page.getWords().length - 1;
        while (l < r) {
            int i = (l + r) / 2;
            if (page.getWords()[i].compareTo(word) < 0) l = i + 1;
            else r = i;
        }
        if (Objects.equals(page.getWords()[l], word)) return l;
        return l;
    }

    /**
     * liefert das Wort, das an der Stelle i gespeichert ist.
     *
     * @param i
     * @return
     */
    public String wordAtPosition(int i) {
        quickSort(page.getWords(), 0, page.getWords().length - 1);
        return page.getWords()[i];
    }
}