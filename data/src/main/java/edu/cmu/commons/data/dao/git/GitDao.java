package edu.cmu.commons.data.dao.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.PersonIdent;

import edu.cmu.commons.data.dao.fs.FSDao;
import edu.cmu.commons.data.io.Serialization;

/**
 * Dao backed by a Git repository, supporting versioning of all persistent
 * entities.
 * @author hazen
 * @param <E>
 * @param <I>
 */
public abstract class GitDao<E, I> extends FSDao<E, I> {
	private final Git git;
	private PersonIdent author;

	public PersonIdent getAuthor() {
		return author;
	}

	public void setAuthor(PersonIdent author) {
		this.author = author;
	}

	public GitDao(Git git, Class<E> entityClass, Class<I> idClass,
			Serialization<E> serialization) {
		super(git.getRepository().getWorkTree(), entityClass, idClass,
				serialization);
		this.git = git;
	}

	@Override
	public void flush() {
		super.flush();
		// TODO commit here?
	}

	public void commit(String message) throws Exception {
		git.add().addFilepattern(".").call();
		git.commit().setAuthor(author).setMessage(message).call();
	}
}
