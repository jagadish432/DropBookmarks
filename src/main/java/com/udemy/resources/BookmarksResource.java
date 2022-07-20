package com.udemy.resources;

import com.google.common.base.Optional;
import com.udemy.core.Bookmark;
import com.udemy.core.User;
import com.udemy.db.BookmarkDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

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
}
