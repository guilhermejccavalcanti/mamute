package org.mamute.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.FlaggableDAO;
import org.mamute.dto.FlaggableAndFlagCount;
import org.mamute.model.Answer;
import org.mamute.model.Comment;
import org.mamute.model.Flag;
import org.mamute.model.FlagType;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mamute.model.interfaces.Flaggable;

public class FlaggableDAOTest extends DatabaseTestCase {
	private FlaggableDAO flaggables;
	private User author = user("author author", "author@brutal.com");
	private Comment commentWithTwo = comment();
	private Comment commentWithOneFlag = comment();
	private Comment flaggedInvisible = comment();
	private Tag tag;
	private Question question;

	@Before
	public void setup() {
		tag = tag("java");
		question = question(author, tag);
		flaggables = new FlaggableDAO(session);
		flaggedInvisible.remove();
		addFlags(commentWithOneFlag, 1, author);
		addFlags(commentWithTwo, 2, author);
		addFlags(flaggedInvisible, 2, author);
		session.save(author);
		session.save(commentWithTwo);
		session.save(flaggedInvisible);
		session.save(commentWithOneFlag);
		session.save(tag);
		session.save(question);
		session.flush();
	}
	
	@Test
	public void should_get_visible_comments_with_two_flags() throws Exception {
		List<FlaggableAndFlagCount> flagged = flaggables.flaggedButVisible(Comment.class);
		assertEquals(1, flagged.size());
		assertEquals(2l, flagged.get(0).getFlagCount());
	}
	
	@Test
	public void should_get_count_of_comments_with_two_flags() throws Exception {
		Comment comment = comment();
		addFlags(comment, 10, author);
		session.save(comment);
		int count = flaggables.flaggedButVisibleCount(Comment.class);
		assertEquals(2, count);
	}
	
	@Test
	public void should_get_count_of_questions_with_two_flags() throws Exception {
		Question other = question(author, tag);
		addFlags(question, 10, author);
		addFlags(other, 1, author);
		session.save(tag);
		session.save(question);
		session.save(other);
		int count = flaggables.flaggedButVisibleCount(Question.class);
		assertEquals(1, count);
	}
	
	@Test
	public void should_get_count_of_answers_with_two_flags() throws Exception {
		Answer flagged = answer("answer answer answer answer answer", question, author);
		Answer other = answer("answer answer answer answer answer", question, author);
		addFlags(flagged, 10, author);
		addFlags(other, 1, author);
		session.save(tag);
		session.save(question);
		session.save(other);
		session.save(flagged);
		int count = flaggables.flaggedButVisibleCount(Answer.class);
		assertEquals(1, count);
	}

	private void addFlags(Flaggable comment, int n, User author) {
		for (int i = 0; i < n; i++) {
			Flag flag = flag(FlagType.RUDE, author);
			session.save(flag);
			comment.add(flag);
		}
	}

	private Comment comment() {
		return comment(author, "comment comment comment comment");
	}

}
