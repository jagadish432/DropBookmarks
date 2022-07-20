package com.udemy.db;


import com.google.common.base.Optional;
import com.udemy.core.Bookmark;
import com.udemy.core.User;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A unit test of class BookmarksDAO.
 *
 */
public class BookmarksDAOTest {

    private static final SessionFactory SESSION_FACTORY = HibernateUtil.getSessionFactory();

    private static Liquibase liquibase = null;
    private Session session;
    private Transaction tx;
    private BookmarkDAO bookmarkDAO;
    private UserDAO userDAO;

    @BeforeClass
    public static void setUpClass() throws LiquibaseException, SQLException {

        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) SESSION_FACTORY;
        DriverManagerConnectionProviderImpl provider = (DriverManagerConnectionProviderImpl) sessionFactoryImpl.getConnectionProvider();
        Connection connection = provider.getConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), database);
    }

    @AfterClass
    public static void tearDownClass() {
        SESSION_FACTORY.close();
    }

    @Before
    public void setUp() throws LiquibaseException {
        liquibase.update("test");
        session = SESSION_FACTORY.openSession();
        userDAO = new UserDAO(SESSION_FACTORY);
        bookmarkDAO = new BookmarkDAO(SESSION_FACTORY);
        tx = null;
    }

    @After
    public void tearDown() throws DatabaseException, LockException {
        liquibase.dropAll();
    }

    /**
     * Test of findForUser method, of class UserDAO.
     */
    @Test
    public void testFindForUser() {
        User user;
        List<Bookmark> bookmarks = null;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            user = userDAO.findByUsernamePassword("udemy", "password").get();

            bookmarks = bookmarkDAO.findForUser(user.getId());

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(user);
        Assert.assertNotNull(bookmarks);
        Assert.assertEquals(user.getId(), bookmarks.get(0).getUser().getId());
    }

    @Test
    public void testFindById() {
        Optional<Bookmark> bookmark;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            bookmark = bookmarkDAO.findById(1);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(bookmark);
        Assert.assertTrue(bookmark.isPresent());
    }

    @Test
    public void testSave() {
        Bookmark upBbookmark, downBookmark;
        User user;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            user = userDAO.findAll().get(0);
            upBbookmark = new Bookmark("yahoo", "www.yahoo.com", "yahoo URL", user);
            bookmarkDAO.save(upBbookmark);

            downBookmark = bookmarkDAO.findById(upBbookmark.getId()).get();

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(downBookmark);
        Assert.assertEquals(downBookmark.getUser().getId(), user.getId());
    }

    @Test
    public void testDelete() {
        List<Bookmark> bookmarks;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            bookmarkDAO.delete(2);
            bookmarks = bookmarkDAO.findForUser(1);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(bookmarks);
        Assert.assertEquals(1, bookmarks.size());
    }

    @Test
    public void testDeleteBookmarksOfUser() {
        List<Bookmark> bookmarks;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            bookmarkDAO.deleteBookmarksOfUser(1);
            bookmarks = bookmarkDAO.findForUser(1);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(bookmarks);
        Assert.assertEquals(0, bookmarks.size());
    }

}
