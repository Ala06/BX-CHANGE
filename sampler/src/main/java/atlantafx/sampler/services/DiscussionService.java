package atlantafx.sampler.services;

import atlantafx.sampler.entities.Discussions;

import java.util.List;

public interface DiscussionService {
    List<Discussions> getAllDiscussions();
    List<Discussions> getDiscussionsByUserId(int userId);
    Discussions getDiscussionById(int discussionId);
    Discussions createDiscussion(int user1Id, int user2Id);
    Discussions findOrCreateDiscussion(int user1Id, int user2Id);
}
