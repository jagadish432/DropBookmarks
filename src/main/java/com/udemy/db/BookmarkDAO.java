package com.udemy.db;

import com.google.common.base.Optional;
import com.udemy.core.Bookmark;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class BookmarkDAO extends AbstractDAO<Bookmark> {

    public BookmarkDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Bookmark> findForUser(long id) {
        return list(namedQuery("com.udemy.core.Bookmark.findForUser").setParameter("id", id));
    }

    public Optional<Bookmark> findById(long id) {
        return Optional.fromNullable(get(id));
    }

    public Bookmark save(Bookmark bookmark) {
        return persist(bookmark);
    }

    public void delete(long id) {
        namedQuery("com.udemy.core.Bookmark.remove").setParameter("id", id).executeUpdate();
    }

    public void deleteBookmarksOfUser(long user_id) {
        namedQuery("com.udemy.core.Bookmark.removeBookmarksOfUser").setParameter("user_id", user_id).executeUpdate();
    }
}
