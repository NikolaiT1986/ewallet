package org.nikolait.assigment.ewallet.util;

import lombok.experimental.UtilityClass;

import java.net.URI;

@UtilityClass
public class TestUriUtils {

    public static boolean pathFromLocationStartsWith(String location, String prefix) {
        String path = URI.create(location).getPath();
        return path.startsWith(prefix);
    }

    public static String extractIdFromLocation(String location) {
        return location.substring(location.lastIndexOf('/') + 1);
    }

}
