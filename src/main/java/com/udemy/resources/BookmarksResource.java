package com.udemy.resources;

import com.google.common.base.Optional;
import com.udemy.core.Bookmark;
import com.udemy.core.User;
import com.udemy.db.BookmarkDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.validation.constraints.Null;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/bookmark")
// https://localhost:8443/bookmarks
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookmarksResource {
    private BookmarkDAO bookmarkDAO;

    public BookmarksResource(BookmarkDAO bookmarkDAO) {
        this.bookmarkDAO = bookmarkDAO;
    }

    @GET
    @UnitOfWork
    public List<Bookmark> getBookmarks(@Auth User user) {
        return bookmarkDAO.findForUser(user.getId());
    }

    private Optional<Bookmark> findIfAuthorized(long bookmarkId, long userId) {
        Optional<Bookmark> result = bookmarkDAO.findById(bookmarkId);
        if(result.isPresent() && userId != result.get().getUser().getId()) {
            throw new ForbiddenException("You're not authorized to see this resource.");
        }

        return result;
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    // eg: https://localhost:8443/bookmark/1
    public Optional<Bookmark> getBookmark(@PathParam("id")LongParam id
            , @Auth User user) {
        return findIfAuthorized(id.get(), user.getId());
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Optional<Bookmark> deleteBookmark(@PathParam("id")LongParam id,
                                             @Auth User user) {
        Optional<Bookmark> bookmark = findIfAuthorized(id.get(), user.getId());
        if (bookmark.isPresent()) {
            bookmarkDAO.delete(id.get());
        }
        return bookmark;
    }

    @POST
    @UnitOfWork
    public Bookmark saveBookmark(Bookmark bookmark, @Auth User user) {
        bookmark.setUser(user);
        return bookmarkDAO.save(bookmark);
    }

    @DELETE
    @UnitOfWork
    public List<Bookmark> deleteAllBookmarks(@Auth User user) {
        List<Bookmark> bookmarks = bookmarkDAO.findForUser(user.getId());
        if ( !bookmarks.isEmpty() ) {
            bookmarkDAO.deleteBookmarksOfUser(user.getId());
        }
        return bookmarks;
    }

    @PUT
    @UnitOfWork
    @Path("/{id}")
    public Optional<Bookmark> updateBookmark(@PathParam("id")LongParam id, Bookmark bookmark,
                                             @Auth User user) {
        Optional<Bookmark> bookmarkFromDB = findIfAuthorized(id.get(), user.getId());
        if (bookmarkFromDB.isPresent()) {
            bookmarkFromDB.get().setName(bookmark.getName());
            bookmarkFromDB.get().setDescription(bookmark.getDescription());
            bookmarkFromDB.get().setUrl(bookmark.getUrl());
            return Optional.fromNullable(bookmarkDAO.save(bookmarkFromDB.get()));
        }
        return bookmarkFromDB;
    }

}
