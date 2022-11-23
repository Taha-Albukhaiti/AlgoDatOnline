package de.vfh.algodat.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import de.vfh.algodat.AnalyzeWords1;

public class AnalyzeWords1GoetheTest {

    @Rule
    public TestRule timeout = new DisableOnDebug(new Timeout(200000, TimeUnit.MILLISECONDS));

    @Test
    public void testFrequencyGoethe() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:wikiGoethe.html");
	assertEquals("Halbgötter kommt einmal vor", 1, aw.frequency("Halbgötter"));
    }
    @Test
    public void testDistanceGoethe() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:wikiGoethe.html");
	assertEquals("Abstand \"Halbgötter\", \"Götter\"", -2, aw.distance("Halbgötter", "Götter"));
    }
    @Test
    public void testMostFrequentGoethe() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:wikiGoethe.html");
	assertEquals(" \"und\" ist am häufigsten", "und", aw.mostFrequent());
    }
    @Test
    public void testUniqueGoethe() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:wikiGoethe.html");
	String[] uniqueWords = aw.unique();
	assertNotNull(uniqueWords);
	assertEquals("many unique words!", 4777, uniqueWords.length);
	Arrays.sort(uniqueWords);
	System.out.println(Arrays.toString(uniqueWords));
	assertEquals("\"Abend\": pos ", "Abend", uniqueWords[24]);
    }
    @Test
    public void testWordNearGoethe() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:wikiGoethe.html");
	int f1 = aw.frequency("Goethe");
	assertEquals("Goethe 747 mal vor", 747, f1);
	String[] wordsNear = aw.wordsNear("Goethe", 2);
	assertNotNull(wordsNear);
	assertEquals("many words near Goethe!", f1*4, wordsNear.length);
	Arrays.sort(wordsNear);
	System.out.println(Arrays.toString(wordsNear));
	assertEquals("\"Abend\": pos ", "Album", wordsNear[8]);
    }
}
