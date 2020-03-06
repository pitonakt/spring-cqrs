package com.pitonak.springcqrs.core.utils;

public final class LinksUtils {
    
    private LinksUtils() {}

    public static String getUrlFromRelation(String resource, String relation, String resourceId) {
        String rel = relation.replaceAll("_", "-");
        rel = prefixUrlWithSlash(rel);
        
        if ("self".equals(relation)) {
            rel = "";
        }
        
        return resource + createPathVariable(resourceId) + rel;
    }
    
    private static String prefixUrlWithSlash(String url) {
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        return url;
    }
    
    private static String createPathVariable(String resourceId) {
        
        if (resourceId == null) {
            return "";
        }
        
        return prefixUrlWithSlash("{" + resourceId + "}");
    }
}