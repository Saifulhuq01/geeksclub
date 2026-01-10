package dev.sivalabs.geeksclub.domain.dto;

public record VoteCounts(long upvotes, long downvotes) {
    public long score() {
        return upvotes - downvotes;
    }
}
