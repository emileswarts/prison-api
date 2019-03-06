package net.syscon.elite.api.support;

import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ResponseDelegate extends Response {
    private final Response delegate;

    private final Object entity;

    protected ResponseDelegate(final Response delegate, final Object entity) {
        this.delegate = delegate;
        this.entity = entity;
    }

    protected ResponseDelegate(final Response delegate) {
        this(delegate, null);
    }

    @Override
    public int getLength() {
        return this.delegate.getLength();
    }

    @Override
    public Locale getLanguage() {
        return this.delegate.getLanguage();
    }

    @Override
    public URI getLocation() {
        return this.delegate.getLocation();
    }

    @Override
    public void close() {
        this.delegate.close();
    }

    @Override
    public int getStatus() {
        return this.delegate.getStatus();
    }

    @Override
    public boolean hasLink(final String p0) {
        return this.delegate.hasLink(p0);
    }

    @Override
    public Link getLink(final String p0) {
        return this.delegate.getLink(p0);
    }

    @Override
    public Set<Link> getLinks() {
        return this.delegate.getLinks();
    }

    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        return this.delegate.getHeaders();
    }

    @Override
    public Date getLastModified() {
        return this.delegate.getLastModified();
    }

    @Override
    public Date getDate() {
        return this.delegate.getDate();
    }

    @Override
    public Object getEntity() {
        return this.entity;
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        return this.delegate.getMetadata();
    }

    @Override
    public StatusType getStatusInfo() {
        return this.delegate.getStatusInfo();
    }

    @Override
    public <T> T readEntity(final Class<T> p0, final Annotation[] p1) {
        return this.delegate.readEntity(p0, p1);
    }

    @Override
    public <T> T readEntity(final GenericType<T> p0, final Annotation[] p1) {
        return this.delegate.readEntity(p0, p1);
    }

    @Override
    public <T> T readEntity(final GenericType<T> p0) {
        return this.delegate.readEntity(p0);
    }

    @Override
    public <T> T readEntity(final Class<T> p0) {
        return this.delegate.readEntity(p0);
    }

    @Override
    public boolean hasEntity() {
        return this.delegate.hasEntity();
    }

    @Override
    public boolean bufferEntity() {
        return this.delegate.bufferEntity();
    }

    @Override
    public MediaType getMediaType() {
        return this.delegate.getMediaType();
    }

    @Override
    public Set<String> getAllowedMethods() {
        return this.delegate.getAllowedMethods();
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        return this.delegate.getCookies();
    }

    @Override
    public EntityTag getEntityTag() {
        return this.delegate.getEntityTag();
    }

    @Override
    public Link.Builder getLinkBuilder(final String p0) {
        return this.delegate.getLinkBuilder(p0);
    }

    @Override
    public MultivaluedMap<String, String> getStringHeaders() {
        return this.delegate.getStringHeaders();
    }

    @Override
    public String getHeaderString(final String p0) {
        return this.delegate.getHeaderString(p0);
    }
}
