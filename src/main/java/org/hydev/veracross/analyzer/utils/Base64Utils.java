package org.hydev.veracross.analyzer.utils;

import java.util.Base64;

import static org.hibernate.internal.util.StringHelper.repeat;

/**
 * This class is used to encode/decode base64-custom format.
 * <p>
 * Class created by the HyDEV Team on 2019-09-07!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-09-07 09:37
 */
public class Base64Utils
{
    public static String encodeBase64C(String text)
    {
        return Base64.getEncoder().encodeToString(text.getBytes())
            .replace("=", "")
            .replace("+", "-")
            .replace("/", "_")
            .replaceAll("[\\s*\t\n\r]", "");
    }

    public static String decodeBase64C(String text)
    {
        if (text.length() % 4 != 0) text += repeat("=", 4 - text.length() % 4);
        return new String(Base64.getDecoder().decode(text));
    }
}
