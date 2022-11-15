package de.vfh.algodat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * Downloads a web page and extracts words and urls.
 *
 * @author weimar
 */
public class DownloadPage {
    /**
     * Source url.
     */
    private String url;
    /**
     * Source common url prefix.
     */
    private String prefix;
    /**
     * content (html source).
     */
    private String content;
    /**
     * Time taken to load page.
     */
    private long time;
    /**
     * words in this page.
     */
    private String[] words = new String[0];
    /**
     * urls in this page.
     */
    private String[] urls = new String[0];

    public static final boolean DEBUG = false;

    @Override
    public String toString() {
        return "\"" + url + "\", "
                // + time + ", "
                + content.length() + ", " + urls.length + ", " + words.length;
    }

    /**
     * Constructor: downloads the web-page and analyzes it.
     *
     * @param name url to download, starting with http or https.
     * @throws IOException if something did not work.
     */
    public DownloadPage(String name) throws IOException {
        url = name.replaceAll("http:", "https:");
        final long startTime = System.currentTimeMillis();
        URL url2 = new URL(url);
        prefix = calculatePrefix(url);
        URLConnection con = url2.openConnection();
        if (con instanceof HttpsURLConnection) {
            HttpsURLConnection conH = (HttpsURLConnection) con;
            conH.setRequestMethod("GET");
            conH.setConnectTimeout(5000);
            conH.setReadTimeout(5000);
            int status = conH.getResponseCode();
            if (status != 200) {
                throw new IOException("Did not work, response: " + status);
            }
            // }else if (con instanceof HttpURLConnection) {
            // HttpURLConnection conH = (HttpURLConnection) con;
            // conH.setRequestMethod("GET");
            // conH.setConnectTimeout(5000);
            // conH.setReadTimeout(5000);
            // int status = conH.getResponseCode();
            // if (status != 200) {
            // throw new IOException("Did not work, response: "+status);
            // }
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer result = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();
        long endTime = System.currentTimeMillis();
        time = endTime - startTime;
        content = result.toString();
        findWords();
        findURLs();
    }

    public static String calculatePrefix(String url) {
        Pattern pattern = Pattern.compile("(http(s)?://[^/]*)|(file:)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "";
        }
    }

    /**
     * extracts all urls from this page
     */
    private void findURLs() {
        // Pattern pattern = Pattern.compile("\"(http[^\"]*)\"");
        Pattern pattern = Pattern.compile("href=\"((file:|http://|https://|/)[^\"]*)\"");
        Matcher matcher = pattern.matcher(content);
        // check all occurrences
        List<String> hits = new ArrayList<String>();
        while (matcher.find()) {
            if (DEBUG) {
                System.out.print("Start index: " + matcher.start());
                System.out.print(" End index: " + matcher.end() + " ");
            }
            String hit = matcher.group(1);
            if (DEBUG) {
                System.out.println(hit);
            }
            if (hit.startsWith("/")) {
                hit = prefix + hit;
                if (DEBUG) {
                    System.out.println(" -> " + hit);
                }
            }
            hits.add(hit);
        }
        urls = hits.toArray(urls);
    }

    /**
     * extracts all words not in tags with at least 2 characters.
     */
    private void findWords() {
        Pattern pattern = Pattern.compile("(<[^>]*>)|<!--[^->]*-->|[ ,.;:\">]?([-a-zA-ZöäüÖÄÜß]*)[ ,.;:\\\"<>!?]");
        Matcher matcher = pattern.matcher(content);
        // check all occurrences
        ArrayList<String> hits = new ArrayList<String>();
        while (matcher.find()) {
            if (matcher.group(2) != null && matcher.group(2).length() > 1 && !matcher.group(2).equals("--")) {
                if (DEBUG) {
                    System.out.print("Start index: " + matcher.start());
                    System.out.print(" End index: " + matcher.end() + " ");
                }
                String hit = matcher.group(2);
                if (DEBUG) {
                    System.out.println(hit);
                }
                hits.add(hit);
            }
        }
        words = hits.toArray(urls);
    }

    /**
     * count the number of occurrences of string in content.
     *
     * @param content text to analyze
     * @param string  to find
     * @return the number
     */
    public static int count(String content, String string) {
        int i = 0;
        int count = 0;
        while ((i = content.indexOf(string, i)) >= 0) {
            i += string.length();
            count++;
        }
        return count;
    }

    // /**
    // * load the startpage of ostfalia.
    // * @throws IOException
    // */
    // public static void loadStart() throws IOException {
    // // DownloadPage start = new DownloadPage("https://www.ostfalia.de");
    // DownloadPage start = new DownloadPage("file:testFile.html");
    // System.out.println(start);
    // final boolean follow = true;
    // if (follow) {
    // for (String url : start.urls) {
    // try{
    // DownloadPage next = new DownloadPage(url);
    // System.out.println(next);
    // }catch(Exception i) {
    // System.out.println("with "+url+": "+i);
    // }
    // }
    // }
    // }
    //
    // public static void main(String... args) {
    // try {
    // loadStart();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    /**
     * The URL (source) of this page
     *
     * @return The URL (source) of this page in the form of a string.
     */
    public String getUrl() {
        return url;
    }

    /**
     * the complete page as a string
     *
     * @return the complete page as a string
     */
    public String getContent() {
        return content;
    }

    /**
     * The time it took to load the page.
     *
     * @return The time it took to load the page.
     */
    private long getTime() {
        return time;
    }

    /**
     * Array of all words in this page.
     *
     * @return an array of all words (strings) on this page.
     */
    public String[] getWords() {
        return words;
    }

    /**
     * Array of all links on this page. If necessary, http://.../ is added for local
     * links. all page-local links (starting with '#') are omitted.
     *
     * @return an array of links (as strings),
     */
    public String[] getLinks() {
        return urls;
    }

}
