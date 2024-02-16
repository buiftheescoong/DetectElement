package org.example;
import com.princeton.wordnet.WordNet;
public class WordNet {




        public static void main(String[] args) {
            // Tạo WordNet
            WordNet wordnet = new WordNet("/path/to/wordnet/dict");

            // Chuỗi 1
            String string1 = "Tôi yêu thích món phở bò.";

            // Chuỗi 2
            String string2 = "Phở bò là món ăn ngon nhất Việt Nam.";

            // Chia chuỗi thành các từ
            String[] words1 = string1.split(" ");
            String[] words2 = string2.split(" ");

            // Tìm kiếm từ đồng nghĩa
            Set<String> synonyms1 = new HashSet<>();
            for (String word : words1) {
                for (Word lemma : wordnet.getSynsets(word)) {
                    synonyms1.add(lemma.getLemma());
                }
            }

            Set<String> synonyms2 = new HashSet<>();
            for (String word : words2) {
                for (Word lemma : wordnet.getSynsets(word)) {
                    synonyms2.add(lemma.getLemma());
                }
            }

            // Tính toán cosine similarity
            double cosineSimilarity = cosineSimilarity(synonyms1, synonyms2);

            // Tính toán Jaccard similarity
            double jaccardSimilarity = jaccardSimilarity(synonyms1, synonyms2);

            // Tính toán Wu-Palmer similarity
            double wuPalmerSimilarity = wuPalmerSimilarity(wordnet, synonyms1, synonyms2);

            // In ra kết quả
            System.out.println("Cosine similarity: " + cosineSimilarity);
            System.out.println("Jaccard similarity: " + jaccardSimilarity);
            System.out.println("Wu-Palmer similarity: " + wuPalmerSimilarity);
        }

        private static double cosineSimilarity(Set<String> set1, Set<String> set2) {
            double numerator = 0;
            for (String word1 : set1) {
                for (String word2 : set2) {
                    numerator += wordnet.getRelatedness(word1, word2);
                }
            }

            double denominator = Math.sqrt(set1.size() * set2.size());
            return numerator / denominator;
        }

        private static double jaccardSimilarity(Set<String> set1, Set<String> set2) {
            return (double) set1.size() / (double) set2.size();
        }

        private static double wuPalmerSimilarity(WordNet wordnet, Set<String> set1, Set<String> set2) {
            double maxSimilarity = 0;
            for (String word1 : set1) {
                for (String word2 : set2) {
                    Synset synset1 = wordnet.getSynsets(word1).get(0);
                    Synset synset2 = wordnet.getSynsets(word2).get(0);
                    double similarity = synset1.path_similarity(synset2);
                    if (similarity > maxSimilarity) {
                        maxSimilarity = similarity;
                    }
                }
            }
            return maxSimilarity;
        }


}
