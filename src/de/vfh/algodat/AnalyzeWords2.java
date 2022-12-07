package de.vfh.algodat;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Taha Al-Bukhaiti
 */
public class AnalyzeWords2 {
    DownloadPage page;

    public AnalyzeWords2(DownloadPage downloadPage) {
        this.page = downloadPage;
    }

    public AnalyzeWords2(String str) throws IOException {
        this(new DownloadPage(str));

    }

    /**
     * liefert die Anzahl von Vorkommen des Wortes word.
     *
     * @param
     * @return
     */
    public int frequency(String word) {
        quickSort(page.getWords(), 0, page.getWords().length - 1);
        int l = 0;
        int s = 0;
        int r = page.getWords().length - 1;
        while (l < r) {
            int i = (l + r) / 2;
            if (page.getWords()[i].compareTo(word) < 0) l = i + 1;
            else r = i;
        }
        for (int m = l; m < page.getWords().length; m++) {
            if (page.getWords()[m].equals(word)) s++;
            else break;
        }
        return s;
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
        quickSort(page.getWords(), 0, page.getWords().length - 1);

        int count = 0;
        if (!page.getWords()[0].equals(page.getWords()[1])) count++;
        for (int i = 1; i < page.getWords().length - 1; i++) {
            if (!page.getWords()[i - 1].equals(page.getWords()[i]) && !page.getWords()[i].equals(page.getWords()[i + 1])) {
                count++;
            }
        }
        if (!page.getWords()[page.getWords().length - 2].equals(page.getWords()[page.getWords().length - 1])) count++;
        int j = 0;
        String[] result = new String[count];
        if (!page.getWords()[0].equals(page.getWords()[1])) {
            result[j] = page.getWords()[0];
            j++;
        }
        for (int i = 1; i < page.getWords().length - 1; i++) {
            if (!page.getWords()[i - 1].equals(page.getWords()[i]) && !page.getWords()[i].equals(page.getWords()[i + 1])) {
                result[j] = page.getWords()[i];
                j++;
            }
        }
        if (!page.getWords()[page.getWords().length - 2].equals(page.getWords()[page.getWords().length - 1])) {
            result[j] = page.getWords()[page.getWords().length - 1];
        }

/*
        int count = 0;
        for (int i  = 0; i < page.getWords().length; i++){
            if (frequency(page.getWords()[i]) == 1) count++;
        }
        String[] result = new String[count];
        int l = 0;
        for (int i  = 0; i < page.getWords().length; i++){
            if (frequency(page.getWords()[i]) == 1) result[++l] = page.getWords()[i];
        }
*/
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
                    tempDistance =  index2 - index1;
                    if (tempDistance < minDistance) {
                        minDistance = tempDistance;
                    }
                } else if (index1 != -1 && index2 != -1) {
                    tempDistance = Math.abs(index1 - index2);

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
        int i, j, k;

        String[] liftArray = new String[n1];
        String[] rechtArray = new String[n2];

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
     * liefert eine Position, an der das Wort gespeichert ist.
     *
     * @param word
     * @return
     */
    public int isStoredAtPosition(String word) {
        mergesort(page.getWords(), 0, page.getWords().length - 1);
       /* int i;
        for (i = 0; i < page.getWords().length; i++) {
            if (word.equals(page.getWords()[i])) break;
        }
        */
        int l = 0;  // [2, 3, 4, 5, 6, 7, 8, 9] => 3  l = 1 - r = 1 i = 0
        int r = page.getWords().length - 1;
        while (l < r) {
            int i = (l + r) / 2;
            if (page.getWords()[i].compareTo(word) < 0) l = i + 1;
            else r = i;
        }
        if (page.getWords()[l].equals(word)) return l;
        return l;
    }

    /**
     * liefert das Wort, das an der Stelle i gespeichert ist.
     *
     * @param i para
     * @return das Wort an der Stelle i
     */
    public String wordAtPosition(int i) {
        mergesort(page.getWords(), 0, page.getWords().length - 1);
        return page.getWords()[i];
    }


}