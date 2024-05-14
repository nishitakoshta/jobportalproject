package com.personal.jobportal.jobportalproject.service.impl;
import com.personal.jobportal.jobportalproject.dto.JobDTO;
import com.personal.jobportal.jobportalproject.dto.PaginatedJobResultDTO;
import com.personal.jobportal.jobportalproject.entity.TfIdfScores;
import com.personal.jobportal.jobportalproject.repository.JobRepository;
import com.personal.jobportal.jobportalproject.repository.TfIdfRepository;
import com.personal.jobportal.jobportalproject.service.TfIdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class TfIdfServiceImpl implements TfIdfService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private TfIdfRepository tfIdfRepository;
    private final Map<String, Map<String, Double>> tfIdfScores;
    public TfIdfServiceImpl(List<String> stringList) {
        tfIdfScores = new HashMap<>();
        for (String input : stringList) {
            Map<String, Double> tfIdfScore = calculateTFIDF(input, stringList);
            tfIdfScores.put(input, tfIdfScore);
        }
    }
    private static Map<String, Double> calculateTF(String input) {
        Map<String, Double> tfMap = new HashMap<>();
        // Check if input is null
        if (input != null) {
            String[] words = input.split(" ");
            int totalWords = 0; // Updated to count non-empty and non-null words
            // Count the occurrence of each unique non-empty and non-null word
            for (String word : words) {
                if (!word.trim().isEmpty() && !word.equalsIgnoreCase("null")) {
                    tfMap.put(word, tfMap.getOrDefault(word, 0.0) + 1.0);
                    totalWords++;
                }
            }
            if (totalWords > 0) {
                // Calculate TF for each word
                for (Map.Entry<String, Double> entry : tfMap.entrySet()) {
                    double tf = entry.getValue() / totalWords;
                    tfMap.put(entry.getKey(), tf);
                }
            }
        }
        return tfMap;
    }
    private static double calculateIDF(List<String> stringList, String termToCheck) {
        int documentFrequency = 0;
        for (String string : stringList) {
            if (string != null && string.contains(termToCheck)) {
                documentFrequency++;
            }
        }
        double idf = Math.log10((double) stringList.size() / (documentFrequency + 1)); // Adding 1 to avoid division by zero
        idf = Math.abs(idf);
        return idf;
    }
    @Override
    public Map<String, Double> calculateTFIDF(String string, List<String> stringList) {
        Map<String, Double> tfIdfScore = new HashMap<>();
        Map<String, Double> tfMap = calculateTF(string); // Calculate TF for the current string
        for (String word : tfMap.keySet()) {
            double tf = tfMap.get(word);
            double idf = calculateIDF(stringList, word); // Calculate IDF for the current word
            double tfIdf = tf * idf;
            tfIdfScore.put(word, tfIdf);
        }
        return tfIdfScore;
    }
    @Override
    public void saveTFIDFScores(Map<String, Double> tfIdfScores, String fieldType, Integer jobId) {
        try{
            for (Map.Entry<String, Double> entry : tfIdfScores.entrySet()) {
                String term = entry.getKey();
                Double tfIdfScore = entry.getValue();
                // Check for Infinity and set a specific value instead
                if (Double.isInfinite(tfIdfScore)) {
                    tfIdfScore = Double.MAX_VALUE;
                }
                List<Object[]> result = jobRepository.checkTermExistence(term, fieldType, jobId);
                if (!result.isEmpty()) {
                    Object[] row = result.get(0);
                    TfIdfScores existScore = createTFIDFScoreFromRow(row);
                    existScore.setJobId(jobId);
                    existScore.setScore(tfIdfScore);
                    tfIdfRepository.save(existScore);
                }else {
                    TfIdfScores tfidfScoreEntity = new TfIdfScores();
                    tfidfScoreEntity.setTerm(term);
                    tfidfScoreEntity.setScore(tfIdfScore);
                    tfidfScoreEntity.setFieldType(fieldType);
                    tfidfScoreEntity.setJobId(jobId);
                    tfIdfRepository.save(tfidfScoreEntity);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public TfIdfScores createTFIDFScoreFromRow(Object[] row) {
        TfIdfScores tfidfScore = new TfIdfScores();
        tfidfScore.setId((Integer) row[0]);
        tfidfScore.setFieldType((String) row[2]);
        tfidfScore.setScore((Double) row[3]);
        tfidfScore.setTerm((String) row[4]);
        return tfidfScore;
    }
    public Map<Integer, Double> search(String query) {
        if ("null".equals(query) || query.trim().isEmpty()) {
            return Collections.emptyMap();
        }
        // Clear TF-IDF scores before processing a new query
        tfIdfScores.clear();
        // Calculate trigrams for each term in the query
        List<String> queryTrigrams = generateTrigrams(query);
        // Map to store final scores for each blogId
        Map<Integer, Double> finalScoresByBlogId = new ConcurrentHashMap<>();
        // Adjust batch size based on experimentation
        int batchSize = 300;
        for (int i = 0; i < queryTrigrams.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, queryTrigrams.size());
            List<String> batchTrigrams = queryTrigrams.subList(i, endIndex);
            batchTrigrams.forEach(trigram -> {
                // Process batch elements sequentially
                List<String> terms = tfIdfRepository.getMatchingStrings(trigram);
                Map<String, Double> queryTfIdfScore = calculateTFIDF(trigram, terms);
                List<Object[]> matchingTerms = tfIdfRepository.getMatchingStringsByTrigram(trigram);
                matchingTerms.forEach(term -> {
                    List<Double> queryTfIdfScoreList = new ArrayList<>(queryTfIdfScore.values());
                    // Use an array to make blogScore effectively final
                    double[] blogScore = {finalScoresByBlogId.getOrDefault((Integer) term[0], 0.0)};
                    // Accumulate the values for each queryTfIdfScore with the corresponding trigramTfIdf
                    queryTfIdfScoreList.parallelStream().forEach(trigramTfIdf -> {
                        double newValue = multiplyMaps(trigramTfIdf, (Double) term[1]);
                        blogScore[0] += newValue;
                    });
                    // Update the finalScoresByBlogId map with the accumulated score for the blogId
                    finalScoresByBlogId.put((Integer) term[0], blogScore[0]);
                });
            });
        }
        // Define the maximum value for replacing Infinity
        double maxScore = Double.MAX_VALUE;
        // Replace Infinity with the maximum value
        finalScoresByBlogId.replaceAll((key, value) -> Double.isInfinite(value) ? maxScore : value);
        return finalScoresByBlogId;
    }
    public static double multiplyMaps(Double map1, Double map2) {
        double result = map1 * map2;
        if (Double.isInfinite(result)) {
            result = Double.MAX_VALUE;
        }
        return result;
    }
    private List<String> generateTrigrams(String input) {
        List<String> trigrams = new ArrayList<>();
        for (int i = 0; i < input.length() - 2; i++) {
            trigrams.add(input.substring(i, i + 3));
        }
        return trigrams;
    }
    @Override
    public PaginatedJobResultDTO getMatchingBlog(String query, int pageIndex) {
        PaginatedJobResultDTO blogDTO;
        try {
            Map<Integer, Double> searchResult = search(query);
            List<Integer> blogIds = new ArrayList<>(searchResult.keySet());
            List<Object[]> blogListDTOByQuery = tfIdfRepository.blogsByIds(blogIds)
                    .parallelStream()
                    .toList();
            List<JobDTO> matchingStories = blogListDTOByQuery.stream()
                    .parallel()
                    .map(this::convertObjectToJobList)
                    .toList();
            matchingStories.forEach(blogListDTOForScore -> {
                int blogId = blogListDTOForScore.getJobId();
                if (searchResult.containsKey(blogId)) {
                    double score = searchResult.get(blogId);
                    blogListDTOForScore.setScore(score);
                }
            });
            // Pagination logic
            int pageSize = 2;
            int totalMatchingStories = matchingStories.size();
            // Ensure pageIndex is within bounds
            if (pageIndex < 0 || pageIndex * pageSize >= totalMatchingStories) {
                return PaginatedJobResultDTO.builder()
                        .searchResult(Collections.emptyList())
                        .pagination(false)
                        .pageIndex(0)
                        .build();
            }
            int startIndex = pageIndex * pageSize;
            int endIndex = Math.min(startIndex + pageSize, matchingStories.size());
            // Ensure endIndex is within bounds
            if (endIndex > totalMatchingStories) {
                endIndex = totalMatchingStories;
            }
            Comparator<JobDTO> blogComparator = Comparator.comparingDouble(JobDTO::getScore).reversed();
            List<JobDTO> sortedMatchingBlogs = matchingStories.stream()
                    .sorted(blogComparator)
                    .toList();
            List<JobDTO> paginatedBlogDTOList = sortedMatchingBlogs.subList(startIndex, endIndex);
            boolean pagination = endIndex < sortedMatchingBlogs.size();
            pageIndex = pagination ? pageIndex + 1 : 0;
            blogDTO = PaginatedJobResultDTO.builder()
                    .searchResult(paginatedBlogDTOList)
                    .pagination(pagination)
                    .pageIndex(pageIndex)
                    .build();
        } catch (Exception e) {
            throw e;
        }
        return blogDTO;
    }
    public JobDTO convertObjectToJobList(Object[] objects) {
        return JobDTO.builder()
                .jobId((Integer) objects[0])
                .title((String) objects[6])
                .category((String) objects[1])
                .createdOn(((Timestamp) objects[2]).toLocalDateTime())
                .description((String) objects[3])
                .location((String) objects[4])
                .requirements(Collections.singletonList((String) objects[5]))
                .build();
    }
}
