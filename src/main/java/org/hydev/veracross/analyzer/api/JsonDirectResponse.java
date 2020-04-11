package org.hydev.veracross.analyzer.api;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Directly respond with a string
 * <p>
 * Class created by the HyDEV Team on 2019-12-08!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-12-08 18:13
 */
@Data
@AllArgsConstructor
public class JsonDirectResponse
{
    String message;
}
