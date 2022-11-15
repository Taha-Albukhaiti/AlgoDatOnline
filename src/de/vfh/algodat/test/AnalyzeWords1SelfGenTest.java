package de.vfh.algodat.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import de.vfh.algodat.AnalyzeWords1;

public class AnalyzeWords1SelfGenTest {

    @Rule
    public TestRule timeout = new DisableOnDebug(new Timeout(500, TimeUnit.MILLISECONDS));

    @BeforeClass
    public static void generateFiles() throws IOException {
	FileWriter fw = new FileWriter(new File("blatest.txt"));
	fw.write("abc ab abcd abc bla blabla blablabla bla. bla, bla! ");
	fw.close();
	FileWriter fw2 = new FileWriter(new File("blatest2.txt"));
	fw2.write(" ss ss ss bla. bla, bla! abc ab ab ab abcd abc bla blabla xx xx xx xx blablabla ");
	fw2.close();
    }

    @Test
    public void testFrequency() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Wort kommt in blatest.txt zweimal vor", 2, aw.frequency("abc"));
    }

    @Test
    public void testFrequency2() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Wort \"bla\" kommt in blatest.txt viermal vor, mit Satzzeichen", 4, aw.frequency("bla"));
    }

    @Test
    public void testFrequency3() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Wort kommt nicht vor", 0, aw.frequency("qwer"));
    }

    @Test
    public void testFrequency4() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Wort ab kommt in blatest.txt einmal vor", 1, aw.frequency("ab"));
    }

    @Test
    public void testFrequency5() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Wort \"blablabla\" kommt in blatest.txt einmal vor", 1, aw.frequency("blablabla"));
    }
    @Test
    public void testDistance1() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Abstand 1", 1, aw.distance("abc", "ab"));
    }

    @Test
    public void testDistance2() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Abstand -1", -1, aw.distance("ab", "abc"));
    }

    @Test
    public void testDistance3() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Abstand 1", 1, aw.distance("bla", "bla"));
    }

    @Test
    public void testDistance4() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("blub existiert in blatest.txt nicht", Integer.MAX_VALUE, aw.distance("bla", "blub"));
    }

    @Test
    public void testDistance5() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("blabla existiert in blatest.txt nur einmal", Integer.MAX_VALUE, aw.distance("blabla", "blabla"));
    }

    @Test
    public void testDistance6() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Abstand Infinity", Integer.MAX_VALUE, aw.distance("qwer", "ab"));
    }

    @Test
    public void testDistance7() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("Abstand Infinity", Integer.MAX_VALUE, aw.distance("abc", "qwer"));
    }

    @Test
    public void testDistance8() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("blablabla existiert in blatest.txt nur einmal, blabla davor!", -1, aw.distance("blablabla", "blabla"));
    }

    @Test
    public void testMostFrequent() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	assertEquals("bla ist in blatest.txt am häufigsten", "bla", aw.mostFrequent());
    }

    @Test
    public void testMostFrequent2() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest2.txt");
	assertEquals("Wort \"bla\" kommt in blatest2.txt viermal vor, mit Satzzeichen", 4, aw.frequency("bla"));
	assertEquals("Wort \"x\" kommt in blatest2.txt viermal vor", 4, aw.frequency("xx"));
	assertEquals("bla ist in blatest2.txt am häufigsten", "bla", aw.mostFrequent());
    }

    @Test
    public void testUnique() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	String[] uniqueWords = aw.unique();
	assertNotNull(uniqueWords);
	assertEquals("four unique words!", 4, uniqueWords.length);
	Arrays.sort(uniqueWords);
	assertEquals("first (when sorted): ", "ab", uniqueWords[0]);
	assertEquals("second (when sorted): ", "abcd", uniqueWords[1]);
	assertEquals("third (when sorted): ", "blabla", uniqueWords[2]);	
    }

    @Test
    public void testWordsNear() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	String[] wordsNear = aw.wordsNear("abc", 1);
	assertNotNull(wordsNear);
	assertEquals("three words near abc!", 3, wordsNear.length);
	Arrays.sort(wordsNear);
	assertEquals("first (when sorted): ", "ab", wordsNear[0]);
	assertEquals("second (when sorted): ", "abcd", wordsNear[1]);
	assertEquals("third (when sorted): ", "bla", wordsNear[2]);
	
    }

    @Test
    public void testWordsNear1a() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	String[] wordsNear = aw.wordsNear("abc", 2);
	assertNotNull(wordsNear);
	assertEquals("three words near abc radius 2!", 6, wordsNear.length);
	Arrays.sort(wordsNear);
	assertEquals("first (when sorted): ", "ab", wordsNear[0]);
	assertEquals("second (when sorted): ", "ab", wordsNear[1]);
	assertEquals("third (when sorted): ", "abcd", wordsNear[2]);
	assertEquals("4th (when sorted): ", "abcd", wordsNear[3]);
	assertEquals("5th (when sorted): ", "bla", wordsNear[4]);
	assertEquals("6th (when sorted): ", "blabla", wordsNear[5]);
	
    }

    @Test
    public void testWordsNearNonexistent() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	String[] wordsNear = aw.wordsNear("qwer", 1);
	assertNotNull(wordsNear);
	assertEquals("No words near qwer!", 0, wordsNear.length);
    }

    @Test
    public void testWordsNear2() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	String[] wordsNear = aw.wordsNear("blabla", 1);
	assertNotNull(wordsNear);
	assertEquals("two words near blabla!", 2, wordsNear.length);
	Arrays.sort(wordsNear);
	assertEquals("first: bla", "bla", wordsNear[0]);
	assertEquals("second: blablabla", "blablabla", wordsNear[1]);
    }
    @Test
    public void testWordsNear3() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	String[] wordsNear = aw.wordsNear("blablabla", 1);
	assertNotNull(wordsNear);
	assertEquals("One word near blablabla!", 2, wordsNear.length);
	Arrays.sort(wordsNear);
	assertEquals("first: bla", "bla", wordsNear[0]);
	assertEquals("second: blabla", "blabla", wordsNear[1]);
    }
    @Test
    public void testWordsNear4() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest.txt");
	String[] wordsNear = aw.wordsNear("bla", 1);
	assertNotNull(wordsNear);
	assertEquals("words near bla!", 7, wordsNear.length);
	Arrays.sort(wordsNear);
	assertEquals("first: ", "abc", wordsNear[0]);
	assertEquals("fourth: ", "bla", wordsNear[1]);
	assertEquals("fifth: ", "bla", wordsNear[2]);
	assertEquals("sixth: ", "bla", wordsNear[3]);
	assertEquals("seventh: ", "bla", wordsNear[4]);
	assertEquals("second: ", "blabla", wordsNear[5]);
	assertEquals("third: ", "blablabla", wordsNear[6]);
    }
    @Test
    public void testWordsNear5() throws IOException {
	AnalyzeWords1 aw = new AnalyzeWords1("file:blatest2.txt");
	String[] wordsNear = aw.wordsNear("blablabla", 1);
	assertNotNull(wordsNear);
	assertEquals("One word near blablabla!", 1, wordsNear.length);
	Arrays.sort(wordsNear);
	assertEquals("word near blablabla in blatest2.txt:", "xx", wordsNear[0]);
    }


}
