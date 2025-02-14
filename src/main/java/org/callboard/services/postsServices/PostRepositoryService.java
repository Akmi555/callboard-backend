package org.callboard.services.postsServices;

import lombok.RequiredArgsConstructor;
import org.callboard.entities.Post;
import org.callboard.entities.Subject;
import org.callboard.exceptions.NotFoundException;
import org.callboard.repositories.PostRepository;
import org.callboard.services.subjectService.SubjectRepositoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostRepositoryService {
    private final PostRepository postRepository;
    private final SubjectRepositoryService subjectRepoService;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post with id: " + id + " not found"));
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void deleteBiId(Long id) {

        postRepository.deleteById(id);
    }

    public void deleteByUserId(Integer id) {

        postRepository.deleteByUserId(id);
    }

    public List<Post> findBySubject(String subject) {
        Subject subjectForSearch = subjectRepoService.findByName(subject);
        return postRepository.findBySubjectId(subjectForSearch.getSubjectId());
    }

    public List<Post> findByUserEmail(String email) {
        return postRepository.findByUserEmail(email);
    }

    public List<Post> findByUserId(Integer userId) {
        return postRepository.findByUserId(userId);
    }
}
