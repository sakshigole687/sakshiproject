import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.similarity.CosineSimilarity;

public class ResumeMatcher {

    // Simple keyword extractor (replace with NLP for production)
    public static Set<String> extractKeywords(String text) {
        text = text.toLowerCase().replaceAll("[^a-z ]", " ");
        List<String> stopwords = Arrays.asList("and", "the", "is", "in", "at", "of", "a", "to", "with");
        Set<String> keywords = new HashSet<>();
        for (String word : text.split("\\s+")) {
            if (!stopwords.contains(word) && word.length() > 2) {
                keywords.add(word);
            }
        }
        return keywords;
    }

    // Cosine Similarity using Apache Commons Text
    public static double computeSimilarity(Set<String> set1, Set<String> set2) {
        Map<CharSequence, Integer> freq1 = new HashMap<>();
        Map<CharSequence, Integer> freq2 = new HashMap<>();
        for (String k : set1)
            freq1.put(k, 1);
        for (String k : set2)
            freq2.put(k, 1);
        CosineSimilarity cs = new CosineSimilarity();
        return cs.cosineSimilarity(freq1, freq2);
    }

    public static void main(String[] args) throws Exception {
        String jobDesc = Files.readString(Path.of("job_description.txt"));
        List<String> resumeFiles = List.of("resume1.txt", "resume2.txt", "resume3.txt");

        Set<String> jobKeywords = extractKeywords(jobDesc);

        Map<String, Double> scores = new HashMap<>();
        for (String resumeFile : resumeFiles) {
            String resumeText = Files.readString(Path.of(resumeFile));
            Set<String> resumeKeywords = extractKeywords(resumeText);
            double sim = computeSimilarity(jobKeywords, resumeKeywords);
            scores.put(resumeFile, sim);
        }

        // Print ranked resumes
        scores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(e -> System.out.printf("%s: %.2f\n", e.getKey(), e.getValue()));
    }
}