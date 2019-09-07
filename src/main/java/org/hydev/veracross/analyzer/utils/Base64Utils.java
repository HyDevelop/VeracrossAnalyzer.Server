package org.hydev.veracross.analyzer.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

import static cc.moecraft.utils.StringUtils.repeat;

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
    public static String encodeBase64C(byte[] bytes)
    {
        return new BASE64Encoder().encode(bytes)
                .replace("=", "")
                .replace("+", "-")
                .replace("/", "_")
                .replaceAll("[\\s*\t\n\r]", "");
    }

    public static String encodeBase64C(String text)
    {
        return encodeBase64C(text.getBytes());
    }

    public static byte[] decodeBase64C(String text)
    {
        if (text.length() % 4 != 0) text += repeat("=", 4 - text.length() % 4);
        try
        {
            return new BASE64Decoder().decodeBuffer(text
                    .replace("-", "+")
                    .replace("_", "/"));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String decodeBase64CStr(String text)
    {
        return new String(decodeBase64C(text));
    }
}
