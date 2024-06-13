package com.example.vnpr;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {
    @SerializedName("search_metadata")
    private SearchMetadata searchMetadata;

    @SerializedName("search_parameters")
    private SearchParameters searchParameters;

    @SerializedName("search_information")
    private SearchInformation searchInformation;

    @SerializedName("suggested_searches")
    private List<SuggestedSearch> suggestedSearches;

    @SerializedName("images_results")
    private List<ImageResult> imageResults;

    @SerializedName("related_searches")
    private List<RelatedSearch> relatedSearches;

    @SerializedName("serpapi_pagination")
    private SerpapiPagination serpapiPagination;

    // Getters and setters
    public SearchMetadata getSearchMetadata() {
        return searchMetadata;
    }

    public void setSearchMetadata(SearchMetadata searchMetadata) {
        this.searchMetadata = searchMetadata;
    }

    public SearchParameters getSearchParameters() {
        return searchParameters;
    }

    public void setSearchParameters(SearchParameters searchParameters) {
        this.searchParameters = searchParameters;
    }

    public SearchInformation getSearchInformation() {
        return searchInformation;
    }

    public void setSearchInformation(SearchInformation searchInformation) {
        this.searchInformation = searchInformation;
    }

    public List<SuggestedSearch> getSuggestedSearches() {
        return suggestedSearches;
    }

    public void setSuggestedSearches(List<SuggestedSearch> suggestedSearches) {
        this.suggestedSearches = suggestedSearches;
    }

    public List<ImageResult> getImageResults() {
        return imageResults;
    }

    public void setImageResults(List<ImageResult> imageResults) {
        this.imageResults = imageResults;
    }

    public List<RelatedSearch> getRelatedSearches() {
        return relatedSearches;
    }

    public void setRelatedSearches(List<RelatedSearch> relatedSearches) {
        this.relatedSearches = relatedSearches;
    }

    public SerpapiPagination getSerpapiPagination() {
        return serpapiPagination;
    }

    public void setSerpapiPagination(SerpapiPagination serpapiPagination) {
        this.serpapiPagination = serpapiPagination;
    }
}

class SearchMetadata {
    private String id;
    private String status;
    @SerializedName("json_endpoint")
    private String jsonEndpoint;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("processed_at")
    private String processedAt;
    @SerializedName("google_images_url")
    private String googleImagesUrl;
    @SerializedName("raw_html_file")
    private String rawHtmlFile;
    @SerializedName("total_time_taken")
    private double totalTimeTaken;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJsonEndpoint() {
        return jsonEndpoint;
    }

    public void setJsonEndpoint(String jsonEndpoint) {
        this.jsonEndpoint = jsonEndpoint;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
    }

    public String getGoogleImagesUrl() {
        return googleImagesUrl;
    }

    public void setGoogleImagesUrl(String googleImagesUrl) {
        this.googleImagesUrl = googleImagesUrl;
    }

    public String getRawHtmlFile() {
        return rawHtmlFile;
    }

    public void setRawHtmlFile(String rawHtmlFile) {
        this.rawHtmlFile = rawHtmlFile;
    }

    public double getTotalTimeTaken() {
        return totalTimeTaken;
    }

    public void setTotalTimeTaken(double totalTimeTaken) {
        this.totalTimeTaken = totalTimeTaken;
    }
}

class SearchParameters {
    private String engine;
    private String q;
    @SerializedName("google_domain")
    private String googleDomain;
    private String hl;
    private String gl;
    private String device;

    // Getters and setters
    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getGoogleDomain() {
        return googleDomain;
    }

    public void setGoogleDomain(String googleDomain) {
        this.googleDomain = googleDomain;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public String getGl() {
        return gl;
    }

    public void setGl(String gl) {
        this.gl = gl;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}

class SearchInformation {
    @SerializedName("image_results_state")
    private String imageResultsState;
    @SerializedName("showing_results_for")
    private String showingResultsFor;
    @SerializedName("spelling_fix")
    private String spellingFix;

    // Getters and setters
    public String getImageResultsState() {
        return imageResultsState;
    }

    public void setImageResultsState(String imageResultsState) {
        this.imageResultsState = imageResultsState;
    }

    public String getShowingResultsFor() {
        return showingResultsFor;
    }

    public void setShowingResultsFor(String showingResultsFor) {
        this.showingResultsFor = showingResultsFor;
    }

    public String getSpellingFix() {
        return spellingFix;
    }

    public void setSpellingFix(String spellingFix) {
        this.spellingFix = spellingFix;
    }
}

class SuggestedSearch {
    private String name;
    private String link;
    private String chips;
    @SerializedName("serpapi_link")
    private String serpapiLink;
    private String thumbnail;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getChips() {
        return chips;
    }

    public void setChips(String chips) {
        this.chips = chips;
    }

    public String getSerpapiLink() {
        return serpapiLink;
    }

    public void setSerpapiLink(String serpapiLink) {
        this.serpapiLink = serpapiLink;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

class ImageResult {
    private int position;
    private String thumbnail;
    @SerializedName("related_content_id")
    private String relatedContentId;
    @SerializedName("serpapi_related_content_link")
    private String serpapiRelatedContentLink;
    private String source;
    @SerializedName("source_logo")
    private String sourceLogo;
    private String title;
    private String link;
    private String original;
    @SerializedName("original_width")
    private int originalWidth;
    @SerializedName("original_height")
    private int originalHeight;
    @SerializedName("is_product")
    private boolean isProduct;

    // Getters and setters
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getRelatedContentId() {
        return relatedContentId;
    }

    public void setRelatedContentId(String relatedContentId) {
        this.relatedContentId = relatedContentId;
    }

    public String getSerpapiRelatedContentLink() {
        return serpapiRelatedContentLink;
    }

    public void setSerpapiRelatedContentLink(String serpapiRelatedContentLink) {
        this.serpapiRelatedContentLink = serpapiRelatedContentLink;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceLogo() {
        return sourceLogo;
    }

    public void setSourceLogo(String sourceLogo) {
        this.sourceLogo = sourceLogo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(int originalWidth) {
        this.originalWidth = originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(int originalHeight) {
        this.originalHeight = originalHeight;
    }

    public boolean isProduct() {
        return isProduct;
    }

    public void setProduct(boolean product) {
        isProduct = product;
    }
}

class RelatedSearch {
    private String link;
    @SerializedName("serpapi_link")
    private String serpapiLink;
    private String query;
    @SerializedName("highlighted_words")
    private List<String> highlightedWords;
    private String thumbnail;

    // Getters and setters
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSerpapiLink() {
        return serpapiLink;
    }

    public void setSerpapiLink(String serpapiLink) {
        this.serpapiLink = serpapiLink;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getHighlightedWords() {
        return highlightedWords;
    }

    public void setHighlightedWords(List<String> highlightedWords) {
        this.highlightedWords = highlightedWords;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

class SerpapiPagination {
    private int current;
    private String next;

    // Getters and setters
    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
