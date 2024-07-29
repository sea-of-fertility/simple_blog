package com.example.simple_blog.service.post;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Comment;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.exception.post.PostException;
import com.example.simple_blog.repository.member.MemberRepository;
import com.example.simple_blog.repository.post.PostRepository;
import com.example.simple_blog.repository.post.comment.CommentRepository;
import com.example.simple_blog.response.post.comment.Comments;
import com.example.simple_blog.service.member.MemberService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;


@SpringBootTest
class CommentServiceTest {


    String testTitle = "title";
    String testContext = "context";

    final String nickName = "nick";
    final String memberName = "hello";
    final String testAddress = "hello@naver.com";
    final String testPassword = "1234@121a";


    @Autowired
    CommentRepository commentRepository;


    @Autowired
    CommentService commentService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;
    @Autowired
    private ApplicationArguments springApplicationArguments;

    @AfterEach
    void setCommentRepository() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @BeforeEach
    void setMemberRepository() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 추가")
    void createComment() throws Exception {

        //given
        Member member = getMember();
        Post post = getPost();

        Comment comment = Comment.builder()
                .author(member)
                .parent(null)
                .post(post)
                .content(testContext)
                .build();

        //when
        Comment save = commentService.save(comment);

        //then
        Assertions.assertThat(commentRepository.count()).isEqualTo(1L);
        Assertions.assertThat(save.getContent()).isEqualTo(testContext);
    }

    @Test
    @DisplayName("대댓글 작성하기")
    void createRecomment() throws Exception {
        //given
        Member member = getMember();
        Post post = getPost();
        Comment comment = Comment.builder()
                .author(member)
                .parent(null)
                .post(post)
                .content(testContext)
                .build();
        Comment save = commentService.save(comment);


        //when
        Comment recomment = Comment.builder()
                .author(member)
                .parent(save)
                .post(post)
                .content(testContext + "children!!")
                .build();

        commentService.save(recomment);
        //then
        Assertions.assertThat(recomment.getParent()).isEqualTo(save);
    }

    @Test
    @DisplayName("삭제된 댓글에 대댓글을 달 경우 예외를 발생한다.")
    void NotFoundComment() throws Exception {
        //given
        Member member = getMember();
        Post post = getPost();

        Comment comment = Comment.builder()
                .author(member)
                .parent(null)
                .post(post)
                .content(testContext)
                .build();

        commentService.save(comment);
        commentRepository.deleteById(comment.getId());

        Comment reComment = Comment.builder()
                .author(member)
                .parent(comment)
                .post(post)
                .content(testContext)
                .build();

        //expect
        Assertions.assertThatThrownBy(() -> commentService.save(reComment)).isInstanceOf(PostException.class);

    }


    @Test
    @Transactional
    @DisplayName("대댓글 조회")
    void reCommentCreate() throws Exception {

        //given
        Member member = getMember();
        Post post = getPost();

        //when
        List<Comment> comments = commentRepository.saveAll(IntStream.range(0, 5).mapToObj(i -> Comment.builder()
                .author(member)
                .parent(null)
                .post(post)
                .content("parent" + i)
                .build()).toList());

        List<Comment> reComment = commentRepository.saveAll(IntStream.range(0, 5).mapToObj(i -> Comment.builder()
                .author(member)
                .parent(comments.get(0))
                .post(post)
                .content("children" + i)
                .build()).toList());


        //then


        List<Comments> getComments = commentService.getComments(post.getId());

        // then
        Assertions.assertThat(getComments.isEmpty()).isFalse();
        Assertions.assertThat(getComments.size()).isEqualTo(5);
    }


    private Member getMember() {
        Member nick = Member.builder()
                .address(testAddress)
                .memberNickName(nickName)
                .memberName(memberName)
                .password(testPassword)
                .build();
        return memberService.save(nick);
    }

    private Post getPost() {
        Member byAddress = memberService.findByAddress(testAddress);
        Post post = Post.builder()
                .title(testTitle)
                .content(testContext)
                .member(byAddress)
                .build();
        postService.save(post);
        return post;
    }

}