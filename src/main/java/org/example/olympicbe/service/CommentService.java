package org.example.olympicbe.service;

import lombok.RequiredArgsConstructor;
import org.example.olympicbe.domain.Comment;
import org.example.olympicbe.domain.Member;
import org.example.olympicbe.domain.Study;
import org.example.olympicbe.repository.CommentRepository;
import org.example.olympicbe.repository.MemberRepository;
import org.example.olympicbe.repository.StudyRepository;
import org.example.olympicbe.web.dto.CommentRequestDTO;
import org.example.olympicbe.web.dto.CommentResponseDTO;
import org.example.olympicbe.web.dto.LikeDislikeResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    // 댓글 생성
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        // studyId에 해당하는 Study 객체 조회
        Study study = studyRepository.findById(commentRequestDTO.getStudyId())
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));

        // memberId에 해당하는 Member 객체 조회
        Member member = memberRepository.findById(commentRequestDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // Comment 엔티티 생성
        Comment comment = new Comment();
        comment.setContent(commentRequestDTO.getContent());
        comment.setStudy(study);
        comment.setMember(member);
        comment.setLikeCount(0);  // 초기 좋아요 수 설정
        comment.setDislikeCount(0);  // 초기 비추천 수 설정

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        // 저장된 댓글을 CommentResponseDTO로 반환
        return new CommentResponseDTO(
                study.getTitle(),
                study.getContent(),
                savedComment.getId(),
                member.getNickname(),
                savedComment.getContent(),
                savedComment.getLikeCount(),
                savedComment.getDislikeCount()
        );
    }

    // 특정 Study에 달린 댓글 조회
    public List<CommentResponseDTO> getCommentsByStudyId(Long studyId) {
        // studyId에 해당하는 Study 객체 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));

        // Study에 달린 댓글 조회
        List<Comment> comments = commentRepository.findByStudyId(studyId);

        // 각 Comment를 CommentResponseDTO로 변환하여 리스트로 반환
        return comments.stream()
                .map(comment -> new CommentResponseDTO(
                        study.getTitle(),
                        study.getContent(),
                        comment.getId(),
                        comment.getMember().getNickname(),
                        comment.getContent(),
                        comment.getLikeCount(),
                        comment.getDislikeCount()
                ))
                .collect(Collectors.toList());
    }

    public void deleteComment(Long studyId, Long commentId) {
        // 댓글이 특정 studyId와 연관된지 확인 후 삭제
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (!comment.getStudy().getId().equals(studyId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified study");
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public LikeDislikeResponseDTO likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);

        return new LikeDislikeResponseDTO(comment.getLikeCount(), comment.getDislikeCount());
    }

    @Transactional
    public LikeDislikeResponseDTO dislikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comment.setDislikeCount(comment.getDislikeCount() + 1);
        commentRepository.save(comment);

        return new LikeDislikeResponseDTO(comment.getLikeCount(), comment.getDislikeCount());
    }
    // 댓글 추천/비추천 수 조회
    public CommentResponseDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        return new CommentResponseDTO(
                comment.getStudy().getTitle(),
                comment.getStudy().getContent(),
                comment.getId(),
                comment.getMember().getNickname(),
                comment.getContent(),
                comment.getLikeCount(),
                comment.getDislikeCount()
        );
    }
}
