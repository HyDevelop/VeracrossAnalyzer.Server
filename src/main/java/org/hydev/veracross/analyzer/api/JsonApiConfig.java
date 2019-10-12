package org.hydev.veracross.analyzer.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementations of this class contains configuration info for json
 * based api nodes.
 * <p>
 * Class created by the HyDEV Team on 2019-10-12!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-12 14:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonApiConfig
{
    /** Check for body length */
    private int maxBodyLength = -1;

    /** Check for key value length */
    private Map<String, Integer> keyLengths = new HashMap<>();

    /**
     * Set max body length
     *
     * @param value Max body length
     * @return Self
     */
    public JsonApiConfig maxBodyLength(int value)
    {
        maxBodyLength = value;
        return this;
    }

    /**
     * Add a key value length check
     *
     * @param key Key name
     * @param length Value length
     * @return Self
     */
    public JsonApiConfig key(String key, int length)
    {
        keyLengths.put(key, length);
        return this;
    }
}
